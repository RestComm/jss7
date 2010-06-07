package org.mobicents.protocols.ss7.stream;

public interface MTPListener {
	
	/**
	 * Called once proper MSU has been streamed
	 * @param msg
	 */
	public void receive(byte[] msg);
	/**
	 * Indicates that status message - linkUp has been received
	 */
	public void linkUp();
	/**
	 * Indicates that either peer sent status message - linkDown or connection has been lost
	 */
	public void linkDown();
	
}
