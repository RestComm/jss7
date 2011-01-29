package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The optional ASP Identifier parameter contains a unique value that is locally
 * significant among the ASPs that support an AS. The SGP should save the ASP
 * Identifier to be used, if necessary, with the Notify message
 * 
 * @author amit bhayani
 * @author vutamhoan
 */
public interface ASPIdentifier extends Parameter {

    public long getAspId();

}
