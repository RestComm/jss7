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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 ModificationRequestFor-CF-Info ::= SEQUENCE { ss-Code [0] SS-Code, basicService [1] Ext-BasicServiceCode OPTIONAL, ss-Status
 * [2] Ext-SS-Status OPTIONAL, forwardedToNumber [3] AddressString OPTIONAL, forwardedToSubaddress [4] ISDN-SubaddressString
 * OPTIONAL, noReplyConditionTime [5] Ext-NoRepCondTime OPTIONAL, modifyNotificationToCSE [6] ModificationInstruction OPTIONAL,
 * extensionContainer [7] ExtensionContainer OPTIONAL, ...}
 *
 * Ext-NoRepCondTime ::= INTEGER (1..100) -- Only values 5-30 are used. -- Values in the ranges 1-4 and 31-100 are reserved for
 * future use -- If received: -- values 1-4 shall be mapped on to value 5 -- values 31-100 shall be mapped on to value 30
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ModificationRequestForCFInfo extends Serializable {

    SSCode getSsCode();

    ExtBasicServiceCode getBasicService();

    ExtSSStatus getSsStatus();

    AddressString getForwardedToNumber();

    ISDNSubaddressString getForwardedToSubaddress();

    Integer getNoReplyConditionTime();

    ModificationInstruction getModifyNotificationToCSE();

    MAPExtensionContainer getExtensionContainer();

}
