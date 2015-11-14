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

import java.io.Serializable;

/**
 *
<code>
QoS-Subscribed ::= OCTET STRING (SIZE (3))
-- Octets are coded according to TS 3GPP TS 24.008 [35] Quality of Service Octets
-- 3-5.

Reliability class, octet 3 (see 3GPP TS 23.107)
Bits
3 2 1
In MS to network direction:
0 0 0 Subscribed reliability class
In network to MS direction:
0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 1 Unused. If received, it shall be interpreted as '010' (Note)
0 1 0 Unacknowledged GTP; Acknowledged LLC and RLC, Protected data
0 1 1 Unacknowledged GTP and LLC; Acknowledged RLC, Protected data
1 0 0 Unacknowledged GTP, LLC, and RLC, Protected data
1 0 1 Unacknowledged GTP, LLC, and RLC, Unprotected data
1 1 1 Reserved
All other values are interpreted as Unacknowledged GTP and LLC; Acknowledged RLC, Protected data in this version of
the protocol.
NOTE: this value was allocated in earlier versions of the protocol.
Delay class, octet 3 (see 3GPP TS 22.060 and 3GPP TS 23.107)
Bits
6 5 4
In MS to network direction:
0 0 0 Subscribed delay class
In network to MS direction:
0 0 0 Reserved
In MS to network direction and in network to MS direction:
0 0 1 Delay class 1
0 1 0 Delay class 2
0 1 1 Delay class 3
1 0 0 Delay class 4 (best effort)
1 1 1 Reserved
All other values are interpreted as Delay class 4 (best effort) in this version
of the protocol.
Bit 7 and 8 of octet 3 are spare and shall be coded all 0.

Precedence class, octet 4 (see 3GPP TS 23.107)
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
Bit 4 of octet 4 is spare and shall be coded as 0.
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

Mean throughput, octet 5 (see 3GPP TS 23.107)
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
Bits 8 to 6 of octet 5 are spare and shall be coded all 0.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface QoSSubscribed extends Serializable {

    byte[] getData();

    QoSSubscribed_ReliabilityClass getReliabilityClass();

    QoSSubscribed_DelayClass getDelayClass();

    QoSSubscribed_PrecedenceClass getPrecedenceClass();

    QoSSubscribed_PeakThroughput getPeakThroughput();

    QoSSubscribed_MeanThroughput getMeanThroughput();

}
