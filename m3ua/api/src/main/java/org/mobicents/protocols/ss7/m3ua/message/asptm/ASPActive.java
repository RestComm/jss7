package org.mobicents.protocols.ss7.m3ua.message.asptm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * The ASP Active message is sent by an ASP to indicate to a remote M3UA peer
 * that it is ready to process signalling traffic for a particular Application
 * Server. The ASP Active message affects only the ASP state for the Routing
 * Keys identified by the Routing Contexts, if present.
 * 
 * @author amit bhayani
 * 
 */
public interface ASPActive extends M3UAMessage {

    /**
     * The Traffic Mode Type parameter identifies the traffic mode of operation
     * of the ASP within an AS. Optional
     * 
     * @return
     */
    public TrafficModeType getTrafficModeType();

    public void setTrafficModeType(TrafficModeType mode);

    /**
     * The optional Routing Context parameter contains (a list of) integers
     * indexing the Application Server traffic that the sending ASP is
     * configured/registered to receive.
     * 
     * @return
     */
    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
