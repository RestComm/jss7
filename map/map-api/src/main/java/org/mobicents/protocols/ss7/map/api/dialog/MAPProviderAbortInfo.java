package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-ProviderAbortInfo ::= SEQUENCE {
 *   map-ProviderAbortReason          MAP-ProviderAbortReason,
 *   ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 *
 * 
 * @author amit bhayani
 *
 */
public interface MAPProviderAbortInfo {
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);
	
	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas);
	
	public MAPProviderAbortReason getMAPProviderAbortReason();

}
