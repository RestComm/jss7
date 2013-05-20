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
 EventTypeSMS ::= ENUMERATED { sms-CollectedInfo (1), o-smsFailure (2), o-smsSubmission (3), sms-DeliveryRequested (11),
 * t-smsFailure (12), t-smsDelivery (13) } -- Values sms-CollectedInfo and sms-DeliveryRequested may be used for TDPs only.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum EventTypeSMS {
    smsCollectedInfo(1), oSmsFailure(2), oSmsSubmission(3), smsDeliveryRequested(11), tSmsFailure(12), tSmsDelivery(13);

    private int code;

    private EventTypeSMS(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static EventTypeSMS getInstance(int code) {
        switch (code) {
            case 1:
                return EventTypeSMS.smsCollectedInfo;
            case 2:
                return EventTypeSMS.oSmsFailure;
            case 3:
                return EventTypeSMS.oSmsSubmission;
            case 11:
                return EventTypeSMS.smsDeliveryRequested;
            case 12:
                return EventTypeSMS.tSmsFailure;
            case 13:
                return EventTypeSMS.tSmsDelivery;
            default:
                return null;
        }
    }

}
