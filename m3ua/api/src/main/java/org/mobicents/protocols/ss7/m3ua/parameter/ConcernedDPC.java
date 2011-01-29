package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The optional Concerned Destination parameter is only used if the SCON message
 * is sent from an ASP to the SGP. It contains the point code of the originator
 * of the message that triggered the SCON message. Any resulting Transfer
 * Controlled (TFC) message from the SG is sent to the Concerned Point Code
 * using the single Affected DPC contained in the SCON message to populate the
 * (affected) Destination field of the TFC message
 * 
 * @author abhayani
 * 
 */
public interface ConcernedDPC extends Parameter {

    public int getPointCode();

}
