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
SDU error ratio, octet 10 (see 3GPP TS 23.107 [81])
Bits
4 3 2 1
In MS to network direction:
0 0 0 0     Subscribed SDU error ratio
In network to MS direction:
0 0 0 0     Reserved
In MS to network direction and in network to MS direction:
The SDU error ratio value consists of 4 bits. The range is is from 1*10-1 to 1*10-6.
0 0 0 1     1*10-2
0 0 1 0     7*10-3
0 0 1 1     1*10-3
0 1 0 0     1*10-4
0 1 0 1     1*10-5
0 1 1 0     1*10-6
0 1 1 1     1*10-1
1 1 1 1     Reserved
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of the protocol.
The MS shall consider all other values as reserved.
</code>
*
* @author sergey vetyutnev
*
*/
public enum ExtQoSSubscribed_SduErrorRatio {
    subscribedSduErrorRatio_Reserved(0), _1_10_minus_2(1), _7_10_minus_3(2), _1_10_minus_3(3), _1_10_minus_4(4), _1_10_minus_5(5), _1_10_minus_6(6), _1_10_minus_1(
            7), reserved(15);

    private int code;

    private ExtQoSSubscribed_SduErrorRatio(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_SduErrorRatio getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved;
        case 1:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_2;
        case 2:
            return ExtQoSSubscribed_SduErrorRatio._7_10_minus_3;
        case 3:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_3;
        case 4:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_4;
        case 5:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_5;
        case 6:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_6;
        case 7:
            return ExtQoSSubscribed_SduErrorRatio._1_10_minus_1;
        default:
            return ExtQoSSubscribed_SduErrorRatio.reserved;
        }
    }

}
