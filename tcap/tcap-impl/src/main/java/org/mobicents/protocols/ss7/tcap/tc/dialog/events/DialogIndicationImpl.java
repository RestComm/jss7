/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DialogIndication;

/**
 * @author baranowb
 *
 */
public abstract class DialogIndicationImpl implements DialogIndication {

	private ComponentIndication[] components;
	private Dialog dialog;
	/**
	 * @return the components
	 */
	public ComponentIndication[] getComponents() {
		return components;
	}
	/**
	 * @param components the components to set
	 */
	public void setComponents(ComponentIndication[] components) {
		this.components = components;
	}
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
