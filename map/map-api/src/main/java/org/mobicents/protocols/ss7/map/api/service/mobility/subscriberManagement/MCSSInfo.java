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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 MC-SS-Info ::= SEQUENCE { ss-Code [0] SS-Code, ss-Status [1] Ext-SS-Status, nbrSB [2] MaxMC-Bearers, nbrUser [3] MC-Bearers,
 * extensionContainer [4] ExtensionContainer OPTIONAL, ...}
 *
 * MaxMC-Bearers ::= INTEGER (2..7)
 *
 * MC-Bearers ::= INTEGER (1..7)
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
