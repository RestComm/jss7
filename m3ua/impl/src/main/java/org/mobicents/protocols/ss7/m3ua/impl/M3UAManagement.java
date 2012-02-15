/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

/**
 * @author amit bhayani
 * 
 */
public class M3UAManagement extends Mtp3UserPartBaseImpl {
	private static final Logger logger = Logger.getLogger(M3UAManagement.class);
	private static final String AS_LIST = "asList";
	private static final String ASP_FACTORY_LIST = "aspFactoryList";
	private static final String DPC_VS_AS_LIST = "route";

	private static final String M3UA_PERSIST_DIR_KEY = "m3ua.persist.dir";
	private static final String USER_DIR_KEY = "user.dir";
	private static final String PERSIST_FILE_NAME = "m3ua.xml";

	private static final M3UAXMLBinding binding = new M3UAXMLBinding();
	private static final String TAB_INDENT = "\t";
	private static final String CLASS_ATTRIBUTE = "type";

	private static final String KEY_SEPARATOR = ":";
	private static final int WILDCARD = -1;

	protected FastList<As> appServers = new FastList<As>();
	protected FastList<AspFactory> aspfactories = new FastList<AspFactory>();

	protected M3UAScheduler m3uaScheduler = new M3UAScheduler();

	private final TextBuilder persistFile = TextBuilder.newInstance();

	private final String name;

	private String persistDir = null;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
	protected MessageFactory messageFactory = new MessageFactoryImpl();

	protected Management transportManagement = null;

	private ScheduledExecutorService fsmTicker;

	private RouteMap<String, As[]> route = new RouteMap<String, As[]>();

	private int maxAsForRoute = 4;

	public M3UAManagement(String name) {
		this.name = name;
		binding.setClassAttribute(CLASS_ATTRIBUTE);
		binding.setAlias(AspFactory.class, "aspFactory");
		binding.setAlias(As.class, "as");
		binding.setAlias(Asp.class, "asp");

	}

	public String getName() {
		return name;
	}

	public String getPersistDir() {
		return persistDir;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
	}

	public int getMaxAsForRoute() {
		return maxAsForRoute;
	}

	public void setMaxAsForRoute(int maxAsForRoute) {
		this.maxAsForRoute = maxAsForRoute;
	}

	public Management getTransportManagement() {
		return transportManagement;
	}

	public void setTransportManagement(Management transportManagement) {
		this.transportManagement = transportManagement;
	}

	public void start() throws Exception {

		if (this.transportManagement == null) {
			throw new NullPointerException("TransportManagement is null");
		}
		
		if(maxAsForRoute <1 || maxAsForRoute > 4){
			throw new Exception("Max AS for a route can be minimum 1 or maximum 4");
		}
		
		super.start();

		this.persistFile.clear();

		if (persistDir != null) {
			this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_")
					.append(PERSIST_FILE_NAME);
		} else {
			persistFile.append(System.getProperty(M3UA_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
		}

		logger.info(String.format("M3UA configuration file path %s", persistFile.toString()));

		binding.setM3uaManagement(this);

		try {
			this.load();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 configuration file. \n%s", e.getMessage()));
		}

		fsmTicker = Executors.newSingleThreadScheduledExecutor();
		fsmTicker.scheduleAtFixedRate(m3uaScheduler, 500, 500, TimeUnit.MILLISECONDS);

		logger.info("Started M3UAManagement");
	}

	public void stop() throws Exception {
		super.stop();

		this.store();
		fsmTicker.shutdown();
	}

	public FastList<As> getAppServers() {
		return appServers;
	}

	public FastList<AspFactory> getAspfactories() {
		return aspfactories;
	}

	public FastMap<String, As[]> getRoute() {
		return this.route;
	}

	protected As getAs(String asName) {
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			if (as.getName().equals(asName)) {
				return as;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Create new {@link As}
	 * </p>
	 * <p>
	 * Command is m3ua as create <as-name> <AS | SGW | IPSP> mode <SE | DE>
	 * ipspType < client | server > rc <routing-context> traffic-mode <traffic
	 * mode>
	 * </p>
	 * <p>
	 * where mode is optional, by default SE. ipspType should be specified if
	 * type is IPSP. rc is optional and traffi-mode is also optional, default is
	 * Loadshare
	 * </p>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public As createAs(String asName, Functionality functionality, ExchangeType exchangeType, IPSPType ipspType,
			RoutingContext rc, TrafficModeType trafficMode, NetworkAppearance na) throws Exception {

		As as = this.getAs(asName);
		if (as != null) {
			throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, asName));
		}

		// TODO check if RC is already taken?

		if (exchangeType == null) {
			exchangeType = ExchangeType.SE;
		}

		if (functionality == Functionality.IPSP && ipspType == null) {
			ipspType = IPSPType.CLIENT;
		}

		as = new As(asName, rc, trafficMode, functionality, exchangeType, ipspType, na);
		as.setM3UAManagement(this);
		FSM localFSM = as.getLocalFSM();
		this.m3uaScheduler.execute(localFSM);

		FSM peerFSM = as.getPeerFSM();
		this.m3uaScheduler.execute(peerFSM);

		appServers.add(as);

		this.store();

		return as;
	}

	public As destroyAs(String asName) throws Exception {
		As as = this.getAs(asName);
		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		if (as.getAspList().size() != 0) {
			throw new Exception(String.format(
					"As=%s still has ASP's assigned. Unassign Asp's before destroying this As", asName));
		}

		for (FastMap.Entry<String, As[]> e = this.route.head(), end = this.route.tail(); (e = e.getNext()) != end;) {
			As[] asList = e.getValue();
			for (int count = 0; count < asList.length; count++) {
				As asTemp = asList[count];
				if (asTemp != null && asTemp.equals(as)) {
					throw new Exception(String.format("As=%s used in route=%s. Remove from route", asName, e.getKey()));
				}
			}
		}

		FSM asLocalFSM = as.getLocalFSM();
		if (asLocalFSM != null) {
			asLocalFSM.cancel();
		}

		FSM asPeerFSM = as.getPeerFSM();
		if (asPeerFSM != null) {
			asPeerFSM.cancel();
		}

		appServers.remove(as);

		this.store();

		return as;
	}

	/**
	 * Create new {@link AspFactory}
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public AspFactory createAspFactory(String aspName, String associationName) throws Exception {
		AspFactory factory = this.getAspFactory(aspName);

		if (factory != null) {
			throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, aspName));
		}

		Association association = this.transportManagement.getAssociation(associationName);
		if (association == null) {
			throw new Exception(String.format("No Association found for name=%s", associationName));
		}

		if (association.isStarted()) {
			throw new Exception(String.format("Association=%s is started", associationName));
		}

		if (association.getAssociationListener() != null) {
			throw new Exception(String.format("Association=%s is already associated", associationName));
		}

		factory = new AspFactory(aspName);
		factory.setAssociation(association);
		factory.setTransportManagement(this.transportManagement);

		aspfactories.add(factory);

		this.store();

		return factory;
	}

	public AspFactory destroyAspFactory(String aspName) throws Exception {
		AspFactory aspFactroy = this.getAspFactory(aspName);
		if (aspFactroy == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_ASP, aspName));
		}

		if (aspFactroy.getAspList().size() != 0) {
			throw new Exception("Asp are still assigned to As. Unassign all");
		}
		aspFactroy.unsetAssociation();
		this.aspfactories.remove(aspFactroy);
		this.store();

		return aspFactroy;
	}

	/**
	 * Associate {@link Asp} to {@link As}
	 * 
	 * @param asName
	 * @param aspName
	 * @return
	 * @throws Exception
	 */
	public Asp assignAspToAs(String asName, String aspName) throws Exception {
		// check ASP and AS exist with given name
		As as = this.getAs(asName);

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		AspFactory aspFactroy = this.getAspFactory(aspName);

		if (aspFactroy == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_ASP, aspName));
		}

		// If ASP already assigned to AS we don't want to re-assign
		for (FastList.Node<Asp> n = as.getAspList().head(), end = as.getAspList().tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			if (asp.getName().equals(aspName)) {
				throw new Exception(String.format(
						"Cannot assign ASP=%s to AS=%s. This ASP is already assigned to this AS", aspName, asName));
			}
		}

		FastList<Asp> asps = aspFactroy.getAspList();

		// Checks for RoutingContext. We know that for null RC there will always
		// be a single ASP assigned to AS and ASP cannot be shared
		if (as.getRoutingContext() == null) {
			// If AS has Null RC, this should be the first assignment of ASP to
			// AS
			if (asps.size() != 0) {
				throw new Exception(String.format(
						"Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS.", aspName, asName));
			}
		} else if (asps.size() > 0) {
			// RoutingContext is not null, make sure there is no ASP that is
			// assigned to AS with null RC
			Asp asp = asps.get(0);
			if (asp != null && asp.getAs().getRoutingContext() == null) {
				throw new Exception(
						String.format(
								"Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS which has null RoutingContext.",
								aspName, asName));
			}
		}

		if (aspFactroy.getFunctionality() != null && aspFactroy.getFunctionality() != as.getFunctionality()) {
			throw new Exception(String.format(
					"Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of type=%s", aspName,
					asName, aspFactroy.getFunctionality()));
		}

		if (aspFactroy.getExchangeType() != null && aspFactroy.getExchangeType() != as.getExchangeType()) {
			throw new Exception(String.format(
					"Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of ExchangeType=%s",
					aspName, asName, aspFactroy.getExchangeType()));
		}

		if (aspFactroy.getIpspType() != null && aspFactroy.getIpspType() != as.getIpspType()) {
			throw new Exception(String.format(
					"Cannot assign ASP=% to AS=%. This ASP is already assigned to other AS of which has IPSP type=%s",
					aspName, asName, aspFactroy.getIpspType()));
		}

		aspFactroy.setExchangeType(as.getExchangeType());
		aspFactroy.setFunctionality(as.getFunctionality());
		aspFactroy.setIpspType(as.getIpspType());

		Asp asp = aspFactroy.createAsp();
		FSM aspLocalFSM = asp.getLocalFSM();
		m3uaScheduler.execute(aspLocalFSM);

		FSM aspPeerFSM = asp.getPeerFSM();
		m3uaScheduler.execute(aspPeerFSM);
		as.addAppServerProcess(asp);

		this.store();

		return asp;
	}

	public Asp unassignAspFromAs(String asName, String aspName) throws Exception {
		// check ASP and AS exist with given name
		As as = this.getAs(asName);

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		Asp asp = as.removeAppServerProcess(aspName);
		asp.getAspFactory().destroyAsp(asp);
		this.store();
		return asp;
	}

	/**
	 * This method should be called by management to start the ASP
	 * 
	 * @param aspName
	 *            The name of the ASP to be started
	 * @throws Exception
	 */
	public void startAsp(String aspName) throws Exception {
		AspFactory aspFactory = this.getAspFactory(aspName);

		if (aspFactory == null) {
			throw new Exception(String.format("No ASP found by name=%s", aspName));
		}

		if (aspFactory.getStatus()) {
			throw new Exception(String.format("ASP name=%s already started", aspName));
		}

		if (aspFactory.getAspList().size() == 0) {
			throw new Exception(String.format("ASP name=%s not assigned to any AS yet", aspName));
		}

		aspFactory.start();
		this.store();
	}

	/**
	 * This method should be called by management to stop the ASP
	 * 
	 * @param aspName
	 *            The name of the ASP to be stopped
	 * @throws Exception
	 */
	public void stopAsp(String aspName) throws Exception {
		AspFactory aspFactory = this.getAspFactory(aspName);

		if (aspFactory == null) {
			throw new Exception(String.format("No ASP found by name=%s", aspName));
		}

		if (!aspFactory.getStatus()) {
			throw new Exception(String.format("ASP name=%s already stopped", aspName));
		}

		aspFactory.stop();
		this.store();
	}

	public void addRoute(int dpc, int opc, int si, String asName) throws Exception {
		As as = null;
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			if (n.getValue().getName().compareTo(asName) == 0) {
				as = n.getValue();
				break;
			}
		}

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();

		As[] asArray = route.get(key);

		if (asArray != null) {
			// check is this As is already added
			for (int count = 0; count < asArray.length; count++) {
				As asTemp = asArray[count];
				if (asTemp != null && as.equals(asTemp)) {
					throw new Exception(String.format("As=%s already added for dpc=%d opc=%d si=%d", as.getName(), dpc,
							opc, si));
				}
			}
		} else {
			asArray = new As[this.maxAsForRoute];
			route.put(key, asArray);
		}

		// Add to first empty slot
		for (int count = 0; count < asArray.length; count++) {
			if (asArray[count] == null) {
				asArray[count] = as;
				this.store();
				return;
			}

		}

		throw new Exception(String.format("dpc=%d opc=%d si=%d combination already has maximum possible As",
				as.getName(), dpc, opc, si));
	}

	public void removeRoute(int dpc, int opc, int si, String asName) throws Exception {
		As as = null;
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			if (n.getValue().getName().compareTo(asName) == 0) {
				as = n.getValue();
				break;
			}
		}

		if (as == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
		}

		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();

		As[] asArray = route.get(key);

		if (asArray == null) {
			throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", as.getName(), dpc, opc,
					si));
		}

		for (int count = 0; count < asArray.length; count++) {
			As asTemp = asArray[count];
			if (asTemp != null && as.equals(asTemp)) {
				asArray[count] = null;
				this.store();
				return;
			}
		}

		throw new Exception(String.format("No AS=%s configured  for dpc=%d opc=%d si=%d", as.getName(), dpc, opc, si));
	}

	public void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
		super.sendTransferMessageToLocalUser(msg, seqControl);
	}

	public void sendPauseMessageToLocalUser(Mtp3PausePrimitive msg) {
		super.sendPauseMessageToLocalUser(msg);
	}

	public void sendResumeMessageToLocalUser(Mtp3ResumePrimitive msg) {
		super.sendResumeMessageToLocalUser(msg);
	}

	public void sendStatusMessageToLocalUser(Mtp3StatusPrimitive msg) {
		super.sendStatusMessageToLocalUser(msg);
	}

	private AspFactory getAspFactory(String aspName) {
		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory aspFactory = n.getValue();
			if (aspFactory.getName().equals(aspName)) {
				return aspFactory;
			}
		}
		return null;
	}

	private As getAsForRoute(int dpc, int opc, int si, int sls) {
		String key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR).append(si))
				.toString();
		As[] asArray = route.get(key);

		if (asArray == null) {
			key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opc).append(KEY_SEPARATOR)
					.append(WILDCARD)).toString();

			asArray = route.get(key);

			if (asArray == null) {
				key = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(WILDCARD).append(KEY_SEPARATOR)
						.append(WILDCARD)).toString();

				asArray = route.get(key);
			}
		}

		if (asArray == null) {
			return null;
		}

		for (int count = 0; count < asArray.length; count++) {
			As as = asArray[count];

			FSM fsm = null;
			if (as != null) {
				if (as.getFunctionality() == Functionality.AS
						|| (as.getFunctionality() == Functionality.SGW && as.getExchangeType() == ExchangeType.DE)
						|| (as.getFunctionality() == Functionality.IPSP && as.getExchangeType() == ExchangeType.DE)
						|| (as.getFunctionality() == Functionality.IPSP && as.getExchangeType() == ExchangeType.SE && as
								.getIpspType() == IPSPType.CLIENT)) {
					fsm = as.getPeerFSM();
				} else {
					fsm = as.getLocalFSM();
				}

				AsState asState = AsState.getState(fsm.getState().getName());

				if (asState == AsState.ACTIVE) {
					return as;
				}

			}// if (as != null)
		}// for

		return null;
	}

	/**
	 * Persist
	 */
	public void store() {

		// TODO : Should we keep reference to Objects rather than recreating
		// everytime?
		try {
			XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);
			writer.write(aspfactories, ASP_FACTORY_LIST, FastList.class);
			writer.write(appServers, AS_LIST, FastList.class);
			writer.write(route, DPC_VS_AS_LIST, RouteMap.class);

			writer.close();
		} catch (Exception e) {
			logger.error("Error while persisting the Rule state in file", e);
		}
	}

	/**
	 * Load and create LinkSets and Link from persisted file
	 * 
	 * @throws Exception
	 */
	public void load() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

			reader.setBinding(binding);
			aspfactories = reader.read(ASP_FACTORY_LIST, FastList.class);
			appServers = reader.read(AS_LIST, FastList.class);
			route = reader.read(DPC_VS_AS_LIST, RouteMap.class);

			// Create Asp's and assign to each of the AS. Schedule the FSM's
			for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
				As as = n.getValue();
				as.setM3UAManagement(this);
				FSM asLocalFSM = as.getLocalFSM();
				m3uaScheduler.execute(asLocalFSM);

				FSM asPeerFSM = as.getPeerFSM();
				m3uaScheduler.execute(asPeerFSM);

				// All the Asp's for this As added in temp list
				FastList<Asp> tempAsp = new FastList<Asp>();
				tempAsp.addAll(as.getAspList());

				// Claer Asp's from this As
				as.getAspList().clear();

				for (FastList.Node<Asp> n1 = tempAsp.head(), end1 = tempAsp.tail(); (n1 = n1.getNext()) != end1;) {
					Asp asp = n1.getValue();

					try {
						// Now let the Asp's be created from respective
						// AspFactory and added to As
						this.assignAspToAs(as.getName(), asp.getName());
					} catch (Exception e) {
						logger.error("Error while assigning Asp to As on loading from xml file", e);
					}
				}
			}

			// Set the transportManagement
			for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
				AspFactory factory = n.getValue();
				factory.setTransportManagement(this.transportManagement);
				try {
					factory.setAssociation(this.transportManagement.getAssociation(factory.associationName));
				} catch (Exception e1) {
					logger.error(String.format(
							"Error setting Assciation=%s for the AspFactory=%s while loading from XML",
							factory.associationName, factory.getName()), e1);
				}

				if (factory.getStatus()) {
					try {
						factory.start();
					} catch (Exception e) {
						logger.error(
								String.format("Error starting the AspFactory=%s while loading from XML",
										factory.getName()), e);
					}
				}
			}

		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}

	@Override
	public void sendMessage(Mtp3TransferPrimitive mtp3TransferPrimitive) throws IOException {
		ProtocolData data = this.parameterFactory.createProtocolData(mtp3TransferPrimitive);
		PayloadData payload = (PayloadData) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
				MessageType.PAYLOAD);
		payload.setData(data);

		As as = this.getAsForRoute(data.getDpc(), data.getOpc(), data.getSI(), data.getSLS());
		if (as == null) {
			logger.error(String.format("Tx : No AS found for routing message %s", payload));
			return;
		}
		payload.setNetworkAppearance(as.getNetworkAppearance());
		payload.setRoutingContext(as.getRoutingContext());
		as.write(payload);
	}
}
