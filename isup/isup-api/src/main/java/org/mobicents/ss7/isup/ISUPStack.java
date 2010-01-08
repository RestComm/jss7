/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup;

import org.mobicents.ss7.SS7Provider;

/**
 * Start time:09:07:18 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ISUPStack {

	public SS7Provider getTransportProvider();
	public ISUPProvider getIsupProvider();
}
