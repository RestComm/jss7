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
import java.util.ArrayList;

/**
*
<code>
TariffPulseFormat ::= SEQUENCE {
  communicationChargeSequencePulse [00] SEQUENCE SIZE(minCommunicationTariffNum.. maxCommunicationTariffNum)
    OF CommunicationChargePulse OPTIONAL ,
  tariffControlIndicators          [01] BIT STRING { non-cyclicTariff (0) } (SIZE(minTariffIndicatorsLen..maxTariffIndicatorsLen)) ,
  callAttemptChargePulse           [02] PulseUnits OPTIONAL ,
  callSetupChargePulse             [03] PulseUnits OPTIONAL
}
-- The communication charges are meter-pulse units, which are to be applied per charge unit time interval.
-- The call attempt pulse units are to be charged only for unsuccessful calls.
-- The call set-up pulse units are to be charged once at start of charging.
</code>
*
* @author sergey vetyutnev
*
*/
public interface TariffPulseFormat extends Serializable {

    ArrayList<CommunicationChargePulse> getCommunicationChargeSequencePulse();

    TariffControlIndicators getTariffControlIndicators();

    PulseUnits getCallAttemptChargePulse();

    PulseUnits getCallSetupChargePulse();

}
