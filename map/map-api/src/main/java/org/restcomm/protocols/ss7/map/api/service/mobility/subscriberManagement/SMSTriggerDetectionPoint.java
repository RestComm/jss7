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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 SMS-TriggerDetectionPoint ::= ENUMERATED { sms-CollectedInfo (1), ..., sms-DeliveryRequest (2) } -- exception handling: --
 * For SMS-CAMEL-TDP-Data and MT-smsCAMELTDP-Criteria sequences containing this -- parameter with any other value than the ones
 * listed the receiver shall ignore -- the whole sequence. -- -- If this parameter is received with any other value than
 * sms-CollectedInfo -- in an SMS-CAMEL-TDP-Data sequence contained in mo-sms-CSI, then the receiver shall -- ignore the whole
 * SMS-CAMEL-TDP-Data sequence. -- -- If this parameter is received with any other value than sms-DeliveryRequest -- in an
 * SMS-CAMEL-TDP-Data sequence contained in mt-sms-CSI then the receiver shall -- ignore the whole SMS-CAMEL-TDP-Data sequence.
 * -- -- If this parameter is received with any other value than sms-DeliveryRequest -- in an MT-smsCAMELTDP-Criteria sequence
 * then the receiver shall -- ignore the whole MT-smsCAMELTDP-Criteria sequence.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum SMSTriggerDetectionPoint {
    smsCollectedInfo(1), smsDeliveryRequest(2);

    private int code;

    private SMSTriggerDetectionPoint(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static SMSTriggerDetectionPoint getInstance(int code) {
        switch (code) {
            case 1:
                return SMSTriggerDetectionPoint.smsCollectedInfo;
            case 2:
                return SMSTriggerDetectionPoint.smsDeliveryRequest;
            default:
                return null;
        }
    }
}
