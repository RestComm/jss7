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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;

/**
 *
 MAP V3:
 *
 * noteSubscriberDataModified OPERATION ::= { --Timer m ARGUMENT NoteSubscriberDataModifiedArg RESULT
 * NoteSubscriberDataModifiedRes -- optional ERRORS { dataMissing | unexpectedDataValue | unknownSubscriber} CODE local:5 }
 *
 * NoteSubscriberDataModifiedArg ::= SEQUENCE { imsi IMSI, msisdn ISDN-AddressString, forwardingInfoFor-CSE [0]
 * Ext-ForwardingInfoFor-CSE OPTIONAL, callBarringInfoFor-CSE [1] Ext-CallBarringInfoFor-CSE OPTIONAL, odb-Info [2] ODB-Info
 * OPTIONAL, camel-SubscriptionInfo [3] CAMEL-SubscriptionInfo OPTIONAL, allInformationSent [4] NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL, ..., ue-reachable [5] ServingNode OPTIONAL, csg-SubscriptionDataList [6]
 * CSG-SubscriptionDataList OPTIONAL, cw-Data [7] CallWaitingData OPTIONAL, ch-Data [8] CallHoldData OPTIONAL, clip-Data [9]
 * ClipData OPTIONAL, clir-Data [10] ClirData OPTIONAL, ect-data [11] EctData OPTIONAL }
 *
 * CSG-SubscriptionDataList ::= SEQUENCE SIZE (1..50) OF CSG-SubscriptionData
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface NoteSubscriberDataModifiedRequest extends MobilityMessage {

    IMSI getImsi();

    ISDNAddressString getMsisdn();

    ExtForwardingInfoForCSE getForwardingInfoForCSE();

    ExtCallBarringInfoForCSE getCallBarringInfoForCSE();

    ODBInfo getOdbInfo();

    CAMELSubscriptionInfo getCamelSubscriptionInfo();

    boolean getAllInformationSent();

    MAPExtensionContainer getExtensionContainer();

    ServingNode getUeReachable();

    ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList();

    CallWaitingData getCwData();

    CallHoldData getChData();

    ClipData getClipData();

    ClirData getClirData();

    EctData getEctData();

}
