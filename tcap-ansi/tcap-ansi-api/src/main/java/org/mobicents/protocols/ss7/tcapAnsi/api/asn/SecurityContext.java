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
*
*
    securityContext     CHOICE {
        integerSecurityId   [0] IMPLICIT INTEGER,
        objectSecurityId    [1] IMPLICIT OBJECT IDENTIFIER
    } OPTIONAL,

*/
public interface SecurityContext extends Encodable {

    int _TAG_SECURITY_CONTEXT_INTEGER = 0;
    int _TAG_SECURITY_CONTEXT_OID = 1;

    Long getIntegerSecurityId();

    void setIntegerSecurityId(Long val);

    long[] getObjectSecurityId();

    void setObjectSecurityId(long[] val);

}
