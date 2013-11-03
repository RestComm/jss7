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

package org.mobicents.protocols.ss7.map.smstpdu;

import org.mobicents.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.Gsm7NationalLanguageIdentifier;

/**
 *
 * @author sergey vetyutnev
 *
 */
public abstract class Gsm7NationalLanguageIdentifierImpl implements Gsm7NationalLanguageIdentifier {

    private NationalLanguageIdentifier nationalLanguageCode;

    public Gsm7NationalLanguageIdentifierImpl(NationalLanguageIdentifier nationalLanguageCode) {
        this.nationalLanguageCode = nationalLanguageCode;
    }

    public Gsm7NationalLanguageIdentifierImpl(byte[] encodedInformationElementData) {
        if (encodedInformationElementData != null && encodedInformationElementData.length > 0)
            this.nationalLanguageCode = NationalLanguageIdentifier.getInstance(encodedInformationElementData[0] & 0xFF);
    }

    public NationalLanguageIdentifier getNationalLanguageIdentifier() {
        return nationalLanguageCode;
    }

    public byte[] getEncodedInformationElementData() {
        return new byte[] { (byte) nationalLanguageCode.getCode() };
    }
}
