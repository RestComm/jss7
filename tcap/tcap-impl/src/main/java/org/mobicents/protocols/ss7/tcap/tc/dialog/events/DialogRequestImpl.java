/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DialogRequest;

/**
 * @author baranowb
 *
 */
public abstract class DialogRequestImpl implements DialogRequest {

	
	private Dialog dialog;

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}


}
