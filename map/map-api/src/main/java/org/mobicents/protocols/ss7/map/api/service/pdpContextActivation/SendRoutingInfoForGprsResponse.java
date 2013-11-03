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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 SendRoutingInfoForGprsRes ::= SEQUENCE { sgsn-Address [0] GSN-Address, ggsn-Address [1] GSN-Address OPTIONAL,
 * mobileNotReachableReason [2] AbsentSubscriberDiagnosticSM OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 * AbsentSubscriberDiagnosticSM ::= INTEGER (0..255) -- AbsentSubscriberDiagnosticSM values are defined in 3GPP TS 23.040
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendRoutingInfoForGprsResponse extends PdpContextActivationMessage {

    GSNAddress getSgsnAddress();

    GSNAddress getGgsnAddress();

    Integer getMobileNotReachableReason();

    MAPExtensionContainer getExtensionContainer();

}
