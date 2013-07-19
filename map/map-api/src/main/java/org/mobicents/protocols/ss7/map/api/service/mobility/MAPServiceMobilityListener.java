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
