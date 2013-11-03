/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.inap.api.primitives;

import java.io.Serializable;

import org.mobicents.protocols.ss7.inap.api.isup.BearerCap;
import org.mobicents.protocols.ss7.inap.api.isup.TmrInap;

/**
*
BearerCapability {PARAMETERS-BOUND : bound} ::= CHOICE {
    bearerCap  [0] OCTET STRING (SIZE(2..bound.&maxBearerCapabilityLength)),
    tmr        [1] OCTET STRING (SIZE(1))
}
-- Indicates the type of bearer capability connection to the user. For bearerCapability, either
-- DSS1 (EN 300 403-1) or the ISUP User Service Information (ITU-T Recommendation Q.763)
-- encoding can be used. Refer
-- to the ITU-T Recommendation Q.763 Transmission Medium Requirement parameter for tmr encoding.

bearerCapability:
This parameter indicates the type of the bearer capability connection or the transmission medium requirements to
the user. See IN CS2 Signalling Interworking Requirements [48].
It is a network option to select one of the two parameters to be used:

- bearerCap:
This parameter contains the value of the DSS1 Bearer Capability parameter (EN 300 403-1 [10]) in case the
SSF is at local exchange level or the value of the ISUP User Service Information parameter
(ITU-T Recommendation Q.763 [20]) in case the SSF is at transit exchange level.
ETSI EN 301 140-1 V1.3.4 (1999-06)

- tmr:
The tmr is encoded as the Transmission Medium Requirement parameter of the ISUP according to
ITU-T Recommendation Q.763 [20].

*
* @author sergey vetyutnev
*
*/
public interface BearerCapability extends Serializable {

    BearerCap getBearerCap();

    TmrInap getTmr();

}
