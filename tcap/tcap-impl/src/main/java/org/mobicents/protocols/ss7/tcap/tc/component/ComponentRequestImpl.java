/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

/**
 * @author baranowb
 * 
 */
public abstract class ComponentRequestImpl implements ComponentRequest {

	private Dialog dialog;
	protected Component component;

	
	
	protected ComponentRequestImpl() {
		super();
		this.component = getEncodableComponent();
	}

	/**
	 * @return the invokeId
	 */
	public Long getInvokeId() {

		return component.getInvokeId();

	}

	/**
	 * @param invokeId
	 *            the invokeId to set
	 */
	public void setInvokeId(Long invokeId) {
		this.component.setInvokeId(invokeId);
	}

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog
	 *            the dialog to set
	 */
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	protected abstract Component getEncodableComponent();
}
