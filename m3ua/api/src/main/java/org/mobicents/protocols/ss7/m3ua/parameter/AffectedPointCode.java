package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The affected point code parameter identifies which point codes are affected,
 * depending on the message type
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface AffectedPointCode extends Parameter {

    /**
     * <p>
     * To make it easier to identify multiple point codes, ranges can be used as
     * well. The mask field is used to identify ranges within the point code.
     * For example, if the mask contains a value of 2, this would indicate that
     * the last two digits of the point code are a “wild card.”
     * </p>
     * 
     * @return returns the mask
     */
    public short[] getMasks();

    /**
     * returns the affected point code.
     * 
     * @return
     */
    public int[] getPointCodes();

}
