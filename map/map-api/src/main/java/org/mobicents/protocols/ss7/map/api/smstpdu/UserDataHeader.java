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

package org.mobicents.protocols.ss7.map.api.smstpdu;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface UserDataHeader extends Serializable {

    int _InformationElementIdentifier_ConcatenatedShortMessages8bit = 0x00;
    int _InformationElementIdentifier_ConcatenatedShortMessages16bit = 0x08;
    int _InformationElementIdentifier_NationalLanguageSingleShift = 0x24;
    int _InformationElementIdentifier_NationalLanguageLockingShift = 0x25;

    byte[] getEncodedData();

    Map<Integer, byte[]> getAllData();

    void addInformationElement(int informationElementIdentifier, byte[] encodedData);

    void addInformationElement(UserDataHeaderElement informationElement);

    byte[] getInformationElementData(int informationElementIdentifier);

    NationalLanguageLockingShiftIdentifier getNationalLanguageLockingShift();

    NationalLanguageSingleShiftIdentifier getNationalLanguageSingleShift();

    ConcatenatedShortMessagesIdentifier getConcatenatedShortMessagesIdentifier();

}