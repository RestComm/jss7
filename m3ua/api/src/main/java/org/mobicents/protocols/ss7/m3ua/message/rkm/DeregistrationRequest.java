package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The DEREG REQ message is sent by an ASP to indicate to a remote M3UA peer
 * that it wishes to deregister a given Routing Key. Typically, an ASP would
 * send this message to an SGP and expects to receive a DEREG RSP message in
 * return with the associated Routing Context value.
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationRequest extends M3UAMessage {

    /**
     * The Routing Context parameter contains (a list of) integers indexing the
     * Application Server traffic that the sending ASP is currently registered
     * to receive from the SGP but now wishes to deregister.
     * 
     * @return
     */
    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);
}
