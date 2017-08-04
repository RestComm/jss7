package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IDialog extends Dialog {
    void processContinue(TCContinueMessage tcm, SccpAddress localAddress, SccpAddress address);
    void processBegin(TCBeginMessage tcb, SccpAddress localAddress, SccpAddress address);

    void handleIdleTimeout();
    void restartIdleTimer();

    void processEnd(TCEndMessage teb, SccpAddress localAddress, SccpAddress address);

    void processAbort(TCAbortMessage tub, SccpAddress localAddress, SccpAddress address);

    void processUni(TCUniMessage tcuni, SccpAddress localAddress, SccpAddress address);

    void operationEnded(ITCAPOperation operation);
    void operationTimedOut(ITCAPOperation invoke,Invoke i);
    void handleOperationTimeout(Long invokeId);

    TCAPProviderImpl getProvider();

    void setLocalSsn(int ssn);
}
