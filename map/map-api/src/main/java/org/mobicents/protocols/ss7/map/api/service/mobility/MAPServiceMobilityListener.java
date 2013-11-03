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

package org.mobicents.protocols.ss7.map.api.service.mobility;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPServiceMobilityListener extends MAPServiceListener {

    // -- Location Management Service
    void onUpdateLocationRequest(UpdateLocationRequest ind);

    void onUpdateLocationResponse(UpdateLocationResponse ind);

    void onCancelLocationRequest(CancelLocationRequest request);

    void onCancelLocationResponse(CancelLocationResponse response);

    void onSendIdentificationRequest(SendIdentificationRequest request);

    void onSendIdentificationResponse(SendIdentificationResponse response);

    void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request);

    void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response);

    void onPurgeMSRequest(PurgeMSRequest request);

    void onPurgeMSResponse(PurgeMSResponse response);

    // -- Authentication management services
    void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind);

    void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind);

    // -- Subscriber Information services
    void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request);

    void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response);

    // -- Subscriber Management services
    void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request);

    void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request);

    // -- International mobile equipment identities management services
    void onCheckImeiRequest(CheckImeiRequest request);

    void onCheckImeiResponse(CheckImeiResponse response);

}
