/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.api.service.mobility.imei;

import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 
 
MAP V1-2-3:
 
MAP V3:
checkIMEI  OPERATION ::= {				--Timer m
	ARGUMENT
		CheckIMEI-Arg
	RESULT
		CheckIMEI-Res
	ERRORS {
		systemFailure |
		dataMissing |
		unknownEquipment}
	CODE	local:43 }

MAP V2:
CheckIMEI ::= OPERATION							--Timer m
 		ARGUMENT
 			imei					IMEI
 		RESULT
 			equipmentStatus			EquipmentStatus
 		ERRORS {
 			SystemFailure,
 			DataMissing,
 			UnknownEquipment
  	}
 
MAP V3:
 CheckIMEIArg ::= SEQUENCE { 
 		imei						IMEI,
 		requestedEquipmentInfo 		RequestedEquipmentInfo,
 		extensionContainer			ExtensionContainer 			OPTIONAL,
 	...}

MAP V2:
 		ARGUMENT
 			imei					IMEI

 * 
 * @author normandes
 *
 */
public interface CheckImeiRequest extends MobilityMessage {

	public IMEI getIMEI();
	
	public RequestedEquipmentInfo getRequestedEquipmentInfo();
	
	public MAPExtensionContainer getExtensionContainer();
	
}
