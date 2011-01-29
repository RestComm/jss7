package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The mandatory Local-RK-Identifier field is used to uniquely identify the
 * registration request. The Identifier value is assigned by the ASP and used to
 * correlate the response in an REG RSP message with the original registration
 * request. The Identifier value must remain unique until the REG RSP message is
 * received.
 * 
 * @author amit bhayani
 * 
 */
public interface LocalRKIdentifier extends Parameter {

    public long getId();
}
