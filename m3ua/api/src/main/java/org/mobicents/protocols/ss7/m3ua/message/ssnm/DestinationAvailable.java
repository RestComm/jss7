package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * <p>
 * Destination Available (DAVA) message is sent from an SGP to all concerned
 * ASPs to indicate that the SG has determined that one or more SS7 destinations
 * are now reachable (and not restricted), or in response to a DAUD message, if
 * appropriate.
 * </p>
 * <p>
 * look at section 3.4.2 in RFC 4666
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface DestinationAvailable extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContexts();

    public void setRoutingContexts(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCodes();

    public void setAffectedPointCodes(AffectedPointCode afpcs);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
