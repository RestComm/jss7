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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

/**
 *
 TA-Id ::= OCTET STRING (SIZE (5)) -- Octets are coded as described in 3GPP TS 29.118.
 *
 * ==Tracking Area Identity: 3GPP TS 29.118: Octets 3 to 7 contain the value part of the Tracking Area Identity information
 * element defined in 3GPP TS 24.301 [14] (starting with octet 2, i.e. not including 3GPP TS 24.301 IEI)
 *
 * 3GPP TS 24.301: 8 7 6 5 4 3 2 1 MCC digit 2 MCC digit 1 octet 2 MNC digit 3 MCC digit 3 octet 3 MNC digit 2 MNC digit 1 octet
 * 4 TAC octet 5 TAC (continued) octet 6
 *
 * MCC, Mobile country code (octet 2 and 3) The MCC field is coded as in ITU-T Rec. E212 [30], annex A. If the TAI is deleted
 * the MCC and MNC shall take the value from the deleted TAI. In abnormal cases, the MCC stored in the UE can contain elements
 * not in the set {0, 1 ... 9}. In such cases the UE should transmit the stored values using full hexadecimal encoding. When
 * receiving such an MCC, the network shall treat the TAI as deleted. MNC, Mobile network code (octet 3 bits 5 to 8, octet 4)
 * The coding of this field is the responsibility of each administration, but BCD coding shall be used. The MNC shall consist of
 * 2 or 3 digits. For PCS 1900 for NA, Federal regulation mandates that a 3-digit MNC shall be used. However a network operator
 * may decide to use only two digits in the MNC in the TAI over the radio interface. In this case, bits 5 to 8 of octet 3 shall
 * be coded as "1111". Mobile equipment shall accept a TAI coded in such a way. In abnormal cases, the MNC stored in the UE can
 * have: - digit 1 or 2 not in the set {0, 1 ... 9}, or - digit 3 not in the set {0, 1 ... 9, F} hex. In such cases the UE shall
 * transmit the stored values using full hexadecimal encoding. When receiving such an MNC, the network shall treat the TAI as
 * deleted. The same handling shall apply for the network, if a 3-digit MNC is sent by the UE to a network using only a 2-digit
 * MNC. TAC, Tracking area code (octet 5 and 6) In the TAC field bit 8 of octet 5 is the most significant bit and bit 1 of octet
 * 6 the least significant bit. The coding of the tracking area code is the responsibility of each administration except that
 * two values are used to mark the TAC, and hence the TAI, as deleted. Coding using full hexadecimal representation may be used.
 * The tracking area code consists of 2 octets. If a TAI has to be deleted then all bits of the tracking area code shall be set
 * to one with the exception of the least significant bit which shall be set to zero. If a USIM is inserted in a mobile
 * equipment with the tracking area code containing all zeros, then the mobile equipment shall recognise this TAC as part of a
 * deleted TAI.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TAId extends Serializable {

    byte[] getData();

    // TODO: add implementing of internal structure

}
