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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
*
callCompletionTreatmentIndicator [2] OCTET STRING (SIZE(1)) OPTIONAL
-- acceptCallCompletionServiceRequest 'xxxx xx01'B,
-- rejectCallCompletionServiceRequest 'xxxx xx10'B
-- network default is accept call completion service request
*
* @author sergey vetyutnev
*
*/
public enum CallCompletionTreatmentIndicator {

    acceptCallCompletionServiceRequest(1), rejectCallCompletionServiceRequest(2);

    private int code;

    private CallCompletionTreatmentIndicator(int code) {
        this.code = code;
    }

    public static CallCompletionTreatmentIndicator getInstance(int code) {
        switch (code & 0x03) {
            case 1:
                return CallCompletionTreatmentIndicator.acceptCallCompletionServiceRequest;
            case 2:
                return CallCompletionTreatmentIndicator.rejectCallCompletionServiceRequest;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }

}
