package org.mobicents.protocols.ss7.m3ua.message.aspsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;

/**
 * The BEAT message is optionally used to ensure that the M3UA peers are still
 * available to each other. It is recommended for use when the M3UA runs over a
 * transport layer other than the SCTP, which has its own heartbeat.
 * 
 * @author amit bhayani
 * 
 */
public interface Heartbeat extends M3UAMessage {
    public HeartbeatData getHeartbeatData();

    public void setHeartbeatData(HeartbeatData hrBtData);
}
