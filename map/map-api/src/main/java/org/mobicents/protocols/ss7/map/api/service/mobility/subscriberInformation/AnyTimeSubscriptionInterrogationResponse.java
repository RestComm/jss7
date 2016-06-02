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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
<code>
AnyTimeSubscriptionInterrogationRes ::= SEQUENCE {
  callForwardingData         [1] CallForwardingData OPTIONAL,
  callBarringData            [2] CallBarringData OPTIONAL,
  odb-Info                   [3] ODB-Info OPTIONAL,
  camel-SubscriptionInfo     [4] CAMEL-SubscriptionInfo OPTIONAL,
  supportedVLR-CAMEL-Phases  [5] SupportedCamelPhases OPTIONAL,
  supportedSGSN-CAMEL-Phases [6] SupportedCamelPhases OPTIONAL,
  extensionContainer         [7] ExtensionContainer OPTIONAL,
  ... ,
  offeredCamel4CSIsInVLR     [8] OfferedCamel4CSIs OPTIONAL,
  offeredCamel4CSIsInSGSN    [9] OfferedCamel4CSIs OPTIONAL,
  msisdn-BS-List             [10] MSISDN-BS-List OPTIONAL,
  csg-SubscriptionDataList   [11] CSG-SubscriptionDataList OPTIONAL,
  cw-Data                    [12] CallWaitingData OPTIONAL,
  ch-Data                    [13] CallHoldData OPTIONAL,
  clip-Data                  [14] ClipData OPTIONAL,
  clir-Data                  [15] ClirData OPTIONAL,
  ect-data                   [16] EctData OPTIONAL
}

MSISDN-BS-List ::= SEQUENCE SIZE (1..50) OF MSISDN-BS

CSG-SubscriptionDataList ::= SEQUENCE SIZE (1..50) OF CSG-SubscriptionData
</code>
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

    EctData getEctData();

}
