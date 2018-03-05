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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 CodecList ::= SEQUENCE { codec1 [1] Codec, codec2 [2] Codec OPTIONAL, codec3 [3] Codec OPTIONAL, codec4 [4] Codec OPTIONAL,
 * codec5 [5] Codec OPTIONAL, codec6 [6] Codec OPTIONAL, codec7 [7] Codec OPTIONAL, codec8 [8] Codec OPTIONAL,
 * extensionContainer [9] ExtensionContainer OPTIONAL, ...} -- Codecs are sent in priority order where codec1 has highest
 * priority
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CodecList extends Serializable {

    Codec getCodec1();

    Codec getCodec2();

    Codec getCodec3();

    Codec getCodec4();

    Codec getCodec5();

    Codec getCodec6();

    Codec getCodec7();

    Codec getCodec8();

    MAPExtensionContainer getExtensionContainer();

}
