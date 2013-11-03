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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
 ForwardingFeature ::= SEQUENCE { basicService BasicServiceCode OPTIONAL, ss-Status [4] SS-Status OPTIONAL, forwardedToNumber
 * [5] ISDN-AddressString OPTIONAL, forwardedToSubaddress [8] ISDN-SubaddressString OPTIONAL, forwardingOptions [6]
 * ForwardingOptions OPTIONAL, noReplyConditionTime [7] NoReplyConditionTime OPTIONAL, ..., longForwardedToNumber [9]
 * FTN-AddressString OPTIONAL }
 *
 * NoReplyConditionTime ::= INTEGER (5..30)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ForwardingFeature extends Serializable {

    BasicServiceCode getBasicService();

    SSStatus getSsStatus();

    ISDNAddressString getForwardedToNumber();

    ISDNAddressString getForwardedToSubaddress();

    ForwardingOptions getForwardingOptions();

    Integer getNoReplyConditionTime();

    FTNAddressString getLongForwardedToNumber();

}
