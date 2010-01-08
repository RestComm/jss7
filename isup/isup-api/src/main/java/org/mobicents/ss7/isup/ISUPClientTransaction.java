/**
 * Start time:08:57:33 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup;

import java.io.IOException;



/**
 * Start time:08:57:33 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ISUPClientTransaction extends ISUPTransaction {
	public void sendRequest() throws ParameterRangeInvalidException, IOException;
	public Object getState();
}
