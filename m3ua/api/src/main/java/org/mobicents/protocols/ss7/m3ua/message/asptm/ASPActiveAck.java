package org.mobicents.protocols.ss7.m3ua.message.asptm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * The ASP Active Ack message is used to acknowledge an ASP Active message
 * received from a remote M3UA peer.
 * 
 * @author amit bhayani
 * 
 */
public interface ASPActiveAck extends M3UAMessage {

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
