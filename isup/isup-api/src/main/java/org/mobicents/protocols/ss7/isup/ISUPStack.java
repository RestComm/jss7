/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author baranowb
 */
public interface ISUPStack {

	public ISUPProvider getIsupProvider();

	public void stop();

	public void start() throws IllegalStateException, StartFailedException;

	public void configure(Properties props);
}
