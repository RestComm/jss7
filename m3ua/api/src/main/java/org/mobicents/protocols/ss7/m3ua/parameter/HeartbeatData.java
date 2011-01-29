package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Heartbeat Data parameter contents are defined by the sending node. The
 * Heartbeat Data could include, for example, a Heartbeat Sequence Number and/or
 * Timestamp. The receiver of a BEAT message does not process this field, as it
 * is only of significance to the sender. The receiver MUST respond with a BEAT
 * Ack message.
 * 
 * @author amit bhayani
 * 
 */
public interface HeartbeatData extends Parameter {
    public String getData();

}
