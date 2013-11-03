/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.isup.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.scheduler.ConcurrentLinkedList;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;

/**
 *
 * @author baranowb
 * @author amit bhayani
 *
 */
@SuppressWarnings("rawtypes")
class Circuit {
    private final int cic;
    private final int dpc;
    private final ISUPProviderImpl provider;

    private ReentrantLock lock = new ReentrantLock();

    private ConcurrentLinkedList<ISUPMessage> incoming = new ConcurrentLinkedList<ISUPMessage>();
    private ConcurrentLinkedList<ISUPMessage> outgoing = new ConcurrentLinkedList<ISUPMessage>();

    private Sender sender;
    private Receiver receiver;

    private ByteArrayOutputStream bos = new ByteArrayOutputStream(300);

    /**
     * @param cic
     */
    public Circuit(int cic, int dpc, ISUPProviderImpl provider, Scheduler scheduler) {
        sender = new Sender(scheduler);
        receiver = new Receiver(scheduler);
        this.cic = cic;
        this.dpc = dpc;
        this.provider = provider;

        // create all timers and deactivate them
        t1 = new TimerT1(scheduler);
        t5 = new TimerT5(scheduler);
        t7 = new TimerT7(scheduler);
        t12 = new TimerT12(scheduler);
        t13 = new TimerT13(scheduler);
        t14 = new TimerT14(scheduler);
        t15 = new TimerT15(scheduler);
        t16 = new TimerT16(scheduler);
        t17 = new TimerT17(scheduler);
        t18 = new TimerT18(scheduler);
        t19 = new TimerT19(scheduler);
        t20 = new TimerT20(scheduler);
        t21 = new TimerT21(scheduler);
        t22 = new TimerT22(scheduler);
        t23 = new TimerT23(scheduler);
        t28 = new TimerT28(scheduler);
        t33 = new TimerT33(scheduler);
        onStop();
    }

    /**
     * @return the cic
     */
    public int getCic() {
        return cic;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
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
        incoming.offer(message);
        receiver.submit();
    }

    /**
     * @param message
     * @throws ParameterException
     * @throws IOException
     */
    public void send(ISUPMessage message) throws ParameterException, IOException {
        outgoing.offer(message);
        sender.submit();
    }

    /**
     * @param message
     * @return
     * @throws ParameterException
     * @throws IOException
     */
    private Mtp3TransferPrimitive decorate(ISUPMessage message) throws ParameterException, IOException {
        ((AbstractISUPMessage) message).encode(bos);
        byte[] encoded = bos.toByteArray();
        int opc = this.provider.getLocalSpc();
        int dpc = this.dpc;
        int si = Mtp3._SI_SERVICE_ISUP;
        int ni = this.provider.getNi();
        int sls = message.getSls() & 0x0F; // promote

        Mtp3TransferPrimitiveFactory factory = this.provider.stack.getMtp3UserPart().getMtp3TransferPrimitiveFactory();
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(si, ni, 0, opc, dpc, sls, encoded);
        return msg;
    }

    // --------------- data handlers ---------------------------
    private class Receiver extends Task {
        public Receiver(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.L4READ_QUEUE;
        }

        public void submit() {
            scheduler.submit(this, getQueueNumber());
        }

        public long perform() {
            lock.lock();
            ISUPMessage message;
            while (!incoming.isEmpty()) {
                message = incoming.poll();
                try {
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
                    ISUPEvent event = new ISUPEvent(provider, message, dpc);
                    provider.deliver(event);
                } catch (Exception e) {
                    // catch exception, so thread dont die.
                    e.printStackTrace();
                }
            }

            lock.unlock();
            return 0;
        }
    }

    private class Sender extends Task {
        public Sender(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.L4WRITE_QUEUE;
        }

        public void submit() {
            scheduler.submit(this, getQueueNumber());
        }

        public long perform() {
            ISUPMessage message;
            while (!outgoing.isEmpty()) {
                try {
                    message = outgoing.poll();
                    lock.lock();
                    bos.reset();

                    // FIXME: add SEG creation?
                    Mtp3TransferPrimitive msg = decorate(message);
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
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

            return 0;
        }
    }

    // ----------------- timer handlers ----------------------

    // FIXME: check how t3 works....

    private TimerT1 t1;
    private TimerT5 t5;
    private Mtp3TransferPrimitive t1t5encodedREL; // keep encoded value, so we can simply send,
    // without spending CPU on encoding.
    private ReleaseMessage t1t5REL; // keep for timers.
    private TimerT7 t7;
    private ISUPMessage t7AddressMessage; // IAM/SAM

    // FIXME: t8 - receive IAM with contuuity check ind.
    // FIXME: t11

    private TimerT12 t12;
    private TimerT13 t13;
    private Mtp3TransferPrimitive t12t13encodedBLO; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private BlockingMessage t12t13BLO; // keep for timers.

    private TimerT14 t14;
    private TimerT15 t15;
    private Mtp3TransferPrimitive t14t15encodedUBL; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private UnblockingMessage t14t15UBL; // keep for timers.

    private TimerT16 t16;
    private TimerT17 t17;
    private Mtp3TransferPrimitive t16t17encodedRSC; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private ResetCircuitMessage t16t17RSC; // keep for timers.

    private TimerT18 t18;
    private TimerT19 t19;
    private Mtp3TransferPrimitive t18t19encodedCGB; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private CircuitGroupBlockingMessage t18t19CGB; // keep for timers.

    private TimerT20 t20;
    private TimerT21 t21;
    private Mtp3TransferPrimitive t20t21encodedCGU; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private CircuitGroupUnblockingMessage t20t21CGU; // keep for timers.

    private TimerT22 t22;
    private TimerT23 t23;
    private Mtp3TransferPrimitive t22t23encodedGRS; // keep encoded value, so we can simply
    // send, without spending CPU on
    // encoding.
    private CircuitGroupResetMessage t22t23GRS; // keep for timers.

    private TimerT28 t28;
    private CircuitGroupQueryMessage t28CQM;

    private TimerT33 t33;
    private InformationRequestMessage t33INR;

    // FIXME: t34 - check how SEG works

    private void startRELTimers(Mtp3TransferPrimitive encoded, ReleaseMessage rel) {
        // FIXME: add lock ?
        this.t1t5encodedREL = encoded;
        this.t1t5REL = rel;

        // it is started always.
        startT1();

        if (!this.t5.isActive()) {
            startT5();
        }
    }

    /**
     * @return
     */
    private boolean stopRELTimers() {
        if (this.t1.isActive() || this.t5.isActive()) {
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
    private void startBLOTimers(Mtp3TransferPrimitive encoded, BlockingMessage message) {
        this.t12t13BLO = message;
        this.t12t13encodedBLO = encoded;
        // it is started always.
        startT12();

        if (!this.t13.isActive())
            startT13();
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
    private void startUBLTimers(Mtp3TransferPrimitive encoded, UnblockingMessage message) {
        this.t14t15UBL = message;
        this.t14t15encodedUBL = encoded;
        // it is started always.
        startT14();

        if (!this.t15.isActive())
            startT15();
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
    private void startRSCTimers(Mtp3TransferPrimitive encoded, ResetCircuitMessage message) {
        this.t16t17RSC = message;
        this.t16t17encodedRSC = encoded;
        // it is started always.
        startT16();

        if (!this.t17.isActive())
            startT17();
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
    private void startGRSTimers(Mtp3TransferPrimitive encoded, CircuitGroupResetMessage message) {
        this.t22t23GRS = message;
        this.t22t23encodedGRS = encoded;
        // it is started always.
        startT22();

        if (!this.t23.isActive())
            startT23();
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
    private void startCGUTimers(Mtp3TransferPrimitive encoded, CircuitGroupUnblockingMessage message) {
        this.t20t21CGU = message;
        this.t20t21encodedCGU = encoded;
        // it is started always.
        startT20();

        if (!this.t21.isActive())
            startT21();
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
    private void startCGBTimers(Mtp3TransferPrimitive encoded, CircuitGroupBlockingMessage message) {
        this.t18t19CGB = message;
        this.t18t19encodedCGB = encoded;
        // it is started always.
        startT18();

        if (!this.t19.isActive())
            startT19();
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
        this.t1.start();
    }

    private boolean cancelT1() {
        if (this.t1.isActive()) {
            this.t1.cancel();
            return true;
        }

        return false;
    }

    private void startT5() {
        cancelT5();
        this.t5.start();
    }

    private boolean cancelT5() {
        if (this.t5.isActive()) {
            this.t5.cancel();
            return true;
        }

        return false;
    }

    private void startT7() {
        cancelT7();
        this.t7.start();
    }

    private boolean cancelT7() {
        if (this.t7.isActive()) {
            this.t7.cancel();
            return true;
        }

        return false;
    }

    private void startT12() {
        cancelT12();
        this.t12.start();
    }

    private boolean cancelT12() {
        if (this.t12.isActive()) {
            this.t12.cancel();
            return true;
        }

        return false;
    }

    private void startT13() {
        cancelT13();
        this.t13.start();
    }

    private boolean cancelT13() {
        if (this.t13.isActive()) {
            this.t13.cancel();
            return true;
        }

        return false;
    }

    private void startT14() {
        cancelT14();
        this.t14.start();
    }

    private boolean cancelT14() {
        if (this.t14.isActive()) {
            this.t14.cancel();
            return true;
        }

        return false;
    }

    private void startT15() {
        cancelT15();
        this.t15.start();
    }

    private boolean cancelT15() {
        if (this.t15.isActive()) {
            this.t15.cancel();
            return true;
        }

        return false;
    }

    private void startT16() {
        cancelT16();
        this.t16.start();
    }

    private boolean cancelT16() {
        if (this.t16.isActive()) {
            this.t16.cancel();
            return true;
        }

        return false;
    }

    private void startT17() {
        cancelT17();
        this.t17.start();
    }

    private boolean cancelT17() {
        if (this.t17.isActive()) {
            this.t17.cancel();
            return true;
        }

        return false;
    }

    private void startT18() {
        cancelT18();
        this.t18.start();
    }

    private boolean cancelT18() {
        if (this.t18.isActive()) {
            this.t18.cancel();
            return true;
        }

        return false;
    }

    private void startT19() {
        cancelT19();
        this.t19.start();
    }

    private boolean cancelT19() {
        if (this.t19.isActive()) {
            this.t19.cancel();
            return true;
        }

        return false;
    }

    private void startT20() {
        cancelT20();
        this.t20.start();
    }

    private boolean cancelT20() {
        if (this.t20.isActive()) {
            this.t20.cancel();
            return true;
        }

        return false;
    }

    private void startT21() {
        cancelT21();
        this.t21.start();
    }

    private boolean cancelT21() {
        if (this.t21.isActive()) {
            this.t21.cancel();
            return true;
        }

        return false;
    }

    private void startT22() {
        cancelT22();
        this.t22.start();
    }

    private boolean cancelT22() {
        if (this.t22.isActive()) {
            this.t22.cancel();
            return true;
        }

        return false;
    }

    private void startT23() {
        cancelT23();
        this.t23.start();
    }

    private boolean cancelT23() {
        if (this.t23.isActive()) {
            this.t23.cancel();
            return true;
        }

        return false;
    }

    private void startT28() {
        cancelT28();
        this.t28.start();
    }

    private boolean cancelT28() {
        if (this.t28.isActive()) {
            this.t28.cancel();
            return true;
        }

        return false;
    }

    private void startT33() {
        cancelT33();
        this.t33.start();
    }

    private boolean cancelT33() {
        if (this.t33.isActive()) {
            this.t33.cancel();
            return true;
        }

        return false;
    }

    private class TimerT1 extends Task {
        private int ttl;

        public TimerT1(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT1Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // cancel it
                this.cancel();
                // start T1
                startT1();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        // TODO: CI required ?
                        provider.send(t1t5encodedREL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T1, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT5 extends Task {
        private int ttl;

        public TimerT5(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT5Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t5, its current runnable.
                cancelT1();
                // cancel it
                this.cancel();
                // restart T5
                startT5();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        final ResetCircuitMessage rcm = provider.getMessageFactory().createRSC(cic);
                        // avoid provider method, since we dont want other timer to be
                        // setup.
                        provider.sendMessage(rcm, dpc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T5, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT7 extends Task {
        private int ttl;

        public TimerT7(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT7Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // cancel it
                this.cancel();
                // send REL
                if (provider.isAutomaticTimerMessages())
                    try {
                        final ReleaseMessage rel = provider.getMessageFactory().createREL(cic);
                        final CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
                        ci.setCauseValue(CauseIndicators._CV_NORMAL_UNSPECIFIED);
                        rel.setCauseIndicators(ci);
                        provider.sendMessage(rel, dpc);
                    } catch (ParameterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t7AddressMessage, ISUPTimeoutEvent.T7, dpc);
                t7AddressMessage = null;
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT12 extends Task {
        private int ttl;

        public TimerT12(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT12Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // cancel it
                this.cancel();
                // start T12
                startT12();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t12t13encodedBLO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T12, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT13 extends Task {
        private int ttl;

        public TimerT13(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT13Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // cancel it
                this.cancel();
                // cancel T12
                cancelT12();
                // restart T13
                startT13();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t12t13encodedBLO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T13, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT14 extends Task {
        private int ttl;

        public TimerT14(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT14Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // cancel it
                this.cancel();
                // start T14
                startT14();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t14t15encodedUBL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T14, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT15 extends Task {
        private int ttl;

        public TimerT15(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT15Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t15, its current runnable.
                this.cancel();
                // cancel T14
                cancelT14();
                // start
                startT15();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t14t15encodedUBL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T15, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT16 extends Task {
        private int ttl;

        public TimerT16(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT16Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // start T14
                startT16();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t16t17encodedRSC);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T16, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT17 extends Task {
        private int ttl;

        public TimerT17(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT17Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t17, its current runnable.
                this.cancel();
                // cancel T16
                cancelT16();
                // restart T17
                startT17();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t16t17encodedRSC);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T17, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT18 extends Task {
        private int ttl;

        public TimerT18(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT18Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // start T18
                startT18();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t18t19encodedCGB);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T18, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT19 extends Task {
        private int ttl;

        public TimerT19(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT19Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t19, its current runnable.
                this.cancel();
                // cancel T18
                cancelT18();
                // restart T19
                startT19();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t18t19encodedCGB);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T19, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT20 extends Task {
        private int ttl;

        public TimerT20(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT20Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // start T20
                startT20();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t20t21encodedCGU);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T20, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT21 extends Task {
        private int ttl;

        public TimerT21(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT21Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t21, its current runnable.
                this.cancel();
                // cancel T20
                cancelT20();
                // restart T21
                startT21();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t20t21encodedCGU);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T21, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT22 extends Task {
        private int ttl;

        public TimerT22(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT22Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // start T22
                startT22();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t22t23encodedGRS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T22, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT23 extends Task {
        private int ttl;

        public TimerT23(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT23Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove t23, its current runnable.
                this.cancel();
                // cancel T22
                cancelT22();
                // restart T23
                startT23();
                // send
                if (provider.isAutomaticTimerMessages())
                    try {
                        provider.send(t22t23encodedGRS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T23, dpc);
                provider.deliver(timeoutEvent);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT28 extends Task {
        private int ttl;

        public TimerT28(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT28Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // notify user
                final CircuitGroupQueryMessage msg = t28CQM;
                t28CQM = null;
                ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, msg, ISUPTimeoutEvent.T28, dpc);
                provider.deliver(timeoutEvent);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
        }
    }

    private class TimerT33 extends Task {
        private int ttl;

        public TimerT33(Scheduler scheduler) {
            super(scheduler);
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        public void start() {
            this.activate(true);
            ttl = (int) (provider.getT33Timeout() / 100);
            scheduler.submitHeatbeat(this);
        }

        public long perform() {
            if (ttl > 0) {
                ttl--;
                scheduler.submitHeatbeat(this);
                return 0;
            }

            try {
                lock.lock();
                // remove us
                this.cancel();
                // send REL
                if (provider.isAutomaticTimerMessages())
                    try {
                        final ReleaseMessage rel = provider.getMessageFactory().createREL(cic);
                        final CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
                        ci.setCauseValue(CauseIndicators._CV_NORMAL_UNSPECIFIED);
                        rel.setCauseIndicators(ci);
                        provider.sendMessage(rel, dpc);
                    } catch (ParameterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                final InformationRequestMessage msg = t33INR;
                t33INR = null;
                // notify user
                final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, msg, ISUPTimeoutEvent.T33, dpc);
                provider.deliver(timeoutEvent);
                // FIXME: do this after call, to prevent send of another msg

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            return 0;
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
