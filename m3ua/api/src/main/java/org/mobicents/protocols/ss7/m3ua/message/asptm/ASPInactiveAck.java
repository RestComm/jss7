package org.mobicents.protocols.ss7.m3ua.message.asptm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The ASP Inactive Ack message is used to acknowledge an ASP Inactive message
 * received from a remote M3UA peer.
 * 
 * @author amit bhayani
 * 
 */
public interface ASPInactiveAck extends M3UAMessage {

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);
}
