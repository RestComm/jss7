package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * Represents the Linkset's mode.
 * </p>
 * <p>
 * Valid modes are
 * <ul>
 * <li>UNCONFIGURED : In this mode the necessary parameters of Linkset are not
 * yet configured</li>
 * <li>
 * <li>CONFIGURED : All the compulsory parameters of Link are configured</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface LinksetMode {

    public static final int UNCONFIGURED = 1;
    public static final int CONFIGURED = 2;

}
