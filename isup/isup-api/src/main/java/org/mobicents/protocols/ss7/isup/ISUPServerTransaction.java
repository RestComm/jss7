/**
 * Start time:08:58:21 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * Start time:08:58:21 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ISUPServerTransaction extends ISUPTransaction{

	public void sendAnswer(ISUPMessage msg) throws ParameterRangeInvalidException,IllegalArgumentException, IOException;
	
}
