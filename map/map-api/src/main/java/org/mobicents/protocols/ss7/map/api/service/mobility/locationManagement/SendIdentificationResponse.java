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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;

/**
<code>
MAP V3: SendIdentificationRes ::= [3] SEQUENCE {
  imsi                    IMSI OPTIONAL,
  -- IMSI shall be present in the first (or only) service response of a dialogue.
  -- If multiple service requests are present in a dialogue then IMSI
  -- shall not be present in any service response other than the first one.
  authenticationSetList AuthenticationSetList OPTIONAL,
  currentSecurityContext  [2] CurrentSecurityContext OPTIONAL,
  extensionContainer      [3] ExtensionContainer OPTIONAL,
  ...
}

MAP V2: SendIdentificationRes ::= SEQUENCE {
  imsi                    IMSI,
  authenticationSetList   AuthenticationSetList OPTIONAL,
  ...
}
</code>
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
