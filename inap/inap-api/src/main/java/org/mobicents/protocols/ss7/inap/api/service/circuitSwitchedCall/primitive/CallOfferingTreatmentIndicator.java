/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
*
<code>
callOfferingTreatmentIndicator [3] OCTET STRING (SIZE(1)) OPTIONAL,
-- callOfferingNotAllowed 'xxxx xx01'B,
-- callOfferingAllowed 'xxxx xx10'B
-- network default is Call Offering not allowed
</code>

*
* @author sergey vetyutnev
*
*/
public enum CallOfferingTreatmentIndicator {
    callOfferingNotAllowed(1), callOfferingAllowed(2);

    private int code;

    private CallOfferingTreatmentIndicator(int code) {
        this.code = code;
    }

    public static CallOfferingTreatmentIndicator getInstance(int code) {
        switch (code & 0x03) {
            case 1:
                return CallOfferingTreatmentIndicator.callOfferingNotAllowed;
            case 2:
                return CallOfferingTreatmentIndicator.callOfferingAllowed;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }

}
