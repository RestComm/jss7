package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * Destination Restricted (DRST) message is optionally sent from the SGP to all
 * concerned ASPs to indicate that the SG has determined that one or more SS7
 * destinations are now restricted from the point of view of the SG, or in
 * response to a DAUD message, if appropriate. The M3UA layer at the ASP is
 * expected to send traffic to the affected destination via an alternate SG with
 * a route of equal priority, but only if such an alternate route exists and is
 * available. If the affected destination is currently considered unavailable by
 * the ASP, The MTP3-User should be informed that traffic to the affected
 * destination can be resumed. In this case, the M3UA layer should route the
 * traffic through the SG initiating the DRST message.
 * 
 * @author amit bhayani
 * 
 */
public interface DestinationRestricted extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContexts();

    public void setRoutingContexts(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCodes();

    public void setAffectedPointCodes(AffectedPointCode afpcs);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
