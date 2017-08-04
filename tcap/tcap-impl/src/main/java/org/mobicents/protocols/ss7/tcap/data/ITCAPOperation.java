package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 * Created by piotr.sokolowski on 2017-06-08.
 */
public interface ITCAPOperation {
    OperationState getState();
    void setState(OperationState state);
    void onReturnResultLast();
    void onError();
    void onReject();
    void startTimer();
    void stopTimer();
    boolean isErrorReported();
    boolean isSuccessReported();
    void setInvoke(Invoke i);
    Invoke getInvoke();
    Long getInvokeId();
    void handleOperationTimeout();
    void free();
}
