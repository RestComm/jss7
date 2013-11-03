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

import java.io.Serializable;

/**
 *
 MT-SMSCause ::= OCTET STRING (SIZE (1)) -- This variable is sent to the gsmSCF for a Short Message delivery failure --
 * notification. -- If the delivery failure is due to RP-ERROR RPDU received from the MS, -- then MT-SMSCause shall be set to
 * the RP-Cause component in the RP-ERROR RPDU. - Refer to 3GPP TS 24.011 [10] for the encoding of RP-Cause values. --
 * Otherwise, if the delivery failure is due to internal failure in the MSC or SGSN -- or time-out from the MS, then MT-SMSCause
 * shall be set to 'Protocol error, -- unspecified', as defined in 3GPP TS 24.011 [10].
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MTSMSCause extends Serializable {

    int getData();

}