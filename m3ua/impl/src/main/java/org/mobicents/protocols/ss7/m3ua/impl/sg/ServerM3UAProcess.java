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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Set;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProcess;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.M3UAServerChannel;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.sctp.SctpChannel;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ServerM3UAProcess implements M3UAProcess {

	private static Logger logger = Logger.getLogger(ServerM3UAProcess.class);

	private String address;
	private int port;

	private M3UAServerChannel serverChannel;
	private M3UASelector selector;
	private ServerM3UAManagement serverM3UAManagement;

	private boolean started = false;

	public ServerM3UAProcess(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public ServerM3UAManagement getServerM3UAManagement() {
		return serverM3UAManagement;
	}

	public void setServerM3UAManagement(ServerM3UAManagement serverM3UAManagement) {
		this.serverM3UAManagement = serverM3UAManagement;
	}

	public int read(ByteBuffer b) throws IOException {
		if (!started) {
			throw new IOException("Client M3UAProcess is stopped");
		}

		int initialPosition = b.position();
		FastList<As> appServers = serverM3UAManagement.getAppServers();
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			byte[] msu = as.poll();
			if (msu != null) {
				b.put(msu);
				break; // Remember read is only for one message
			}
		}
		return (b.position() - initialPosition);
	}

	public int write(ByteBuffer b) throws IOException {

		if (!started) {
			throw new IOException("Client M3UAProcess is stopped");
		}

		int write = 0;

		if (b.hasRemaining()) {
			byte[] msu = new byte[b.limit() - b.position()];
			write = msu.length;
			b.get(msu);
			M3UAProvider m3uaProvider = this.serverM3UAManagement.getM3uaProvider();
			ProtocolData data = m3uaProvider.getParameterFactory().createProtocolData(0, msu);
			PayloadData payload = (PayloadData) m3uaProvider.getMessageFactory().createMessage(
					MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
			payload.setData(data);

			As as = this.serverM3UAManagement.m3uaRouter.getAs(data.getDpc(), data.getOpc(), (short) data.getSI());
			if (as != null && as.getState() == AsState.ACTIVE || as.getState() == AsState.PENDING) {
				payload.setRoutingContext(as.getRoutingContext());
				as.write(payload);
			} else {
				logger.error(String.format("No AS found for this message. Dropping message %s", payload));
			}

		}

		return write;
	}

	public void start() throws IOException {
		selector = this.serverM3UAManagement.getM3uaProvider().openSelector();
		serverChannel = this.serverM3UAManagement.getM3uaProvider().openServerChannel();
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

	public void execute() throws IOException {
		if (!started) {
			return;
		}

		this.serverM3UAManagement.getM3uaScheduler().tick();

		FastList<M3UASelectionKey> selections = selector.selectNow();

		for (FastList.Node<M3UASelectionKey> n = selections.head(), end = selections.tail(); (n = n.getNext()) != end;) {

			M3UASelectionKey key = n.getValue();
			
//			if(!key.isValid()){
//				//If Key is not valid, lets go to next one
//				continue;
//			}
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

			FastList<AspFactory> aspfactories = this.serverM3UAManagement.getAspfactories();
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
}
