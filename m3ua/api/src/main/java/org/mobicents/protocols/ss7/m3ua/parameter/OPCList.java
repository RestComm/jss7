package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Originating Point Code List parameter contains one or more SS7 OPC
 * entries, and its format is the same as for the Destination Point Code
 * parameter. The absence of the OPC List parameter in the Routing Key indicates
 * the use of any OPC value.
 * 
 * @author amit bhayani
 * 
 */
public interface OPCList extends Parameter {
    
    public short[] getMasks();
    
    public int[] getPointCodes();

}
