package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The status parameter is used in the notify message to provide the status of
 * the specified entity. There are two fields in this parameter, the status type
 * and the status information field. The status information field provides the
 * current state of the entity identified in the routing context of the notify
 * message.
 * 
 * @author amit bhayani
 * 
 */
public interface Status extends Parameter {
    public int getType();

    public int getInfo();
}
