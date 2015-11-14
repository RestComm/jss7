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
Delivery of erroneous SDUs, octet 6 (see 3GPP TS 23.107 [81])
Bits
3 2 1
In MS to network direction:
0 0 0       Subscribed delivery of erroneous SDUs
In network to MS direction:
0 0 0       Reserved
In MS to network direction and in network to MS direction:
0 0 1       No detect ('-')
0 1 0       Erroneous SDUs are delivered ('yes')
0 1 1       Erroneous SDUs are not delivered ('no')
1 1 1       Reserved
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of this protocol.
The MS shall consider all other values as reserved
</code>
*
* @author sergey vetyutnev
*
*/
public enum ExtQoSSubscribed_DeliveryOfErroneousSdus {
    subscribedDeliveryOfErroneousSdus_Reserved(0), noDetect(1), erroneousSdusAreDelivered_Yes(2), erroneousSdusAreNotDelivered_No(3), reserved(7);

    private int code;

    private ExtQoSSubscribed_DeliveryOfErroneousSdus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtQoSSubscribed_DeliveryOfErroneousSdus getInstance(int code) {
        switch (code) {
        case 0:
            return ExtQoSSubscribed_DeliveryOfErroneousSdus.subscribedDeliveryOfErroneousSdus_Reserved;
        case 1:
            return ExtQoSSubscribed_DeliveryOfErroneousSdus.noDetect;
        case 2:
            return ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes;
        case 3:
            return ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No;
        default:
            return ExtQoSSubscribed_DeliveryOfErroneousSdus.reserved;
        }
    }

}
