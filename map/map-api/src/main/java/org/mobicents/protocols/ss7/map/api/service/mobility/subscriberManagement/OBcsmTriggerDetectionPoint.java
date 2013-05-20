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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 O-BcsmTriggerDetectionPoint ::= ENUMERATED { collectedInfo (2), ..., routeSelectFailure (4) } -- exception handling: -- For
 * O-BcsmCamelTDPData sequences containing this parameter with any -- other value than the ones listed the receiver shall ignore
 * the whole -- O-BcsmCamelTDPDatasequence. -- For O-BcsmCamelTDP-Criteria sequences containing this parameter with any -- other
 * value than the ones listed the receiver shall ignore the whole -- O-BcsmCamelTDP-Criteria sequence.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum OBcsmTriggerDetectionPoint {
    collectedInfo(2), routeSelectFailure(4);

    private int code;

    private OBcsmTriggerDetectionPoint(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static OBcsmTriggerDetectionPoint getInstance(int code) {
        switch (code) {
            case 2:
                return OBcsmTriggerDetectionPoint.collectedInfo;
            case 4:
                return OBcsmTriggerDetectionPoint.routeSelectFailure;
            default:
                return null;
        }
    }
}
