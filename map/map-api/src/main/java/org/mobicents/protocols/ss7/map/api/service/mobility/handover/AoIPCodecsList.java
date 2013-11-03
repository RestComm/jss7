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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 AoIPCodecsList ::= SEQUENCE { codec1 [1] AoIPCodec, codec2 [2] AoIPCodec OPTIONAL, codec3 [3] AoIPCodec OPTIONAL, codec4 [4]
 * AoIPCodec OPTIONAL, codec5 [5] AoIPCodec OPTIONAL, codec6 [6] AoIPCodec OPTIONAL, codec7 [7] AoIPCodec OPTIONAL, codec8 [8]
 * AoIPCodec OPTIONAL, extensionContainer [9] ExtensionContainer OPTIONAL, ...} -- Codecs are sent in priority order where
 * codec1 has highest priority
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AoIPCodecsList extends Serializable {

    AoIPCodec getCodec1();

    AoIPCodec getCodec2();

    AoIPCodec getCodec3();

    AoIPCodec getCodec4();

    AoIPCodec getCodec5();

    AoIPCodec getCodec6();

    AoIPCodec getCodec7();

    AoIPCodec getCodec8();

    MAPExtensionContainer getExtensionContainer();

}
