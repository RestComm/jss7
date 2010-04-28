package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * From GSM 09.02 Version 5.15.1 Release 1996
 * 
 * MAP-OpenInfo ::= SEQUENCE {
 *   destinationReference             [0] AddressString
 *   originationReference             [1] AddressString
 *   ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 *
 *
 * @author amit bhayani
 *
 */
public interface MAPOpenInfo {
	
	public byte[] getDestReference();

	public void setDestReference(byte[] destReference);
	
	public byte[] getOrigReference();

	public void setOrigReference(byte[] origReference);
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);

}
