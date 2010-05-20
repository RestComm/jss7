package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;

/**
 * map-accept [1] IMPLICIT SEQUENCE {
 *   ... ,
 *  extensionContainer SEQUENCE {
 *     privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 ..
 *        SEQUENCE {
 *           extId      MAP-EXTENSION .&extensionId ( {
 *              ,
 *              ...} ) ,
 *           extType    MAP-EXTENSION .&ExtensionType ( {
 *              ,
 *              ...} { @extId   } ) OPTIONAL} OPTIONAL,
 *     pcs-Extensions [1] IMPLICIT SEQUENCE {
 *        ... } OPTIONAL,
 *     ... } OPTIONAL},
 *
 * 
 * @author amit bhayani
 *
 */
public class MAPAcceptInfoImpl implements MAPAcceptInfo {
	
	protected static final int MAP_ACCEPT_INFO_TAG = 0x01;
	
	private MAPDialog mapDialog = null;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}
	
	public void decode(AsnInputStream ais) throws AsnException, IOException, MAPException{
		//TODO I donno what is here?
	}
	
	public void encode(AsnOutputStream asnOS) throws IOException, MAPException{
		//TODO I donno what is here?
	}

}
