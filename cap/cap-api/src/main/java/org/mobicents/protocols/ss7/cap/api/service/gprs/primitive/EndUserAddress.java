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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

/**
 *
 EndUserAddress {PARAMETERS-BOUND: bound} ::= SEQUENCE { pDPTypeOrganization [0] OCTET STRING (SIZE(1)), pDPTypeNumber [1]
 * OCTET STRING (SIZE(1)), pDPAddress [2] OCTET STRING (SIZE(1 .. 63)) OPTIONAL } -- Indicates the EndUserAddress, refer to 3GPP
 * TS 29.060 [12] for the encoding. -- The pDPTypeOrganization shall use the least significant 4 bits of the octet encoded. --
 * The sender of this parameter shall set the most significant 4 bits of the octet to 1. -- The receiver of this parameter shall
 * ignore the most significant 4 bits of this octet.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EndUserAddress {

    int getPDPTypeOrganization();

    int getPDPTypeNumber();

    byte[] getPDPAddress();

}
