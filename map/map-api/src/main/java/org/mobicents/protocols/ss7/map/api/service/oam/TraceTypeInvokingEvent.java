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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
*
<code>
MSC/BSS Trace Type:
2, 1 - Invoking Event
  0 - MOC, MTC, SMS MO, SMS MT, PDS MO, PDS MT, SS, Location Updates, IMSI attach, IMSI detach
  1 - MOC, MTC, SMS_MO, SMS_MT, PDS MO, PDS MT, SS only
  2 - Location updates, IMSI attach IMSI detach only
  3 - Operator definable

HLR Trace Type:
2, 1 - Invoking Event
  0 - All HLR Interactions
  1 - Spare
  2 - Spare
  3 - Operator definable
</code>
*
* @author sergey vetyutnev
*
*/
public enum TraceTypeInvokingEvent {
    InvokingEvent_0(0), InvokingEvent_1(1), InvokingEvent_2(2), InvokingEvent_3(3);

    private int code;

    private TraceTypeInvokingEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TraceTypeInvokingEvent getInstance(int code) {
        switch (code) {
        case 0:
            return TraceTypeInvokingEvent.InvokingEvent_0;
        case 1:
            return TraceTypeInvokingEvent.InvokingEvent_1;
        case 2:
            return TraceTypeInvokingEvent.InvokingEvent_2;
        case 3:
            return TraceTypeInvokingEvent.InvokingEvent_3;
        default:
            return null;
        }
    }

}
