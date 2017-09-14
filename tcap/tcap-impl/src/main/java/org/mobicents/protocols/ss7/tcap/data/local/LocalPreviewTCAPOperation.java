package org.mobicents.protocols.ss7.tcap.data.local;

import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.data.ITCAPOperation;
import org.mobicents.protocols.ss7.tcap.data.PreviewDialogImpl;

/**
 * Created by piotr.sokolowski on 2017-06-09.
 */
public class LocalPreviewTCAPOperation implements ITCAPOperation {
    public LocalPreviewTCAPOperation(boolean sideB, LocalPreviewDialogData dialog, PreviewDialogImpl previewDialog, Invoke ci) {
        this.dialog=dialog;
        this.invoke=ci;
        this.sideB=sideB;
        this.previewDialog=previewDialog;
    }
    private final  PreviewDialogImpl previewDialog;
    private final LocalPreviewDialogData dialog;
    private final Invoke invoke;

    private OperationState state = OperationState.Idle;
    private final boolean sideB;

    public boolean isSideB() {
        return sideB;
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
        if (this.dialog == null ) {
            // bad call on server side.
            return;
        }
        OperationState old = this.state;
        this.state = state;
        if (old != state) {

            switch (state) {
                case Sent:
                    // start timer
                    break;
                case Idle:
                case Reject_W:
                    previewDialog.operationEnded(this);
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
    public void startTimer() {

    }

    @Override
    public void stopTimer() {

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
    public void handleOperationTimeout() {

    }

    @Override
    public void free() {
        dialog.freeTcapOperation(this);
    }
}
