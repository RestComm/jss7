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
 E-UTRAN-CGI ::= OCTET STRING (SIZE (7)) -- Octets are coded as described in 3GPP TS 29.118.
 *
 * ==E-UTRAN Cell Global Identity: 3GPP TS 29.118: The E-UTRAN Cell Global Identity information element indicates the UE's
 * current E-UTRAN Cell Global Identity. The coding of the E-UTRAN Cell Global Identity value is according to ECGI field
 * information element as specified in subclause 8.21.5 of 3GPP TS 29.274 [17A]
 *
 * ETSI TS 129 274: ECGI field The coding of ECGI (E-UTRAN Cell Global Identifier) is depicted in Figure 8.21.5-1. Only zero or
 * one ECGI field shall be present in ULI IE. Bits Octets 8 7 6 5 4 3 2 1 e MCC digit 2 MCC digit 1 e+1 MNC digit 3 MCC digit 3
 * e+2 MNC digit 2 MNC digit 1 e+3 Spare ECI e+4 to e+6 ECI (E-UTRAN Cell Identifier) The E-UTRAN Cell Identifier (ECI) consists
 * of 28 bits. The ECI field shall start with Bit 4 of octet e+3, which is the most significant bit. Bit 1 of Octet e+6 is the
 * least significant bit. The coding of the E-UTRAN cell identifier is the responsibility of each administration. Coding using
 * full hexadecimal representation shall be used.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EUtranCgi extends Serializable {

    byte[] getData();

    // TODO: add implementing of internal structure

}
