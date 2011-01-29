package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;

/**
 * The Deregistration Response (DEREG RSP) message is used as a response to the
 * DEREG REQ message from a remote M3UA peer.
 * 
 * TODO : The message only supports single DeregistrationResult. 
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationResponse extends M3UAMessage {

    public DeregistrationResult getDeregistrationResult();

    public void setDeregistrationResult(DeregistrationResult result);

}
