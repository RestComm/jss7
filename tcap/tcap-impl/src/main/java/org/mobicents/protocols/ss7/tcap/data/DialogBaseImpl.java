package org.mobicents.protocols.ss7.tcap.data;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogUniAPDU;
import org.mobicents.protocols.ss7.tcap.asn.ErrorCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.OperationCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ProblemImpl;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.Utils;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public abstract class DialogBaseImpl implements IDialog {
    private static final Logger logger = Logger.getLogger(DialogImpl.class);
    abstract IDialogDataBase getBaseData();

    abstract int getLocalSsn();

    public final TCAPProviderImpl provider;

    DialogBaseImpl(TCAPProviderImpl  provider) {
        this.provider=provider;
    }

/*
 * (non-Javadoc)
 *
 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getDialogId()
 */
    public Long getLocalDialogId() {
        IDialogDataBase data=getBaseData();
        return data.getLocalTransactionIdObject();
    }

    /**
     *
     */
    public Long getRemoteDialogId() {
        IDialogDataBase data=getBaseData();
        if (data.getRemoteTransactionId() != null && data.getRemoteTransactionIdObject() == null) {
            data.setRemoteTransactionIdObject(Utils.decodeTransactionId(data.getRemoteTransactionId()));
        }

        return data.getRemoteTransactionIdObject();
    }

    @Override
    public int getNetworkId() {
        IDialogDataBase data=getBaseData();
        return data.getNetworkId();
    }

    @Override
    public void setNetworkId(int networkId) {
        IDialogDataBase data=getBaseData();
        data.setNetworkId(networkId);
    }

    /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getRemoteAddress()
         */
    public SccpAddress getRemoteAddress() {
        IDialogDataBase data=getBaseData();

        return data.getRemoteAddress();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getLocalAddress()
     */
    public SccpAddress getLocalAddress() {
        IDialogDataBase data=getBaseData();

        return data.getLocalAddress();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#isEstabilished()
     */
    public boolean isEstabilished() {
        IDialogDataBase data=getBaseData();
        return data.getState() == TRPseudoState.Active;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#isStructured()
     */
    public boolean isStructured() {
        IDialogDataBase data=getBaseData();

        return data.isStructured();
    }


    @Override
    public ReentrantLock getDialogLock() {

        IDialogDataBase data=getBaseData();
        return data.getDialogLock();
    }

    /**
     * @return the acn
     */
    public ApplicationContextName getApplicationContextName() {
        IDialogDataBase data=getBaseData();
        return data.getLastACN();
    }

    /**
     * @return the ui
     */
    public UserInformation getUserInformation() {
        IDialogDataBase data=getBaseData();
        return data.getLastUI();
    }

    /**
     * Adding the new incoming invokeId into incomingInvokeList list
     *
     * @param invokeId
     * @return false: failure - this invokeId already present in the list
     */
    private boolean addIncomingInvokeId(Long invokeId) {
        IDialogDataBase data=getBaseData();
        return data.addIncomingInvoke(invokeId);
    }

    private void removeIncomingInvokeId(Long invokeId){
        IDialogDataBase data=getBaseData();
        data.removeIncomingInvoke(invokeId);
    }

    /**
     * @param remoteTransactionId the remoteTransactionId to set
     */
    public void setRemoteTransactionId(byte[] remoteTransactionId) {
        IDialogDataBase data=getBaseData();

        data.setRemoteTransactionId(remoteTransactionId);
    }

    /**
     * @param localAddress the localAddress to set
     */
    public void setLocalAddress(SccpAddress localAddress) {

        IDialogDataBase data=getBaseData();
        data.setLocalAddress(localAddress);
    }

    /**
     * @param remoteAddress the remoteAddress to set
     */
    public void setRemoteAddress(SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        data.setRemoteAddress(remoteAddress);
    }

    public void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        try {
            data.getDialogLock().lock();

            try {
                data.setRemoteAddress(remoteAddress);
                data.setLocalAddress(localAddress);

                // no dialog portion!
                // convert to indications
                TCUniIndicationImpl tcUniIndication = (TCUniIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
                        .createUniIndication(this);

                tcUniIndication.setDestinationAddress(localAddress);
                tcUniIndication.setOriginatingAddress(remoteAddress);
                // now comps
                Component[] comps = msg.getComponent();
                tcUniIndication.setComponents(comps);

                processRcvdComp(comps);

                if (msg.getDialogPortion() != null) {
                    // it should be dialog req?
                    DialogPortion dp = msg.getDialogPortion();
                    DialogUniAPDU apdu = (DialogUniAPDU) dp.getDialogAPDU();
                    data.setLastACN(apdu.getApplicationContextName());
                    data.setLastUI(apdu.getUserInformation());
                    tcUniIndication.setApplicationContextName(data.getLastACN());
                    tcUniIndication.setUserInformation(data.getLastUI());
                }

                // lets deliver to provider, this MUST not throw anything
                this.provider.deliver(this, tcUniIndication);

            } finally {
                this.release();
            }
        } finally {
            data.getDialogLock().unlock();
        }
    }


    public void processRcvdComp(Component[] comps) {
        if (provider.getStack().getStatisticsEnabled()) {
            for (Component comp : comps) {
                switch (comp.getType()) {
                    case Invoke:
                        provider.getStack().getCounterProviderImpl().updateInvokeReceivedCount();

                        Invoke inv = (Invoke) comp;
                        OperationCodeImpl oc = (OperationCodeImpl) inv.getOperationCode();
                        if (oc != null) {
                            this.provider.getStack().getCounterProviderImpl().updateIncomingInvokesPerOperationCode(oc.getStringValue());
                        }
                        break;
                    case ReturnResult:
                        this.provider.getStack().getCounterProviderImpl().updateReturnResultReceivedCount();
                        break;
                    case ReturnResultLast:
                        this.provider.getStack().getCounterProviderImpl().updateReturnResultLastReceivedCount();
                        break;
                    case ReturnError:
                        this.provider.getStack().getCounterProviderImpl().updateReturnErrorReceivedCount();

                        ReturnError re = (ReturnError) comp;
                        ErrorCodeImpl ec = (ErrorCodeImpl) re.getErrorCode();
                        if (ec != null) {
                            this.provider.getStack().getCounterProviderImpl().updateIncomingErrorsPerErrorCode(ec.getStringValue());
                        }
                        break;
                    case Reject:
                        Reject rej = (Reject) comp;
                        if (!rej.isLocalOriginated()) {
                            this.provider.getStack().getCounterProviderImpl().updateRejectReceivedCount();

                            ProblemImpl prob = (ProblemImpl) rej.getProblem();
                            if (prob != null) {
                                this.provider.getStack().getCounterProviderImpl().updateIncomingRejectPerProblem(prob.getStringValue());
                            }
                        }
                        break;
                }
            }
        }
    }

    public Long getRelayedLocalDialogId() {
        return getBaseData().getLocalRelayedTransactionIdObject();
    }

    public void setRelayedLocalDialogId(Long relayedDialogId) {
        getBaseData().setLocalRelayedTransactionIdObject( relayedDialogId );
    }

    public abstract void sendAbnormalDialog();

    protected abstract Component[] processOperationsState(Component[] component);

    public abstract void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress);
    public abstract void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress);



    public abstract void startIdleTimer();
    public void handleIdleTimeout() {
        IDialogDataBase data=getBaseData();
        getDialogLock().lock();
        try {
            data.setIdleTimerHandle(null);
            data.setIdleTimerActionTaken(false);
            data.setIdleTimerInvoked(true);
            provider.timeout(this);
            // send abort
            if (data.isIdleTimerActionTaken()) {
                startIdleTimer();
            } else {
                release();
            }

        } finally {
            data.setIdleTimerInvoked(false);
            getDialogLock().unlock();
        }
    }

    public void stopIdleTimer() {
        IDialogDataBase data=getBaseData();
        if(logger.isTraceEnabled()) {
            logger.trace("Stopping Idle Timer :"+data);
        }
        if (!data.isStructured())
            return;

        try {
            data.getDialogLock().lock();
            if (data.getIdleTimerHandle() != null) {
                provider.getTimerFacility().cancel(data.getIdleTimerHandle());
                data.setIdleTimerHandle(null);
            }
        } finally {
            data.getDialogLock().unlock();
        }
    }

    public void restartIdleTimer() {
        stopIdleTimer();
        startIdleTimer();
    }

    public TRPseudoState getState() {
        return getBaseData().getState();
    }
    public Object getUserObject() {
        return getBaseData().getUserObject();
    }
    public void setUserObject(Object userObject) {
        getBaseData().setUserObject(userObject);
    }

}
