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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
 AnyTimeSubscriptionInterrogationRes ::= SEQUENCE { callForwardingData [1] CallForwardingData OPTIONAL, callBarringData [2]
 * CallBarringData OPTIONAL, odb-Info [3] ODB-Info OPTIONAL, camel-SubscriptionInfo [4] CAMEL-SubscriptionInfo OPTIONAL,
 * supportedVLR-CAMEL-Phases [5] SupportedCamelPhases OPTIONAL, supportedSGSN-CAMEL-Phases [6] SupportedCamelPhases OPTIONAL,
 * extensionContainer [7] ExtensionContainer OPTIONAL, ... , offeredCamel4CSIsInVLR [8] OfferedCamel4CSIs OPTIONAL,
 * offeredCamel4CSIsInSGSN [9] OfferedCamel4CSIs OPTIONAL, msisdn-BS-List [10] MSISDN-BS-List OPTIONAL, csg-SubscriptionDataList
 * [11] CSG-SubscriptionDataList OPTIONAL, cw-Data [12] CallWaitingData OPTIONAL, ch-Data [13] CallHoldData OPTIONAL, clip-Data
 * [14] ClipData OPTIONAL, clir-Data [15] ClirData OPTIONAL, ect-data [16] EctData OPTIONAL }
 *
 * MSISDN-BS-List ::= SEQUENCE SIZE (1..50) OF MSISDN-BS
 *
 * CSG-SubscriptionDataList ::= SEQUENCE SIZE (1..50) OF CSG-SubscriptionData
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AnyTimeSubscriptionInterrogationResponse extends MobilityMessage {

    CallForwardingData getCallForwardingData();

    CallBarringData getCallBarringData();

    ODBInfo getOdbInfo();

    CAMELSubscriptionInfo getCamelSubscriptionInfo();

    SupportedCamelPhases getsupportedVlrCamelPhases();

    SupportedCamelPhases getsupportedSgsnCamelPhases();

    MAPExtensionContainer getExtensionContainer();

    OfferedCamel4CSIs getOfferedCamel4CSIsInVlr();

    OfferedCamel4CSIs getOfferedCamel4CSIsInSgsn();

    ArrayList<MSISDNBS> getMsisdnBsList();

    ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList();

    CallWaitingData getCwData();

    CallHoldData getChData();

    ClipData getClipData();

    ClirData getClirData();

    EctData getImsi();

}
