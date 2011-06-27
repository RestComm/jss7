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

package org.mobicents.protocols.ss7.m3ua.impl.as;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.sctp.SctpChannel;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * @author amit bhayani
 * 
 */
public class ClientM3UAManagement extends M3UAManagement {

	private static final Logger logger = Logger.getLogger(ClientM3UAManagement.class);

	private static final String DPC_VS_AS_LIST = "dpcVsAsList";

	private static final String CLIENTROUTE_PERSIST_FILE_NAME = "m3ua-clientroute.xml";

	private final TextBuilder clientRoutePersistFile = TextBuilder.newInstance();

	protected M3UASelector selector;

	// Stores DPC vs List of AS such that this DPC can by served by List of AS's
	private FastMap<Integer, FastList<As>> dpcRoute = new FastMap<Integer, FastList<As>>();

	// Stores DPC vs List of AS names. This is explicitly for persisting to xml
	private FastMap<Integer, FastList<String>> dpcVsAsName = new FastMap<Integer, FastList<String>>();

	/**
	 * 
	 */
	public ClientM3UAManagement() {
		this.PERSIST_FILE_NAME = "m3ua-client.xml";
	}

	@Override
	public void start() throws IOException {
		selector = m3uaProvider.openSelector();

		super.start();

		this.clientRoutePersistFile.clear();

		if (persistDir != null) {
			this.clientRoutePersistFile.append(persistDir).append(File.separator).append(CLIENTROUTE_PERSIST_FILE_NAME);
		} else {
			this.clientRoutePersistFile
					.append(System.getProperty(M3UA_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
					.append(File.separator).append(CLIENTROUTE_PERSIST_FILE_NAME);
		}

		logger.info(String.format("M3UA client route file path %s", clientRoutePersistFile.toString()));

		try {
			this.loadRoute();
		} catch (FileNotFoundException e) {
			logger.warn(String.format("Failed to load the SS7 client route file. \n%s", e.getMessage()));
		}
	}

	@Override
	public void stop() throws IOException {
		super.stop();

		// selector.close();

		this.storeRoute();
	}

	public FastMap<Integer, FastList<String>> getDpcVsAsName() {
		return dpcVsAsName;
	}

	/**
	 * Expected command is m3ua route remove <dpc> <as-name>
	 * 
	 * @param dpc
	 * @param asName
	 * @throws Exception
	 */
	public void removeRouteAsForDpc(int dpc, String asName) throws Exception {

		if (!dpcRoute.containsKey(dpc)) {
			throw new Exception(String.format(M3UAOAMMessages.NO_ROUTE_DEFINED_FOR_DPC, dpc));
		}

		FastList<As> asList = dpcRoute.get(dpc);
		FastList<String> asNames = dpcVsAsName.get(dpc);

		if (asList == null || asList.size() == 0) {
			throw new Exception(String.format(M3UAOAMMessages.NO_ROUTE_DEFINED_FOR_DPC, dpc));
		}

		As asTemp = null;
		for (FastList.Node<As> n = asList.head(), end = asList.tail(); (n = n.getNext()) != end;) {
			As value = n.getValue();
			if (value.getName().equals(asName)) {
				asTemp = value;
				break;
			}
		}

		if (asTemp == null) {
			throw new Exception(String.format(M3UAOAMMessages.NO_AS_ROUTE_FOR_DPC, asName, dpc));
		}

		asList.remove(asTemp);
		asNames.remove(asName);

		this.storeRoute();

	}

	/**
	 * Expected command is m3ua route add <dpc> <as-name>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public void addRouteAsForDpc(int dpc, String asName) throws Exception {
		As asTemp = null;
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			if (as.getName().equals(asName)) {
				asTemp = as;
				break;
			}
		}

		if (asTemp == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ROUTE_AS_FOR_DPC_FAIL_NO_AS, asName));
		}

		FastList<As> asList = dpcRoute.get(dpc);
		FastList<String> asNames = dpcVsAsName.get(dpc);

		if (asList == null) {
			asList = new FastList<As>();
			dpcRoute.put(dpc, asList);

			asNames = new FastList<String>();
			dpcVsAsName.put(dpc, asNames);
		}

		for (FastList.Node<As> n = asList.head(), end = asList.tail(); (n = n.getNext()) != end;) {
			As value = n.getValue();
			if (value.getName().equals(asName)) {
				throw new Exception(String.format(M3UAOAMMessages.ROUTE_AS_FOR_DPC_EXIST, asName, dpc));
			}
		}

		asList.add(asTemp);
		asNames.add(asName);

		this.storeRoute();

	}

	public As getAsForDpc(int dpc, int sls) {
		FastList<As> asList = dpcRoute.get(dpc);

		if (asList == null) {
			return null;
		}
		// TODO Based on SLS, select the As for load-balancing
		for (FastList.Node<As> n = asList.head(), end = asList.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			if (as.getState() == AsState.ACTIVE) {
				return as;
			}
		}

		return null;
	}

	/**
	 * Expected command is m3ua as create rc <rc> <as-name>
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public As createAppServer(String args[]) throws Exception {

		if (args.length < 6) {
			// minimum 6 args needed
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String rcKey = args[3];
		if (rcKey == null || !rcKey.equals("rc") ) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String rc = args[4];
		if (rc == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		RoutingContext rcObj = m3uaProvider.getParameterFactory().createRoutingContext(
				new long[] { Long.parseLong(rc) });

		String name = args[5];

		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			if (as.getName().equals(name)) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, name));
			}
		}

		AsImpl as = new AsImpl(name, rcObj, null, null, this.m3uaProvider);
		m3uaScheduler.execute(as.getFSM());
		appServers.add(as);

		this.store();

		return as;

	}

	/**
	 * Command to craete ASPFactory is "m3ua asp create ip <local-ip> port
	 * <local-port> remip <remip> remport <remport> <asp-name>"
	 * 
	 * @param args
	 * @return
	 */
	public AspFactory createAspFactory(String[] args) throws Exception {

		if (args.length != 12) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[3] == null || args[3].compareTo("ip") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[4] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}
		String ip = args[4];

		if (args[5] == null || args[5].compareTo("port") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		int port = Integer.parseInt(args[6]);

		if (args[7] == null || args[7].compareTo("remip") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[8] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}
		String remIp = args[8];

		if (args[9] == null || args[9].compareTo("remport") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		int remPort = Integer.parseInt(args[10]);

		if (args[11] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String name = args[11];

		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory fact = n.getValue();
			if (fact.getName().compareTo(name) == 0) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, name));
			}

			if (fact.getIp().compareTo(ip) == 0 && fact.getPort() == port) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_IPPORT_EXIST, ip, port));
			}
		}

		AspFactory factory = new LocalAspFactory(name, ip, port, remIp, remPort, this.m3uaProvider);
		aspfactories.add(factory);

		this.store();

		return factory;
	}

	public void startAsp(String aspName) throws Exception {

		LocalAspFactory localAspFact = (LocalAspFactory) this.getAspFactory(aspName);

		if (localAspFact == null) {
			throw new Exception(String.format("No ASP found by name=%s", aspName));
		}

		// If the AspList for this AsFactory is zero means yet no ASP is created
		if (localAspFact.getAspList().size() == 0) {
			throw new Exception(String.format("ASP name=%s not assigned to any AS", aspName));
		}

		M3UAChannel channel = this.m3uaProvider.openChannel();
		channel.bind(new InetSocketAddress(localAspFact.getIp(), localAspFact.getPort()));

		M3UASelectionKey skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		skey.attach(localAspFact);

		channel.connect(new InetSocketAddress(localAspFact.getRemIp(), localAspFact.getRemPort()));

		if (channel.isConnectionPending()) {

			// TODO Loop? Or may be sleep for while?
			while (!channel.isConnected()) {
				channel.finishConnect();
			}
		}

		localAspFact.setChannel(channel);
		((SctpChannel) channel).setNotificationHandler(localAspFact.getAssociationHandler());

		localAspFact.start();
		logger.info(String.format("Started ASP name=%s local-ip=%s local-pot=%d rem-ip=%s rem-port=%d",
				localAspFact.getName(), localAspFact.getIp(), localAspFact.getPort(), localAspFact.getRemIp(),
				localAspFact.getRemPort()));
		this.store();

		localAspFact.onCommStateChange(CommunicationState.UP);
	}

	public void stopAsp(String aspName) throws Exception {
		LocalAspFactory localAspFact = (LocalAspFactory) this.getAspFactory(aspName);

		if (localAspFact == null) {
			throw new Exception(String.format("No ASP found by name %s", aspName));
		}

		localAspFact.stop();
		this.store();
	}

	private AspFactory getAspFactory(String aspName) {
		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory aspFactory = n.getValue();
			if (aspFactory.getName().compareTo(aspName) == 0) {
				return aspFactory;
			}
		}
		return null;
	}

	/**
	 * Persist
	 */
	public void storeRoute() {

		// TODO : Should we keep reference to Objects rather than recreating
		// everytime?
		try {
			XMLObjectWriter writer = XMLObjectWriter
					.newInstance(new FileOutputStream(clientRoutePersistFile.toString()));
			writer.setBinding(binding);
			// Enables cross-references.
			// writer.setReferenceResolver(new XMLReferenceResolver());
			writer.setIndentation(TAB_INDENT);
			writer.write(dpcVsAsName, DPC_VS_AS_LIST, FastMap.class);

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
	public void loadRoute() throws FileNotFoundException {

		XMLObjectReader reader = null;
		try {
			reader = XMLObjectReader.newInstance(new FileInputStream(clientRoutePersistFile.toString()));
			reader.setBinding(binding);
			dpcVsAsName = reader.read(DPC_VS_AS_LIST, FastMap.class);

			// Lets populate the Route
			for (FastMap.Entry<Integer, FastList<String>> e = dpcVsAsName.head(), end = dpcVsAsName.tail(); (e = e
					.getNext()) != end;) {
				Integer dpc = e.getKey();
				FastList<String> appServers = e.getValue();

				for (FastList.Node<String> n = appServers.head(), end1 = appServers.tail(); (n = n.getNext()) != end1;) {
					String asName = n.getValue();

					try {
						this.addRouteAsForDpc(dpc, asName);
					} catch (Exception e1) {
						logger.error("Error while loading client route from xml", e1);
					}
				}
			}

		} catch (XMLStreamException ex) {
			// this.logger.info(
			// "Error while re-creating Linksets from persisted file", ex);
		}
	}
}
