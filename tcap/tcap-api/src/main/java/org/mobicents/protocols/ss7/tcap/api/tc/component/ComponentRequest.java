package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;

public interface ComponentRequest {


	public ComponentType getType();
	
	public Dialog getDialog();
	
	public Long getInvokeId();
	
	public void setInvokeId(Long id);
	
}
