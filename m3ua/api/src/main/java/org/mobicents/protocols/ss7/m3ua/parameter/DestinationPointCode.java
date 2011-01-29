package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Destination Point Code parameter identifies the Destination Point Code of
 * incoming SS7 traffic for which the ASP is registering. For an alias point
 * code configuration, the DPC parameter would be repeated for each point code.
 * The format is the same as described for the Affected Destination parameter in
 * the DUNA message
 * 
 */
public interface DestinationPointCode extends Parameter {

    public int getPointCode();
    public short getMask();

}
