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
Peak throughput, octet 4 (see 3GPP TS 23.107)
This field is the binary representation of the Peak Throughput Class (1 to 9). The corresponding peak throughput to each
peak throughput class is indicated.
Bits
8 7 6 5
In MS to network direction:
0 0 0 0 Subscribed peak throughput
In network to MS direction:
0 0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 0 1 Up to 1 000 octet/s
0 0 1 0 Up to 2 000 octet/s
0 0 1 1 Up to 4 000 octet/s
0 1 0 0 Up to 8 000 octet/s
0 1 0 1 Up to 16 000 octet/s
0 1 1 0 Up to 32 000 octet/s
0 1 1 1 Up to 64 000 octet/s
1 0 0 0 Up to 128 000 octet/s
1 0 0 1 Up to 256 000 octet/s
1 1 1 1 Reserved
All other values are interpreted as Up to 1 000 octet/s in this
version of the protocol.
</code>
*
* @author sergey vetyutnev
*
*/
public enum QoSSubscribed_PeakThroughput {
    subscribedPeakThroughput_Reserved(0), upTo_1000_octetS(1), upTo_2000_octetS(2), upTo_4000_octetS(3), upTo_8000_octetS(4), upTo_16000_octetS(5), upTo_32000_octetS(
            6), upTo_64000_octetS(7), upTo_128000_octetS(8), upTo_256000_octetS(9), reserved(15);

    private int code;

    private QoSSubscribed_PeakThroughput(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static QoSSubscribed_PeakThroughput getInstance(int code) {
        switch (code) {
        case 0:
            return QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved;
        case 1:
            return QoSSubscribed_PeakThroughput.upTo_1000_octetS;
        case 2:
            return QoSSubscribed_PeakThroughput.upTo_2000_octetS;
        case 3:
            return QoSSubscribed_PeakThroughput.upTo_4000_octetS;
        case 4:
            return QoSSubscribed_PeakThroughput.upTo_8000_octetS;
        case 5:
            return QoSSubscribed_PeakThroughput.upTo_16000_octetS;
        case 6:
            return QoSSubscribed_PeakThroughput.upTo_32000_octetS;
        case 7:
            return QoSSubscribed_PeakThroughput.upTo_64000_octetS;
        case 8:
            return QoSSubscribed_PeakThroughput.upTo_128000_octetS;
        case 9:
            return QoSSubscribed_PeakThroughput.upTo_256000_octetS;

        default:
            return QoSSubscribed_PeakThroughput.reserved;
        }
    }
}
