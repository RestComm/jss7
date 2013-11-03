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

import java.io.IOException;
import java.io.OutputStream;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TbcdStringWithFiller extends TbcdString {

    protected static int DIGIT_MASK = 0xFF;

    public TbcdStringWithFiller(int minLength, int maxLength, String _PrimitiveName) {
        super(minLength, maxLength, _PrimitiveName);
    }

    public TbcdStringWithFiller(int minLength, int maxLength, String _PrimitiveName, String data) {
        super(minLength, maxLength, _PrimitiveName, data);
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.data == null)
            throw new MAPException("Error while encoding the " + _PrimitiveName + ": data is not defined");

        encodeString(asnOs, this.data);
        this.encodeFiller(asnOs);
    }

    public void encodeFiller(OutputStream asnOs) throws MAPException {

        for (int i = data.length() + 1; i < this.maxLength * 2; i = i + 2) {
            try {
                asnOs.write(DIGIT_MASK);
            } catch (IOException e) {
                throw new MAPException("Error when encoding TbcdString: " + e.getMessage(), e);
            }
        }

    }

}
