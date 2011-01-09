package org.mobicents.protocols.ss7.m3ua.message;

import org.mobicents.protocols.ss7.m3ua.message.parm.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.message.parm.RoutingContext;

public interface DestinationUnavailable extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext p);

}
