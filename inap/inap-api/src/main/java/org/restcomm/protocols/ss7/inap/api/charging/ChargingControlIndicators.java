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

package org.restcomm.protocols.ss7.inap.api.charging;

import java.io.Serializable;

/**
*
<code>
ChargingControlIndicators ::= BIT STRING {
  subscriberCharge (0),
  immediateChangeOfActuallyAppliedTariff (1),
  delayUntilStart (2)
} (SIZE(minChargingControlIndicatorsLen..maxChargingControlIndicatorsLen))

minChargingControlIndicatorsLen INTEGER ::= 1
maxChargingControlIndicatorsLen INTEGER ::= 8

-- Coding of "subscriberCharge":
-- 0 - advice-of-charge: charging information only to be used by the advice of charge service.
-- 1 - subscriber-charge: charging information to be used by the subscriber charging program.
-- Coding of "immediateChangeOfActuallyAppliedTariff":
-- 0 - immediate tariff change without restart
-- 1 - immediate tariff change with restart
-- It is only used to change the actually applied tariff.
-- Coding of 'delayUntilStart':
-- 0 - start tariffing, if it is not already started, without waiting for the 'start' signal
-- 1 - delay start of tariffing up to the receipt of the 'start' signal
</code>
*
* @author sergey vetyutnev
*
*/
public interface ChargingControlIndicators extends Serializable {

    boolean getSubscriberCharge();

    Boolean getImmediateChangeOfActuallyAppliedTariff();

    Boolean getDelayUntilStart();

}
