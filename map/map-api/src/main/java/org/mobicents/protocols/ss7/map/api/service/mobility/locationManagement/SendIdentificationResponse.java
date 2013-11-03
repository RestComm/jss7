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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;

/**
 *
 MAP V3: SendIdentificationRes ::= [3] SEQUENCE { imsi IMSI OPTIONAL, -- IMSI shall be present in the first (or only) service
 * response of a dialogue. -- If multiple service requests are present in a dialogue then IMSI -- shall not be present in any
 * service response other than the first one. authenticationSetList AuthenticationSetList OPTIONAL, currentSecurityContext
 * [2]CurrentSecurityContext OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 * MAP V2: SendIdentificationRes ::= SEQUENCE { imsi IMSI authenticationSetList AuthenticationSetList OPTIONAL, ...}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendIdentificationResponse extends MobilityMessage {

    IMSI getImsi();

    AuthenticationSetList getAuthenticationSetList();

    CurrentSecurityContext getCurrentSecurityContext();

    MAPExtensionContainer getExtensionContainer();

}
