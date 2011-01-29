package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * <p>
 * The Signalling Congestion SCON message can be sent from an SGP to all
 * concerned ASPs to indicate that an SG has determined that there is congestion
 * in the SS7 network to one or more destinations, or to an ASP in response to a
 * DATA or DAUD message, as appropriate. For some MTP protocol variants (e.g.,
 * ANSI MTP) the SCON message may be sent when the SS7 congestion level changes.
 * The SCON message MAY also be sent from the M3UA layer of an ASP to an M3UA
 * peer, indicating that the congestion level of the M3UA layer or the ASP has
 * changed.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface SignallingCongestion extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContexts();

    public void setRoutingContexts(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCodes();

    public void setAffectedPointCodes(AffectedPointCode afpcs);

    public ConcernedDPC getConcernedDPC();

    public void setConcernedDPC(ConcernedDPC dpc);

    public CongestedIndication getCongestedIndication();

    public void setCongestedIndication(CongestedIndication congInd);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
