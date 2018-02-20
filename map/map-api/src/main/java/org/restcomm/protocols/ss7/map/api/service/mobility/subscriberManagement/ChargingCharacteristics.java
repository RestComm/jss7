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
ChargingCharacteristics ::= OCTET STRING (SIZE (2))
-- Octets are coded according to 3GPP TS 32.215.

hargingCharacteristics ::= OCTET STRING (SIZE(2))
--
-- Bit 0-3: Profile Index
-- Bit 4-15: For Behavior

Profile index bits (byte 0, bits 3-0 - P3-P0):
the P3 (N) flag in the Charging Characteristics indicates normal charging,
the P2 (P) flag indicates prepaid charging,
the P1 (F) flag indicates flat rate charging and
the P0 (H) flag indicates charging by hot billing.
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ChargingCharacteristics extends Serializable {

    byte[] getData();

    boolean isNormalCharging();

    boolean isPrepaidCharging();

    boolean isFlatRateChargingCharging();

    boolean isChargingByHotBillingCharging();

}
