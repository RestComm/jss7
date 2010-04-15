package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;

public interface ComponentIndication {

	/**
	 * Returns type of component, each component is indetified by intiger code.
	 * 
	 * @return
	 */
	public ComponentType getType();

	public Dialog getDialog();

	public Long getInvokeId();
}
