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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
<code>
MC-SS-Info ::= SEQUENCE {
  ss-Code             [0] SS-Code,
  ss-Status           [1] Ext-SS-Status,
  nbrSB               [2] MaxMC-Bearers,
  nbrUser             [3] MC-Bearers,
  extensionContainer  [4] ExtensionContainer OPTIONAL,
  ...
}

MaxMC-Bearers ::= INTEGER (2..7)

MC-Bearers ::= INTEGER (1..7)
</code>
 *
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MCSSInfo extends Serializable {

    SSCode getSSCode();

    ExtSSStatus getSSStatus();

    int getNbrSB();

    int getNbrUser();

    MAPExtensionContainer getExtensionContainer();

}
