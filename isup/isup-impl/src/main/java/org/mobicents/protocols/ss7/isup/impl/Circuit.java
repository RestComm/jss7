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

package org.mobicents.protocols.ss7.isup.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.util.FastList;
import javolution.util.ReentrantLock;

import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.InformationMessage;
import org.mobicents.protocols.ss7.isup.message.InformationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.mtp.Mtp3;

/**
 *
 * @author baranowb
 * 
 */
@SuppressWarnings("rawtypes")
class Circuit implements Runnable {
	private final int cic;
	private final int dpc;
	private final ISUPProviderImpl provider;

	private ReentrantLock lock = new ReentrantLock();
	private ScheduledExecutorService executor;
	private List<ISUPMessage> incoming = new FastList<ISUPMessage>(); // use
																		// linked
																		// list
																		// ?
	private ByteArrayOutputStream bos = new ByteArrayOutputStream(300);

	/**
	 * @param cic
	 */
	public Circuit(int cic, int dpc,ISUPProviderImpl provider) {
		this.cic = cic;
		this.dpc = dpc;
		this.provider = provider;
		this.executor = provider.getExecutor(cic);
	}

	/**
	 * @param timerId
	 * @return
	 */
	public boolean cancelTimer(int timerId) {
		try {

			lock.lock();
			switch (timerId) {
			case ISUPTimeoutEvent.T1:
				return cancelT1();
			case ISUPTimeoutEvent.T5:
				return cancelT5();
			case ISUPTimeoutEvent.T7:
				return cancelT7();
			case ISUPTimeoutEvent.T12:
				return cancelT12();
			case ISUPTimeoutEvent.T13:
				return cancelT13();
			case ISUPTimeoutEvent.T14:
				return cancelT14();
			case ISUPTimeoutEvent.T15:
				return cancelT15();
			case ISUPTimeoutEvent.T16:
				return cancelT16();
			case ISUPTimeoutEvent.T17:
				return cancelT17();
			case ISUPTimeoutEvent.T18:
				return cancelT18();
			case ISUPTimeoutEvent.T19:
				return cancelT19();
			case ISUPTimeoutEvent.T20:
				return cancelT20();
			case ISUPTimeoutEvent.T21:
				return cancelT21();
			case ISUPTimeoutEvent.T22:
				return cancelT22();
			case ISUPTimeoutEvent.T23:
				return cancelT23();
			case ISUPTimeoutEvent.T28:
				return cancelT28();
			case ISUPTimeoutEvent.T33:
				return cancelT33();
			default:
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param message
	 */
	public void receive(ISUPMessage message) {
		incoming.add(message);
		this.executor.execute(this);
	}

	/**
	 * @param message
	 * @throws ParameterException
	 * @throws IOException 
	 */
	public void send(ISUPMessage message) throws ParameterException, IOException {
		try {
			lock.lock();
			bos.reset();
			// FIXME: add SEG creation?
			byte[] msg = decorate(message);
			// process timers
			switch (message.getMessageType().getCode()) {
			case ReleaseMessage.MESSAGE_CODE:
				startRELTimers(msg, (ReleaseMessage) message);
				break;
			case SubsequentAddressMessage.MESSAGE_CODE:
			case InitialAddressMessage.MESSAGE_CODE:
				startXAMTimers(message);
				break;
			case BlockingMessage.MESSAGE_CODE:
				startBLOTimers(msg, (BlockingMessage) message);
				break;
			case UnblockingMessage.MESSAGE_CODE:
				startUBLTimers(msg, (UnblockingMessage) message);
				break;

			case ResetCircuitMessage.MESSAGE_CODE:
				startRSCTimers(msg, (ResetCircuitMessage) message);
				break;

			case CircuitGroupBlockingMessage.MESSAGE_CODE:
				startCGBTimers(msg, (CircuitGroupBlockingMessage) message);
				break;

			case CircuitGroupUnblockingMessage.MESSAGE_CODE:
				startCGUTimers(msg, (CircuitGroupUnblockingMessage) message);
				break;
			case CircuitGroupResetMessage.MESSAGE_CODE:
				startGRSTimers(msg, (CircuitGroupResetMessage) message);
				break;
			case CircuitGroupQueryMessage.MESSAGE_CODE:
				startCQMTimers((CircuitGroupQueryMessage) message);
				break;
			case InformationRequestMessage.MESSAGE_CODE:
				startINRTimers((InformationRequestMessage) message);
				break;
			}
			// send
			provider.send(msg);
		} finally {
			lock.unlock();
		}

	}
	
	/**
	 * @param message
	 * @return
	 * @throws ParameterException 
	 * @throws IOException 
	 */
	private byte[] decorate(ISUPMessage message) throws ParameterException, IOException {
		((AbstractISUPMessage) message).encode(bos);
		byte[] encoded = bos.toByteArray();
		int opc = this.provider.getLocalSpc();
		int dpc = this.dpc;
		int si = Mtp3._SI_SERVICE_ISUP;
		int ni = this.provider.getNi();
		int sls = message.getSls() & 0x0F; //promote
		int ssi = ni << 2;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		// encoding routing label
		bout.write((byte) (((ssi & 0x0F) << 4) | (si & 0x0F)));
		bout.write((byte) dpc);
		bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
		bout.write((byte) (opc >> 2));
		bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));
		bout.write(encoded);
		byte[] msg = bout.toByteArray();
		return msg;
	}

	public void run() {
		try {
			lock.lock();
			ISUPMessage message = this.incoming.remove(0);

			// process timers
			switch (message.getMessageType().getCode()) {
			// FIXME: add check for SEG
			case ReleaseCompleteMessage.MESSAGE_CODE:
				// this is tricy BS.... its REL and RSC are answered with RLC

				if (!stopRELTimers()) {
					stopRSCTimers();
				}
				break;
			case AddressCompleteMessage.MESSAGE_CODE:
			case ConnectMessage.MESSAGE_CODE:
				stoptXAMTimers();
				break;
			case BlockingAckMessage.MESSAGE_CODE:
				stoptBLOTimers();
				break;
			case UnblockingAckMessage.MESSAGE_CODE:
				stoptUBLTimers();
				break;
			case CircuitGroupBlockingMessage.MESSAGE_CODE:
				stoptCGBTimers();
				break;

			case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
				stoptCGUTimers();
				break;
			case CircuitGroupResetAckMessage.MESSAGE_CODE:
				stoptGRSTimers();
				break;
			case CircuitGroupQueryResponseMessage.MESSAGE_CODE:
				stoptCQMTimers();
				break;
			case InformationMessage.MESSAGE_CODE:
				stoptINRTimers();
				break;
			}

			// deliver
			ISUPEvent event = new ISUPEvent(provider, message);
			this.provider.deliver(event);
		} catch (Exception e) {
			// catch exception, so thread dont die.
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	// ----------------- timer handlers ----------------------

	// FIXME: check how t3 works....

	private Future t1;
	private Future t5;
	private byte[] t1t5encodedREL; // keep encoded value, so we can simply send,
									// without spending CPU on encoding.
	private ReleaseMessage t1t5REL; // keep for timers.
	private Future t7;
	private ISUPMessage t7AddressMessage; // IAM/SAM

	// FIXME: t8 - receive IAM with contuuity check ind.
	// FIXME: t11
	// FIXME: t11
	private Future t12;
	private Future t13;
	private byte[] t12t13encodedBLO; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private BlockingMessage t12t13BLO; // keep for timers.

	private Future t14;
	private Future t15;
	private byte[] t14t15encodedUBL; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private UnblockingMessage t14t15UBL; // keep for timers.

	private Future t16;
	private Future t17;
	private byte[] t16t17encodedRSC; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private ResetCircuitMessage t16t17RSC; // keep for timers.

	private Future t18;
	private Future t19;
	private byte[] t18t19encodedCGB; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private CircuitGroupBlockingMessage t18t19CGB; // keep for timers.

	private Future t20;
	private Future t21;
	private byte[] t20t21encodedCGU; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private CircuitGroupUnblockingMessage t20t21CGU; // keep for timers.

	private Future t22;
	private Future t23;
	private byte[] t22t23encodedGRS; // keep encoded value, so we can simply
										// send, without spending CPU on
										// encoding.
	private CircuitGroupResetMessage t22t23GRS; // keep for timers.

	private Future t28;
	private CircuitGroupQueryMessage t28CQM;

	private Future t33;
	private InformationRequestMessage t33INR;

	// FIXME: t34 - check how SEG works

	private void startRELTimers(byte[] encoded, ReleaseMessage rel) {
		// FIXME: add lock ?
		this.t1t5encodedREL = encoded;
		this.t1t5REL = rel;

		// it is started always.
		startT1();

		if (t5 == null) {
			startT5();
		}
	}

	/**
	 * @return
	 */
	private boolean stopRELTimers() {
		if (this.t1 != null || this.t5 != null) {
			cancelT1();
			cancelT5();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startXAMTimers(ISUPMessage message) {
		this.cancelT7();
		this.t7AddressMessage = message;
		this.startT7();
	}

	/**
	 * @return
	 */
	private void stoptXAMTimers() {
		cancelT7();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startBLOTimers(byte[] encoded, BlockingMessage message) {
		this.t12t13BLO = message;
		this.t12t13encodedBLO = encoded;
		// it is started always.
		startT12();

		if (this.t13 == null) {
			startT13();
		}

	}

	/**
	 * @return
	 */
	private void stoptBLOTimers() {
		cancelT12();
		cancelT13();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startUBLTimers(byte[] encoded, UnblockingMessage message) {
		this.t14t15UBL = message;
		this.t14t15encodedUBL = encoded;
		// it is started always.
		startT14();

		if (this.t15 == null) {
			startT15();
		}

	}

	/**
	 * @return
	 */
	private void stoptUBLTimers() {
		cancelT14();
		cancelT15();

	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startRSCTimers(byte[] encoded, ResetCircuitMessage message) {
		this.t16t17RSC = message;
		this.t16t17encodedRSC = encoded;
		// it is started always.
		startT16();

		if (this.t17 == null) {
			startT17();
		}

	}

	/**
	 * @return
	 */
	private void stopRSCTimers() {
		cancelT16();
		cancelT17();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startINRTimers(InformationRequestMessage message) {
		this.t33INR = message;
		startT33();
	}

	/**
	 * @return
	 */
	private void stoptINRTimers() {
		cancelT33();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCQMTimers(CircuitGroupQueryMessage message) {
		this.t28CQM = message;

		// it is started always.
		startT28();
		// FIXME: can we send more than one?
	}

	/**
	 * @return
	 */
	private void stoptCQMTimers() {
		cancelT28();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startGRSTimers(byte[] encoded, CircuitGroupResetMessage message) {
		this.t22t23GRS = message;
		this.t22t23encodedGRS = encoded;
		// it is started always.
		startT22();

		if (this.t23 == null) {
			startT23();
		}
	}

	/**
	 * @return
	 */
	private void stoptGRSTimers() {
		cancelT22();
		cancelT23();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCGUTimers(byte[] encoded, CircuitGroupUnblockingMessage message) {
		this.t20t21CGU = message;
		this.t20t21encodedCGU = encoded;
		// it is started always.
		startT20();

		if (this.t21 == null) {
			startT21();
		}

	}

	/**
	 * @return
	 */
	private void stoptCGUTimers() {
		cancelT20();
		cancelT21();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCGBTimers(byte[] encoded, CircuitGroupBlockingMessage message) {
		this.t18t19CGB = message;
		this.t18t19encodedCGB = encoded;
		// it is started always.
		startT18();

		if (this.t19 == null) {
			startT19();
		}

	}

	/**
	 * @return
	 */
	private void stoptCGBTimers() {
		cancelT18();
		cancelT19();
	}

	private void startT1() {
		cancelT1();
		this.t1 = this.executor.schedule(new TimerT1(), this.provider.getT1Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT1() {
		if (this.t1 != null) {
			this.t1.cancel(false);
			this.t1 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT5() {
		cancelT5();
		this.t5 = this.executor.schedule(new TimerT5(), this.provider.getT5Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT5() {
		if (this.t5 != null) {
			this.t5.cancel(false);
			this.t5 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT7() {
		cancelT7();
		this.t7 = this.executor.schedule(new TimerT7(), this.provider.getT7Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT7() {
		if (this.t7 != null) {
			this.t7.cancel(false);
			this.t7 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT12() {
		cancelT12();
		this.t12 = this.executor.schedule(new TimerT12(), this.provider.getT12Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT12() {
		if (this.t12 != null) {
			this.t12.cancel(false);
			this.t12 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT13() {
		cancelT13();
		this.t13 = this.executor.schedule(new TimerT13(), this.provider.getT13Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT13() {
		if (this.t13 != null) {
			this.t13.cancel(false);
			this.t13 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT14() {
		cancelT14();
		this.t14 = this.executor.schedule(new TimerT14(), this.provider.getT14Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT14() {
		if (this.t14 != null) {
			this.t14.cancel(false);
			this.t14 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT15() {
		cancelT15();
		this.t15 = this.executor.schedule(new TimerT15(), this.provider.getT15Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT15() {
		if (this.t15 != null) {
			this.t15.cancel(false);
			this.t15 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT16() {
		cancelT16();
		this.t16 = this.executor.schedule(new TimerT16(), this.provider.getT16Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT16() {
		if (this.t16 != null) {
			this.t16.cancel(false);
			this.t16 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT17() {
		cancelT17();
		this.t17 = this.executor.schedule(new TimerT17(), this.provider.getT17Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT17() {
		if (this.t17 != null) {
			this.t17.cancel(false);
			this.t17 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT18() {
		cancelT18();
		this.t18 = this.executor.schedule(new TimerT18(), this.provider.getT18Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT18() {
		if (this.t18 != null) {
			this.t18.cancel(false);
			this.t18 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT19() {
		cancelT19();
		this.t19 = this.executor.schedule(new TimerT19(), this.provider.getT19Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT19() {
		if (this.t19 != null) {
			this.t19.cancel(false);
			this.t19 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT20() {
		cancelT20();
		this.t20 = this.executor.schedule(new TimerT20(), this.provider.getT20Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT20() {
		if (this.t20 != null) {
			this.t20.cancel(false);
			this.t20 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT21() {
		cancelT21();
		this.t21 = this.executor.schedule(new TimerT21(), this.provider.getT21Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT21() {
		if (this.t21 != null) {
			this.t21.cancel(false);
			this.t21 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT22() {
		cancelT22();
		this.t22 = this.executor.schedule(new TimerT22(), this.provider.getT22Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT22() {
		if (this.t22 != null) {
			this.t22.cancel(false);
			this.t22 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT23() {
		cancelT23();
		this.t23 = this.executor.schedule(new TimerT23(), this.provider.getT23Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT23() {
		if (this.t23 != null) {
			this.t23.cancel(false);
			this.t23 = null;
			return true;
		} else {
			return false;
		}

	}

	private void startT28() {
		cancelT28();
		this.t28 = this.executor.schedule(new TimerT28(), this.provider.getT28Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT28() {
		if (this.t28 != null) {
			this.t28.cancel(false);
			this.t28 = null;
			return true;
		} else {
			return false;
		}
	}

	private void startT33() {
		cancelT33();
		this.t33 = this.executor.schedule(new TimerT33(), this.provider.getT33Timeout(), TimeUnit.MILLISECONDS);
	}

	private boolean cancelT33() {
		if (this.t33 != null) {
			this.t33.cancel(false);
			this.t33 = null;
			return true;
		} else {
			return false;
		}
	}

	private class TimerT1 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t1 = null;
				// start T1
				startT1();
				// send
				provider.send(t1t5encodedREL);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T1);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT5 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove t5, its current runnable.
				t5 = null;
				// cancel T1
				cancelT1();
				// restart T5
				startT5();
				// send
				ResetCircuitMessage rcm = provider.getMessageFactory().createRSC(cic);
				// avoid provider method, since we dont want other timer to be
				// setup.
				provider.sendMessage(rcm);

				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T5);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT7 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				t7 = null;
				// send REL
				ReleaseMessage rel = provider.getMessageFactory().createREL(cic);

				try {
					CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
					// TODO: add CI values
					rel.setCauseIndicators(ci);
					provider.sendMessage(rel);
				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t7AddressMessage, ISUPTimeoutEvent.T7);
				t7AddressMessage = null;
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT12 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t12 = null;
				// start T12
				startT12();
				// send
				provider.send(t12t13encodedBLO);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T12);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT13 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();

				// remove t13, its current runnable.
				t13 = null;
				// cancel T12
				cancelT12();
				// restart T13
				startT13();
				// send
				provider.send(t12t13encodedBLO);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T13);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT14 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t14 = null;
				// start T14
				startT14();
				// send
				provider.send(t14t15encodedUBL);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T14);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT15 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove t15, its current runnable.
				t15 = null;
				// cancel T14
				cancelT14();
				// start
				startT15();
				// send
				provider.send(t14t15encodedUBL);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T15);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT16 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t16 = null;
				// start T14
				startT16();
				// send
				provider.send(t16t17encodedRSC);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T16);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT17 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove t17, its current runnable.
				t17 = null;
				// cancel T16
				cancelT16();
				// restart T17
				startT17();
				// send
				provider.send(t16t17encodedRSC);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T17);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT18 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t18 = null;
				// start T18
				startT18();
				// send
				provider.send(t18t19encodedCGB);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T18);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT19 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove t19, its current runnable.
				t19 = null;
				// cancel T18
				cancelT18();
				// restart T19
				startT19();
				// send
				provider.send(t18t19encodedCGB);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T19);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT20 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t20 = null;
				// start T20
				startT20();
				// send
				provider.send(t20t21encodedCGU);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T20);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT21 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove t21, its current runnable.
				t21 = null;
				// cancel T20
				cancelT20();
				// restart T21
				startT21();
				// send
				provider.send(t20t21encodedCGU);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T21);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT22 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();

				// remove us
				t22 = null;
				// start T22
				startT22();
				// send
				provider.send(t22t23encodedGRS);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T22);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT23 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {

				lock.lock();
				// remove t23, its current runnable.
				t23 = null;
				// cancel T22
				cancelT22();
				// restart T23
				startT23();
				// send
				provider.send(t22t23encodedGRS);
				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T23);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT28 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t28 = null;

				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t28CQM, ISUPTimeoutEvent.T28);
				provider.deliver(timeoutEvent);
				t28CQM = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	private class TimerT33 implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */

		public void run() {
			try {
				lock.lock();
				// remove us
				t33 = null;

				// notify user
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t33INR, ISUPTimeoutEvent.T33);
				provider.deliver(timeoutEvent);
				// FIXME: do this after call, to prevent send of another msg
				t33INR = null;

				// send REL
				ReleaseMessage rel = provider.getMessageFactory().createREL(cic);

				try {
					CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
					// TODO: add CI values
					rel.setCauseIndicators(ci);
					provider.sendMessage(rel);
				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

	}

	/**
	 * 
	 */
	public void onStop() {
		try {
			lock.lock();
			cancelT1();
			cancelT5();
			cancelT12();
			cancelT13();
			cancelT14();
			cancelT15();
			cancelT16();
			cancelT17();
			cancelT18();
			cancelT19();
			cancelT20();
			cancelT21();
			cancelT22();
			cancelT23();
			cancelT28();
			cancelT33();
			
		} finally {
			lock.unlock();
		}
	}

}
