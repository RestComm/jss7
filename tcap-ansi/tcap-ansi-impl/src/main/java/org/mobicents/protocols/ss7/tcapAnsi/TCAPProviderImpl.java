/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tcapAnsi;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcapAnsi.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TCNoticeIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TCResponseMessageImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TCUnidentifiedMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcapAnsi.asn.Utils;
import org.mobicents.protocols.ss7.tcapAnsi.tc.component.ComponentPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCQueryIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCConversationIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCResponseIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCUniIndicationImpl;
import org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events.TCUserAbortIndicationImpl;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.mobicents.ss7.congestion.MemoryCongestionMonitorImpl;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCAPProviderImpl implements TCAPProvider, SccpListener {

    private static final Logger logger = Logger.getLogger(TCAPProviderImpl.class); // listenres

    private transient List<TCListener> tcListeners = new CopyOnWriteArrayList<TCListener>();
    protected transient ScheduledExecutorService _EXECUTOR;
    // boundry for Uni directional dialogs :), tx id is always encoded
    // on 4 octets, so this is its max value
    // private static final long _4_OCTETS_LONG_FILL = 4294967295l;
    private transient ComponentPrimitiveFactory componentPrimitiveFactory;
    private transient DialogPrimitiveFactory dialogPrimitiveFactory;
    private transient SccpProvider sccpProvider;

    private transient MessageFactory messageFactory;
    private transient ParameterFactory parameterFactory;

    private transient TCAPStackImpl stack; // originating TX id ~=Dialog, its direct
    // mapping, but not described
    // explicitly...
    private transient FastMap<Long, DialogImpl> dialogs = new FastMap<Long, DialogImpl>();
    protected transient FastMap<PrevewDialogDataKey, PrevewDialogData> dialogPreviewList = new FastMap<PrevewDialogDataKey, PrevewDialogData>();
    private transient FastMap<Integer, NetworkIdState> networkIdStateList = new FastMap<Integer, NetworkIdState>().shared();
    private NetworkIdStateListUpdater currentNetworkIdStateListUpdater;

    private int seqControl = 0;
    private int ssn;
    private long curDialogId = 0;

    private int cumulativeCongestionLevel = 0;
    private int executorCongestionLevel = 0;
    private MemoryCongestionMonitorImpl memoryCongestionMonitor;
    private FastMap<String, Integer> lstUserPartCongestionLevel = new FastMap<String, Integer>();

    protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn) {
        super();
        this.sccpProvider = sccpProvider;
        this.ssn = ssn;
        messageFactory = sccpProvider.getMessageFactory();
        parameterFactory = sccpProvider.getParameterFactory();
        this.stack = stack;

        this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl(this);
        this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
    }

    public boolean getPreviewMode() {
        return this.stack.getPreviewMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#addTCListener(org.mobicents .protocols.ss7.tcap.api.TCListener)
     */

    public void addTCListener(TCListener lst) {
        if (this.tcListeners.contains(lst)) {
        } else {
            this.tcListeners.add(lst);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#removeTCListener(org.mobicents .protocols.ss7.tcap.api.TCListener)
     */
    public void removeTCListener(TCListener lst) {
        this.tcListeners.remove(lst);

    }

    private boolean checkAvailableTxId(Long id) {
        if (!this.dialogs.containsKey(id))
            return true;
        else
            return false;
    }

    // some help methods... crude but will work for first impl.
    private Long getAvailableTxId() throws TCAPException {
        if (this.dialogs.size() >= this.stack.getMaxDialogs())
            throw new TCAPException("Current dialog count exceeds its maximum value");

        while (true) {
            if (this.curDialogId < this.stack.getDialogIdRangeStart())
                this.curDialogId = this.stack.getDialogIdRangeStart() - 1;
            if (++this.curDialogId > this.stack.getDialogIdRangeEnd())
                this.curDialogId = this.stack.getDialogIdRangeStart();
            Long id = this.curDialogId;
            if (checkAvailableTxId(id))
                return id;
        }
    }

    // get next Seq Control value available
    private synchronized int getNextSeqControl() {
        seqControl++;
        if (seqControl > 255) {
            seqControl = 0;

        }
        return seqControl;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.TCAPProvider# getComopnentPrimitiveFactory()
     */
    public ComponentPrimitiveFactory getComponentPrimitiveFactory() {

        return this.componentPrimitiveFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getDialogPrimitiveFactory ()
     */
    public DialogPrimitiveFactory getDialogPrimitiveFactory() {

        return this.dialogPrimitiveFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
     * .protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        Dialog res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), null);
        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateAllLocalEstablishedDialogsCount();
            this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
        }
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
     * .protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, Long id)
     */
    public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id) throws TCAPException {
        Dialog res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), id);
        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateAllLocalEstablishedDialogsCount();
            this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
        }
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
     * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        return _getDialog(localAddress, remoteAddress, false, getNextSeqControl(), null);
    }

    private Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl, Long id) throws TCAPException {
        return _getDialog(localAddress, remoteAddress, true, seqControl, id);
    }

    private Dialog _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured, int seqControl, Long id)
            throws TCAPException {

        if (this.stack.getPreviewMode()) {
            throw new TCAPException("Can not create a Dialog in a PreviewMode");
        }

        if (localAddress == null) {
            throw new NullPointerException("LocalAddress must not be null");
        }

        synchronized (this.dialogs) {
            if (id == null) {
                id = this.getAvailableTxId();
            } else {
                if (!checkAvailableTxId(id)) {
                    throw new TCAPException("Suggested local TransactionId is already present in system: " + id);
                }
            }
            if (structured) {
                DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl, this.stack.getPreviewMode());

                this.dialogs.put(id, di);
                if (this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateMinDialogsCount(this.dialogs.size());
                    this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.dialogs.size());
                }

                return di;
            } else {
                DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl, this.stack.getPreviewMode());
                return di;
            }
        }
    }

    protected long getCurrentDialogsCount() {
        return this.dialogs.size();
    }

    public void send(byte[] data, boolean returnMessageOnError, SccpAddress destinationAddress, SccpAddress originatingAddress,
            int seqControl, int networkId) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                this.ssn, returnMessageOnError, null, null);
        msg.setNetworkId(networkId);
        sccpProvider.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int networkId) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, networkId);
    }

    public void deliver(DialogImpl dialogImpl, TCQueryIndicationImpl msg) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcQueryReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCQuery(msg);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCConversationIndicationImpl tcContinueIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcConversationReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCConversation(tcContinueIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCResponseIndicationImpl tcEndIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcResponseReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCResponse(tcEndIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void deliver(DialogImpl dialogImpl, TCPAbortIndicationImpl tcAbortIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcPAbortReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCPAbort(tcAbortIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCUserAbortIndicationImpl tcAbortIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcUserAbortReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCUserAbort(tcAbortIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCUniIndicationImpl tcUniIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcUniReceivedCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCUni(tcUniIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void deliver(DialogImpl dialogImpl, TCNoticeIndicationImpl tcNoticeIndication) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCNotice(tcNoticeIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void release(DialogImpl d) {
        Long did = d.getLocalDialogId();

        if (!d.getPreviewMode()) {
            synchronized (this.dialogs) {
                this.dialogs.remove(did);
                if (this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateMinDialogsCount(this.dialogs.size());
                    this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.dialogs.size());
                }
            }

            this.doRelease(d);
        }
    }

    private void doRelease(DialogImpl d) {

        if (d.isStructured() && this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateDialogReleaseCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onDialogReleased(d);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering dialog release.", e);
            }
        }
    }

    /**
     * @param d
     */
    public void timeout(DialogImpl d) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateDialogTimeoutCount();
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onDialogTimeout(d);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering dialog release.", e);
            }
        }
    }

    public TCAPStackImpl getStack() {
        return this.stack;
    }

    // ///////////////////////////////////////////
    // Some methods invoked by operation FSM //
    // //////////////////////////////////////////
    public Future createOperationTimer(Runnable operationTimerTask, long invokeTimeout) {

        return this._EXECUTOR.schedule(operationTimerTask, invokeTimeout, TimeUnit.MILLISECONDS);
    }

    public void operationTimedOut(InvokeImpl tcInvokeRequestImpl) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onInvokeTimeout(tcInvokeRequestImpl);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering Begin.", e);
            }
        }
    }

    void start() {
        logger.info("Starting TCAP Provider");

        this._EXECUTOR = Executors.newScheduledThreadPool(4, new DefaultThreadFactory("Tcap-Thread"));
        this.sccpProvider.registerSccpListener(ssn, this);
        logger.info("Registered SCCP listener with address " + ssn);

        // congestion caring
        updateNetworkIdStateList();
        this._EXECUTOR.scheduleWithFixedDelay(new CongestionExecutor(), 1000, 1000, TimeUnit.MILLISECONDS);
        memoryCongestionMonitor = new MemoryCongestionMonitorImpl();
        lstUserPartCongestionLevel.clear();
    }

    void stop() {
        stopNetworkIdStateList();

        this._EXECUTOR.shutdown();
        this.sccpProvider.deregisterSccpListener(ssn);

        this.dialogs.clear();
        this.dialogPreviewList.clear();
    }

    protected void sendProviderAbort(PAbortCause pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId) {
        if (this.stack.getPreviewMode())
            return;

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setPAbortCause(pAbortCause);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            if (this.stack.getStatisticsEnabled()) {
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount();
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendRejectAsProviderAbort(PAbortCause pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId) {
        if (this.stack.getPreviewMode())
            return;

        RejectProblem rp = RejectProblem.getFromPAbortCause(pAbortCause);
        if (rp == null)
            rp = RejectProblem.transactionBadlyStructuredTransPortion;

        TCResponseMessageImpl msg = (TCResponseMessageImpl) TcapFactory.createTCResponseMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        Component[] cc = new Component[1];
        Reject r = TcapFactory.createComponentReject();
        r.setProblem(rp);
        cc[0] = r;
        msg.setComponent(cc);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            if (this.stack.getStatisticsEnabled()) {
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount();
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    public void onMessage(SccpDataMessage message) {

        try {
            byte[] data = message.getData();
            SccpAddress localAddress = message.getCalledPartyAddress();
            SccpAddress remoteAddress = message.getCallingPartyAddress();

            // asnData - it should pass
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_PRIVATE) {
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                return;
            }

            switch (tag) {
                case TCConversationMessage._TAG_CONVERSATION_WITH_PERM:
                case TCConversationMessage._TAG_CONVERSATION_WITHOUT_PERM:
                    TCConversationMessage tcm = null;
                    try {
                        tcm = TcapFactory.createTCConversationMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCConversationMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            boolean isDP = false;
                            if (tcUnidentified.isDialogPortionExists()) {
                                isDP = true;
                            } else {
                                Dialog ddi = null;
                                if (tcUnidentified.getDestinationTransactionId() != null) {
                                    long dialogId = Utils.decodeTransactionId(tcUnidentified.getDestinationTransactionId());
                                    ddi = this.dialogs.get(dialogId);
                                }
                                if (ddi != null && ddi.getProtocolVersion() != null)
                                    isDP = true;
                            }
                            if (isDP) {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendProviderAbort(PAbortCause.BadlyStructuredDialoguePortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            } else {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendRejectAsProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendRejectAsProviderAbort(PAbortCause.BadlyStructuredTransactionPortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            }
                        }
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        this.sendProviderAbort(PAbortCause.ResourceUnavailable, tcm.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        return;
                    }

                    long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
                    DialogImpl di;
                    if (this.stack.getPreviewMode()) {
                        PrevewDialogDataKey ky1 = new PrevewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        long dId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                        PrevewDialogDataKey ky2 = new PrevewDialogDataKey(message.getIncomingOpc(), (message
                                .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky1, ky2, localAddress, remoteAddress, seqControl);
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-Conversation: No dialog/transaction for id: " + dialogId);
                        if (tcm.getDialogPortion() != null) {
                            this.sendProviderAbort(PAbortCause.UnassignedRespondingTransactionID,
                                    tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    message.getNetworkId());
                        } else {
                            this.sendRejectAsProviderAbort(PAbortCause.UnassignedRespondingTransactionID,
                                    tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    message.getNetworkId());
                        }
                    } else {
                        di.processConversation(tcm, localAddress, remoteAddress,
                                tag == TCConversationMessage._TAG_CONVERSATION_WITH_PERM);
                    }

                    break;

                case TCQueryMessage._TAG_QUERY_WITH_PERM:
                case TCQueryMessage._TAG_QUERY_WITHOUT_PERM:
                    TCQueryMessage tcb = null;
                    try {
                        tcb = TcapFactory.createTCQueryMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCQueryMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (tcUnidentified.isDialogPortionExists()) {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendProviderAbort(PAbortCause.BadlyStructuredDialoguePortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            } else {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendRejectAsProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendRejectAsProviderAbort(PAbortCause.BadlyStructuredTransactionPortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            }
                        }
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 2) {
                        // rejecting of new incoming TCAP dialogs
                        this.sendProviderAbort(PAbortCause.ResourceUnavailable, tcb.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        return;
                    }

                    di = null;
                    try {
                        if (this.stack.getPreviewMode()) {
                            long dId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());
                            PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingOpc(), (message
                                    .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                    .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress()
                                    .getSubsystemNumber(), dId);
                            di = (DialogImpl) this.createPreviewDialog(ky, localAddress, remoteAddress, seqControl);
                        } else {
                            di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                        }

                    } catch (TCAPException e) {
                        if (tcb.getDialogPortion() != null) {
                            this.sendProviderAbort(PAbortCause.ResourceUnavailable, tcb.getOriginatingTransactionId(),
                                    remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        } else {
                            this.sendRejectAsProviderAbort(PAbortCause.ResourceUnavailable, tcb.getOriginatingTransactionId(),
                                    remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        }
                        logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                        return;
                    }

                    if (tcb.getDialogPortion() != null) {
                        if (tcb.getDialogPortion().getProtocolVersion() != null) {
                            di.setProtocolVersion(tcb.getDialogPortion().getProtocolVersion());
                        } else {
                            ProtocolVersion pv = TcapFactory.createProtocolVersionEmpty();
                            di.setProtocolVersion(pv);
                        }
                    }

                    if (this.stack.getStatisticsEnabled()) {
                        this.stack.getCounterProviderImpl().updateAllRemoteEstablishedDialogsCount();
                        this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
                    }
                    di.setNetworkId(message.getNetworkId());
                    di.processQuery(tcb, localAddress, remoteAddress, tag == TCQueryMessage._TAG_QUERY_WITH_PERM);

                    if (this.stack.getPreviewMode()) {
                        di.getPrevewDialogData().setLastACN(di.getApplicationContextName());
                        di.getPrevewDialogData().setOperationsSentB(di.operationsSent);
                        di.getPrevewDialogData().setOperationsSentA(di.operationsSentA);
                    }

                    break;

                case TCResponseMessage._TAG_RESPONSE:
                    TCResponseMessage teb = null;
                    try {
                        teb = TcapFactory.createTCResponseMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCResponseMessage: " + e.toString(), e);
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        return;
                    }

                    dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId());
                    if (this.stack.getPreviewMode()) {
                        PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, seqControl);
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-Response: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processResponse(teb, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCAbortMessage._TAG_ABORT:
                    TCAbortMessage tub = null;
                    try {
                        tub = TcapFactory.createTCAbortMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCAbortMessage: " + e.toString(), e);
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        return;
                    }

                    dialogId = Utils.decodeTransactionId(tub.getDestinationTransactionId());
                    if (this.stack.getPreviewMode()) {
                        long dId = Utils.decodeTransactionId(tub.getDestinationTransactionId());
                        PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, seqControl);
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-ABORT: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processAbort(tub, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCUniMessage._TAG_UNI:
                    TCUniMessage tcuni;
                    try {
                        tcuni = TcapFactory.createTCUniMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCUniMessage: " + e.toString(), e);
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        return;
                    }

                    DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
                    uniDialog.processUni(tcuni, localAddress, remoteAddress);
                    break;

                default:
                    unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
        }
    }

    private void unrecognizedPackageType(SccpDataMessage message, SccpAddress localAddress, SccpAddress remoteAddress, AsnInputStream ais, int tag,
            int networkId) {
        if (this.stack.getPreviewMode()) {
            return;
        }

        logger.error(String.format("Rx unidentified messageType=%s. SccpMessage=%s", tag, message));
        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
        try {
            tcUnidentified.decode(ais);
        } catch (ParseException e) {
            // we do nothing
        }

        if (tcUnidentified.getOriginatingTransactionId() != null) {
            byte[] otid = tcUnidentified.getOriginatingTransactionId();

            if (tcUnidentified.getDestinationTransactionId() != null) {
                Long dtid = Utils.decodeTransactionId(tcUnidentified.getDestinationTransactionId());
                this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            } else {
                this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            }
        } else {
            this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, new byte[4], remoteAddress, localAddress, message.getSls(), networkId);
        }
    }

    public void onNotice(SccpNoticeMessage msg) {

        if (this.stack.getPreviewMode()) {
            return;
        }

        DialogImpl dialog = null;

        try {
            byte[] data = msg.getData();
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
            tcUnidentified.decode(ais);

            if (tcUnidentified.getOriginatingTransactionId() != null) {
                long otid = Utils.decodeTransactionId(tcUnidentified.getOriginatingTransactionId());
                dialog = this.dialogs.get(otid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg), e);
        }

        TCNoticeIndicationImpl ind = new TCNoticeIndicationImpl();
        ind.setRemoteAddress(msg.getCallingPartyAddress());
        ind.setLocalAddress(msg.getCalledPartyAddress());
        ind.setDialog(dialog);
        ind.setReportCause(msg.getReturnCause().getValue());

        if (dialog != null) {
            try {
                dialog.dialogLock.lock();

                this.deliver(dialog, ind);

                if (dialog.getState() != TRPseudoState.Active) {
                    dialog.release();
                }
            } finally {
                dialog.dialogLock.unlock();
            }
        } else {
            this.deliver(dialog, ind);
        }
    }

    private Dialog createPreviewDialog(PrevewDialogDataKey ky, SccpAddress localAddress, SccpAddress remoteAddress,
            int seqControl) throws TCAPException {
        synchronized (this.dialogPreviewList) {
            if (this.dialogPreviewList.size() >= this.stack.getMaxDialogs())
                throw new TCAPException("Current dialog count exceeds its maximum value");

            // checking if a Dialog is current already exists
            PrevewDialogData pddx = this.dialogPreviewList.get(ky);
            if (pddx != null) {
                this.removePreviewDialog(pddx);
                throw new TCAPException("Dialog with trId=" + ky.origTxId + " is already exists - we ignore it and drops curent dialog");
            }

            Long dialogId = this.getAvailableTxIdPreview();
            PrevewDialogData pdd = new PrevewDialogData(this, dialogId);
            this.dialogPreviewList.put(ky, pdd);
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, seqControl, this._EXECUTOR, this, pdd, false);
            pdd.setPrevewDialogDataKey1(ky);

            pdd.startIdleTimer();

            return di;
        }
    }

    private Long getAvailableTxIdPreview() throws TCAPException {
        while (true) {
            if (this.curDialogId < this.stack.getDialogIdRangeStart())
                this.curDialogId = this.stack.getDialogIdRangeStart() - 1;
            if (++this.curDialogId > this.stack.getDialogIdRangeEnd())
                this.curDialogId = this.stack.getDialogIdRangeStart();
            Long id = this.curDialogId;
            return id;
        }
    }

    protected Dialog getPreviewDialog(PrevewDialogDataKey ky1, PrevewDialogDataKey ky2, SccpAddress localAddress,
            SccpAddress remoteAddress, int seqControl) {
        synchronized (this.dialogPreviewList) {
            PrevewDialogData pdd = this.dialogPreviewList.get(ky1);
            DialogImpl di = null;
            boolean sideB = false;
            if (pdd != null) {
                sideB = pdd.getPrevewDialogDataKey1().equals(ky1);
                di = new DialogImpl(localAddress, remoteAddress, seqControl, this._EXECUTOR, this, pdd, sideB);
            } else {
                if (ky2 != null)
                    pdd = this.dialogPreviewList.get(ky2);
                if (pdd != null) {
                    sideB = pdd.getPrevewDialogDataKey1().equals(ky1);
                    di = new DialogImpl(localAddress, remoteAddress, seqControl, this._EXECUTOR, this, pdd, sideB);
                } else {
                    return null;
                }
            }

            pdd.restartIdleTimer();

            if (pdd.getPrevewDialogDataKey2() == null && ky2 != null) {
                if (pdd.getPrevewDialogDataKey1().equals(ky1))
                    pdd.setPrevewDialogDataKey2(ky2);
                else
                    pdd.setPrevewDialogDataKey2(ky1);
                this.dialogPreviewList.put(pdd.getPrevewDialogDataKey2(), pdd);
            }

            return di;
        }
    }

    protected void removePreviewDialog(DialogImpl di) {
        synchronized (this.dialogPreviewList) {
            PrevewDialogData pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey1());
            if (pdd == null && di.prevewDialogData.getPrevewDialogDataKey2() != null) {
                pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey2());
            }

            if (pdd != null)
                removePreviewDialog(pdd);
        }

        this.doRelease(di);
    }

    protected void removePreviewDialog(PrevewDialogData pdd) {
        synchronized (this.dialogPreviewList) {
            this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey1());
            if (pdd.getPrevewDialogDataKey2() != null) {
                this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey2());
            }
        }
        pdd.stopIdleTimer();

        // TODO ??? : create Dialog and invoke "this.doRelease(di);"
    }
    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
            RemoteSccpStatus remoteSccpStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
        Integer ni = networkId;
        NetworkIdState prev = networkIdStateList.get(ni);
        if (!networkIdState.equals(prev)) {
            logger.warn("Outgoing congestion control: TCAP-ANSI: onNetworkIdState: networkId=" + networkId + ", networkIdState="
                    + networkIdState);
        }

        networkIdStateList.put(ni, networkIdState);
    }

    @Override
    public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
        return networkIdStateList;
    }

    @Override
    public NetworkIdState getNetworkIdState(int networkId) {
        return networkIdStateList.get(networkId);
    }

    private void stopNetworkIdStateList() {
        NetworkIdStateListUpdater curUpd = currentNetworkIdStateListUpdater;
        if (curUpd != null)
            curUpd.cancel();
    }

    private void updateNetworkIdStateList() {
        stopNetworkIdStateList();
        currentNetworkIdStateListUpdater = new NetworkIdStateListUpdater();
        this._EXECUTOR.schedule(currentNetworkIdStateListUpdater, 5000, TimeUnit.MILLISECONDS);

        networkIdStateList = this.sccpProvider.getNetworkIdStateList();
    }

    protected class PrevewDialogDataKey {
        public int dpc;
        public String sccpDigits;
        public int ssn;
        public long origTxId;

        public PrevewDialogDataKey(int dpc, String sccpDigits, int ssn, long txId) {
            this.dpc = dpc;
            this.sccpDigits = sccpDigits;
            this.ssn = ssn;
            this.origTxId = txId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof PrevewDialogDataKey))
                return false;
            PrevewDialogDataKey b = (PrevewDialogDataKey) obj;

            if (this.sccpDigits != null) {
                // sccpDigits + ssn
                if (!this.sccpDigits.equals(b.sccpDigits))
                    return false;
            } else {
                // dpc + ssn
                if (this.dpc != b.dpc)
                    return false;
            }
            if (this.ssn != b.ssn)
                return false;
            if (this.origTxId != b.origTxId)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            if (this.sccpDigits != null) {
                result = prime * result + ((sccpDigits == null) ? 0 : sccpDigits.hashCode());
            } else {
                result = prime * result + this.dpc;
            }
            result = prime * result + this.ssn;
            result = prime * result + (int) (this.origTxId + (this.origTxId >> 32));
            return result;
        }
    }

    private class NetworkIdStateListUpdater implements Runnable, Serializable {
        private boolean isCancelled;

        public void cancel() {
            isCancelled = true;
        }

        @Override
        public void run() {
            if (isCancelled || !stack.isStarted())
                return;

            updateNetworkIdStateList();
        }
    }

    private class CongestionExecutor implements Runnable {

        @Override
        public void run() {
            // MTP3 Executor monitors
            ExecutorCongestionMonitor[] lst = sccpProvider.getExecutorCongestionMonitorList();
            int maxExecutorCongestionLevel = 0;
            for (ExecutorCongestionMonitor ecm : lst) {
                int level = ecm.getAlarmLevel();
                if (maxExecutorCongestionLevel < level)
                    maxExecutorCongestionLevel = level;
                try {
                    ecm.setDelayThreshold_1(stack.getCongControl_ExecutorDelayThreshold_1());
                    ecm.setDelayThreshold_2(stack.getCongControl_ExecutorDelayThreshold_2());
                    ecm.setDelayThreshold_3(stack.getCongControl_ExecutorDelayThreshold_3());
                    ecm.setBackToNormalDelayThreshold_1(stack.getCongControl_ExecutorBackToNormalDelayThreshold_1());
                    ecm.setBackToNormalDelayThreshold_2(stack.getCongControl_ExecutorBackToNormalDelayThreshold_2());
                    ecm.setBackToNormalDelayThreshold_3(stack.getCongControl_ExecutorBackToNormalDelayThreshold_3());
                } catch (Exception e) {
                    // this must not be
                }
            }
            executorCongestionLevel = maxExecutorCongestionLevel;

            // MemoryMonitor
            memoryCongestionMonitor.monitor();
            memoryCongestionMonitor.setMemoryThreshold1(stack.getCongControl_MemoryThreshold_1());
            memoryCongestionMonitor.setMemoryThreshold2(stack.getCongControl_MemoryThreshold_2());
            memoryCongestionMonitor.setMemoryThreshold3(stack.getCongControl_MemoryThreshold_3());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold1(stack.getCongControl_BackToNormalMemoryThreshold_1());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold2(stack.getCongControl_BackToNormalMemoryThreshold_2());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold3(stack.getCongControl_BackToNormalMemoryThreshold_3());

            // cumulativeCongestionLevel
            int newCumulativeCongestionLevel = getCumulativeCongestionLevel();
            if (cumulativeCongestionLevel != newCumulativeCongestionLevel) {
                logger.warn("Outgoing congestion control: Changing of internal congestion level: "
                        + newCumulativeCongestionLevel + "->" + cumulativeCongestionLevel + "\n"
                        + getCumulativeCongestionLevelString());
                cumulativeCongestionLevel = newCumulativeCongestionLevel;
            }
        }
    }

    @Override
    public synchronized void setUserPartCongestionLevel(String congObject, int level) {
        if (congObject != null) {
            if (level > 0) {
                lstUserPartCongestionLevel.put(congObject, level);
            } else {
                lstUserPartCongestionLevel.remove(congObject);
            }
        }
    }

    @Override
    public int getMemoryCongestionLevel() {
        return memoryCongestionMonitor.getAlarmLevel();
    }

    @Override
    public int getExecutorCongestionLevel() {
        return executorCongestionLevel;
    }

    @Override
    public int getCumulativeCongestionLevel() {
        int level = 0;
        for (Integer lev : lstUserPartCongestionLevel.values()) {
            if (level < lev) {
                level = lev;
            }
        }
        int lev2 = getMemoryCongestionLevel();
        if (level < lev2) {
            level = lev2;
        }
        lev2 = getExecutorCongestionLevel();
        if (level < lev2) {
            level = lev2;
        }

        return level;
    }

    protected String getCumulativeCongestionLevelString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CongestionLevel=[");

        int i1 = 0;
        int lev2 = getMemoryCongestionLevel();
        if (lev2 > 0) {
            sb.append("MemoryCongestionLevel=");
            sb.append(lev2);
        }
        lev2 = getExecutorCongestionLevel();
        if (lev2 > 0) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append(", ");
            sb.append("ExecutorCongestionLevel=");
            sb.append(lev2);
        }
        for (Entry<String, Integer> entry : lstUserPartCongestionLevel.entrySet()) {
            lev2 = entry.getValue();
            if (lev2 > 0) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append("UserPartCongestion=");
                sb.append(entry.getKey());
                sb.append("-");
                sb.append(lev2);
            }
        }

        if (this.stack.isCongControl_blockingIncomingTcapMessages()) {
            lev2 = getCumulativeCongestionLevel();
            if (lev2 == 3) {
                sb.append(", all incoming TCAP messages will be rejected");
            }
            if (lev2 == 2) {
                sb.append(", new incoming TCAP dialogs will be rejected");
            }
        }

        sb.append("]");

        return sb.toString();
    }

}
