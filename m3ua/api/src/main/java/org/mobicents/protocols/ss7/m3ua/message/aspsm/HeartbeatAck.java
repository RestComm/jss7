package org.mobicents.protocols.ss7.m3ua.message.aspsm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;

/**
 * The BEAT Ack message is sent in response to a received BEAT message. It
 * includes all the parameters of the received BEAT message, without any change.
 * 
 * @author amit bhayani
 * 
 */
public interface HeartbeatAck extends M3UAMessage {
    public HeartbeatData getHeartbeatData();

    public void setHeartbeatData(HeartbeatData hrBtData);
}
