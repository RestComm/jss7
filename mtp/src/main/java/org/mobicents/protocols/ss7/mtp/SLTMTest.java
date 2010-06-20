package org.mobicents.protocols.ss7.mtp;

import java.util.concurrent.TimeUnit;

public interface SLTMTest {
	public void start();

	public void stop();

	/**
	 * This methods should be called to acknowledge that current tests is
	 * passed.
	 * 
	 */
	public void ack();

	/**
	 * Sends SLTM message using this link.
	 * 
	 * @param timeout
	 *            the amount of time in millesecond for awaiting response.
	 */
	public void ping(long timeout);

}
