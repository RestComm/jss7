package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;

/**
 * The Registration Response (REG RSP) message is used as a response to the REG
 * REQ message from a remote M3UA peer. It contains indications of
 * success/failure for registration requests and returns a unique Routing
 * Context value for successful registration requests, to be used in subsequent
 * M3UA Traffic Management protocol.
 * 
 * @author amit bhayani
 * 
 */
public interface RegistrationResponse extends M3UAMessage {
    public RegistrationResult getRegistrationResult();

    public void setRegistrationResult(RegistrationResult rslt);
}
