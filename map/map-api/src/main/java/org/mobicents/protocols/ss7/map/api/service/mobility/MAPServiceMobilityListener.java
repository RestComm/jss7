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
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
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

    void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest ind);

    void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse ind);

    // -- Fault Recovery services
    void onResetRequest(ResetRequest ind);

    void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest ind);

    void onRestoreDataRequest(RestoreDataRequest ind);

    void onRestoreDataResponse(RestoreDataResponse ind);

    // -- Subscriber Information services
    void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request);

    void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response);

    void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request);

    void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response);

    void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request);

    void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response);

    // -- Subscriber Management services
    void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request);

    void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request);

    void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request);

    void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request);

    // -- International mobile equipment identities management services
    void onCheckImeiRequest(CheckImeiRequest request);

    void onCheckImeiResponse(CheckImeiResponse response);

    // -- OAM service: activateTraceMode operation can be present in networkLocUpContext and gprsLocationUpdateContext application contexts
    void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility ind);

    void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind);

}
