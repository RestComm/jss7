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

import org.restcomm.protocols.ss7.inap.api.primitives.INAPExtensions;

/**
*
<code>
AddOnChargingInformation ::= SEQUENCE {
  chargingControlIndicators [00] ChargingControlIndicators ,
  addOncharge [01] CHOICE {
    addOnChargeCurrency [00] CurrencyFactorScale ,
    addOnChargePulse    [01] PulseUnits
  },
  extensions                [02] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
  originationIdentification [03] ChargingReferenceIdentification,
  destinationIdentification [04] ChargingReferenceIdentification OPTIONAL,
  currency                  [05] Currency
}
-- This message is used to add an amount of charge for the call and does not alter the current tariff.
-- The destinationIdentification is not available in an initial AddOnChargingInformation, in all subsequent ones it is included, see
-- "Handling of Identifiers".
-- In the message the
-- add-on charge has either the pulse or currency format.
</code>
*
* @author sergey vetyutnev
*
*/
public interface AddOnChargingInformation extends Serializable {

    ChargingControlIndicators getChargingControlIndicators();

    AddOncharge getAddOncharge();

    INAPExtensions getExtensions();

    ChargingReferenceIdentification getOriginationIdentification();

    ChargingReferenceIdentification getDestinationIdentification();

    Currency getCurrency();

}
