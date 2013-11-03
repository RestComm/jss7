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

package org.mobicents.protocols.ss7.map.api.service.mobility.handover;

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1-2-3:
 *
 * MAP V3: sendEndSignal OPERATION ::= { --Timer l ARGUMENT SendEndSignal-Arg RESULT SendEndSignal-Res CODE local:29 }
 *
 * MAP V2: SendEndSignal ::= OPERATION --Timer l ARGUMENT bss-APDU ExternalSignalInfo -- optional -- bss-APDU must be absent in
 * version 1 -- bss-APDU must be present in version greater 1 RESULT
 *
 *
 * MAP V3: SendEndSignal-Arg ::= [3] SEQUENCE { an-APDU AccessNetworkSignalInfo, extensionContainer [0] ExtensionContainer
 * OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendEndSignalRequest extends MobilityMessage {

    AccessNetworkSignalInfo getAnAPDU();

    MAPExtensionContainer getExtensionContainer();

    // this parameter is for MAP V2
    ExternalSignalInfo getBssAPDU();

}
