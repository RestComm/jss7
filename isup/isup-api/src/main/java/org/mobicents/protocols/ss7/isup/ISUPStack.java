/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author baranowb
 */
public interface ISUPStack {
	
	/**
	 * Get instance of provider.
	 * @return
	 */
	public ISUPProvider getIsupProvider();
	/**
	 * Stop stack and all underlying resources.
	 */
	public void stop();
	/**
	 * Start stack and all underlying resources
	 * @throws IllegalStateException - if stack is already running or is not configured yet.
	 * @throws StartFailedException - if start failed for some other reason.
	 */
	public void start() throws IllegalStateException, StartFailedException;
	/**
	 * Configure this stack and its resources, like MTP.
	 * @param props
	 * @throws ConfigurationException
	 */
	public void configure(Properties props) throws ConfigurationException;
}
