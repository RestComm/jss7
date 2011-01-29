package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Deregistration Result parameter contains the deregistration status for a
 * single Routing Context in a DEREG REQ message. The number of results in a
 * single DEREG RSP message MAY be anywhere from one to the total number of
 * number of Routing Context values found in the corresponding DEREG REQ
 * message.
 * 
 * Where multiple DEREG RSP messages are used in reply to DEREG REQ message, a
 * specific result SHOULD be in only one DEREG RSP message.
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationResult extends Parameter {
    public RoutingContext getRoutingContext();

    public DeregistrationStatus getDeregistrationStatus();
}
