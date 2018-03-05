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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

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
