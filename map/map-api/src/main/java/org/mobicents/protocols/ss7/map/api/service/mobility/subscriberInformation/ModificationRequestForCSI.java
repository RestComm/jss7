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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 ModificationRequestFor-CSI ::= SEQUENCE { requestedCamel-SubscriptionInfo [0] RequestedCAMEL-SubscriptionInfo,
 * modifyNotificationToCSE [1] ModificationInstruction OPTIONAL, modifyCSI-State [2] ModificationInstruction OPTIONAL,
 * extensionContainer [3] ExtensionContainer OPTIONAL, ..., additionalRequestedCAMEL-SubscriptionInfo [4]
 * AdditionalRequestedCAMEL-SubscriptionInfo OPTIONAL } -- requestedCamel-SubscriptionInfo shall be discarded if --
 * additionalRequestedCAMEL-SubscriptionInfo is received
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ModificationRequestForCSI extends Serializable {

    RequestedCAMELSubscriptionInfo getRequestedCamelSubscriptionInfo();

    ModificationInstruction getModifyNotificationToCSE();

    ModificationInstruction getModifyCSIState();

    MAPExtensionContainer getExtensionContainer();

    AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo();

}
