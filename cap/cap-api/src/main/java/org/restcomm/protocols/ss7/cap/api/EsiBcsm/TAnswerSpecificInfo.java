/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.EsiBcsm;

import java.io.Serializable;

import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

/**
 *
<code>
tAnswerSpecificInfo [10] SEQUENCE {
  destinationAddress      [50] CalledPartyNumber {bound} OPTIONAL,
  or-Call                 [51] NULL OPTIONAL,
  forwardedCall           [52] NULL OPTIONAL,
  chargeIndicator         [53] ChargeIndicator OPTIONAL,
  ext-basicServiceCode    [54] Ext-BasicServiceCode OPTIONAL,
  ext-basicServiceCode2   [55] Ext-BasicServiceCode OPTIONAL,
  ...
},
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TAnswerSpecificInfo extends Serializable {

    CalledPartyNumberCap getDestinationAddress();

    boolean getOrCall();

    boolean getForwardedCall();

    ChargeIndicator getChargeIndicator();

    ExtBasicServiceCode getExtBasicServiceCode();

    ExtBasicServiceCode getExtBasicServiceCode2();

}