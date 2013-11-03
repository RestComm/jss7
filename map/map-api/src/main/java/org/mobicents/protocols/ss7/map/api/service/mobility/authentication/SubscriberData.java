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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSInfo;

/**
 *
 SubscriberData ::= SEQUENCE { msisdn [1] ISDN-AddressString OPTIONAL, category [2] Category OPTIONAL, subscriberStatus [3]
 * SubscriberStatus OPTIONAL, bearerServiceList [4] BearerServiceList OPTIONAL, teleserviceList [6] TeleserviceList OPTIONAL,
 * provisionedSS [7] SS-InfoList OPTIONAL, odb-Data [8] ODB-Data OPTIONAL, -- odb-Data must be absent in version 1
 * roamingRestrictionDueToUnsupportedFeature [9] NULL OPTIONAL, -- roamingRestrictionDueToUnsupportedFeature must be absent --
 * in version 1 regionalSubscriptionData [10] ZoneCodeList OPTIONAL -- regionalSubscriptionData must be absent in version 1 }
 *
 * BearerServiceList ::= SEQUENCE SIZE (1..50) OF BearerServiceCode
 *
 * TeleserviceList ::= SEQUENCE SIZE (1..20) OF TeleserviceCode
 *
 * SS-InfoList ::= SEQUENCE SIZE (1..30) OF SS-Info
 *
 * ZoneCodeList ::= SEQUENCE SIZE (1..10) OF ZoneCode
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SubscriberData extends Serializable {

    ISDNAddressString getMsisdn();

    Category getCategory();

    SubscriberStatus getSubscriberStatus();

    ArrayList<BearerServiceCode> getBearerServiceList();

    ArrayList<TeleserviceCode> getTeleserviceList();

    ArrayList<SSInfo> getProvisionedSS();

    ODBData getOdbData();

    boolean getRoamingRestrictionDueToUnsupportedFeature();

    ArrayList<ZoneCode> getRegionalSubscriptionData();

}
