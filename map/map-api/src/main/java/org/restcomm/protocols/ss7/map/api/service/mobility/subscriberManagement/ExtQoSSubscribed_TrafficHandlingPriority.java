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
<code>
Traffic handling priority, octet 11 (see 3GPP TS 23.107 [81])
Bits
2 1
In MS to network direction:
0 0     Subscribed traffic handling priority
In network to MS direction:
0 0     Reserved
In MS to network direction and in network to MS direction:
0 1     Priority level 1
1 0     Priority level 2
1 1     Priority level 3
The Traffic handling priority value is ignored if the Traffic Class is Conversational class, Streaming class or Background class.
</code>
*
* @author sergey vetyutnev
*
*/
public enum ExtQoSSubscribed_TrafficHandlingPriority {
    subscribedTrafficHandlingPriority_Reserved(0), priorityLevel_1(1), priorityLevel_2(2), priorityLevel_3(3);

    private int code;

    private ExtQoSSubscribed_TrafficHandlingPriority(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_TrafficHandlingPriority getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_TrafficHandlingPriority.subscribedTrafficHandlingPriority_Reserved;
        case 1:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1;
        case 2:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_2;
        case 3:
            return ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3;
        default:
            return null;
        }
    }

}
