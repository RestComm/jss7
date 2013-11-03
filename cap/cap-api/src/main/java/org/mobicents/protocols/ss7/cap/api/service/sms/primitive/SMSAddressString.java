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

package org.mobicents.protocols.ss7.cap.api.service.sms.primitive;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;

/**
 *
 SMS-AddressString ::= AddressString (SIZE (1 .. maxSMS-AddressStringLength))
 -- This data type is used to transport CallingPartyNumber for MT-SMS.
 -- If this data type is used for MO-SMS, then the maximum number of digits shall be 16.
 -- An SMS-AddressString may contain an alphanumeric character string. In this
 -- case, a nature of address indicator '101'B is used, in accordance with
 -- 3GPP TS 23.040 [6]. The address is coded in accordance with the GSM 7-bit
 -- default alphabet definition and the SMS packing rules as specified in
 -- 3GPP TS 23.038 [15] in this case.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SMSAddressString extends AddressString {

}
