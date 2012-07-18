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

package org.mobicents.protocols.ss7.cap.api.EsiGprs;

import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;

/**
*

pdp-ContextchangeOfPositionSpecificInformation
[1] SEQUENCE {
accessPointName [0] AccessPointName {bound} OPTIONAL,
chargingID [1] GPRSChargingID OPTIONAL,
locationInformationGPRS [2] LocationInformationGPRS OPTIONAL,
endUserAddress [3] EndUserAddress {bound} OPTIONAL,
qualityOfService [4] QualityOfService OPTIONAL,
timeAndTimeZone [5] TimeAndTimezone {bound} OPTIONAL,
...,
gGSNAddress [6] GSN-Address OPTIONAL
},

* 
* @author sergey vetyutnev
* 
*/
public interface PdpContextchangeOfPositionSpecificInformation {

	public AccessPointName getAccessPointName();

	public GPRSChargingID getChargingID();

	public LocationInformationGPRS getLocationInformationGPRS();

	public EndUserAddress getEndUserAddress();

	public QualityOfService getQualityOfService();

	public TimeAndTimezone getTimeAndTimezone();

	public GSNAddress getGSNAddress();

}
