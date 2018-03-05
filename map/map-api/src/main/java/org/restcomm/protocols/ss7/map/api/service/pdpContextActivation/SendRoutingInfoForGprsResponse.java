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

package org.restcomm.protocols.ss7.map.api.service.pdpContextActivation;

import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
<code>
SendRoutingInfoForGprsRes ::= SEQUENCE {
  sgsn-Address              [0] GSN-Address,
  ggsn-Address              [1] GSN-Address OPTIONAL,
  mobileNotReachableReason  [2] AbsentSubscriberDiagnosticSM OPTIONAL,
  extensionContainer        [3] ExtensionContainer OPTIONAL,
  ...
}

AbsentSubscriberDiagnosticSM ::= INTEGER (0..255)
-- AbsentSubscriberDiagnosticSM values are defined in 3GPP TS 23.040
</code>
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
