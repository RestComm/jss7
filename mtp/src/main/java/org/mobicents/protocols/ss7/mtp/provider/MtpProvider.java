package org.mobicents.protocols.ss7.mtp.provider;

import java.io.IOException;
import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

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
	 * @throws IOException
	 *             - when IO can not be performed, ie, link is not up.
	 * @return
	 */
	public void send(byte[] msu) throws IOException;

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

	/**
	 * Method which configures implementation. Depending on implementation
	 * different properties are supported. However each property starts with
	 * "mtp." prefix.
	 * 
	 * @param p
	 */
	public void configure(Properties p) throws ConfigurationException;
	/**
	 * Checks if link is up;
	 * @return
	 */
	public boolean isLinkUp();

}
