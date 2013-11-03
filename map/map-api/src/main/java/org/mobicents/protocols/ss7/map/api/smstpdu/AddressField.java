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

import java.io.OutputStream;
import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 * Each address field of the SM-TL consists of the following sub-fields: An Address-Length field of one octet, a Type-of-Address
 * field of one octet, and one Address-Value field of variable length
 *
The Type-of-Address field format is as follows:
Bit 7 = 1 Type-of-number:
Bits 6 5 4 0 0 0 Unknown
1) 0 0 1 International number
2) 0 1 0 National number
3) 0 1 1 Network specific number
4) 1 0 0 Subscriber number
5) 1 0 1 Alphanumeric, (coded according to 3GPP TS 23.038 [9] GSM 7-bit default alphabet)
   1 1 0 Abbreviated number
   1 1 1 Reserved for extension

Numbering-plan-identification
Bits 3 2 1 0
0 0 0 0 Unknown
0 0 0 1 ISDN/telephone numbering plan (E.164 [17]/E.163[18])
0 0 1 1 Data numbering plan (X.121)
0 1 0 0 Telex numbering plan
0 1 0 1 Service Centre Specific plan 1)
0 1 1 0 Service Centre Specific plan 1)
1 0 0 0 National numbering plan
1 0 0 1 Private numbering plan
1 0 1 0 ERMES numbering plan (ETSI DE/PS 3 01-3)
1 1 1 1 Reserved for extension All other values are reserved.
 *
 * @author sergey vetyutnev
 *
 */
public interface AddressField extends Serializable {

    TypeOfNumber getTypeOfNumber();

    NumberingPlanIdentification getNumberingPlanIdentification();

    String getAddressValue();

    void encodeData(OutputStream stm) throws MAPException;

}