package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

public interface DialogIndication {

	/**
	 * Return dialog for this indication
	 * @return
	 */
	public Dialog getDialog();
	/**
	 * get components if present, if there are none, it will return null;
	 * @return
	 */
	public ComponentIndication[] getComponents();
	
}
