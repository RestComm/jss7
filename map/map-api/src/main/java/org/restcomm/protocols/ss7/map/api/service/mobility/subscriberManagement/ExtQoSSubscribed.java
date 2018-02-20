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

import java.io.Serializable;

/**
 *
<code>
Ext-QoS-Subscribed ::= OCTET STRING (SIZE (1..9))
-- OCTET 1:
-- Allocation/Retention Priority (This octet encodes each priority level defined in
-- 23.107 as the binary value of the priority level, declaration in 29.060)
-- Octets 2-9 are coded according to 3GPP TS 24.008 [35] Quality of Service Octets
-- 6-13.

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

Delivery order, octet 6 (see 3GPP TS 23.107 [81])
Bits
5 4 3
In MS to network direction:
0 0     Subscribed delivery order
In network to MS direction:
0 0     Reserved
In MS to network direction and in network to MS direction:
0 1     With delivery order ('yes')
1 0     Without delivery order ('no')
1 1     Reserved

Traffic class, octet 6 (see 3GPP TS 23.107 [81])
Bits
8 7 6
In MS to network direction:
0 0 0       Subscribed traffic class
In network to MS direction:
0 0 0       Reserved
In MS to network direction and in network to MS direction:
0 0 1       Conversational class
0 1 0       Streaming class
0 1 1       Interactive class
1 0 0       Background class
1 1 1       Reserved
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of this protocol.

Maximum SDU size, octet 7 (see 3GPP TS 23.107 [81])
In MS to network direction:
0 0 0 0 0 0 0 0     Subscribed maximum SDU size
1 1 1 1 1 1 1 1     Reserved
In network to MS direction:
0 0 0 0 0 0 0 0     Reserved
1 1 1 1 1 1 1 1     Reserved
In MS to network direction and in network to MS direction:
For values in the range 00000001 to 10010110 the Maximum SDU size value is binary coded in 8 bits, using a granularity of 10 octets, giving a range of values from 10 octets to 1500 octets.
Values above 10010110 are as below:
1 0 0 1 0 1 1 1     1502 octets
1 0 0 1 1 0 0 0     1510 octets
1 0 0 1 1 0 0 1     1520 octets
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of this protocol.
The MS shall consider all other values as reserved.

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

Maximum bit rate for downlink, octet 9 (see 3GPP TS 23.107 [81])
Coding is identical to that of Maximum bit rate for uplink.
If the sending entity wants to indicate a Maximum bit rate for downlink higher than 8640 kbps,
it shall set octet 9 to 11111110, i.e. 8640 kbps, and shall encode the value for the Maximum bit rate in octet 15.
In this version of the protocol, for messages specified in the present document, the sending entity shall not request 0 kbps
for both the Maximum bitrate for downlink and the Maximum bitrate for uplink at the same time. Any entity receiving a request for 0 kbps in both the Maximum bitrate for downlink
and the Maximum bitrate for uplink shall consider that as a syntactical error (see clause 8).

Residual Bit Error Rate (BER), octet 10 (see 3GPP TS 23.107 [81])
Bits
8 7 6 5
In MS to network direction:
0 0 0 0     Subscribed residual BER
In network to MS direction:
0 0 0 0     Reserved
In MS to network direction and in network to MS direction:
The Residual BER value consists of 4 bits. The range is from 5*10-2 to 6*10-8.
0 0 0 1     5*10-2
0 0 1 0     1*10-2
0 0 1 1     5*10-3
0 1 0 0     4*10-3
0 1 0 1     1*10-3
0 1 1 0     1*10-4
0 1 1 1     1*10-5
1 0 0 0     1*10-6
1 0 0 1     6*10-8
1 1 1 1     Reserved
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol.
The network shall return a negotiated value which is explicitly defined in this version of the protocol.
The MS shall consider all other values as reserved.

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

Guaranteed bit rate for uplink, octet 12 (See 3GPP TS 23.107 [81])
Coding is identical to that of Maximum bit rate for uplink.
If the sending entity wants to indicate a Guaranteed bit rate for uplink higher than 8640 kbps,
it shall set octet 12 to 11111110, i.e. 8640 kbps, and shall encode the value for the Guaranteed bit rate in octet 18.
The Guaranteed bit rate for uplink value is ignored if the Traffic Class is Interactive class or Background class,
or Maximum bit rate for uplink is set to 0 kbps.

Guaranteed bit rate for downlink, octet 13(See 3GPP TS 23.107 [81])
Coding is identical to that of Maximum bit rate for uplink.
If the sending entity wants to indicate a Guaranteed bit rate for downlink higher than 8640 kbps,
it shall set octet 13 to 11111110, i.e. 8640 kbps, and shall encode the value for the Guaranteed bit rate in octet 16.
The Guaranteed bit rate for downlink value is ignored if the Traffic Class is Interactive class or Background class,
or Maximum bit rate for downlink is set to 0 kbps.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ExtQoSSubscribed extends Serializable {

    byte[] getData();

    int getAllocationRetentionPriority();

    ExtQoSSubscribed_DeliveryOfErroneousSdus getDeliveryOfErroneousSdus();

    ExtQoSSubscribed_DeliveryOrder getDeliveryOrder();

    ExtQoSSubscribed_TrafficClass getTrafficClass();

    ExtQoSSubscribed_MaximumSduSize getMaximumSduSize();

    ExtQoSSubscribed_BitRate getMaximumBitRateForUplink();

    ExtQoSSubscribed_BitRate getMaximumBitRateForDownlink();

    ExtQoSSubscribed_ResidualBER getResidualBER();

    ExtQoSSubscribed_SduErrorRatio getSduErrorRatio();

    ExtQoSSubscribed_TrafficHandlingPriority getTrafficHandlingPriority();

    ExtQoSSubscribed_TransferDelay getTransferDelay();

    ExtQoSSubscribed_BitRate getGuaranteedBitRateForUplink();

    ExtQoSSubscribed_BitRate getGuaranteedBitRateForDownlink();

}
