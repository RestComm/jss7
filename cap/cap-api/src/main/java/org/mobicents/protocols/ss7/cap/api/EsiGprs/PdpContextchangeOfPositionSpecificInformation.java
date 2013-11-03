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

package org.mobicents.protocols.ss7.cap.api.EsiGprs;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;

/**
 *
 pdp-ContextchangeOfPositionSpecificInformation [1] SEQUENCE { accessPointName [0] AccessPointName {bound} OPTIONAL,
 * chargingID [1] GPRSChargingID OPTIONAL, locationInformationGPRS [2] LocationInformationGPRS OPTIONAL, endUserAddress [3]
 * EndUserAddress {bound} OPTIONAL, qualityOfService [4] QualityOfService OPTIONAL, timeAndTimeZone [5] TimeAndTimezone {bound}
 * OPTIONAL, ..., gGSNAddress [6] GSN-Address OPTIONAL },
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PdpContextchangeOfPositionSpecificInformation extends Serializable {

    AccessPointName getAccessPointName();

    GPRSChargingID getChargingID();

    LocationInformationGPRS getLocationInformationGPRS();

    EndUserAddress getEndUserAddress();

    QualityOfService getQualityOfService();

    TimeAndTimezone getTimeAndTimezone();

    GSNAddress getGSNAddress();

}