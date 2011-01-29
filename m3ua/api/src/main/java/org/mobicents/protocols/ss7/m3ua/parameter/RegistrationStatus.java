package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * This is used to indicate whether or not a registration was successful.
 * </p>
 * <p>
 * <ul>
 * <li>0 Successfully Registered</li>
 * <li>1 Error - Unknown</li>
 * <li>2 Error - Invalid DPC</li>
 * <li>3 Error - Invalid Network Appearance</li>
 * <li>4 Error - Invalid Routing Key</li>
 * <li>5 Error - Permission Denied</li>
 * <li>6 Error - Cannot Support Unique Routing</li>
 * <li>7 Error - Routing Key not Currently Provisioned</li>
 * <li>8 Error - Insufficient Resources</li>
 * <li>9 Error - Unsupported RK parameter Field</li>
 * <li>10 Error - Unsupported/Invalid Traffic Handling Mode</li>
 * <li>11 Error - Routing Key Change Refused</li>
 * <li>12 Error - Routing Key Already Registered</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface RegistrationStatus extends Parameter {

    public int getStatus();

}
