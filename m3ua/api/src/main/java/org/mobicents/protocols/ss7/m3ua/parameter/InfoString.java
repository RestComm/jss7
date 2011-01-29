package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The optional INFO String parameter can carry any meaningful UTF-8 [10]
 * character string along with the message. Length of the INFO String parameter
 * is from 0 to 255 octets.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface InfoString extends Parameter {

    public String getString();
}
