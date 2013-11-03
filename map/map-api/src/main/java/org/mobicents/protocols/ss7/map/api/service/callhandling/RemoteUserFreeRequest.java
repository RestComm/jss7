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

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CCBSFeature;

/**
 *
 MAP V3:
 *
 * remoteUserFree OPERATION ::= { --Timer ml ARGUMENT RemoteUserFreeArg RESULT RemoteUserFreeRes ERRORS { unexpectedDataValue |
 * dataMissing | incompatibleTerminal | absentSubscriber | systemFailure | busySubscriber} CODE local:75 }
 *
 * RemoteUserFreeArg ::= SEQUENCE{ imsi [0] IMSI, callInfo [1] ExternalSignalInfo, ccbs-Feature [2] CCBS-Feature,
 * translatedB-Number [3] ISDN-AddressString, replaceB-Number [4] NULL OPTIONAL, alertingPattern [5] AlertingPattern OPTIONAL,
 * extensionContainer [6] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface RemoteUserFreeRequest extends CallHandlingMessage {

     IMSI getImsi();

     ExternalSignalInfo getCallInfo();

     CCBSFeature getCcbsFeature();

     ISDNAddressString getTranslatedBNumber();

     boolean getReplaceBNumber();

     AlertingPattern getAlertingPattern();

     MAPExtensionContainer getExtensionContainer();

}
