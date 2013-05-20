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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

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
public interface ForwardingFeature {

    BasicServiceCode getBasicService();

    SSStatus getSsStatus();

    ISDNAddressString getForwardedToNumber();

    ISDNAddressString getForwardedToSubaddress();

    ForwardingOptions getForwardingOptions();

    Integer getNoReplyConditionTime();

    FTNAddressString getLongForwardedToNumber();

}
