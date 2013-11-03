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

package org.mobicents.protocols.ss7.cap.api.EsiBcsm;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

/**
 *
 oAnswerSpecificInfo [5] SEQUENCE { destinationAddress [50] CalledPartyNumber {bound} OPTIONAL, or-Call [51] NULL OPTIONAL,
 * forwardedCall [52] NULL OPTIONAL, chargeIndicator [53] ChargeIndicator OPTIONAL, ext-basicServiceCode [54]
 * Ext-BasicServiceCode OPTIONAL, ext-basicServiceCode2 [55] Ext-BasicServiceCode OPTIONAL, ... },
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OAnswerSpecificInfo extends Serializable {

    CalledPartyNumberCap getDestinationAddress();

    boolean getOrCall();

    boolean getForwardedCall();

    ChargeIndicator getChargeIndicator();

    ExtBasicServiceCode getExtBasicServiceCode();

    ExtBasicServiceCode getExtBasicServiceCode2();

}
