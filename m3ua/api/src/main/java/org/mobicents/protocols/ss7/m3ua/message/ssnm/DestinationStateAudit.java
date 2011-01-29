package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * Destination State Audit (DAUD) MAY be sent from the ASP to the SGP to audit
 * the availability/congestion state of SS7 routes from the SG to one or more
 * affected destinations.
 * 
 * @author amit bhayani
 * 
 */
public interface DestinationStateAudit extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContexts();

    public void setRoutingContexts(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCodes();

    public void setAffectedPointCodes(AffectedPointCode afpcs);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
