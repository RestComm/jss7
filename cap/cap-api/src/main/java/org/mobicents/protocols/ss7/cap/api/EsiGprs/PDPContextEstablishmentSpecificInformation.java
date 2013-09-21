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

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;

/**
 *
 pDPContextEstablishmentSpecificInformation [4] SEQUENCE { accessPointName [0] AccessPointName {bound} OPTIONAL,
 * endUserAddress [1] EndUserAddress {bound} OPTIONAL, qualityOfService [2] QualityOfService OPTIONAL, locationInformationGPRS
 * [3] LocationInformationGPRS OPTIONAL, timeAndTimeZone [4] TimeAndTimezone {bound} OPTIONAL, pDPInitiationType [5]
 * PDPInitiationType OPTIONAL, ..., secondaryPDP-context [6] NULL OPTIONAL },
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PDPContextEstablishmentSpecificInformation extends Serializable {

    AccessPointName getAccessPointName();

    EndUserAddress getEndUserAddress();

    QualityOfService getQualityOfService();

    LocationInformationGPRS getLocationInformationGPRS();

    TimeAndTimezone getTimeAndTimezone();

    PDPInitiationType getPDPInitiationType();

    boolean getSecondaryPDPContext();

}