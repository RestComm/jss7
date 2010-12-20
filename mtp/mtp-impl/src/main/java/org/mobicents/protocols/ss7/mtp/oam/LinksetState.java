package org.mobicents.protocols.ss7.mtp.oam;

/**
 * 
 * @author amit bhayani
 *
 */
public interface LinksetState {
	/**
	 * Indicates the linkset does not have any “available” links and cannot
	 * transport traffic
	 */
	public static final int UNAVAILABLE = 1;

	/**
	 * Indicates the linkset has been shutdown in the configuration
	 */
	public static final int SHUTDOWN = 2;

	/**
	 * Indicates the linkset has at least one available link and can carry
	 * traffic
	 */
	public static final int AVAILABLE = 3;
}
