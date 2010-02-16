/**
 * Start time:12:56:06 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

import org.mobicents.protocols.ss7.isup.ISUPComponent;

/**
 * Start time:12:56:06 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
public interface ISUPParameter extends ISUPComponent {
//	/**
//	 * Returns tag representing this element.
//	 * 
//	 * @return
//	 */
	//byte[] getTag();
	//Returns value of certain _PARAMETER_CODE element;
	int getCode();
}
