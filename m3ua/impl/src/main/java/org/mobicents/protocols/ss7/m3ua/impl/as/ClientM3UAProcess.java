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

import java.io.IOException;
import java.nio.ByteBuffer;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProcess;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
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
public class ClientM3UAProcess implements M3UAProcess {

	private static Logger logger = Logger.getLogger(ClientM3UAProcess.class);

	private volatile boolean started = false;
	private ClientM3UAManagement clientM3UAManagement;

	public ClientM3UAProcess() {
	}

	public ClientM3UAManagement getClientM3UAManagement() {
		return clientM3UAManagement;
	}

	public void setClientM3UAManagement(ClientM3UAManagement clientM3UAManagement) {
		this.clientM3UAManagement = clientM3UAManagement;
	}

	public int read(ByteBuffer b) throws IOException {
		if (!started) {
			throw new IOException("Client M3UAProcess is stopped");
		}

		int initialPosition = b.position();
		FastList<As> appServers = clientM3UAManagement.getAppServers();
		for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
			As as = n.getValue();
			byte[] msu = as.poll();
			if (msu != null) {
				b.put(msu);
				// Remember read is only one message at a time
				break;
			}
		}// for
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
			M3UAProvider m3uaProvider = this.clientM3UAManagement.getM3uaProvider();
			ProtocolData data = m3uaProvider.getParameterFactory().createProtocolData(0, msu);
			PayloadData payload = (PayloadData) m3uaProvider.getMessageFactory().createMessage(
					MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
			payload.setData(data);
			
			//System.out.println(String.format("Sending payload data = %s", payload));
			
			As as = this.clientM3UAManagement.getAsForDpc(data.getDpc(), data.getSLS());
			if (as == null) {
				logger.error(String.format("Tx : No AS found for routing message %s", payload));
				return write;
			}

			payload.setRoutingContext(as.getRoutingContext());
			as.write(payload);

		}

		return write;
	}

	public void start() throws IOException {
		this.started = true;
	}

	public void stop() {
		this.started = false;
	}

	public void execute() throws IOException {

		if (!started) {
			throw new IOException("Client M3UAProcess is stopped");
		}

		clientM3UAManagement.getM3uaScheduler().tick();

		FastList<M3UASelectionKey> selections = clientM3UAManagement.selector.selectNow();

		for (FastList.Node<M3UASelectionKey> n = selections.head(), end = selections.tail(); (n = n.getNext()) != end;) {
			M3UASelectionKey key = n.getValue();
			if (key.isReadable()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Transmitting data from M3UA channel to hardware");
				}
				read(key);
			}

			if (key.isWritable()) {
				write(key);
			}
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
