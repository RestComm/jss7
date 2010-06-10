package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

public interface TCAPProvider {

	/**
	 * Create new structured dialog.
	 * @param localAddress - desired local address
	 * @param remoteAddress - initial remote address, it can change after first TCContinue. 
	 * @return
	 */
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;
	/**
	 * Create new unstructured dialog.
	 * @param localAddress
	 * @param remoteAddress
	 * @return
	 * @throws TCAPException
	 */
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;

	///////////////
	// Factories //
	///////////////
	
	public DialogPrimitiveFactory getDialogPrimitiveFactory();
	public ComponentPrimitiveFactory getComponentPrimitiveFactory();
	
	///////////////
	// Listeners //
	///////////////	
	
	public void addTCListener(TCListener lst);

	public void removeTCListener(TCListener lst);
}
