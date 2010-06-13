package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * From GSM 09.02 Version 5.15.1 Release 1996
 * 
 * MAP-OpenInfo ::= SEQUENCE { destinationReference [0] AddressString
 * originationReference [1] AddressString ..., extensionContainer
 * ExtensionContainer -- extensionContainer must not be used in version 2 }
 * 
 * 
 * @author amit bhayani
 * 
 */
public interface MAPOpenInfo {

	/**
	 * Get the destination {@link AddressString}
	 * 
	 * @return
	 */
	public AddressString getDestReference();

	/**
	 * Set the destination {@link AddressString}
	 * 
	 * @param destReference
	 */
	public void setDestReference(AddressString destReference);

	/**
	 * Get the originating {@link AddressString}
	 * 
	 * @return
	 */
	public AddressString getOrigReference();

	/**
	 * Set the originating {@link AddressString}
	 * 
	 * @param origReference
	 */
	public void setOrigReference(AddressString origReference);

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

}
