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

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;

/**
 *
 MAP V3:
 *
 * forwardGroupCallSignalling OPERATION ::= { --Timer s ARGUMENT ForwardGroupCallSignallingArg CODE local:42 }
 *
 * ForwardGroupCallSignallingArg ::= SEQUENCE { imsi IMSI OPTIONAL, uplinkRequestAck [0] NULL OPTIONAL, uplinkReleaseIndication
 * [1] NULL OPTIONAL, uplinkRejectCommand [2] NULL OPTIONAL, uplinkSeizedCommand [3] NULL OPTIONAL, uplinkReleaseCommand [4]
 * NULL OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ..., stateAttributes [5] StateAttributes OPTIONAL,
 * talkerPriority [6] TalkerPriority OPTIONAL, additionalInfo [7] AdditionalInfo OPTIONAL, emergencyModeResetCommandFlag [8]
 * NULL OPTIONAL, sm-RP-UI [9] SignalInfo OPTIONAL, an-APDU [10] AccessNetworkSignalInfo OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ForwardGroupCallSignallingRequest extends CallHandlingMessage {

     IMSI getImsi();

     boolean getUplinkRequestAck();

     boolean getUplinkReleaseIndication();

     boolean getUplinkRejectCommand();

     boolean getUplinkSeizedCommand();

     boolean getUplinkReleaseCommand();

     MAPExtensionContainer getExtensionContainer();

     StateAttributes getStateAttributes();

     TalkerPriority getTalkerPriority();

     AdditionalInfo getAdditionalInfo();

     boolean getEmergencyModeResetCommandFlag();

     SmsSignalInfo getSmRpUi();

     AccessNetworkSignalInfo getAnApdu();

}
