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
import org.mobicents.protocols.ss7.map.api.smstpdu.NationalLanguageSingleShiftIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class NationalLanguageSingleShiftIdentifierImpl extends Gsm7NationalLanguageIdentifierImpl implements
        NationalLanguageSingleShiftIdentifier {

    public NationalLanguageSingleShiftIdentifierImpl(NationalLanguageIdentifier nationalLanguageCode) {
        super(nationalLanguageCode);
    }

    public NationalLanguageSingleShiftIdentifierImpl(byte[] encodedInformationElementData) {
        super(encodedInformationElementData);
    }

    public int getEncodedInformationElementIdentifier() {
        return UserDataHeader._InformationElementIdentifier_NationalLanguageSingleShift;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NationalLanguageSingleShiftIdentifier [");
        sb.append("nationalLanguageCode=");
        sb.append(this.getNationalLanguageIdentifier());
        sb.append("]");

        return sb.toString();
    }
}
