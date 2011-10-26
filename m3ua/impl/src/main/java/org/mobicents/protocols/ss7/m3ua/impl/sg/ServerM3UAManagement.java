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

package org.mobicents.protocols.ss7.m3ua.impl.sg;

import java.io.FileNotFoundException;
import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.sctp.Association;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.as.LocalAspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.router.ServerM3UARouter;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class ServerM3UAManagement extends M3UAManagement {

	private static final Logger logger = Logger.getLogger(ServerM3UAManagement.class);

	protected ServerM3UARouter m3uaRouter = new ServerM3UARouter();

	protected MessageFactory messageFactory = new MessageFactoryImpl();
	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

	/**
	 * 
	 */
	public ServerM3UAManagement() {
		this.PERSIST_FILE_NAME = "m3ua-server.xml";
	}

	/**
	 * Expected command is m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list>
	 * si <si-list> traffic-mode {broadcast|loadshare|override} <ras-name>
	 * 
	 * opc, si and traffic-mode is not compulsory
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public As createAppServer(String args[]) throws Exception {

		if (args.length < 9) {
			// minimum 8 args needed
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String rcKey = args[3];
		if (rcKey == null || rcKey.compareTo("rc") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String rc = args[4];
		if (rc == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		RoutingContext rcObj = this.parameterFactory.createRoutingContext(new long[] { Long.parseLong(rc) });

		// Routing Key
		if (args[5] == null || args[5].compareTo("rk") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[6] == null || args[6].compareTo("dpc") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		int dpc = Integer.parseInt(args[7]);
		DestinationPointCode dspobj = this.parameterFactory.createDestinationPointCode(dpc, (short) 0);
		OPCList opcListObj = null;
		ServiceIndicators si = null;
		TrafficModeType trMode = null;
		String name = null;

		if (args[8] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args.length >= 11) {
			// OPC also defined
			if (args[8].compareTo("opc") == 0) {
				// comma separated OPC list
				String opcListStr = args[9];
				if (opcListStr == null) {
					throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
				}

				String[] opcListArr = opcListStr.split(",");
				int[] opcs = new int[opcListArr.length];
				short[] masks = new short[opcListArr.length];

				for (int count = 0; count < opcListArr.length; count++) {
					opcs[count] = Integer.parseInt(opcListArr[count]);
					masks[count] = 0; // TODO mask should be sent in command
				}

				opcListObj = this.parameterFactory.createOPCList(opcs, masks);

				if (args.length >= 13) {
					if (args[10].compareTo("si") == 0) {
						si = this.createSi(args, 11);
						if (args[12] == null) {
							throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
						}

						if (args.length == 15) {
							if (args[12].compareTo("traffic-mode") != 0) {
								throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
							}
							trMode = createTrMode(args, 13);
							name = args[14];
						} else {
							name = args[12];
						}

					} else if (args[10].compareTo("traffic-mode") == 0) {
						trMode = createTrMode(args, 11);
						name = args[12];
					} else {
						throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
					}

				} else {
					name = args[10];
				}

			} else if (args[8].compareTo("si") == 0) {
				si = this.createSi(args, 9);

				if (args[10] == null) {
					throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
				}

				if (args.length == 13) {
					if (args[10].compareTo("traffic-mode") != 0) {
						throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
					}
					trMode = createTrMode(args, 11);
					name = args[12];
				}

			} else if (args[8].compareTo("traffic-mode") == 0) {
				trMode = this.createTrMode(args, 9);
				name = args[10];

			} else {
				throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
			}

		} else {
			name = args[8];
		}

		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			if (as.getName().compareTo(name) == 0) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, name));
			}
			// TODO : Check for duplication of RoutingKey
		}
		RoutingKey rk = this.parameterFactory.createRoutingKey(null, rcObj, trMode, null,
				new DestinationPointCode[] { dspobj }, si != null ? new ServiceIndicators[] { si } : null,
				opcListObj != null ? new OPCList[] { opcListObj } : null);
		As as = new As(name, rcObj, rk, trMode, Functionality.SGW);
		as.setM3UAManagement(this);
		m3uaRouter.addRk(rk, as);

		m3uaScheduler.execute(as.getFSM());
		appServers.add(as);

		this.store();

		return as;

	}

	/**
	 * Command to create ASPFactory is
	 * "m3ua rasp create <asp-name> <assoc-name>"
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public AspFactory createAspFactory(String[] args) throws Exception {
		if (args.length != 5) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[3] == null || args[4] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String name = args[3];

		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory fact = n.getValue();
			if (fact.getName().compareTo(name) == 0) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, name));
			}
		}

		String associationName = args[4];
		Association association = this.sctpManagement.getAssociation(associationName);

		if (association == null) {
			throw new Exception(String.format("No Association found for name=%s", associationName));
		}

		if (association.isStarted()) {
			throw new Exception(String.format("Association=%s is started", associationName));
		}

		if (association.getAssociationListener() != null) {
			throw new Exception(String.format("Association=%s is already associated", associationName));
		}

		AspFactory factory = new RemAspFactory(name);
		aspfactories.add(factory);
		factory.setAssociation(association);

		this.store();
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement#startAsp(java.lang
	 * .String)
	 */
	@Override
	public void managementStartAsp(String aspName) throws Exception {
		RemAspFactory remAspFactory = (RemAspFactory) this.getAspFactory(aspName);

		if (remAspFactory == null) {
			throw new Exception(String.format("No ASP found by name=%s", aspName));
		}

		// If the AspList for this AsFactory is zero means yet no ASP is created
		if (remAspFactory.getAspList().size() == 0) {
			throw new Exception(String.format("ASP name=%s not assigned to any AS", aspName));
		}

		if (remAspFactory.getStatus()) {
			throw new Exception(String.format("ASP name=%s already started", aspName));
		}

		remAspFactory.start();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement#stopAsp(java.lang
	 * .String)
	 */
	@Override
	public void managementStopAsp(String aspName) throws Exception {
		AspFactory aspFact = this.getAspFactory(aspName);

		if (aspFact == null) {
			throw new Exception(String.format("No ASP found by name %s", aspName));
		}

		aspFact.stop();

		this.store();

		logger.info(String.format("Stopped ASP name=%s ", aspFact.getName()));	}

	private ServiceIndicators createSi(String args[], int index) throws Exception {
		ServiceIndicators si = null;

		String siStr = args[index++];
		if (siStr == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String[] sitArr = siStr.split(",");
		short[] sis = new short[sitArr.length];

		for (int count = 0; count < sis.length; count++) {
			sis[count] = Short.parseShort(sitArr[count]);
		}

		si = this.parameterFactory.createServiceIndicators(sis);
		return si;

	}

	private TrafficModeType createTrMode(String[] args, int index) throws Exception {
		if (args[index] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[index].compareTo("broadcast") == 0) {
			return this.parameterFactory.createTrafficModeType(TrafficModeType.Broadcast);
		} else if (args[index].compareTo("override") == 0) {
			return this.parameterFactory.createTrafficModeType(TrafficModeType.Override);
		} else if (args[index].compareTo("loadshare") == 0) {
			return this.parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
		}
		throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
	}

	@Override
	public void load() throws FileNotFoundException {
		super.load();
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			try {
				this.m3uaRouter.addRk(as.getRoutingKey(), as);
			} catch (Exception e) {
				logger.error("Error while pupoulating Server Router loading from xml file", e);
			}
		}
	}

	@Override
	public void sendMessage(Mtp3TransferPrimitive mtp3) throws IOException {
		ProtocolData data = this.parameterFactory.createProtocolData(mtp3);
		PayloadData payload = (PayloadData) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
				MessageType.PAYLOAD);
		payload.setData(data);

		As as = this.m3uaRouter.getAs(data.getDpc(), data.getOpc(), (short) data.getSI());
		if (as != null && (as.getState() == AsState.ACTIVE || as.getState() == AsState.PENDING)) {
			payload.setRoutingContext(as.getRoutingContext());
			as.write(payload);
		} else {
			logger.error(String.format("No AS found for this message. Dropping message %s", payload));
		}

		payload.setRoutingContext(as.getRoutingContext());
		as.write(payload);

	}
}
