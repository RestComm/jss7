package org.mobicents.protocols.ss7.map.api.service.mobility.imei;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V3:
CheckIMEI-Res ::= SEQUENCE {
	equipmentStatus	EquipmentStatus	OPTIONAL,
	bmuef		UESBI-Iu		OPTIONAL,
	extensionContainer	[0] ExtensionContainer	OPTIONAL,
	...}

MAP V2:
RESULT
equipmentStatus		EquipmentStatus

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CheckIMEIResponse extends MobilityMessage {

	public EquipmentStatus getEquipmentStatus();

	public UESBIIu getBmuef();

	public MAPExtensionContainer getExtensionContainer();

}
