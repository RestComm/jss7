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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 MAP V3: InsertSubscriberDataRes ::= SEQUENCE { teleserviceList [1] TeleserviceList OPTIONAL, bearerServiceList [2]
 * BearerServiceList OPTIONAL, ss-List [3] SS-List OPTIONAL, odb-GeneralData [4] ODB-GeneralData OPTIONAL,
 * regionalSubscriptionResponse [5] RegionalSubscriptionResponse OPTIONAL, supportedCamelPhases [6] SupportedCamelPhases
 * OPTIONAL, extensionContainer [7] ExtensionContainer OPTIONAL, ... , offeredCamel4CSIs [8] OfferedCamel4CSIs OPTIONAL,
 * supportedFeatures [9] SupportedFeatures OPTIONAL }
 *
 *
 * MAP V2: InsertSubscriberDataRes ::= SEQUENCE { teleserviceList[1] TeleserviceList OPTIONAL, bearerServiceList[2]
 * BearerServiceList OPTIONAL, ss-List[3] SS-List OPTIONAL, odb-GeneralData[4] ODB-GeneralData OPTIONAL,
 * regionalSubscriptionResponse [5] RegionalSubscriptionResponse OPTIONAL, -- regionalSubscriptionResponse must be absent in
 * version 1 ...}
 *
 * TeleserviceList ::= SEQUENCE SIZE (1..20) OF Ext-TeleserviceCode
 *
 * BearerServiceList ::= SEQUENCE SIZE (1..50) OF Ext-BearerServiceCode
 *
 * SS-List ::= SEQUENCE SIZE (1..30) OF SS-Code
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InsertSubscriberDataResponse extends MobilityMessage {

    ArrayList<ExtTeleserviceCode> getTeleserviceList();

    ArrayList<ExtBearerServiceCode> getBearerServiceList();

    ArrayList<SSCode> getSSList();

    ODBGeneralData getODBGeneralData();

    RegionalSubscriptionResponse getRegionalSubscriptionResponse();

    SupportedCamelPhases getSupportedCamelPhases();

    MAPExtensionContainer getExtensionContainer();

    OfferedCamel4CSIs getOfferedCamel4CSIs();

    SupportedFeatures getSupportedFeatures();

}
