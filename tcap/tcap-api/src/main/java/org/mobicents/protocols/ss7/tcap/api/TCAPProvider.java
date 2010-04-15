package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

public interface TCAPProvider {

	public Dialog getNewDialog();

	// FIXME: add sccp addresses?

	public void addTCListener(TCListener lst);

	public void removeTCListener(TCListener lst);
	
	//TR part for direct access

	
	public void addTRListener(TRListener lst);

	public void removeTRListener(TRListener lst);
	
	///////////////
	// Factories //
	///////////////
	
	
	public DialogPrimitiveFactory getDialogPrimitiveFactory();
	public ComponentPrimitiveFactory getComopnentPrimitiveFactory();
}
