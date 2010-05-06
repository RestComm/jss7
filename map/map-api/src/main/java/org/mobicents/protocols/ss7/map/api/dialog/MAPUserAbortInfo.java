package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-UserAbortInfo ::=
 *     SEQUENCE {
 *        map-UserAbortChoice MAP-UserAbortChoice,
 *        ...
 *        extensionContainer  ExtensionContainer OPTIONAL
 *    }
 *
 * @author amit bhayani
 *
 */
public interface MAPUserAbortInfo {
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);
	
	public MAPUserAbortChoice getMAPUserAbortChoice();
	
	public void setMAPUserAbortChoice(MAPUserAbortChoice mapUsrAbrtChoice);
	
	
	

}
