package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

public interface TCAPProvider {

	public Dialog getNewDialog();

	///////////////
	// Factories //
	///////////////
	
	public DialogPrimitiveFactory getDialogPrimitiveFactory();
	public ComponentPrimitiveFactory getComopnentPrimitiveFactory();
}
