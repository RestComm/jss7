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

package org.mobicents.protocols.ss7.tcap;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDUImpl;
import org.mobicents.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ResultType;
import org.mobicents.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCUnidentifiedMessage;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.Utils;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DraftParsedMessageImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;
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

//    private transient FastMap<Long, DialogImpl> dialogs = new FastMap <Long, DialogImpl>();
    private transient ConcurrentHashMap<Long, DialogImpl> dialogs = new ConcurrentHashMap <Long, DialogImpl>();

//    protected transient FastMap<PrevewDialogDataKey, PrevewDialogData> dialogPreviewList = new FastMap<PrevewDialogDataKey, PrevewDialogData>();
    protected transient ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData> dialogPreviewList = new ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData>();
    private transient FastMap<Integer, NetworkIdState> networkIdStateList = new FastMap<Integer, NetworkIdState>().shared();
    private NetworkIdStateListUpdater currentNetworkIdStateListUpdater;

    private AtomicInteger seqControl = new AtomicInteger(1);
    private int ssn;
    private long curDialogId = 0;
//    private AtomicLong currentDialogId = new AtomicLong(1);

    private int cumulativeCongestionLevel = 0;
    private int executorCongestionLevel = 0;
    private int executorCountWithCongestionLevel_1 = 0;
    private int executorCountWithCongestionLevel_2 = 0;
    private int executorCountWithCongestionLevel_3 = 0;
    private MemoryCongestionMonitorImpl memoryCongestionMonitor;
    private transient FastMap<String, Integer> lstUserPartCongestionLevel = new FastMap<String, Integer>();
    private int userPartCongestionLevel_1 = 0;
    private int userPartCongestionLevel_2 = 0;
    private int userPartCongestionLevel_3 = 0;

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

    private synchronized Long getAvailableTxId() throws TCAPException {
        if (this.dialogs.size() >= this.stack.getMaxDialogs())
            throw new TCAPException("Current dialog count exceeds its maximum value");

        while (true) {
//            Long id;
//            if (!currentDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart() + 1)) {
//                id = currentDialogId.getAndIncrement();
//            } else {
//                id = this.stack.getDialogIdRangeStart();
//            }
//            if (checkAvailableTxId(id))
//                return id;



            if (this.curDialogId < this.stack.getDialogIdRangeStart())
                this.curDialogId = this.stack.getDialogIdRangeStart() - 1;
            if (++this.curDialogId > this.stack.getDialogIdRangeEnd())
                this.curDialogId = this.stack.getDialogIdRangeStart();
            Long id = this.curDialogId;
            if (checkAvailableTxId(id))
                return id;
        }
    }

    protected void resetDialogIdValueAfterRangeChange() {
        if (this.curDialogId < this.stack.getDialogIdRangeStart())
            this.curDialogId = this.stack.getDialogIdRangeStart();
        if (this.curDialogId >= this.stack.getDialogIdRangeEnd())
            this.curDialogId = this.stack.getDialogIdRangeEnd() - 1;

        // if (this.currentDialogId.longValue() < this.stack.getDialogIdRangeStart())
        // this.currentDialogId.set(this.stack.getDialogIdRangeStart());
        // if (this.currentDialogId.longValue() >= this.stack.getDialogIdRangeEnd())
        // this.currentDialogId.set(this.stack.getDialogIdRangeEnd() - 1);
    }

    // get next Seq Control value available
    protected int getNextSeqControl() {
        int res = seqControl.getAndIncrement();

        // if (!seqControl.compareAndSet(256, 1)) {
        // return seqControl.getAndIncrement();
        // } else {
        // return 0;
        // }

        // seqControl++;
        // if (seqControl > 255) {
        // seqControl = 0;
        //
        // }
        // return seqControl;

    // get next Seq Control value available

        if (this.stack.getSlsRangeType() == SlsRangeType.Odd) {
            if (res % 2 == 0)
                res++;
        } else if (this.stack.getSlsRangeType() == SlsRangeType.Even) {
            if (res %2 != 0)
                res++;
        }
        res = res & 0xFF;

        return res;
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
        DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), null);
        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateAllLocalEstablishedDialogsCount();
            this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
        }
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
     * .protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, Long id)
     */
    public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id) throws TCAPException {
        DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), id);
        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateAllLocalEstablishedDialogsCount();
            this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
        }
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
     * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        DialogImpl res = _getDialog(localAddress, remoteAddress, false, getNextSeqControl(), null);
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    private DialogImpl getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl, Long id) throws TCAPException {
        return _getDialog(localAddress, remoteAddress, true, seqControl, id);
    }

    private DialogImpl _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured, int seqControl, Long id)
            throws TCAPException {

        if (this.stack.getPreviewMode()) {
            throw new TCAPException("Can not create a Dialog in a PreviewMode");
        }

        if (localAddress == null) {
            throw new NullPointerException("LocalAddress must not be null");
        }

        // synchronized (this.dialogs) {

        if (id == null) {
            id = this.getAvailableTxId();
        } else {
            if (!checkAvailableTxId(id)) {
                throw new TCAPException("Suggested local TransactionId is already present in system: " + id);
            }
        }
        if (structured) {
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl,
                    this.stack.getPreviewMode());

            this.dialogs.put(id, di);
            if (this.stack.getStatisticsEnabled()) {
                this.stack.getCounterProviderImpl().updateMinDialogsCount(this.dialogs.size());
                this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.dialogs.size());
            }

            return di;
        } else {
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this._EXECUTOR, this, seqControl,
                    this.stack.getPreviewMode());
            return di;
        }

        // }

    }

    private void setSsnToDialog(DialogImpl di, int ssn) {
        if (ssn != this.ssn) {
            if (ssn <= 0 || !this.stack.isExtraSsnPresent(ssn))
                ssn = this.ssn;
        }
        di.setLocalSsn(ssn);
    }

    @Override
    public int getCurrentDialogsCount() {
        return this.dialogs.size();
    }

    public void send(byte[] data, boolean returnMessageOnError, SccpAddress destinationAddress, SccpAddress originatingAddress,
            int seqControl, int networkId, int localSsn) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                localSsn, returnMessageOnError, null, null);
        msg.setNetworkId(networkId);
        sccpProvider.updateSPCongestion(ssn, getCumulativeCongestionLevel());
        sccpProvider.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
    }

    public void deliver(DialogImpl dialogImpl, TCBeginIndicationImpl msg) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcBeginReceivedCount(dialogImpl);
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCBegin(msg);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCContinueIndicationImpl tcContinueIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcContinueReceivedCount(dialogImpl);
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCContinue(tcContinueIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCEndIndicationImpl tcEndIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcEndReceivedCount(dialogImpl);
        }
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCEnd(tcEndIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void deliver(DialogImpl dialogImpl, TCPAbortIndicationImpl tcAbortIndication) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcPAbortReceivedCount(dialogImpl, tcAbortIndication.getPAbortCause());
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
            this.stack.getCounterProviderImpl().updateTcUserAbortReceivedCount(dialogImpl);
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
            this.stack.getCounterProviderImpl().updateTcUniReceivedCount(dialogImpl);
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

            // synchronized (this.dialogs) {

            this.dialogs.remove(did);
            if (this.stack.getStatisticsEnabled()) {
                this.stack.getCounterProviderImpl().updateMinDialogsCount(this.dialogs.size());
                this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.dialogs.size());
            }

            // }

            this.doRelease(d);
        }
    }

    private void doRelease(DialogImpl d) {

        if (d.isStructured() && this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateDialogReleaseCount(d);
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
            this.stack.getCounterProviderImpl().updateDialogTimeoutCount(d);
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

    @Override
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
        logger.info("Registered SCCP listener with ssn " + ssn);

        List<Integer> extraSsns = this.stack.getExtraSsns();
        if (extraSsns != null) {
            for (Integer I1 : extraSsns) {
                if (I1 != null) {
                    int extraSsn = I1;
                    this.sccpProvider.registerSccpListener(extraSsn, this);
                    logger.info("Registered SCCP listener with extra ssn " + extraSsn);
                }
            }
        }

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

        List<Integer> extraSsns = this.stack.getExtraSsns();
        if (extraSsns != null) {
            for (Integer I1 : extraSsns) {
                if (I1 != null) {
                    int extraSsn = I1;
                    this.sccpProvider.deregisterSccpListener(extraSsn);
                }
            }
        }

        this.dialogs.clear();
        this.dialogPreviewList.clear();
    }

    protected void sendProviderAbort(PAbortCauseType pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
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
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount(remoteTransactionId, pAbortCause);
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber());
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendProviderAbort(DialogServiceProviderType pt, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, ApplicationContextName acn, int networkId) {
        if (this.stack.getPreviewMode())
            return;

        DialogPortion dp = TcapFactory.createDialogPortion();
        dp.setUnidirectional(false);

        DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
        apdu.setDoNotSendProtocolVersion(this.getStack().getDoNotSendProtocolVersion());

        Result res = TcapFactory.createResult();
        res.setResultType(ResultType.RejectedPermanent);
        ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
        rsd.setDialogServiceProviderType(pt);
        apdu.setResultSourceDiagnostic(rsd);
        apdu.setResult(res);
        apdu.setApplicationContextName(acn);
        dp.setDialogAPDU(apdu);

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setDialogPortion(dp);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            if (this.stack.getStatisticsEnabled()) {
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount(remoteTransactionId, PAbortCauseType.NoReasonGiven);
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber());
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

            // FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

            // asnData - it should pass
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_APPLICATION) {
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                return;
            }

            switch (tag) {
            // continue first, usually we will get more of those. small perf
            // boost
                case TCContinueMessage._TAG:
                    TCContinueMessage tcm = null;
                    try {
                        tcm = TcapFactory.createTCContinueMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCContinueMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (e.getPAbortCauseType() != null) {
                                this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(),
                                        remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                            } else {
                                this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion,
                                        tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                        message.getSls(), message.getNetworkId());
                            }
                        }
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcm.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        return;
                    }

                    long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
                    DialogImpl di;
                    if (this.stack.getPreviewMode()) {
                        PreviewDialogDataKey ky1 = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        long dId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                        PreviewDialogDataKey ky2 = new PreviewDialogDataKey(message.getIncomingOpc(), (message
                                .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky1, ky2, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
                        this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                    } else {
                        di.processContinue(tcm, localAddress, remoteAddress);
                    }

                    break;

                case TCBeginMessage._TAG:
                    TCBeginMessage tcb = null;
                    try {
                        tcb = TcapFactory.createTCBeginMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCBeginMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (e.getPAbortCauseType() != null) {
                                this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(),
                                        remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                            } else {
                                this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion,
                                        tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                        message.getSls(), message.getNetworkId());
                            }
                        }
                        return;
                    }
                    if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
                            && tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDUImpl) {
                        DialogRequestAPDUImpl dlg = (DialogRequestAPDUImpl) tcb.getDialogPortion().getDialogAPDU();
                        if (!dlg.getProtocolVersion().isSupportedVersion()) {
                            logger.error("Unsupported protocol version of  has been received when parsing TCBeginMessage");
                            this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion,
                                    tcb.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    dlg.getApplicationContextName(), message.getNetworkId());
                            return;
                        }
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 2) {
                        // rejecting of new incoming TCAP dialogs
                        this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        return;
                    }

                    di = null;
                    try {
                        if (this.stack.getPreviewMode()) {
                            long dId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());
                            PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingOpc(), (message
                                    .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                    .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress()
                                    .getSubsystemNumber(), dId);
                            di = (DialogImpl) this.createPreviewDialog(ky, localAddress, remoteAddress, 0);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                        } else {
                            di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                        }

                    } catch (TCAPException e) {
                        this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                        return;
                    }

                    if (this.stack.getStatisticsEnabled()) {
                        this.stack.getCounterProviderImpl().updateAllRemoteEstablishedDialogsCount();
                        this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
                    }
                    di.setNetworkId(message.getNetworkId());
                    di.processBegin(tcb, localAddress, remoteAddress);

                    if (this.stack.getPreviewMode()) {
                        di.getPrevewDialogData().setLastACN(di.getApplicationContextName());
                        di.getPrevewDialogData().setOperationsSentB(di.operationsSent);
                        di.getPrevewDialogData().setOperationsSentA(di.operationsSentA);
                    }

                    break;

                case TCEndMessage._TAG:
                    TCEndMessage teb = null;
                    try {
                        teb = TcapFactory.createTCEndMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCEndMessage: " + e.toString(), e);
                        return;
                    }

                    if (this.stack.isCongControl_blockingIncomingTcapMessages() && cumulativeCongestionLevel >= 3) {
                        // rejecting of new incoming TCAP dialogs
                        return;
                    }

                    dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId());
                    if (this.stack.getPreviewMode()) {
                        PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-END: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processEnd(teb, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCAbortMessage._TAG:
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
                        PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
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

                case TCUniMessage._TAG:
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
                    setSsnToDialog(uniDialog, message.getCalledPartyAddress().getSubsystemNumber());
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
            int networkId) throws ParseException {
        if (this.stack.getPreviewMode()) {
            return;
        }

        logger.error(String.format("Rx unidentified tag=%s, tagClass=. SccpMessage=%s", tag, ais.getTagClass(), message));
        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
        tcUnidentified.decode(ais);

        if (tcUnidentified.getOriginatingTransactionId() != null) {
            byte[] otid = tcUnidentified.getOriginatingTransactionId();

            if (tcUnidentified.getDestinationTransactionId() != null) {
                Long dtid = Utils.decodeTransactionId(tcUnidentified.getDestinationTransactionId());
                this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            } else {
                this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            }
        } else {
            this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, new byte[0], remoteAddress, localAddress, message.getSls(), networkId);
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

    protected  Dialog createPreviewDialog(PreviewDialogDataKey ky, SccpAddress localAddress, SccpAddress remoteAddress,
            int seqControl) throws TCAPException {

        if (this.dialogPreviewList.size() >= this.stack.getMaxDialogs())
            throw new TCAPException("Current dialog count exceeds its maximum value");

        Long dialogId = this.getAvailableTxIdPreview();
        PreviewDialogData pdd = new PreviewDialogData(this, dialogId);
        pdd.setPrevewDialogDataKey1(ky);
        PreviewDialogData pddx = this.dialogPreviewList.putIfAbsent(ky, pdd);
        if (pddx != null) {
            this.removePreviewDialog(pddx);
            throw new TCAPException("Dialog with trId=" + ky.origTxId
                    + " is already exists - we ignore it and drops curent dialog");
        }

        DialogImpl di = new DialogImpl(localAddress, remoteAddress, seqControl, this._EXECUTOR, this, pdd, false);

        pdd.startIdleTimer();

        return di;

        // synchronized (this.dialogPreviewList) {
        // if (this.dialogPreviewList.size() >= this.stack.getMaxDialogs())
        // throw new TCAPException("Current dialog count exceeds its maximum value");
        //
        // // checking if a Dialog is current already exists
        // PreviewDialogData pddx = this.dialogPreviewList.get(ky);
        // if (pddx != null) {
        // this.removePreviewDialog(pddx);
        // throw new TCAPException("Dialog with trId=" + ky.origTxId +
        // " is already exists - we ignore it and drops curent dialog");
        // }
        //
        // Long dialogId = this.getAvailableTxIdPreview();
        // PreviewDialogData pdd = new PreviewDialogData(this, dialogId);
        // this.dialogPreviewList.put(ky, pdd);
        // DialogImpl di = new DialogImpl(localAddress, remoteAddress, seqControl, this._EXECUTOR, this, pdd, false);
        // pdd.setPrevewDialogDataKey1(ky);
        //
        // pdd.startIdleTimer();
        //
        // return di;
        // }
    }

    protected synchronized Long getAvailableTxIdPreview() throws TCAPException {
        while (true) {
            // Long id;
            // if (!currentDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart() + 1)) {
            // id = currentDialogId.getAndIncrement();
            // } else {
            // id = this.stack.getDialogIdRangeStart();
            // }
            // return id;

            if (this.curDialogId < this.stack.getDialogIdRangeStart())
                this.curDialogId = this.stack.getDialogIdRangeStart() - 1;
            if (++this.curDialogId > this.stack.getDialogIdRangeEnd())
                this.curDialogId = this.stack.getDialogIdRangeStart();
            Long id = this.curDialogId;
            return id;
        }
    }

    protected Dialog getPreviewDialog(PreviewDialogDataKey ky1, PreviewDialogDataKey ky2, SccpAddress localAddress,
            SccpAddress remoteAddress, int seqControl) {

        // synchronized (this.dialogPreviewList) {

        PreviewDialogData pdd = this.dialogPreviewList.get(ky1);
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
        // }
    }

    protected void removePreviewDialog(DialogImpl di) {
        // synchronized (this.dialogPreviewList) {

        PreviewDialogData pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey1());
        if (pdd == null && di.prevewDialogData.getPrevewDialogDataKey2() != null) {
            pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey2());
        }

        if (pdd != null)
            removePreviewDialog(pdd);

        // }

        this.doRelease(di);
    }

    protected void removePreviewDialog(PreviewDialogData pdd) {

        // synchronized (this.dialogPreviewList) {

        this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey1());
        if (pdd.getPrevewDialogDataKey2() != null) {
            this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey2());
        }

        // }

        pdd.stopIdleTimer();

        // TODO ??? : create Dialog and invoke "this.doRelease(di);"
    }

    @Override
    public DraftParsedMessage parseMessageDraft(byte[] data) {
        try {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();

            AsnInputStream ais = new AsnInputStream(data);

            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_APPLICATION) {
                res.setParsingErrorReason("Message tag class must be CLASS_APPLICATION");
                return res;
            }

            switch (tag) {
                case TCContinueMessage._TAG:
                    AsnInputStream localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCContinueMessage._TAG_OTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("originatingTransactionId tag/tagClass is bad for TC-CONTINUE message");
                        return res;
                    }
                    byte[] originatingTransactionId = localAis.readOctetString();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId));

                    tag = localAis.readTag();
                    if (tag != TCContinueMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-CONTINUE message");
                        return res;
                    }
                    byte[] destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId));

                    res.setMessageType(MessageType.Continue);
                    break;

                case TCBeginMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCBeginMessage._TAG_OTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("originatingTransactionId tag/tagClass is bad for TC-BEGIN message");
                        return res;
                    }
                    originatingTransactionId = localAis.readOctetString();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId));

                    res.setMessageType(MessageType.Begin);
                    break;

                case TCEndMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCEndMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-END message");
                        return res;
                    }
                    destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId));

                    res.setMessageType(MessageType.End);
                    break;

                case TCAbortMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCAbortMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-ABORT message");
                        return res;
                    }
                    destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId));

                    res.setMessageType(MessageType.Abort);
                    break;

                case TCUniMessage._TAG:
                    res.setMessageType(MessageType.Unidirectional);
                    break;

                default:
                    res.setParsingErrorReason("Unrecognized message tag");
                    break;
            }

            return res;
        }
        catch (Exception e) {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();
            res.setParsingErrorReason("Exception when message parsing: " + e.getMessage());
            return res;
        }
    }

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
            logger.warn("Outgoing congestion control: TCAP: onNetworkIdState: networkId=" + networkId + ", networkIdState="
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
        this._EXECUTOR.schedule(currentNetworkIdStateListUpdater, 1000, TimeUnit.MILLISECONDS);

        networkIdStateList = this.sccpProvider.getNetworkIdStateList();

        int cntNotAvailable = 0;
        int cntCongLevel1 = 0;
        int cntCongLevel2 = 0;
        int cntCongLevel3 = 0;
        if (networkIdStateList != null) {
            for (NetworkIdState state : networkIdStateList.values()) {
                if (!state.isAvailavle())
                    cntNotAvailable++;
                if (state.getCongLevel() >= 1) {
                    cntCongLevel1++;
                }
                if (state.getCongLevel() >= 2) {
                    cntCongLevel1++;
                    cntCongLevel2++;
                }
                if (state.getCongLevel() >= 3) {
                    cntCongLevel1++;
                    cntCongLevel2++;
                    cntCongLevel3++;
                }
            }
        }

        this.stack.getCounterProviderImpl().updateMaxNetworkIdAreasNotAvailable(cntNotAvailable);
        this.stack.getCounterProviderImpl().updateMaxNetworkIdAreasCongLevel_1(cntCongLevel1);
        this.stack.getCounterProviderImpl().updateMaxNetworkIdAreasCongLevel_2(cntCongLevel2);
        this.stack.getCounterProviderImpl().updateMaxNetworkIdAreasCongLevel_3(cntCongLevel3);
    }

    public int getNetworkIdAreasNotAvailableCount() {
        int cntNotAvailable = 0;
        for (NetworkIdState state : networkIdStateList.values()) {
            if (!state.isAvailavle())
                cntNotAvailable++;
        }
        return cntNotAvailable;
    }

    public int getNetworkIdAreasCongLevel_1_Count() {
        int cntCongLevel1 = 0;
        for (NetworkIdState state : networkIdStateList.values()) {
            if (state.getCongLevel() >= 1) {
                cntCongLevel1++;
            }
        }
        return cntCongLevel1;
    }

    public int getNetworkIdAreasCongLevel_2_Count() {
        int cntCongLevel2 = 0;
        for (NetworkIdState state : networkIdStateList.values()) {
            if (state.getCongLevel() >= 2) {
                cntCongLevel2++;
            }
        }
        return cntCongLevel2;
    }

    public int getNetworkIdAreasCongLevel_3_Count() {
        int cntCongLevel3 = 0;
        for (NetworkIdState state : networkIdStateList.values()) {
            if (state.getCongLevel() >= 3) {
                cntCongLevel3++;
            }
        }
        return cntCongLevel3;
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
            int countExecutorCountWithCongestionLevel_1 = 0;
            int countExecutorCountWithCongestionLevel_2 = 0;
            int countExecutorCountWithCongestionLevel_3 = 0;
            for (ExecutorCongestionMonitor ecm : lst) {
                int level = ecm.getAlarmLevel();
                if (maxExecutorCongestionLevel < level)
                    maxExecutorCongestionLevel = level;
                if (level >= 1) {
                    countExecutorCountWithCongestionLevel_1++;
                }
                if (level >= 2) {
                    countExecutorCountWithCongestionLevel_1++;
                    countExecutorCountWithCongestionLevel_2++;
                }
                if (level >= 3) {
                    countExecutorCountWithCongestionLevel_1++;
                    countExecutorCountWithCongestionLevel_2++;
                    countExecutorCountWithCongestionLevel_3++;
                }
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
            executorCountWithCongestionLevel_1 = countExecutorCountWithCongestionLevel_1;
            executorCountWithCongestionLevel_2 = countExecutorCountWithCongestionLevel_2;
            executorCountWithCongestionLevel_3 = countExecutorCountWithCongestionLevel_3;

            stack.getCounterProviderImpl().updateMaxExecutorsCongLevel_1(executorCountWithCongestionLevel_1);
            stack.getCounterProviderImpl().updateMaxExecutorsCongLevel_2(executorCountWithCongestionLevel_2);
            stack.getCounterProviderImpl().updateMaxExecutorsCongLevel_3(executorCountWithCongestionLevel_3);

            // MemoryMonitor

            memoryCongestionMonitor.setMemoryThreshold1(stack.getCongControl_MemoryThreshold_1());
            memoryCongestionMonitor.setMemoryThreshold2(stack.getCongControl_MemoryThreshold_2());
            memoryCongestionMonitor.setMemoryThreshold3(stack.getCongControl_MemoryThreshold_3());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold1(stack.getCongControl_BackToNormalMemoryThreshold_1());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold2(stack.getCongControl_BackToNormalMemoryThreshold_2());
            memoryCongestionMonitor.setBackToNormalMemoryThreshold3(stack.getCongControl_BackToNormalMemoryThreshold_3());

            memoryCongestionMonitor.monitor();
            stack.getCounterProviderImpl().updateMaxMemoryCongLevel(memoryCongestionMonitor.getAlarmLevel());

            // cumulativeCongestionLevel
            int newCumulativeCongestionLevel = getCumulativeCongestionLevel();
            if (cumulativeCongestionLevel != newCumulativeCongestionLevel) {
                logger.warn("Outgoing congestion control: Changing of internal congestion level: " + cumulativeCongestionLevel
                        + "->" + newCumulativeCongestionLevel + "\n" + getCumulativeCongestionLevelString());
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

            int cntUserPartCongestionLevel_1 = 0;
            int cntUserPartCongestionLevel_2 = 0;
            int cntUserPartCongestionLevel_3 = 0;
            for (Integer lev : lstUserPartCongestionLevel.values()) {
                if (lev >= 1) {
                    cntUserPartCongestionLevel_1++;
                }
                if (lev >= 2) {
                    cntUserPartCongestionLevel_1++;
                    cntUserPartCongestionLevel_2++;
                }
                if (lev >= 3) {
                    cntUserPartCongestionLevel_1++;
                    cntUserPartCongestionLevel_2++;
                    cntUserPartCongestionLevel_3++;
                }
            }
            userPartCongestionLevel_1 = cntUserPartCongestionLevel_1;
            userPartCongestionLevel_2 = cntUserPartCongestionLevel_2;
            userPartCongestionLevel_3 = cntUserPartCongestionLevel_3;

            stack.getCounterProviderImpl().updateMaxUserPartsCongLevel_1(userPartCongestionLevel_1);
            stack.getCounterProviderImpl().updateMaxUserPartsCongLevel_2(userPartCongestionLevel_2);
            stack.getCounterProviderImpl().updateMaxUserPartsCongLevel_3(userPartCongestionLevel_3);
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

    public int getExecutorCountWithCongestionLevel_1() {
        return executorCountWithCongestionLevel_1;
    }

    public int getExecutorCountWithCongestionLevel_2() {
        return executorCountWithCongestionLevel_2;
    }

    public int getExecutorCountWithCongestionLevel_3() {
        return executorCountWithCongestionLevel_3;
    }

    public int getUserPartCongestionLevel_1() {
        return userPartCongestionLevel_1;
    }

    public int getUserPartCongestionLevel_2() {
        return userPartCongestionLevel_2;
    }

    public int getUserPartCongestionLevel_3() {
        return userPartCongestionLevel_3;
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
