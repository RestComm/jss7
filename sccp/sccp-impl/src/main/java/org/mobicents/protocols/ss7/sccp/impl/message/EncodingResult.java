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

package org.mobicents.protocols.ss7.sccp.impl.message;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum EncodingResult {

    // Successed encoding, data available for transfer
    Success,
    // Sccp error, returnCause should be returned or message discarded
    ReturnFailure,
    // Exception: data==null or has zero length
    DataMissed,
    // Exception: data length exceeds maximum possible safe length (2560)
    DataMaxLengthExceeded,
    // Exception: CalledPartyAddress is missed
    CalledPartyAddressMissing,
    // Exception: CallingPartyAddress is missed
    CallingPartyAddressMissing,
    // Exception: ProtocolClass is missed
    ProtocolClassMissing;

}
