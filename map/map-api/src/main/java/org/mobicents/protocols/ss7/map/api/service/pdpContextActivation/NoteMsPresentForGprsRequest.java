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

package org.mobicents.protocols.ss7.map.api.service.pdpContextActivation;

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V3:
 *
 * noteMsPresentForGprs OPERATION ::= { --Timer m ARGUMENT NoteMsPresentForGprsArg RESULT NoteMsPresentForGprsRes -- optional
 * ERRORS { systemFailure | dataMissing | unexpectedDataValue | unknownSubscriber} CODE local:26 }
 *
 * NoteMsPresentForGprsArg ::= SEQUENCE { imsi [0] IMSI, sgsn-Address [1] GSN-Address, ggsn-Address [2] GSN-Address OPTIONAL,
 * extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface NoteMsPresentForGprsRequest extends PdpContextActivationMessage {

    IMSI getImsi();

    GSNAddress getSgsnAddress();

    GSNAddress getGgsnAddress();

    MAPExtensionContainer getExtensionContainer();

}
