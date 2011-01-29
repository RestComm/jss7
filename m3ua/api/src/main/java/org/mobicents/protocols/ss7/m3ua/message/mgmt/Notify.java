package org.mobicents.protocols.ss7.m3ua.message.mgmt;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 * The Notify message used to provide an autonomous indication of M3UA events to
 * an M3UA peer.
 * 
 * @author amit bhayani
 * 
 */
public interface Notify extends M3UAMessage {
    public Status getStatus();

    public void setStatus(Status status);

    public ASPIdentifier getASPIdentifier();

    public void setASPIdentifier(ASPIdentifier id);

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);
}
