/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.util.Set;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.Sgp;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.mobicents.protocols.ss7.m3ua.impl.sctp.SctpChannel;
import org.mobicents.protocols.ss7.m3ua.impl.sctp.SctpProvider;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SgpImpl implements Sgp {

	private static Logger logger = Logger.getLogger(SgpImpl.class);

	private FastList<As> appServers = new FastList<As>();
	private FastList<AspFactory> aspfactories = new FastList<AspFactory>();

	private String address;
	private int port;

	private M3UAProvider m3uaProvider;
	private M3UAServerChannel serverChannel;
	private M3UASelector selector;

	private boolean started = false;

	private M3UAScheduler m3uaScheduler = new M3UAScheduler();

	public SgpImpl(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public PayloadData poll() {
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			PayloadData payload = as.poll();
			if (payload != null) {
				return payload;
			}
		}
		return null;
	}

	public void send(byte[] msu) throws IOException {
		ProtocolData data = m3uaProvider.getParameterFactory().createProtocolData(0, msu);

		PayloadData payload = (PayloadData) this.m3uaProvider.getMessageFactory().createMessage(
				MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
		payload.setData(data);

		// TODO : Algo to select correct AS depending on above ProtocolData and
		// Routing Key. Also check if AS is ACTIVE else throw error?
		As as = this.appServers.get(0);
		if (as != null && as.getState() == AsState.ACTIVE || as.getState() == AsState.PENDING) {
			payload.setRoutingContext(as.getRoutingContext());
			as.write(payload);
		} else {
			logger.error(String.format("Dropping message %s as AS=%s", payload, as));
		}

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

		RoutingContext rcObj = m3uaProvider.getParameterFactory().createRoutingContext(
				new long[] { Long.parseLong(rc) });

		// Routing Key
		if (args[5] == null || args[5].compareTo("rk") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[6] == null || args[6].compareTo("dpc") != 0) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		int dpc = Integer.parseInt(args[7]);
		DestinationPointCode dspobj = m3uaProvider.getParameterFactory().createDestinationPointCode(dpc, (short) 0);
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

				opcListObj = m3uaProvider.getParameterFactory().createOPCList(opcs, masks);

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
		RoutingKey rk = m3uaProvider.getParameterFactory().createRoutingKey(null, rcObj, trMode, null,
				new DestinationPointCode[] { dspobj }, si != null ? new ServiceIndicators[] { si } : null,
				opcListObj != null ? new OPCList[] { opcListObj } : null);
		RemAsImpl as = new RemAsImpl(name, rcObj, rk, trMode, this.m3uaProvider);
		m3uaScheduler.execute(as.getFSM());
		appServers.add(as);
		return as;

	}

	/**
	 * Command to create ASPFactory is "m3ua rasp create ip <ip> port <port>
	 * <asp-name>"
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public AspFactory createAspFactory(String[] args) throws Exception {
		if (args.length != 8) {
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

		if (args[7] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		String name = args[7];

		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			AspFactory fact = n.getValue();
			if (fact.getName().compareTo(name) == 0) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, name));
			}

			if (fact.getIp().compareTo(ip) == 0 && fact.getPort() == port) {
				throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_IPPORT_EXIST, ip, port));
			}
		}
		AspFactory factory = new RemAspFactory(name, ip, port, this.m3uaProvider);
		aspfactories.add(factory);
		return factory;
	}

	public Asp assignAspToAs(String asName, String aspName) throws Exception {
		// check ASP and AS exist with given name
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

		AspFactory aspFactroy = null;
		for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
			if (n.getValue().getName().compareTo(aspName) == 0) {
				aspFactroy = n.getValue();
				break;
			}
		}

		if (aspFactroy == null) {
			throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_ASP, aspName));
		}

		Asp asp = aspFactroy.createAsp();
		m3uaScheduler.execute(asp.getFSM());
		as.addAppServerProcess(asp);

		return asp;
	}

	public void start() throws IOException {
		m3uaProvider = SctpProvider.provider();
		selector = m3uaProvider.openSelector();
		serverChannel = m3uaProvider.openServerChannel();
		serverChannel.bind(new InetSocketAddress(address, port));
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		this.started = true;

		logger.info(String.format("Signalling Gateway Started. M3UA Bound to %s:%d", address, port));
	}

	public void stop() {
		// TODO : Process to bring down all ASP and AS
		started = false;
		try {
			selector.close();
			serverChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void perform() throws IOException {
		if (!started) {
			return;
		}

		m3uaScheduler.tick();

		FastList<M3UASelectionKey> selections = selector.selectNow();

		for (FastList.Node<M3UASelectionKey> n = selections.head(), end = selections.tail(); (n = n.getNext()) != end;) {

			M3UASelectionKey key = n.getValue();

			if (key.isAcceptable()) {
				accept((M3UAServerChannel) key.channel());
			} else {

				if (key.isReadable()) {
					if (logger.isTraceEnabled()) {
						logger.trace("Transmitting data from SigGateway to M3UA channel");
					}
					read(key);
				}

				if (key.isWritable()) {
					write(key);
				}
			}
		}
	}

	private void accept(M3UAServerChannel serverChannel) throws IOException {

		boolean provisioned = false;
		int port = 0;
		InetAddress inetAddress = null;

		M3UAChannel channel = serverChannel.accept();
		Set<SocketAddress> socAddresses = ((SctpChannel) channel).getRemoteAddresses();

		for (SocketAddress sockAdd : socAddresses) {

			inetAddress = ((InetSocketAddress) sockAdd).getAddress();
			port = ((InetSocketAddress) sockAdd).getPort();

			// accept connection only from provisioned IP and Port.
			for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end
					&& !provisioned;) {
				AspFactory aspFactory = n.getValue();

				// compare port and ip of remote with provisioned
				if ((port == aspFactory.getPort()) && (inetAddress.getHostAddress().compareTo(aspFactory.getIp()) == 0)) {
					M3UASelectionKey key = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					key.attach(aspFactory);
					provisioned = true;
					((SctpChannel) channel).setNotificationHandler(aspFactory.getAssociationHandler());
					logger.info("Connected with " + channel);
					
					aspFactory.onCommStateChange(CommunicationState.UP);
					break;
				}
			}
		}

		if (!provisioned) {
			logger.warn(String.format("Received connect request from non provisioned %s:%d address. Closing Channel",
					inetAddress.getHostAddress(), port));
			channel.close();

		}
	}

	private void read(M3UASelectionKey key) {
		M3UAChannel channel = null;
		try {
			channel = (M3UAChannel) key.channel();
			M3UAMessage message = channel.receive();
			((AspFactory) key.attachment()).read(message);
		} catch (IOException e) {
			logger.error(
					String.format("IOException while reading for Aspfactory name=%s ",
							((AspFactory) key.attachment()).getName()), e);

			try {
				channel.close();
			} catch (Exception ex) {
				// Ignore
			}

			((AspFactory) key.attachment()).onCommStateChange(CommunicationState.LOST);

		}
	}

	private void write(M3UASelectionKey key) {
		M3UAChannel channel = null;
		try {
			channel = (M3UAChannel) key.channel();
			AspFactory factory = ((AspFactory) key.attachment());
			M3UAMessage message = null;
			while ((message = factory.txPoll()) != null) {
				channel.send(message);
			}
		} catch (IOException e) {
			logger.error(
					String.format("IOException while writting for Aspfactory name=%s ",
							((AspFactory) key.attachment()).getName()), e);

			try {
				channel.close();
			} catch (Exception ex) {
				// Ignore
			}

			// TODO Transition to COMM_DOWN
			((AspFactory) key.attachment()).onCommStateChange(CommunicationState.LOST);

		}
	}

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

		si = m3uaProvider.getParameterFactory().createServiceIndicators(sis);
		return si;

	}

	private TrafficModeType createTrMode(String[] args, int index) throws Exception {
		if (args[index] == null) {
			throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
		}

		if (args[index].compareTo("broadcast") == 0) {
			return this.m3uaProvider.getParameterFactory().createTrafficModeType(TrafficModeType.Broadcast);
		} else if (args[index].compareTo("override") == 0) {
			return this.m3uaProvider.getParameterFactory().createTrafficModeType(TrafficModeType.Override);
		} else if (args[index].compareTo("loadshare") == 0) {
			return this.m3uaProvider.getParameterFactory().createTrafficModeType(TrafficModeType.Loadshare);
		}
		throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
	}

	public void startAsp(String aspName) throws Exception {
		throw new UnsupportedOperationException("Start ASP not supported in SGW");
	}

	public void stopAsp(String aspName) throws Exception {
		throw new UnsupportedOperationException("Stop ASP not supported in SGW");
	}

}
