/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.api.service.mobility.imei;

import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1-2-3:
 *
 * MAP V3: checkIMEI OPERATION ::= { --Timer m ARGUMENT CheckIMEI-Arg RESULT CheckIMEI-Res ERRORS { systemFailure | dataMissing
 * | unknownEquipment} CODE local:43 }
 *
 * MAP V2: CheckIMEI ::= OPERATION --Timer m ARGUMENT imei IMEI RESULT equipmentStatus EquipmentStatus ERRORS { SystemFailure,
 * DataMissing, UnknownEquipment }
 *
 * MAP V3: CheckIMEIArg ::= SEQUENCE { imei IMEI, requestedEquipmentInfo RequestedEquipmentInfo, extensionContainer
 * ExtensionContainer OPTIONAL, ...}
 *
 * MAP V2: ARGUMENT imei IMEI
 *
 *
 * @author normandes
 *
 */
public interface CheckImeiRequest extends MobilityMessage {

    IMEI getIMEI();

    RequestedEquipmentInfo getRequestedEquipmentInfo();

    MAPExtensionContainer getExtensionContainer();

    IMSI getIMSI();

}
