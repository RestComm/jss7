package org.mobicents.protocols.ss7.m3ua.message.mgmt;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The Error message is used to notify a peer of an error event associated with
 * an incoming message. For example, the message type might be unexpected given
 * the current state, or a parameter value might be invalid. Error messages MUST
 * NOT be generated in response to other Error messages.
 * 
 * @author amit bhayani
 * 
 */
public interface Error extends M3UAMessage {

    public ErrorCode getErrorCode();

    public void setErrorCode(ErrorCode code);

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance netApp);

    public AffectedPointCode getAffectedPointCode();

    public void setAffectedPointCode(AffectedPointCode affPc);

    public DiagnosticInfo getDiagnosticInfo();

    public void setDiagnosticInfo(DiagnosticInfo diagInfo);

}
