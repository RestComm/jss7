package org.mobicents.protocols.ss7.sccp;

import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.IOException;

public interface SccpConnection {
    int getSls();
    int getLocalSsn();

    // source local reference
    LocalReference getLocalReference();

    // destination local reference
    LocalReference getRemoteReference();

    // not available after disconnect
    boolean isAvailable();


    void send(byte[] data) throws Exception;

    SccpConnectionState getState();

    // for protocol class 3 only
    Credit getCredit();

    void establish(SccpConnCrMessage message) throws IOException;

    void reset(ResetCause reason) throws Exception;

    void disconnect(RefusalCause reason, byte[] data) throws Exception;
    void disconnect(ReleaseCause reason, byte[] data) throws Exception;

    void confirm(SccpAddress respondingAddress) throws Exception;

    SccpListener getListener();
}
