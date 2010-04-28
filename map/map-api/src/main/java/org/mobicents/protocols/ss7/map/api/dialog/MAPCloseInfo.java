package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-CloseInfo ::= SEQUENCE {
 *    ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 *
 * @author amit bhayani
 *
 */
public interface MAPCloseInfo {
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);

}
