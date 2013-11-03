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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.Serializable;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPAsnPrimitive extends Serializable {

    int getTag() throws MAPException;

    int getTagClass();

    boolean getIsPrimitive();

    /**
     * Decoding the length and the content of the primitive (the tag has already read)
     *
     * @param ansIS The AsnInputStream that contains the length and the content of the primitive
     * @throws MAPParsingComponentException
     */
    void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException;

    /**
     * Decoding the content of the primitive (the tag and the length have already read)
     *
     * @param ansIS The AsnInputStream that contains the content of the primitive
     * @param length The length of the content
     * @throws MAPParsingComponentException
     */
    void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException;

    /**
     * Encoding the tag, the length and the content. Tag and tag class are universal
     *
     * @param asnOs
     * @throws MAPException
     */
    void encodeAll(AsnOutputStream asnOs) throws MAPException;

    /**
     * Encoding the tag, the length and the content. Tag and tag class are defined
     *
     * @param asnOs
     * @param tagClass
     * @param tag
     * @throws MAPException
     */
    void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException;

    /**
     * Encoding the content.
     *
     * @param asnOs
     * @throws MAPException
     */
    void encodeData(AsnOutputStream asnOs) throws MAPException;

}
