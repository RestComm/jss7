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
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ResultType;
import org.mobicents.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCUnidentifiedMessage;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.Utils;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.data.IDialog;
import org.mobicents.protocols.ss7.tcap.data.IDialogDataStorage;
import org.mobicents.protocols.ss7.tcap.data.IPreviewDialogDataStorage;
import org.mobicents.protocols.ss7.tcap.data.PreviewDialogDataKey;
import org.mobicents.protocols.ss7.tcap.data.PreviewDialogImpl;
import org.mobicents.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCAPProviderImpl implements TCAPProvider, SccpListener {

    private static final Logger logger = Logger.getLogger(TCAPProviderImpl.class); // listenres

    private transient List<TCListener> tcListeners = new CopyOnWriteArrayList<TCListener>();
    protected transient ScheduledThreadPoolExecutor _EXECUTOR;
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
/*
    private boolean checkAvailableTxId1(Long id) {
        if (!this.stack.getDialogDataStorage().containsKey(id))
            return true;
        else
            return false;
    }

    // some help methods... crude but will work for first impl.
    private Long getAvailableTxId() throws TCAPException {

        if (this.stack.getDialogDataStorage().size() >= this.stack.getMaxDialogs())
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
    */

    // get next Seq Control value available
    private synchronized int getNextSeqControl() {
        seqControl++;
        if (seqControl > 255) {
            seqControl = 0;

        }

        if (this.stack.getSlsRangeType() == SlsRangeType.Odd) {
            if (seqControl % 2 == 0)
                seqControl++;
        } else if (this.stack.getSlsRangeType() == SlsRangeType.Even) {
            if (seqControl %2 != 0)
                seqControl++;
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
        IDialog res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), null);
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
        IDialog res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), id);
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
        IDialog res = _getDialog(localAddress, remoteAddress, false, getNextSeqControl(), null);
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    private IDialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl, Long id) throws TCAPException {
        return _getDialog(localAddress, remoteAddress, true, seqControl, id);
    }

    private IDialog _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured, int seqControl, Long id)
            throws TCAPException {

        if (this.stack.getPreviewMode()) {
            throw new TCAPException("Can not create a Dialog in a PreviewMode");
        }

        if (localAddress == null) {
            throw new NullPointerException("LocalAddress must not be null");
        }

        synchronized (stack.getDialogDataStorage()) {
            IDialog di = stack.getDialogDataStorage().createDialog(localAddress, remoteAddress, id, structured, seqControl);
            if (structured && this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateMinDialogsCount(this.stack.getDialogDataStorage().getSize());
                    this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.stack.getDialogDataStorage().getSize());
            }
            return di;
        }
    }

    public int getCurrentDialogsCount() {
        return stack.getDialogDataStorage().getSize();
    }

    @Override
    public Dialog getDialog(Long localDialogId) {
        return stack.getDialogDataStorage().getDialog(localDialogId);
    }


    private void setSsnToDialog(IDialog di, int ssn) {
        if (ssn != this.ssn) {
            if (ssn <= 0 || !this.stack.isExtraSsnPresent(ssn))
                ssn = this.ssn;
        }
        di.setLocalSsn(ssn);
    }

    public void send(final byte[] data, final boolean returnMessageOnError, final SccpAddress destinationAddress, final SccpAddress originatingAddress,
                     final int seqControl, final int networkId,final int localSsn) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        final SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                localSsn, returnMessageOnError, null, null);
        msg.setNetworkId(networkId);

        //
        getDialogDataStorage().sendToSccp(sccpProvider,msg);

    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
    }

    public void deliver(IDialog dialogImpl, TCBeginIndicationImpl msg) {

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

    public void deliver(IDialog dialogImpl, TCContinueIndicationImpl tcContinueIndication) {

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

    public void deliver(IDialog dialogImpl, TCEndIndicationImpl tcEndIndication) {

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

    public void deliver(IDialog dialogImpl, TCPAbortIndicationImpl tcAbortIndication) {

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

    public void deliver(IDialog dialogImpl, TCUserAbortIndicationImpl tcAbortIndication) {

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

    public void deliver(IDialog dialogImpl, TCUniIndicationImpl tcUniIndication) {

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

    public void deliver(IDialog dialogImpl, TCNoticeIndicationImpl tcNoticeIndication) {
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

    public void release(IDialog d) {
        Long did = d.getLocalDialogId();

        if (!d.getPreviewMode()) {
            this.doRelease(d);
            synchronized (stack.getDialogDataStorage()) {
                this.stack.getDialogDataStorage().removeDialog(d);
                if (this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateMinDialogsCount(this.stack.getDialogDataStorage().getSize());
                    this.stack.getCounterProviderImpl().updateMaxDialogsCount(this.stack.getDialogDataStorage().getSize());
                }
            }
        }
    }

    private void doRelease(IDialog d) {

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
    public void timeout(IDialog d) {

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

    public void operationTimedOut(Invoke tcInvokeRequestImpl) {
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
        this._EXECUTOR = new ScheduledThreadPoolExecutor(10);
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
    }

    void stop() {
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

        this.stack.getDialogDataStorage().clear();
        this.stack.getPreviewDialogDataStorage().clear();
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
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount();
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId,localAddress.getSubsystemNumber());
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
                this.stack.getCounterProviderImpl().updateTcPAbortSentCount();
            }
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId,localAddress.getSubsystemNumber());
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
                            this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                    message.getSls(), message.getNetworkId());
                        } else {
                            this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
                                    localAddress, message.getSls(), message.getNetworkId());
                        }
                    }
                    return;
                }

                long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId());
               IDialog di;
                if (this.stack.getPreviewMode()) {
                    PreviewDialogDataKey ky2 = new PreviewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dialogId);
                    long dId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId());
                    PreviewDialogDataKey ky1 = new PreviewDialogDataKey(message.getIncomingOpc(),
                            (message.getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCallingPartyAddress().getSubsystemNumber(), dId);
                    di = (IDialog) getPreviewDialogDataStorage().getPreviewDialog(ky1, ky2, localAddress, remoteAddress, seqControl);
                    setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                } else {
                    di = this.stack.getDialogDataStorage().getDialog(dialogId);
                }
                if (di == null) {
                    logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
                    this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                            message.getNetworkId());
                } else {
                    try {
                        di.getDialogLock().lock();
                        this.getDialogDataStorage().beginTransaction();
                        di.processContinue(tcm, localAddress, remoteAddress);
                    } finally {
                        try {
                            this.getDialogDataStorage().commitTransaction();
                            di.getDialogLock().unlock();
                        } catch (Exception e) {
                            logger.error("Failed to commit transaction",e);
                        }
                    }

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
                                    message.getSls(), message.getNetworkId());
                        } else {
                            this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, tcUnidentified.getOriginatingTransactionId(), remoteAddress,
                                    localAddress, message.getSls(), message.getNetworkId());
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
                                message.getSls(), dlg.getApplicationContextName(), message.getNetworkId());
                        return;
                    }
                }

                di = null;
                try {
                    if (this.stack.getPreviewMode()) {
                        long dId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId());
                        PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingOpc(),
                                (message.getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress().getGlobalTitle().getDigits()
                                        : null), message.getCallingPartyAddress().getSubsystemNumber(), dId);
                        PreviewDialogImpl pdi=getPreviewDialogDataStorage().createPreviewDialog(ky, localAddress, remoteAddress, seqControl);
                        di=pdi;
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = (IDialog) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    }
                } catch (TCAPException e) {
                    this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
                            message.getSls(), message.getNetworkId());
                    logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                    return;
                }

                if (this.stack.getStatisticsEnabled()) {
                    this.stack.getCounterProviderImpl().updateAllRemoteEstablishedDialogsCount();
                    this.stack.getCounterProviderImpl().updateAllEstablishedDialogsCount();
                }
                di.setNetworkId(message.getNetworkId());
                try {
                    di.getDialogLock().lock();
                    this.getDialogDataStorage().beginTransaction();
                    di.processBegin(tcb, localAddress, remoteAddress);
                } finally {
                    try {
                        this.getDialogDataStorage().commitTransaction();
                        di.getDialogLock().unlock();
                    } catch (Exception e) {
                        logger.error("Failed to commit transaction",e);
                    }
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
                    PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dialogId);
                    di = (IDialog) this.getPreviewDialog(null, ky, localAddress, remoteAddress, seqControl);
                    setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                } else {
                    di = this.stack.getDialogDataStorage().getDialog(dialogId);
                }
                if (di == null) {
                    logger.warn("TC-END: No dialog/transaction for id: " + dialogId);
                } else {
                    try {
                        di.getDialogLock().lock();
                        this.getDialogDataStorage().beginTransaction();
                        di.processEnd(teb, localAddress, remoteAddress);
                    } finally {
                        try {
                            this.getDialogDataStorage().commitTransaction();
                            di.getDialogLock().unlock();
                        } catch (Exception e) {
                            logger.error("Failed to commit transaction",e);
                        }
                    }

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
                    PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(),
                            (message.getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress().getGlobalTitle().getDigits() : null),
                            message.getCalledPartyAddress().getSubsystemNumber(), dId);
                    di = (IDialog) this.getPreviewDialog(ky, null, localAddress, remoteAddress, seqControl);
                    setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                } else {
                    di = this.stack.getDialogDataStorage().getDialog(dialogId);
                }
                if (di == null) {
                    logger.warn("TC-ABORT: No dialog/transaction for id: " + dialogId);
                } else {
                    try {
                        di.getDialogLock().lock();
                        this.getDialogDataStorage().beginTransaction();
                        di.processAbort(tub, localAddress, remoteAddress);
                    } finally {
                        try {
                            this.getDialogDataStorage().commitTransaction();
                            di.getDialogLock().unlock();
                        } catch (Exception e) {
                            logger.error("Failed to commit transaction",e);
                        }
                    }

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

                IDialog uniDialog = (IDialog) this.getNewUnstructuredDialog(localAddress, remoteAddress);
                setSsnToDialog(uniDialog, message.getCalledPartyAddress().getSubsystemNumber());
                try {
                    uniDialog.getDialogLock().lock();
                    this.getDialogDataStorage().beginTransaction();
                    uniDialog.processUni(tcuni, localAddress, remoteAddress);
                } finally {
                    try {
                        this.getDialogDataStorage().commitTransaction();
                        uniDialog.getDialogLock().unlock();
                    } catch (Exception e) {
                        logger.error("Failed to commit transaction",e);
                    }
                }
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

       IDialog dialog = null;

        try {
            byte[] data = msg.getData();
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
            tcUnidentified.decode(ais);

            if (tcUnidentified.getOriginatingTransactionId() != null) {
                long otid = Utils.decodeTransactionId(tcUnidentified.getOriginatingTransactionId());
                dialog = this.stack.getDialogDataStorage().getDialog(otid);
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
                dialog.getDialogLock().lock();
                this.getDialogDataStorage().beginTransaction();
                this.deliver(dialog, ind);
                if (dialog.getState() != TRPseudoState.Active) {
                    dialog.release();
                }
            } catch(Exception e) {
                logger.error("Problems with notice delivery",e);
            } finally {
                try {
                    this.getDialogDataStorage().commitTransaction();
                    dialog.getDialogLock().unlock();
                } catch (Exception e) {
                    logger.error("Failed to commit transaction",e);
                }
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

    protected IDialog getPreviewDialog(PreviewDialogDataKey ky1, PreviewDialogDataKey ky2, SccpAddress localAddress,
                                      SccpAddress remoteAddress, int seqControl) {
        IDialog dialog=getPreviewDialogDataStorage().getPreviewDialog(ky1,ky2,localAddress,remoteAddress,seqControl);
        dialog.restartIdleTimer();
        return dialog;
    }

    public void removePreviewDialog(IDialog di) {
        getPreviewDialogDataStorage().removeDialog(di);
        this.doRelease(di);
    }

    public IDialogDataStorage getDialogDataStorage() {
        return stack.getDialogDataStorage();
    }


    public IPreviewDialogDataStorage getPreviewDialogDataStorage() {
        return stack.getPreviewDialogDataStorage();
    }

    public ScheduledThreadPoolExecutor getLocalExecutor() {
        return _EXECUTOR;
    }
}
