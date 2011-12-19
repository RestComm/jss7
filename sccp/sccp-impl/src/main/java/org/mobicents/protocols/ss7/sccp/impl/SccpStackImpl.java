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
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class SccpStackImpl implements SccpStack, Mtp3UserPartListener {
	private final Logger logger;

	protected final static int OP_READ_WRITE = 3;

//	public static final int SI_SCCP = 3; // Service Indicator for SCCP
//	public static final int SI_SNMM = 0; // Service Indicator for Signaling
//											// Network Management Messages

	protected volatile State state = State.IDLE;
//	private ReentrantLock stateLock = new ReentrantLock();
	
	// provider ref, this can be real provider or pipe, for tests.
	protected SccpProviderImpl sccpProvider;

	protected Router router;
	protected SccpResource sccpResource;

//	protected Executor executor;

//	protected Executor layer3exec;

	protected MessageFactoryImpl messageFactory;

	protected SccpManagement sccpManagement;
	protected SccpRoutingControl sccpRoutingControl;

	protected Mtp3UserPart mtp3UserPart = null;

	protected int localSpc;
	protected int ni = 2;
	
	private final String name;
	
	private String persistDir = null;

//	protected ConcurrentLinkedQueue<byte[]> txDataQueue = new ConcurrentLinkedQueue<byte[]>();

	public SccpStackImpl(String name) {
		this.name = name;
		this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName()+"-"+this.name);
		
		messageFactory = new MessageFactoryImpl();
		sccpProvider = new SccpProviderImpl(this);
		// FIXME: make this configurable
		sccpManagement = new SccpManagement(name, sccpProvider, this);
		sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);

		sccpManagement.setSccpRoutingControl(sccpRoutingControl);
		sccpRoutingControl.setSccpManagement(sccpManagement);

		this.state = State.CONFIGURED;

	}
	
	public String getPersistDir() {
		return persistDir;
	}

	public void setPersistDir(String persistDir) {
		this.persistDir = persistDir;
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
		// stateLock.lock();
		// try{
		// executor = Executors.newFixedThreadPool(1);
		//
		// layer3exec = Executors.newFixedThreadPool(1);
		this.router = new Router(this.name);
		this.router.setPersistDir(this.persistDir);
		this.router.start();
		
		this.sccpResource = new SccpResource(this.name);
		this.sccpResource.setPersistDir(this.persistDir);
		this.sccpResource.start();

		logger.info("Starting routing engine...");
		this.sccpRoutingControl.start();
		logger.info("Starting management ...");
		this.sccpManagement.start();
		logger.info("Starting MSU handler...");
		// layer3exec.execute(new MtpStreamHandler());

		this.mtp3UserPart.addMtp3UserPartListener(this);

		this.state = State.RUNNING;
		// }finally
		// {
		// stateLock.unlock();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
	 */
	public void stop() {
		logger.info("Stopping ...");
		// stateLock.lock();
		// try
		// {
		this.state = State.IDLE;
		// executor = null;
		//
		// layer3exec = null;

		this.mtp3UserPart.removeMtp3UserPartListener(this);

		logger.info("Stopping management...");
		this.sccpManagement.stop();
		logger.info("Stopping routing engine...");
		this.sccpRoutingControl.stop();
		logger.info("Stopping MSU handler...");
		
		this.sccpResource.stop();
		
		this.router.stop();

		// }finally
		// {
		// stateLock.unlock();
		// }
		
	}

	public Router getRouter() {
		return this.router;
	}

	public SccpResource getSccpResource() {
		return sccpResource;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

	protected void send(SccpMessageImpl message) throws IOException {

		if (this.state != State.RUNNING){
			logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
			return;
		}
		
		try {
			this.sccpRoutingControl.routeMssgFromSccpUser(message);
		} catch (IOException e) {
			// log here Exceptions from MTP3 level
			logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
			e.printStackTrace();
			throw e;
		}
		
//		MessageHandler handler = new MessageHandler(message);
//		executor.execute(handler);
	}

	@Override
	public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {
		
		logger.warn(String.format("Rx : %s", msg));

		if (this.state != State.RUNNING){
			logger.error("Cannot consume MTP3 PASUE message as SCCP stack is not RUNNING");
			return;
		}
		
		sccpManagement.handleMtp3Pause(msg.getAffectedDpc());
	}

	@Override
	public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {
		logger.warn(String.format("Rx : %s", msg));

		if (this.state != State.RUNNING){
			logger.error("Cannot consume MTP3 RESUME message as SCCP stack is not RUNNING");
			return;
		}
		
		sccpManagement.handleMtp3Resume(msg.getAffectedDpc());
	}

	@Override
	public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
		logger.warn(String.format("Rx : %s", msg));
		if (this.state != State.RUNNING){
			logger.error("Cannot consume MTP3 STATUS message as SCCP stack is not RUNNING");
			return;
		}
		
		sccpManagement.handleMtp3Status(msg.getCause(), msg.getAffectedDpc(), msg.getCongestionLevel());
	}

	@Override
	public void onMtp3TransferMessage(Mtp3TransferPrimitive mtp3Msg) {

		if (this.state != State.RUNNING){
			logger.error("Received MTP3TransferPrimitive from lower layer but SCCP stack is not RUNNING");
			return;
		}

		// process only SCCP messages
		if (mtp3Msg.getSi() != Mtp3._SI_SERVICE_SCCP){
			logger.warn(String.format("Received Mtp3TransferPrimitive from lower layer with Service Indicator=%d whic is not SCCP. Dropping this message", mtp3Msg.getSi()));
			return;
		}
		
		SccpMessageImpl msg = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(mtp3Msg.getData());
			DataInputStream in = new DataInputStream(bais);
			int mt = in.readUnsignedByte();
			msg = ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, in);
			msg.setDpc(mtp3Msg.getDpc());
			msg.setOpc(mtp3Msg.getOpc());
			msg.setSls(mtp3Msg.getSls());
			
			if(logger.isDebugEnabled()){
				logger.debug(String.format("Rx : SCCP message from MTP %s", msg));
			}
			
			sccpRoutingControl.routeMssgFromMtp(msg);
		} catch (IOException e) {
			logger.error("IOException while decoding SCCP message: " + e.getMessage(), e);
//			e.printStackTrace();
		}
	}

//	private class MessageHandler implements Runnable {
//		// MSU as input stream
//		private ByteArrayInputStream data;
//		private SccpMessageImpl message;
//		private boolean mtpOriginated = false; // tell if we send it, or receive
//												// :)
//
//		protected MessageHandler(byte[] msu) {
//			//System.out.println(Utils.hexDump(msu));
//			this.data = new ByteArrayInputStream(msu);
//			this.message = null;
//			this.mtpOriginated = true;
//		}
//
//		protected MessageHandler(SccpMessageImpl message) {
//			this.message = message;
//			this.mtpOriginated = false;
//		}
//
//		private SccpMessageImpl parse() throws IOException {
//			// wrap stream with DataInputStream
//			DataInputStream in = new DataInputStream(data);
//
//			int sio = 0;
//			sio = in.read() & 0xff;
//
//			// getting service indicator
//			int si = sio & 0x0f;
//
//			switch (si) {
//			case SI_SCCP:
//				// skip remaining 4 bytes
//				byte b1 = in.readByte();
//				byte b2 = in.readByte();
//				byte b3 = in.readByte();
//				byte b4 = in.readByte();
//
//				int dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
//				int opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
//				int sls = ((b4 & 0xf0) >> 4);
//
//				// determine msg type
//				int mt = in.readUnsignedByte();
//				SccpMessageImpl msg = ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, in);
//				msg.setDpc(dpc);
//				msg.setOpc(opc);
//				msg.setSls(sls);
//				return msg;
//
//			case SI_SNMM:
//				sccpManagement.handleMtp3Primitive(in);
//				return null;
//			default:
//				if (logger.isEnabledFor(Level.WARN)) {
//					logger.warn(String.format("SI is not SCCP. SI=%d ", si));
//				}
//				break;
//			}
//
//			return null;
//
//		}
//
//		public void run() {
//
//			if (message == null) {
//				try {
//					message = parse();
//				} catch (IOException e) {
//					logger.warn("Corrupted message received");
//					return;
//				}
//			}
//			// not each msg suppose routing or delivery to listener
//			if (message != null) {
//				if (logger.isDebugEnabled()) {
//					logger.debug(String.format("Rx : SCCP Message=%s", message.toString()));
//				}
//				try {
//					if (mtpOriginated) {
//						sccpRoutingControl.routeMssgFromMtp(message);
//					} else {
//						sccpRoutingControl.routeMssgFromSccpUser(message);
//					}
//				} catch (IOException e) {
//					logger.error(String.format("Error while routing message=%s", message), e);
//				}
//			}
//		}// end of run
//	}

//	private class MtpStreamHandler implements Runnable {
//		ByteBuffer rxBuffer = ByteBuffer.allocateDirect(1000);
//		ByteBuffer txBuffer = ByteBuffer.allocateDirect(1000);
//		int rxBytes = 0;
//		@SuppressWarnings("unused")
//		int txBytes = 0;
//
//		public void run() {
//			// Execute only till state is Running
//
//			while (state == State.RUNNING) {
//				stateLock.lock();
//				try {
////					try {
////						Thread.sleep(5);
////					} catch (InterruptedException e2) {
////						// TODO Auto-generated catch block
////						e2.printStackTrace();
////					}
//					try {
//						// Execute the MTP3UserPart
//						mtp3UserPart.execute();
//
//						rxBytes = 0;
//						rxBuffer.clear();
//						try {
//							rxBytes = mtp3UserPart.read(rxBuffer);
//							if (rxBytes != 0) {
//								byte[] data = new byte[rxBytes];
//								rxBuffer.flip();
//								rxBuffer.get(data);
//								MessageHandler handler = new MessageHandler(data);
//								executor.execute(handler);
//							}
//						} catch (IOException e) {
//							logger.error("Error while readig data from Mtp3UserPart", e);
//						}
//
//						// Iterate till we send all data
//						while (!txDataQueue.isEmpty()) {
//							txBuffer.clear();
//							txBuffer.put(txDataQueue.poll());
//							txBuffer.flip();
//							try {
//								txBytes = mtp3UserPart.write(txBuffer);
//							} catch (IOException e) {
//								logger.error("Error while writting data to Mtp3UserPart", e);
//							}
//						}// while txDataQueue
//					} catch (IOException e1) {
//						logger.error("Error in MtpStreamHandler", e1);
//					}
//				} finally {
//					stateLock.unlock();
//				}
//			}// end of while
//
//		}
//	}
}
