package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * Service Indicator contains one or more Service Indicators from the values
 * described in the MTP3-User Identity field of the DUPU message. The absence of
 * the SI parameter in the Routing Key indicates the use of any SI value,
 * excluding of course MTP management. Where an SI parameter does not contain a
 * multiple of four SIs, the parameter is padded out to 32-byte alignment.
 * 
 * @author amit bhayani
 * 
 */
public interface ServiceIndicators extends Parameter {

    public short[] getIndicators();

}
