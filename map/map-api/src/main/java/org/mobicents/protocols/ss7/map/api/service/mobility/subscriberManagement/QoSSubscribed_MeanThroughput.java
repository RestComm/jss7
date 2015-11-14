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
This field is the binary representation of the Mean Throughput Class (1 to 18; mean throughput class 30 is reserved and
31 is best effort). The corresponding mean throughput to each mean throughput class is indicated.
Bits
5 4 3 2 1
In MS to network direction:
0 0 0 0 0 Subscribed mean throughput
In network to MS direction:
0 0 0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 0 0 1 100 octet/h
0 0 0 1 0 200 octet/h
0 0 0 1 1 500 octet/h
0 0 1 0 0 1 000 octet/h
0 0 1 0 1 2 000 octet/h
0 0 1 1 0 5 000 octet/h
0 0 1 1 1 10 000 octet/h
0 1 0 0 0 20 000 octet/h
0 1 0 0 1 50 000 octet/h
0 1 0 1 0 100 000 octet/h
0 1 0 1 1 200 000 octet/h
0 1 1 0 0 500 000 octet/h
0 1 1 0 1 1 000 000 octet/h
0 1 1 1 0 2 000 000 octet/h
0 1 1 1 1 5 000 000 octet/h
1 0 0 0 0 10 000 000 octet/h
1 0 0 0 1 20 000 000 octet/h
1 0 0 1 0 50 000 000 octet/h
1 1 1 1 0 Reserved
1 1 1 1 1 Best effort
The value Best effort indicates that throughput shall be made available to the MS on a per need and availability basis.
All other values are interpreted as Best effort in this
version of the protocol.
</code>
*
* @author sergey vetyutnev
*
*/
public enum QoSSubscribed_MeanThroughput {
    subscribedMeanThroughput_Reserved(0), _100_octetH(1), _200_octetH(2), _500_octetH(3), _1000_octetH(4), _2000_octetH(5), _5000_octetH(6), _10000_octetH(7), _20000_octetH(
            8), _50000_octetH(9), _100000_octetH(10), _200000_octetH(11), _500000_octetH(12), _1000000_octetH(13), _2000000_octetH(14), _5000000_octetH(15), _10000000_octetH(
            16), _20000000_octetH(17), _50000000_octetH(18), reserved(30), bestEffort(31);

    private int code;

    private QoSSubscribed_MeanThroughput(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static QoSSubscribed_MeanThroughput getInstance(int code) {
        switch (code) {
        case 0:
            return QoSSubscribed_MeanThroughput.subscribedMeanThroughput_Reserved;
        case 1:
            return QoSSubscribed_MeanThroughput._100_octetH;
        case 2:
            return QoSSubscribed_MeanThroughput._200_octetH;
        case 3:
            return QoSSubscribed_MeanThroughput._500_octetH;
        case 4:
            return QoSSubscribed_MeanThroughput._1000_octetH;
        case 5:
            return QoSSubscribed_MeanThroughput._2000_octetH;
        case 6:
            return QoSSubscribed_MeanThroughput._5000_octetH;
        case 7:
            return QoSSubscribed_MeanThroughput._10000_octetH;
        case 8:
            return QoSSubscribed_MeanThroughput._20000_octetH;
        case 9:
            return QoSSubscribed_MeanThroughput._50000_octetH;
        case 10:
            return QoSSubscribed_MeanThroughput._100000_octetH;
        case 11:
            return QoSSubscribed_MeanThroughput._200000_octetH;
        case 12:
            return QoSSubscribed_MeanThroughput._500000_octetH;
        case 13:
            return QoSSubscribed_MeanThroughput._1000000_octetH;
        case 14:
            return QoSSubscribed_MeanThroughput._2000000_octetH;
        case 15:
            return QoSSubscribed_MeanThroughput._5000000_octetH;
        case 16:
            return QoSSubscribed_MeanThroughput._10000000_octetH;
        case 17:
            return QoSSubscribed_MeanThroughput._20000000_octetH;
        case 18:
            return QoSSubscribed_MeanThroughput._50000000_octetH;

        case 31:
            return QoSSubscribed_MeanThroughput.bestEffort;
        default:
            return QoSSubscribed_MeanThroughput.reserved;
        }
    }
}
