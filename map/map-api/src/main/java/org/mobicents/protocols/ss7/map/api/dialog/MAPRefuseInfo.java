package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-RefuseInfo ::= SEQUENCE {
 *   reason Reason,
 *   ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 *
 * @author amit bhayani
 *
 */
public interface MAPRefuseInfo {
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);
	
	public Reason getReason();
	
	public void setReason(Reason reason);

}
