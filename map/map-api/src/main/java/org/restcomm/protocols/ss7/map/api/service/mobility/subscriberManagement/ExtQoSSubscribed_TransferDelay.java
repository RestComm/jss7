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
Transfer delay, octet 11 (See 3GPP TS 23.107 [81])
Bits
8 7 6 5 4 3
In MS to network direction:
0 0 0 0 0 0     Subscribed transfer delay
In network to MS direction:
0 0 0 0 0 0     Reserved
In MS to network direction and in network to MS direction:
0 0 0 0 0 1     The Transfer delay is binary coded in 6 bits, using a granularity of 10 ms
0 0 1 1 1 1     giving a range of values from 10 ms to 150 ms in 10 ms increments
0 1 0 0 0 0     The transfer delay is 200 ms + ((the binary coded value in 6 bits  010000) * 50 ms)
0 1 1 1 1 1     giving a range of values from 200 ms to 950 ms in 50ms increments
1 0 0 0 0 0     The transfer delay is 1000 ms + ((the binary coded value in 6 bits  100000) * 100 ms)
1 1 1 1 1 0     giving a range of values from 1000 ms to 4000 ms in 100ms increments
1 1 1 1 1 1     Reserved
The Transfer delay value is ignored if the Traffic Class is Interactive class or Background class.
</code>
*
* @author sergey vetyutnev
*
*/
public interface ExtQoSSubscribed_TransferDelay {

    int getSourceData();

    /**
     * @return TransferDelay (ms) value or 0 when source==0 or not specified in
     *         23.107
     */
    int getTransferDelay();

}
