package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The Deregistration Result Status field indicates the success or the reason
 * for failure of the deregistration.
 * </p>
 * 
 * <p>
 * Its values may be:
 * <ul>
 * <li>0 Successfully Deregistered</li>
 * <li>1 Error - Unknown</li>
 * <li>2 Error - Invalid Routing Context</li>
 * <li>3 Error - Permission Denied</li>
 * <li>4 Error - Not Registered</li>
 * <li>5 Error - ASP Currently Active for Routing Context</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationStatus extends Parameter {
    public int getStatus();
}
