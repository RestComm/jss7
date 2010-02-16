/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 * @author baranowb
 *
 */
public interface DialogRequest {
	/**
	 * Return dialog for this indication
	 * @return
	 */
	public Dialog getDialog();

}
