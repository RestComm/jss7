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

package org.mobicents.protocols.ss7.cap.api.service.sms.primitive;

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
public interface MTSMSCause {

    int getData();

}
