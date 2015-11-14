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
