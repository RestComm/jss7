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

package org.mobicents.protocols.ss7.tcap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
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
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

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
    private transient Map<Long, DialogImpl> dialogs = new HashMap<Long, DialogImpl>();
    protected transient Map<PrevewDialogDataKey, PrevewDialogData> dialogPreviewList = new HashMap<PrevewDialogDataKey, PrevewDialogData>();

    private int seqControl = 0;
    private int ssn;
    private long curDialogId = 0;

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
            int seqControl) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                this.ssn, returnMessageOnError, null, null);
        sccpProvider.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress);
    }

    public void deliver(DialogImpl dialogImpl, TCBeginIndicationImpl msg) {

        if (this.stack.getStatisticsEnabled()) {
            this.stack.getCounterProviderImpl().updateTcBeginReceivedCount();
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
            this.stack.getCounterProviderImpl().updateTcContinueReceivedCount();
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
            this.stack.getCounterProviderImpl().updateTcEndReceivedCount();
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

        this._EXECUTOR = Executors.newScheduledThreadPool(4);
        this.sccpProvider.registerSccpListener(ssn, this);
        logger.info("Registered SCCP listener with address " + ssn);
    }

    void stop() {
        this._EXECUTOR.shutdown();
        this.sccpProvider.deregisterSccpListener(ssn);

        this.dialogs.clear();
        this.dialogPreviewList.clear();
    }

    protected void sendProviderAbort(PAbortCauseType pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl) {
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
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendProviderAbort(DialogServiceProviderType pt, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, ApplicationContextName acn) {
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
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount();
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    public void onCoordRequest(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    public void onCoordResponse(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

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
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag);
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
                            this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                    message.getSls());
                        } else {
                            this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
                                    localAddress, message.getSls());
                        }
                    }
                    return;
                }

                long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
                DialogImpl di;
                if (this.stack.getPreviewMode()) {
                    PrevewDialogDataKey ky1 = new PrevewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dialogId);
                    long dId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                    PrevewDialogDataKey ky2 = new PrevewDialogDataKey(message.getIncomingOpc(),
                            (message.getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCallingPartyAddress().getSubsystemNumber(), dId);
                    di = (DialogImpl) this.getPreviewDialog(ky1, ky2, localAddress, remoteAddress, seqControl);
                } else {
                    di = this.dialogs.get(dialogId);
                }
                if (di == null) {
                    logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
                    this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls());
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
                            this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                    message.getSls());
                        } else {
                            this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
                                    localAddress, message.getSls());
                        }
                    }
                    return;
                }
                if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
                        && tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDUImpl) {
                    DialogRequestAPDUImpl dlg = (DialogRequestAPDUImpl) tcb.getDialogPortion().getDialogAPDU();
                    if (!dlg.getProtocolVersion().isSupportedVersion()) {
                        logger.error("Unsupported protocol version of  has been received when parsing TCBeginMessage");
                        this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion, tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
                                message.getSls(), dlg.getApplicationContextName());
                        return;
                    }
                }

                di = null;
                try {
                    if (this.stack.getPreviewMode()) {
                        long dId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());
                        PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingOpc(),
                                (message.getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress().getGlobalTitle().getDigits()
                                        : null), message.getCallingPartyAddress().getSubsystemNumber(), dId);
                        di = (DialogImpl) this.createPreviewDialog(ky, localAddress, remoteAddress, seqControl);
                    } else {
                        di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                    }

                } catch (TCAPException e) {
                    this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls());
                    logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                    return;
                }

                if (this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateAllRemoteEstablishedDialogsCount();
                    this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
                }
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

                dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId());
                if (this.stack.getPreviewMode()) {
                    PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dialogId);
                    di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, seqControl);
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

                dialogId = Utils.decodeTransactionId(tub.getDestinationTransactionId());
                if (this.stack.getPreviewMode()) {
                    long dId = Utils.decodeTransactionId(tub.getDestinationTransactionId());
                    PrevewDialogDataKey ky = new PrevewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dId);
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

            case TCUniMessage._TAG:
                TCUniMessage tcuni;
                try {
                    tcuni = TcapFactory.createTCUniMessage(ais);
                } catch (ParseException e) {
                    logger.error("ParseException when parsing TCUniMessage: " + e.toString(), e);
                    return;
                }

                DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
                uniDialog.processUni(tcuni, localAddress, remoteAddress);
                break;

            default:
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
        }
    }

    private void unrecognizedPackageType(SccpDataMessage message, SccpAddress localAddress, SccpAddress remoteAddress, AsnInputStream ais, int tag)
            throws ParseException {
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
                this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls());
            } else {
                this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls());
            }
        } else {
            this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, new byte[0], remoteAddress, localAddress, message.getSls());
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

    public void onPcState(int arg0, SignallingPointStatus arg1, int arg2, RemoteSccpStatus arg3) {
        // TODO Auto-generated method stub

    }

    public void onState(int arg0, int arg1, boolean arg2, int arg3) {
        // TODO Auto-generated method stub

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
            if (pdd == null) {
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
            this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey2());
        }
        pdd.stopIdleTimer();

        // TODO ??? : create Dialog and invoke "this.doRelease(di);"
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
}
