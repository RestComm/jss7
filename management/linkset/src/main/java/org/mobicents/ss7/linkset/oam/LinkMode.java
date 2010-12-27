package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * Represents the Link's mode.
 * </p>
 * <p>
 * Valid modes are
 * <ul>
 * <li>UNCONFIGURED : In this mode the necessary parameters of Links are not
 * yet configured</li>
 * <li>
 * <li>Configured : All the compulsory parameters of Link are configured</li>
 * </ul>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface LinkMode {

	public static final int UNCONFIGURED = 1;
	public static final int CONFIGURED = 2;

}
