/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

/**
 * @author baranowb
 *
 */
public abstract class ComponentIndicationImpl implements ComponentIndication {

	private Dialog dialog;

	protected Component component;
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication#getDialog()
	 */
	public Dialog getDialog() {
		return this.dialog;
	}

	
	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}


	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication#getInvokeId()
	 */
	public Long getInvokeId() {
		return component.getInvokeId();
	}

	
}
