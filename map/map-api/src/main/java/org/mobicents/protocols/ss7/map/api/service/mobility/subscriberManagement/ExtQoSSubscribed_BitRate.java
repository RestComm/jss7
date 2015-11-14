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
Maximum bit rate for uplink, octet 8
Bits
8 7 6 5 4 3 2 1
In MS to network direction:
0 0 0 0 0 0 0 0 Subscribed maximum bit rate for uplink
In network to MS direction:
0 0 0 0 0 0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 0 0 0 0 0 1     The maximum bit rate is binary coded in 8 bits, using a granularity of 1 kbps
0 0 1 1 1 1 1 1 giving a range of values from 1 kbps to 63 kbps in 1 kbps increments.
0 1 0 0 0 0 0 0     The maximum bit rate is 64 kbps + ((the binary coded value in 8 bits 01000000) * 8 kbps)
0 1 1 1 1 1 1 1 giving a range of values from 64 kbps to 568 kbps in 8 kbps increments.
1 0 0 0 0 0 0 0     The maximum bit rate is 576 kbps + ((the binary coded value in 8 bits 10000000) * 64 kbps)
1 1 1 1 1 1 1 0 giving a range of values from 576 kbps to 8640 kbps in 64 kbps increments.
1 1 1 1 1 1 1 1 0kbps
If the sending entity wants to indicate a Maximum bit rate for uplink higher than 8640 kbps, it shall set octet 8 to 11111110,
i.e. 8640 kbps, and shall encode the value for the Maximum bit rate in octet 17.
</code>
*
* @author sergey vetyutnev
*
*/
public interface ExtQoSSubscribed_BitRate {

    int getSourceData();

    /**
     * @return BitRate (kbit/s) value or 0 when source==0 or not specified in
     *         23.107
     */
    int getBitRate();

}
