package org.mobicents.protocols.ss7.mtp.oam;

/**
 * 
 * @author amit bhayani
 *
 */
public interface LinkState {

	/**
	 * Indicates the link is not available to carry traffic. This can occur if
	 * the link is remotely or locally inhibited by a user. It can also be
	 * unavailable if MTP2 has not been able to successfully activate the link
	 * connection or the link test messages sent by MTP3 are not being
	 * acknowledged.
	 */
	public static final int UNAVAILABLE = 1;

	/**
	 * Indicates the link has been shutdown in the configuration. A link is
	 * shutdown when it is shutdown at the MTP3 layer.
	 */
	public static final int SHUTDOWN = 2;

	/**
	 * Indicates the link is active and able to transport traffic.
	 */
	public static final int AVAILABLE = 3;

	/**
	 * A link is FAILED when the link is not shutdown but is unavailable at
	 * layer2 for some reason. For example Initial Alignment failed?
	 */
	public static final int FAILED = 4;

}
