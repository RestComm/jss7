package org.mobicents.protocols.ss7.m3ua.parameter;


/**
 * The Correlation Id parameter uniquely identifies the MSU carried in the
 * Protocol Data within an AS. This Correlation Id parameter is assigned by the
 * sending M3UA.
 * 
 * @author amit bhayani
 * 
 */
public interface CorrelationId extends Parameter {
    public long getCorrelationId();
}
