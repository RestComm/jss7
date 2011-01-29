package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * When included, the optional Diagnostic Information can be any information
 * germane to the error condition, to assist in identification of the error
 * condition. The Diagnostic Information SHOULD contain the offending message. A
 * Diagnostic Information parameter with a zero length parameter is not
 * considered an error (this means that the Length field in the TLV will be set
 * to 4).
 * 
 * @author amit bhayani
 * 
 */
public interface DiagnosticInfo extends Parameter {
    public String getInfo();
}
