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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 ServiceType ::= SEQUENCE { serviceTypeIdentity LCSServiceTypeID, gmlc-Restriction [0] GMLC-Restriction OPTIONAL,
 * notificationToMSUser [1] NotificationToMSUser OPTIONAL, -- If notificationToMSUser is not received, the default value
 * according to -- 3GPP TS 23.271 shall be assumed. extensionContainer [2] ExtensionContainer OPTIONAL, ... }
 *
 * LCSServiceTypeID ::= INTEGER (0..127) -- the integer values 0-63 are reserved for Standard LCS service types -- the integer
 * values 64-127 are reserved for Non Standard LCS service types
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ServiceType extends Serializable {

    int getServiceTypeIdentity();

    GMLCRestriction getGMLCRestriction();

    NotificationToMSUser getNotificationToMSUser();

    MAPExtensionContainer getExtensionContainer();

}
