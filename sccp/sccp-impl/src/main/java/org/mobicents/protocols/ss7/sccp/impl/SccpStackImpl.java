/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.MTPInfo;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.utils.Utils;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.ss7.linkset.oam.Layer4;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class SccpStackImpl implements SccpStack, Layer4 {
	private static final Logger logger = Logger.getLogger(SccpStackImpl.class);

	protected final static int OP_READ_WRITE = 3;

	protected State state = State.IDLE;

	// provider ref, this can be real provider or pipe, for tests.
	protected SccpProviderImpl sccpProvider;

	protected RouterImpl router;
	protected Executor executor;

	protected Executor layer3exec;

	// Hold LinkSet here. LinkSet's name as key and actual LinkSet as Object
	protected volatile FastMap<String, Linkset> linksets = new FastMap<String, Linkset>();

	// Hold the byte[] that needs to be writtent to Linkset
	private volatile FastMap<String, ConcurrentLinkedQueue<byte[]>> linksetQueue = new FastMap<String, ConcurrentLinkedQueue<byte[]>>();

	private volatile LinksetSelector linkSetSelector = new LinksetSelector();

	protected MessageFactoryImpl messageFactory;

	public SccpStackImpl() {
		messageFactory = new MessageFactoryImpl();
		sccpProvider = new SccpProviderImpl(this);
		// why this is present?
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
	 */
	public void start() throws IllegalStateException {
		logger.info("Starting ...");

		executor = Executors.newFixedThreadPool(1);

		this.state = State.RUNNING;

		layer3exec = Executors.newFixedThreadPool(1);

		layer3exec.execute(new MtpStreamHandler());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
	 */
	public void stop() {
		this.state = State.IDLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpStack#setRouter(org.mobicents.protocols
	 * .ss7.sccp.Router)
	 */
	public void setRouter(RouterImpl router) {
		this.router = router;
	}

	public RouterImpl getRouter() {
		return this.router;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

	public void setTransferType(int type) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

//	private Linkset findLinkset(int pc) {
//		for (FastMap.Entry<String, Linkset> e = linksets.head(), end = linksets.tail(); (e = e.getNext()) != end;) {
//			Linkset linkset = e.getValue();
//			if (linkset.getApc() == pc) {
//				return linkset;
//			}
//		}
//		return null;
//	}

	private Linkset findLinkset(String name) {
		return this.linksets.get(name);
	}

	protected void send(SccpMessage message) throws IOException {
		MessageHandler handler = new MessageHandler(message);
		executor.execute(handler);
	}

	private void send(Linkset linkset, SccpMessage message, MTPInfo mtpInfo) throws IOException {
		// prepare routing label
		int opc = mtpInfo.getOpc();
		int dpc = mtpInfo.getDpc();
		int si = 3;// SCCP //Why not static? Mtp3._SI_SERVICE_SCCP
		int ni = linkset.getNi();
		int sls = mtpInfo.getSls();
		int ssi = ni << 2;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		// encoding routing label
		bout.write((byte) (((ssi & 0x0F) << 4) | (si & 0x0F)));
		bout.write((byte) dpc);
		bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
		bout.write((byte) (opc >> 2));
		bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));

		((SccpMessageImpl) message).encode(bout);
		byte[] msg = bout.toByteArray();
		// linkset.getLinksetStream().write(msg);
		System.out.println("Sending SCCP to " + linkset);
		this.linksetQueue.get(linkset.getName()).add(msg);
	}

	private void route(SccpMessageImpl msg, boolean mtpOriginated) throws IOException {
		// TODO: check Q.714 how this should be done

		SccpAddress calledPartyAddress = msg.getCalledPartyAddress();
		System.out.println(String.format("Route %s", msg.toString()));

		Rule rule = router.find(calledPartyAddress);

		if (rule == null) {
			// route not defined? try to deliver to local listener

			boolean delivered = sccpProvider.notify(calledPartyAddress, msg);
			if (mtpOriginated && !delivered && msg.getProtocolClass().getHandling() == ProtocolClass.HANDLING_RET_ERR) {
				this.sendSccpError(msg);
			}
			return;
		}

		// translate address
		SccpAddress address = rule.translate(calledPartyAddress);

		// TODO: allow to set parameters?
		msg.setCalledPartyAddress(address);
		// linkset not defined? send to local listener
		if (rule.getMTPInfo() == null) {
			boolean delivered = sccpProvider.notify(address, msg);
			if (mtpOriginated && !delivered && msg.getProtocolClass().getHandling() == ProtocolClass.HANDLING_RET_ERR) {
				this.sendSccpError(msg);
			}
			return;
		}
		// find linkset
		Linkset linkset = findLinkset(rule.getMTPInfo().getName());
		if (linkset == null) {
			throw new IOException(String.format("No linkset matching %s", rule.getMTPInfo().getName()));
		}
		send(linkset, msg, rule.getMTPInfo());
	}

	private void sendSccpError(SccpMessageImpl msg) throws IOException {
		// in case we did not consume and this message has arrived from
		// other end.... we have to reply in some way Q.714 4.2 for now
		SccpMessage ans = null;
		ReturnCause returnCause = this.sccpProvider.getParameterFactory().createReturnCause(
				ReturnCause.SUBSYSTEM_FAILURE); // not sure if its
												// proper
		switch (msg.getType()) {
		case UnitDataService.MESSAGE_TYPE:
			ans = messageFactory.createUnitDataService(returnCause, msg.getCallingPartyAddress(),
					msg.getCalledPartyAddress()); // switch addresses
			((UnitDataService) ans).setData(((UnitData) msg).getData());
			break;
		case XUnitDataService.MESSAGE_TYPE:
			HopCounter hc = this.sccpProvider.getParameterFactory().createHopCounter(HopCounter.COUNT_HIGH);
			ans = messageFactory.createXUnitDataService(hc, returnCause, msg.getCallingPartyAddress(),
					msg.getCalledPartyAddress()); // switch addresses
			((XUnitDataService) ans).setData(((XUnitData) msg).getData());
			break;
		default:
			if (logger.isEnabledFor(Level.WARN))
				logger.warn("Not supported error condition! Message: " + msg);
			return;
		}

		if (ans != null) {
			this.sccpProvider.send(ans);
		}

	}

	private class MessageHandler implements Runnable {
		// MSU as input stream
		private ByteArrayInputStream data;
		private SccpMessageImpl message;
		private boolean mtpOriginated = false; // tell if we send it, or receive
												// :)

		protected MessageHandler(byte[] msu) {
			System.out.println(Utils.hexDump(msu));
			this.data = new ByteArrayInputStream(msu);
			this.message = null;
			this.mtpOriginated = true;
		}

		protected MessageHandler(SccpMessage message) {
			this.message = (SccpMessageImpl) message;
			this.mtpOriginated = false;
		}

		private SccpMessageImpl parse() throws IOException {
			// wrap stream with DataInputStream
			DataInputStream in = new DataInputStream(data);

			int sio = 0;
			sio = in.read() & 0xff;

			// getting service indicator
			int si = sio & 0x0f;

			// ignore msg if this is not sccp service.
			if (si != 3) {
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn(String.format("SI is not SCCP. SI=%d ", si));
				}
				return null;
			}

			// skip remaining 4 bytes
			in.skip(4);

			// determine msg type
			int mt = in.readUnsignedByte();
			return ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, in);
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
			// not each msg suppose routing or delivery to listner
			if (message != null) {
				try {
					route(message, mtpOriginated);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void add(Linkset linkset) {

		try {
			linkset.getLinksetStream().register(this.linkSetSelector);
			linksets.put(linkset.getName(), linkset);
			linksetQueue.put(linkset.getName(), new ConcurrentLinkedQueue<byte[]>());
		} catch (IOException ex) {
			logger.error(String.format("Registration for %s LinksetStream failed", linkset.getName()), ex);
		}
	}

	public void remove(Linkset linkset) {
		linksets.remove(linkset.getName());
		linksetQueue.remove(linkset.getName());
	}

	private class MtpStreamHandler implements Runnable {

		// TODO: Stream API should use ByteBuffer
		byte[] rxBuffer = new byte[1000];
		byte[] txBuffer;

		public void run() {
			// Execute only till state is Running
			while (state == State.RUNNING) {

				try {
					FastList<SelectorKey> selected = linkSetSelector.selectNow(OP_READ_WRITE, 1);
					for (FastList.Node<SelectorKey> n = selected.head(), end = selected.tail(); (n = n.getNext()) != end;) {
						int length = ((LinksetStream) n.getValue().getStream()).read(rxBuffer);

						// Read data
						if (length != 0) {
							byte[] data = new byte[length];
							System.arraycopy(rxBuffer, 0, data, 0, length);

							MessageHandler handler = new MessageHandler(data);
							executor.execute(handler);
						}

						// write data
						txBuffer = linksetQueue.get(((LinksetStream) n.getValue().getStream()).getName()).poll();
						if (txBuffer != null) {
							n.getValue().getStream().write(txBuffer);
						}

					}

					// TODO : should add any Thread.wait() or let it iterate
					// continuously?
				} catch (IOException ex) {
					logger.error("Error while reading data from LinksetStream", ex);
				}
			}
		}

	}
}
