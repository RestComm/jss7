package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 * Destination User Part Unavailable (DUPU) message is used by an SGP to inform
 * concerned ASPs that a remote peer MTP3-User Part (e.g., ISUP or SCCP) at an
 * SS7 node is unavailable.
 * 
 * @author amit bhayani
 * 
 */
public interface DestinationUPUnavailable extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCode();

    public void setAffectedPointCode(AffectedPointCode afpc);

    public UserCause getUserCause();

    public void setUserCause(UserCause usrCau);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}