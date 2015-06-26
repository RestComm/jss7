package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * Listener interface to allow library users to implement their own statistics counters.
 * @author Miklos Pocsaji
 *
 */
public interface TCAPCounterProviderImplListener {

    void updateTcUniReceivedCount(Dialog dialog);

    void updateTcUniSentCount(Dialog dialog);

    void updateTcBeginReceivedCount(Dialog dialog);

    void updateTcBeginSentCount(Dialog dialog);

    void updateTcContinueReceivedCount(Dialog dialog);

    void updateTcContinueSentCount(Dialog dialog);

    void updateTcEndReceivedCount(Dialog dialog);

    void updateTcEndSentCount(Dialog dialog);

    void updateTcPAbortReceivedCount(Dialog dialog, PAbortCauseType cause);

    void updateTcPAbortSentCount(byte[] originatingTransactionId, PAbortCauseType cause);

    void updateTcUserAbortReceivedCount(Dialog dialog);

    void updateTcUserAbortSentCount(Dialog dialog);

    void updateInvokeReceivedCount(Dialog dialog, Invoke invoke);

    void updateInvokeSentCount(Dialog dialog, Invoke invoke);

    void updateReturnResultReceivedCount(Dialog dialog);

    void updateReturnResultSentCount(Dialog dialog);

    void updateReturnResultLastReceivedCount(Dialog dialog);

    void updateReturnResultLastSentCount(Dialog dialog);

    void updateReturnErrorReceivedCount(Dialog dialog);

    void updateReturnErrorSentCount(Dialog dialog);

    void updateRejectReceivedCount(Dialog dialog);

    void updateRejectSentCount(Dialog dialog);

    void updateDialogTimeoutCount(Dialog dialog);

    void updateDialogReleaseCount(Dialog dialog);
}
