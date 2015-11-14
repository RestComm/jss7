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
<code>
Bits
3 2 1
In MS to network direction:
0 0 0 Subscribed precedence
In network to MS direction:
0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 1 High priority
0 1 0 Normal priority
0 1 1 Low priority
1 1 1 Reserved
All other values are interpreted as Normal priority in this version of the protocol.
</code>
*
* @author sergey vetyutnev
*
*/
public enum QoSSubscribed_PrecedenceClass {
    subscribedPrecedence_Reserved(0), highPriority(1), normalPriority(2), lowPriority(3), reserved(7);

    private int code;

    private QoSSubscribed_PrecedenceClass(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static QoSSubscribed_PrecedenceClass getInstance(int code) {
        switch (code) {
        case 0:
            return QoSSubscribed_PrecedenceClass.subscribedPrecedence_Reserved;
        case 1:
            return QoSSubscribed_PrecedenceClass.highPriority;
        case 2:
            return QoSSubscribed_PrecedenceClass.normalPriority;
        case 3:
            return QoSSubscribed_PrecedenceClass.lowPriority;
        default:
            return QoSSubscribed_PrecedenceClass.reserved;
        }
    }

}
