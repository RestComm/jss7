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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 EPC-AV ::= SEQUENCE { rand RAND, xres XRES, autn AUTN, kasme KASME, extensionContainer ExtensionContainer OPTIONAL, ...}
 *
 *
 * RAND ::= OCTET STRING (SIZE (16)) XRES ::= OCTET STRING (SIZE (4..16)) AUTN ::= OCTET STRING (SIZE (16)) KASME ::= OCTET
 * STRING (SIZE (32))
 *
 * @author sergey vetyutnev
 *
 */
public interface EpcAv extends Serializable {

    byte[] getRand();

    byte[] getXres();

    byte[] getAutn();

    byte[] getKasme();

    MAPExtensionContainer getExtensionContainer();

}
