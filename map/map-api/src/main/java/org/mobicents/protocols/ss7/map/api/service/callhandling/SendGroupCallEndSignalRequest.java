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

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;

/**
 *
 MAP V3:
 *
 * sendGroupCallEndSignal OPERATION ::= { --Timer l ARGUMENT SendGroupCallEndSignalArg RESULT SendGroupCallEndSignalRes CODE
 * local:40 }
 *
 * SendGroupCallEndSignalArg ::= SEQUENCE { imsi IMSI OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ...,
 * talkerPriority [0]TalkerPriority OPTIONAL, additionalInfo [1]AdditionalInfo OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendGroupCallEndSignalRequest extends CallHandlingMessage {

     IMSI getImsi();

     MAPExtensionContainer getExtensionContainer();

     TalkerPriority getTalkerPriority();

     AdditionalInfo getAdditionalInfo();

}
