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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;

/**
 * 

SubscriberData ::= SEQUENCE { 240
	msisdn 				[1] ISDN-AddressString OPTIONAL, 241
	category 			[2] Category OPTIONAL, 242
	subscriberStatus 	[3] SubscriberStatus OPTIONAL, 243
	bearerServiceList 	[4] BearerServiceList OPTIONAL, 244
	teleserviceList 	[6] TeleserviceList OPTIONAL, 245
	provisionedSS 		[7] SS-InfoList OPTIONAL, 246
	odb-Data 			[8] ODB-Data OPTIONAL, 247
-- odb-Data must be absent in version 1
	roamingRestrictionDueToUnsupportedFeature [9] NULL OPTIONAL, 249
-- roamingRestrictionDueToUnsupportedFeature must be absent
-- in version 1
	regionalSubscriptionData [10] ZoneCodeList OPTIONAL 252
-- regionalSubscriptionData must be absent in version 1
}

 * 
 * @author sergey vetyutnev
 * 
 */
public interface SubscriberData {

	public ISDNAddressString getMsisdn();

	public Category getCategory();

	public SubscriberStatus getSubscriberStatus();

	public BearerServiceList getBearerServiceList();

	public TeleserviceList getTeleserviceList();

	public SSInfoList getProvisionedSS();

	public ODBData getOdbData();

	public boolean getRoamingRestrictionDueToUnsupportedFeature();

	public ZoneCodeList getRegionalSubscriptionData();

}
