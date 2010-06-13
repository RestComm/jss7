package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-ProviderAbortInfo ::= SEQUENCE { map-ProviderAbortReason
 * MAP-ProviderAbortReason, ..., extensionContainer ExtensionContainer --
 * extensionContainer must not be used in version 2 }
 * 
 * 
 * @author amit bhayani
 * 
 */
public interface MAPProviderAbortInfo {

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
	 * Set the {@link MAPProviderAbortReason} indicating the reason why stack
	 * aborted this MAP Dialog
	 * 
	 * @param mapProvAbrtReas
	 */
	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas);

	/**
	 * get the {@link MAPProviderAbortReason} indicating the reason why stack
	 * aborted this MAP Dialog
	 * 
	 * @return
	 */
	public MAPProviderAbortReason getMAPProviderAbortReason();

}
