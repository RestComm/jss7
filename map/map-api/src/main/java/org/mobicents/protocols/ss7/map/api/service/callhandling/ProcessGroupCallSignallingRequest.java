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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;

/**
 *
 MAP V3:
 *
 * processGroupCallSignalling OPERATION ::= { --Timer s ARGUMENT ProcessGroupCallSignallingArg CODE local:41 }
 *
 * ProcessGroupCallSignallingArg ::= SEQUENCE { uplinkRequest [0] NULL OPTIONAL, uplinkReleaseIndication [1] NULL OPTIONAL,
 * releaseGroupCall [2] NULL OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ..., talkerPriority [3] TalkerPriority
 * OPTIONAL, additionalInfo [4] AdditionalInfo OPTIONAL, emergencyModeResetCommandFlag [5] NULL OPTIONAL, an-APDU [6]
 * AccessNetworkSignalInfo OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ProcessGroupCallSignallingRequest extends CallHandlingMessage {

    boolean getUplinkRequest();

    boolean getUplinkReleaseIndication();

    boolean getReleaseGroupCall();

    MAPExtensionContainer getExtensionContainer();

    TalkerPriority getTalkerPriority();

    AdditionalInfo getAdditionalInfo();

    boolean getEmergencyModeResetCommandFlag();

    AccessNetworkSignalInfo getAnAPDU();

}
