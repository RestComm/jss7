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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.GlobalCellId;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.TMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;

/**
 *
 MAP V3:
 *
 * sendGroupCallInfo OPERATION ::= { --Timer m ARGUMENT SendGroupCallInfoArg RESULT SendGroupCallInfoRes ERRORS { systemFailure
 * | ongoingGroupCall | unexpectedDataValue | dataMissing | teleserviceNotProvisioned | unknownSubscriber} CODE local:84 }
 *
 * SendGroupCallInfoArg ::= SEQUENCE { requestedInfo RequestedInfo, groupId Long-GroupId, teleservice Ext-TeleserviceCode,
 * cellId [0] GlobalCellId OPTIONAL, imsi [1] IMSI OPTIONAL, tmsi [2] TMSI OPTIONAL, additionalInfo [3] AdditionalInfo OPTIONAL,
 * talkerPriority [4] TalkerPriority OPTIONAL, cksn [5] Cksn OPTIONAL, extensionContainer [6] ExtensionContainer OPTIONAL, ... }
 *
 * Cksn ::= OCTET STRING (SIZE (1)) -- The internal structure is defined in 3GPP TS 24.008
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendGroupCallInfoRequest extends CallHandlingMessage {

     RequestedInfo getRequestedInfo();

     LongGroupId getGroupId();

     ExtTeleserviceCode getTeleservice();

     GlobalCellId getCellId();

     IMSI getImsi();

     TMSI getTmsi();

     AdditionalInfo getAdditionalInfo();

     TalkerPriority getTalkerPriority();

     byte[] getCksn();

     MAPExtensionContainer getExtensionContainer();

}
