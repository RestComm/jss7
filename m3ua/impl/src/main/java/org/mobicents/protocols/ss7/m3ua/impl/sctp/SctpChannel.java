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
package org.mobicents.protocols.ss7.m3ua.impl.sctp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Set;

import org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import com.sun.nio.sctp.SctpStandardSocketOption;

/**
 * @author amit bhayani
 * 
 */
public class SctpChannel extends M3UAChannelImpl {

	// receiver buffer
	private ByteBuffer rxBuffer = ByteBuffer.allocateDirect(8192);
	// transmittor buffer
	private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

	// msgInfo
	private MessageInfo msgInfo;
	// provider instance
	private SctpProvider provider;

	private NotificationHandler notificationHandler = null;

	/**
	 * Creates new channel.
	 * 
	 * @param provider
	 *            the provider instance.
	 * @param channel
	 *            the underlying socket channel.
	 * @throws java.io.IOException
	 */
	protected SctpChannel(SctpProvider provider, AbstractSelectableChannel channel) throws IOException {
		super(channel);
		this.provider = provider;

		// Set max stream in/out here
		((com.sun.nio.sctp.SctpChannel) channel).setOption(SctpStandardSocketOption.SCTP_INIT_MAXSTREAMS,
				SctpStandardSocketOption.InitMaxStreams.create(32, 32));

		// set SCTP_FRAGMENT_INTERLEAVE to zero. The MessageFactory will fail if
		// set to 1 or 2
		((com.sun.nio.sctp.SctpChannel) channel).setOption(SctpStandardSocketOption.SCTP_FRAGMENT_INTERLEAVE, 0);

		// clean transmission buffer
		txBuffer.clear();
		txBuffer.rewind();
		txBuffer.flip();

		// clean receiver buffer
		rxBuffer.clear();
		rxBuffer.rewind();
		rxBuffer.flip();
	}

	public NotificationHandler getNotificationHandler() {
		return notificationHandler;
	}

	public void setNotificationHandler(NotificationHandler notificationHandler) {
		this.notificationHandler = notificationHandler;
	}

	/**
	 * Opens this channel
	 * 
	 * @return the new channel
	 * @throws java.io.IOException
	 */
	public static SctpChannel open(SctpProvider provider) throws IOException {
		return new SctpChannel(provider, com.sun.nio.sctp.SctpChannel.open());
	}

	public Set<SocketAddress> getRemoteAddresses() throws IOException {
		return ((com.sun.nio.sctp.SctpChannel) channel).getRemoteAddresses();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.m3ua.M3UAChannel#bind(java.net.SocketAddress)
	 */
	@Override
	public void bind(SocketAddress address) throws IOException {
		((com.sun.nio.sctp.SctpChannel) channel).bind(address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.m3ua.M3UAChannel#connect(java.net.SocketAddress
	 * )
	 */
	@Override
	public boolean connect(SocketAddress remote) throws IOException {
		return ((com.sun.nio.sctp.SctpChannel) channel).connect(remote);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#finishConnect()
	 */
	@Override
	public boolean finishConnect() throws IOException {
		return ((com.sun.nio.sctp.SctpChannel) channel).finishConnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#close()
	 */
	@Override
	public void close() throws IOException {
		((com.sun.nio.sctp.SctpChannel) channel).shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#isConnected()
	 */
	@Override
	public boolean isConnected() {
		try {
			return ((com.sun.nio.sctp.SctpChannel) channel).association() != null;
		} catch (IOException e) {
			// Do we care?
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#isConnectionPending()
	 */
	@Override
	public boolean isConnectionPending() {
		return ((com.sun.nio.sctp.SctpChannel) channel).isConnectionPending();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl#doRead()
	 */
	@Override
	protected void doRead() throws IOException {
		// clean rx buffer
		rxBuffer.clear();

		// reading data from SctpChannel
		while ((msgInfo = ((com.sun.nio.sctp.SctpChannel) channel).receive(rxBuffer, null, notificationHandler)) != null) {
			int len = msgInfo.bytes();

			if (len <= 0) {
				// TODO Should close the SctpChannel?
				return;
			}

			rxBuffer.flip();

			// split stream on to the messages
			while (rxBuffer.position() < rxBuffer.limit()) {
				// try to read message
				M3UAMessageImpl message = ((MessageFactoryImpl) provider.getMessageFactory()).createMessage(rxBuffer);
				if (message != null) {
					rxQueue.offer(message);
				}
			}

			rxBuffer.clear();
		}// end of outer while loop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl#doWrite()
	 */
	@Override
	protected void doWrite() throws IOException {
		if (txBuffer.hasRemaining()) {
			// All data wasn't sent in last doWrite. Try to send it now
			((com.sun.nio.sctp.SctpChannel) channel).send(txBuffer, msgInfo);
		}

		if (!txQueue.isEmpty() && !txBuffer.hasRemaining()) {
			// If txBuffer has no data remaining to be sent and txQueue has more
			// messages to be sent, do it now
			while (!txQueue.isEmpty()) {
				// Lets read all the messages in txQueue and send
				txBuffer.clear();
				M3UAMessageImpl msg = txQueue.poll();
				msg.encode(txBuffer);

				// See section 1.4.7. SCTP Stream Mapping of RFC
				// http://tools.ietf.org/html/rfc4666
				// 1. The DATA message MUST NOT be sent on stream 0.
				// 2. The ASPSM, MGMT, RKM classes SHOULD be sent on stream 0
				// (other than BEAT, BEAT ACK and NTFY messages).
				// 3. The SSNM, ASPTM classes and BEAT, BEAT ACK and NTFY
				// messages can be sent on any stream.

				switch (msg.getMessageClass()) {
				case MessageClass.ASP_STATE_MAINTENANCE:
				case MessageClass.MANAGEMENT:
				case MessageClass.ROUTING_KEY_MANAGEMENT:
					msgInfo = MessageInfo.createOutGoing(null, 0);
					msgInfo.payloadProtocolID(3);
					break;
				case MessageClass.TRANSFER_MESSAGES:
					// MTP3-User traffic may be assigned to individual streams
					// based on, for example, the SLS value in the MTP3 Routing
					// Label, subject of course to the maximum number of streams
					// supported by the underlying SCTP association.
					PayloadData payload = (PayloadData) msg;
					// TODO : What about sls greater than streamNumber?
					System.out.println(payload.getData().getSLS());
					msgInfo = MessageInfo.createOutGoing(null, payload.getData().getSLS());

					// 7.1. SCTP Payload Protocol Identifier : IANA has assigned
					// an M3UA value (3) for the Payload Protocol Identifier in
					// the SCTP DATA chunk.
					msgInfo.payloadProtocolID(3);
					break;

				// Let default messages also go through stream 0
				default:
					msgInfo = MessageInfo.createOutGoing(null, 0);
					msgInfo.payloadProtocolID(3);
					break;

				}

				txBuffer.flip();
				int sent = ((com.sun.nio.sctp.SctpChannel) channel).send(txBuffer, msgInfo);
				if (txBuffer.hasRemaining()) {
					// Couldn't send all data. Lets return now and try to send
					// this message
					return;
				}
			}// end of while
		}
	}
}
