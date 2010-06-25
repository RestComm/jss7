/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api;

import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

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
	
	public void start() throws IllegalStateException, StartFailedException;
	
	public void configure(Properties properties);
}
