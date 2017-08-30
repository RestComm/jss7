package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public class LocalTCAPOperation implements Serializable, ITCAPOperation {

    private transient LocalDialogData data;
    private final Invoke invoke;

    private OperationState state = OperationState.Idle;
    private Object timerHandle;

    LocalTCAPOperation(LocalDialogData data, Invoke invoke) {
        this.data=data;
        this.invoke=invoke;
    }

    /**
     * @return the state
     */
    @Override
    public OperationState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    @Override
    public void setState(OperationState state) {
        if (this.data == null || this.data.getDialog()==null) {
            // bad call on server side.
            return;
        }
        OperationState old = this.state;
        this.state = state;
        if (old != state) {

            switch (state) {
                case Sent:
                    // start timer
                    this.startTimer();
                    break;
                case Idle:
                case Reject_W:
                    this.stopTimer();
                    data.getDialog().operationEnded(this);
            }
            if (state == OperationState.Sent) {

            } else if (state == OperationState.Idle || state == OperationState.Reject_W) {

            }

        }
    }

    @Override
    public void onReturnResultLast() {
        this.setState(OperationState.Idle);

    }

    @Override
    public void onError() {
        this.setState(OperationState.Idle);

    }

    @Override
    public void onReject() {
        this.setState(OperationState.Idle);
    }

    @Override
    public synchronized void startTimer() {
        if (this.data==null || this.data.getDialog() == null || this.data.getDialog().getPreviewMode())
            return;

        this.stopTimer();
        if (this.invoke.getTimeout() > 0)
            this.timerHandle = this.data.getDialog().getProvider().getTimerFacility().schedule(new OperationTimerTask(this), invoke.getTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void stopTimer() {
        if (this.timerHandle != null) {
            this.data.getDialog().getProvider().getTimerFacility().cancel(this.timerHandle);
            timerHandle=null;
        }
    }

    @Override
    public boolean isErrorReported() {
        if (this.invoke.getInvokeClass() == InvokeClass.Class1 || this.invoke.getInvokeClass() == InvokeClass.Class2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isSuccessReported() {
        if (this.invoke.getInvokeClass() == InvokeClass.Class1 || this.invoke.getInvokeClass() == InvokeClass.Class3) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Invoke getInvoke() {
        return invoke;
    }

    @Override
    public Long getInvokeId() {
        return invoke.getInvokeId();
    }

    @Override
    public void free() {
        data.freeTcapOperation(this);

    }

    private static class OperationTimerTask implements ITimerTask {
        final Long dialogId;
        final Long invokeId;

        OperationTimerTask(LocalTCAPOperation tcapOperation) {
            dialogId=tcapOperation.data.getDialog().getLocalDialogId();
            invokeId=tcapOperation.getInvokeId();
        }

        @Override
        public String getId() {
            return "Dialog/OperationTimer/"+dialogId+"/"+invokeId;
        }

        @Override
        public void handleTimeEvent(TCAPProviderImpl tpi) {
            IDialog dlg=tpi.getDialogDataStorage().getDialog(dialogId);
            dlg.getDialogLock().lock();
            try {
                dlg.handleOperationTimeout(invokeId);
            } finally {
                dlg.getDialogLock().unlock();
            }
        }
    }

    public void handleOperationTimeout() {
        try {
            data.getDialog().getDialogLock().lock();
            Invoke ii=getInvoke();
            // op failed, we must delete it from dialog and notify!
            timerHandle = null;
            setState(OperationState.Idle);
            // TC-L-CANCEL
            data.getDialog().operationTimedOut(this,ii);
        } finally {
            data.getDialog().getDialogLock().unlock();
        }
    }
}
