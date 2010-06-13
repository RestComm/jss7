package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-UserAbortInfo ::= SEQUENCE { map-UserAbortChoice MAP-UserAbortChoice, ...
 * extensionContainer ExtensionContainer OPTIONAL }
 * 
 * @author amit bhayani
 * 
 */
public interface MAPUserAbortInfo {

	/**
	 * Get the {@link MAPDialog} for which this event is fired
	 * 
	 * @return
	 */
	public MAPDialog getMAPDialog();

	/**
	 * Set the {@link MAPDialog}
	 * 
	 * @param mapDialog
	 */
	public void setMAPDialog(MAPDialog mapDialog);

	/**
	 * Get the {@link MAPUserAbortChoice} indicating the reason why peer aborted
	 * the MAP Dialog
	 * 
	 * @return
	 */
	public MAPUserAbortChoice getMAPUserAbortChoice();

	/**
	 * Set the {@link MAPUserAbortChoice}
	 * 
	 * @param mapUsrAbrtChoice
	 */
	public void setMAPUserAbortChoice(MAPUserAbortChoice mapUsrAbrtChoice);

}
