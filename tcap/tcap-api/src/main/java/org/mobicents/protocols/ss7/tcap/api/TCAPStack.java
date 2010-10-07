/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api;

import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

/**
 * @author baranowb
 *
 */
public interface TCAPStack {

	/**
	 * Returns stack provider.
	 * @return
	 */
	public TCAPProvider getProvider();
	/**
	 * Stops this stack and transport layer(SCCP)
	 */
	public void stop();
	/**
	 * Start stack and transport layer(SCCP)
	 * @throws IllegalStateException - if stack is already running or not configured
	 * @throws StartFailedException
	 */
	public void start() throws IllegalStateException, StartFailedException;
//	/**
//	 * Configure stack and transport layer.
//	 * @param properties
//	 * @throws ConfigurationException
//	 */
//	public void configure(Properties properties) throws ConfigurationException;
}
