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
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 LCS-PrivacyClass ::= SEQUENCE { ss-Code SS-Code, ss-Status Ext-SS-Status, notificationToMSUser [0] NotificationToMSUser
 * OPTIONAL, -- notificationToMSUser may be sent only for SS-codes callSessionRelated -- and callSessionUnrelated. If not
 * received for SS-codes callSessionRelated -- and callSessionUnrelated, -- the default values according to 3GPP TS 23.271 shall
 * be assumed. externalClientList [1] ExternalClientList OPTIONAL, -- externalClientList may be sent only for SS-code
 * callSessionUnrelated to a -- visited node that does not support LCS Release 4 or later versions. -- externalClientList may be
 * sent only for SS-codes callSessionUnrelated and -- callSessionRelated to a visited node that supports LCS Release 4 or later
 * versions. plmnClientList [2] PLMNClientList OPTIONAL, -- plmnClientList may be sent only for SS-code plmnoperator.
 * extensionContainer [3] ExtensionContainer OPTIONAL, ..., ext-externalClientList [4] Ext-ExternalClientList OPTIONAL, --
 * Ext-externalClientList may be sent only if the visited node supports LCS Release 4 or -- later versions, the user did specify
 * more than 5 clients, and White Book SCCP is used. serviceTypeList [5] ServiceTypeList OPTIONAL -- serviceTypeList may be sent
 * only for SS-code serviceType and if the visited node -- supports LCS Release 5 or later versions. -- -- if segmentation is
 * used, the complete LCS-PrivacyClass shall be sent in one segment }
 *
 * ExternalClientList ::= SEQUENCE SIZE (0..5) OF ExternalClient
 *
 * PLMNClientList ::= SEQUENCE SIZE (1..5) OF LCSClientInternalID
 *
 * Ext-ExternalClientList ::= SEQUENCE SIZE (1..35) OF ExternalClient
 *
 * ServiceTypeList ::= SEQUENCE SIZE (1..32) OF ServiceType
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LCSPrivacyClass extends Serializable {

    SSCode getSsCode();

    ExtSSStatus getSsStatus();

    NotificationToMSUser getNotificationToMSUser();

    ArrayList<ExternalClient> getExternalClientList();

    ArrayList<LCSClientInternalID> getPLMNClientList();

    MAPExtensionContainer getExtensionContainer();

    ArrayList<ExternalClient> getExtExternalClientList();

    ArrayList<ServiceType> getServiceTypeList();

}
