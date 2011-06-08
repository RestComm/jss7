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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.utils.Utils;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class SccpStackImpl implements SccpStack {
	private static final Logger logger = Logger.getLogger(SccpStackImpl.class);

	protected final static int OP_READ_WRITE = 3;

	public static final int SI_SCCP = 3; // Service Indicator for SCCP
	public static final int SI_SNMM = 0; // Service Indicator for Signaling
											// Network
	// Management Messages

	protected State state = State.IDLE;

	// provider ref, this can be real provider or pipe, for tests.
	protected SccpProviderImpl sccpProvider;

	protected Router router;
	protected SccpResource sccpResource;

	protected Executor executor;

	protected Executor layer3exec;

	protected MessageFactoryImpl messageFactory;

	protected SccpManagement sccpManagement;
	protected SccpRoutingControl sccpRoutingControl;

	protected Mtp3UserPart mtp3UserPart = null;

	protected int localSpc;
	protected int ni = 2;

	protected ConcurrentLinkedQueue<byte[]> txDataQueue = new ConcurrentLinkedQueue<byte[]>();

	public SccpStackImpl() {
		messageFactory = new MessageFactoryImpl();
		sccpProvider = new SccpProviderImpl(this);
		// FIXME: make this configurable
		sccpManagement = new SccpManagement(sccpProvider, this);
		sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);

		sccpManagement.setSccpRoutingControl(sccpRoutingControl);
		sccpRoutingControl.setSccpManagement(sccpManagement);

		this.state = State.CONFIGURED;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
	 */
	public SccpProvider getSccpProvider() {
		return sccpProvider;
	}

	public int getLocalSpc() {
		return localSpc;
	}

	public void setLocalSpc(int localSpc) {
		this.localSpc = localSpc;
	}

	public int getNi() {
		return ni;
	}

	public void setNi(int ni) {
		this.ni = ni;
	}

	public Mtp3UserPart getMtp3UserPart() {
		return mtp3UserPart;
	}

	public void setMtp3UserPart(Mtp3UserPart mtp3UserPart) {
		this.mtp3UserPart = mtp3UserPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
	 */
	public void start() throws IllegalStateException {
		logger.info("Starting ...");

		executor = Executors.newFixedThreadPool(1);

		layer3exec = Executors.newFixedThreadPool(1);

		logger.info("Starting routing engine...");
		this.sccpRoutingControl.start();
		logger.info("Starting management ...");
		this.sccpManagement.start();
		logger.info("Starting MSU handler...");
		layer3exec.execute(new MtpStreamHandler());

		this.state = State.RUNNING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
	 */
	public void stop() {
		logger.info("Stopping ...");

		executor = null;

		layer3exec = null;

		logger.info("Stopping management...");
		this.sccpManagement.stop();
		logger.info("Stopping routing engine...");
		this.sccpRoutingControl.stop();
		logger.info("Stopping MSU handler...");
		this.state = State.IDLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpStack#setRouter(org.mobicents.protocols
	 * .ss7.sccp.Router)
	 */
	public void setRouter(Router router) {
		this.router = router;
	}

	public Router getRouter() {
		return this.router;
	}

	public SccpResource getSccpResource() {
		return sccpResource;
	}

	public void setSccpResource(SccpResource sccpResource) {
		this.sccpResource = sccpResource;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

	protected void send(SccpMessageImpl message) throws IOException {
		MessageHandler handler = new MessageHandler(message);
		executor.execute(handler);
	}

	private class MessageHandler implements Runnable {
		// MSU as input stream
		private ByteArrayInputStream data;
		private SccpMessageImpl message;
		private boolean mtpOriginated = false; // tell if we send it, or receive
												// :)

		protected MessageHandler(byte[] msu) {
			//System.out.println(Utils.hexDump(msu));
			this.data = new ByteArrayInputStream(msu);
			this.message = null;
			this.mtpOriginated = true;
		}

		protected MessageHandler(SccpMessageImpl message) {
			this.message = message;
			this.mtpOriginated = false;
		}

		private SccpMessageImpl parse() throws IOException {
			// wrap stream with DataInputStream
			DataInputStream in = new DataInputStream(data);

			int sio = 0;
			sio = in.read() & 0xff;

			// getting service indicator
			int si = sio & 0x0f;

			switch (si) {
			case SI_SCCP:
				// skip remaining 4 bytes
				byte b1 = in.readByte();
				byte b2 = in.readByte();
				byte b3 = in.readByte();
				byte b4 = in.readByte();

				int dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
				int opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
				int sls = ((b4 & 0xf0) >> 4);

				// determine msg type
				int mt = in.readUnsignedByte();
				SccpMessageImpl msg = ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, in);
				msg.setDpc(dpc);
				msg.setOpc(opc);
				msg.setSls(sls);
				return msg;

			case SI_SNMM:
				sccpManagement.handleMtp3Primitive(in);
				return null;
			default:
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("SI is not SCCP. SI=%d ", si));
				}
				break;
			}

			return null;

		}

		public void run() {

			if (message == null) {
				try {
					message = parse();
				} catch (IOException e) {
					logger.warn("Corrupted message received");
					return;
				}
			}
			// not each msg suppose routing or delivery to listener
			if (message != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Rx : SCCP Message=%s", message.toString()));
				}
				try {
					if (mtpOriginated) {
						sccpRoutingControl.routeMssgFromMtp(message);
					} else {
						sccpRoutingControl.routeMssgFromSccpUser(message);
					}
				} catch (IOException e) {
					logger.error(String.format("Error while routing message=%s", message), e);
				}
			}
		}// end of run
	}

	private class MtpStreamHandler implements Runnable {
		ByteBuffer rxBuffer = ByteBuffer.allocateDirect(1000);
		ByteBuffer txBuffer = ByteBuffer.allocateDirect(1000);
		int rxBytes = 0;
		@SuppressWarnings("unused")
		int txBytes = 0;

		public void run() {
			// Execute only till state is Running
			while (state == State.RUNNING) {

				try {
					// Execute the MTP3UserPart
					mtp3UserPart.execute();

					rxBytes = 0;
					rxBuffer.clear();
					try {
						rxBytes = mtp3UserPart.read(rxBuffer);
						if (rxBytes != 0) {
							byte[] data = new byte[rxBytes];
							rxBuffer.flip();
							rxBuffer.get(data);
							MessageHandler handler = new MessageHandler(data);
							executor.execute(handler);
						}
					} catch (IOException e) {
						logger.error("Error while readig data from Mtp3UserPart", e);
					}

					// Iterate till we send all data
					while (!txDataQueue.isEmpty()) {
						txBuffer.clear();
						txBuffer.put(txDataQueue.poll());
						txBuffer.flip();
						try {
							txBytes = mtp3UserPart.write(txBuffer);
						} catch (IOException e) {
							logger.error("Error while writting data to Mtp3UserPart", e);
						}
					}// while txDataQueue
				} catch (IOException e1) {
					logger.error("Error in MtpStreamHandler", e1);
				}
			}// end of while
		}
	}
}
