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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn;

/**
 *
 * @author sergey vetyutnev

Confidentiality ::= SEQUENCE {
    confidentialityId   CHOICE {
        integerConfidentialityId    [0] IMPLICIT INTEGER,
        objectConfidentialityId     [1] IMPLICIT OBJECT IDENTIFIER
    } OPTIONAL,
    ...
    --The extension marker indicates the possible presence of items
    --in the confidentiality set that are used by the confidentiality
    --algorithm.
}

 *
 */
public interface Confidentiality extends Encodable {

    int _TAG_CONFIDENTIALITY = 2;
    int _TAG_INTEGER_CONFIDENTIALITY_ID = 0;
    int _TAG_OBJECT_CONFIDENTIALITY_ID = 1;

    Long getIntegerConfidentialityId();

    void setIntegerConfidentialityId(Long val);

    long[] getObjectConfidentialityId();

    void setObjectConfidentialityId(long[] val);

}
