package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * <p>
 * The optional Traffic Mode Type parameter identifies the traffic mode of
 * operation of the ASP(s) within an Application Server.
 * </p>
 * <p>
 * The valid values for Traffic Mode Type are shown in the following table:
 * <ul>
 * <li>1 Override</li>
 * <li>2 Loadshare</li>
 * <li>3 Broadcast</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface TrafficModeType extends Parameter {

    public long getMode();

}
