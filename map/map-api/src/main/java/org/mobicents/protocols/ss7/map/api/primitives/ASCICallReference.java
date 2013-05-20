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

package org.mobicents.protocols.ss7.map.api.primitives;

/**
 * ASCI-CallReference ::= TBCD-STRING (SIZE (1..8)) -- digits of VGCS/VBS-area,Group-ID are concatenated in this order if there
 * is a -- VGCS/VBS-area.
 *
 * TBCD-STRING ::= OCTET STRING -- This type (Telephony Binary Coded Decimal String) is used to -- represent several digits from
 * 0 through 9, *, #, a, b, c, two -- digits per octet, each digit encoded 0000 to 1001 (0 to 9), -- 1010 (*), 1011 (#), 1100
 * (a), 1101 (b) or 1110 (c); 1111 used -- as filler when there is an odd number of digits.
 *
 * -- bits 8765 of octet n encoding digit 2n -- bits 4321 of octet n encoding digit 2(n-1) +1
 *
 * @author sergey vetyutnev
 *
 */
public interface ASCICallReference {

    String getData();

}
