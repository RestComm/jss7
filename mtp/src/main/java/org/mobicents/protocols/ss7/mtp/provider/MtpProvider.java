package org.mobicents.protocols.ss7.mtp.provider;

import java.io.IOException;

/**
 * Interface for classes able to provide MTP signaling.
 * 
 * @author baranowb
 * 
 */
public interface MtpProvider {

	/**
	 * Sets listener for MTP callbacks. If null is passed internal refence is
	 * cleared.
	 * 
	 * @param lst
	 */
	public void setMtpListener(MtpListener lst);


	/**
	 * Passes argument to MTP layers for processing. Passed buffer should not be
	 * reused after passing to this method!
	 * 
	 * @param msu
	 * @return
	 */
	public void send(byte[] msu);

	/**
	 * Starts this provider implementation. Depending on internal it can start
	 * local MTP process, or M3UA layer.
	 * 
	 * @throws IOException
	 * @throws StartFailedException
	 */
	public void start() throws IOException, StartFailedException;

	/**
	 * Stops this provider. This call clears all references, ie. listener is
	 * cleared as {@link #setMtpListener(MtpListener)} with null argument.
	 */
	public void stop();

}
