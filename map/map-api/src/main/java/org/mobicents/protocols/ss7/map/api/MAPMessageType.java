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

package org.mobicents.protocols.ss7.map.api;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum MAPMessageType {

    // -- mobility
    anyTimeInterrogation_Request, anyTimeInterrogation_Response, anyTimeSubscriptionInterrogation_Request, anyTimeSubscriptionInterrogation_Response, sendAuthenticationInfo_Request, sendAuthenticationInfo_Response, updateLocation_Request, updateLocation_Response, checkIMEI_Request, checkIMEI_Response, cancelLocation_Request, cancelLocation_Response, insertSubscriberData_Request, insertSubscriberData_Response, deleteSubscriberData_Request, deleteSubscriberData_Response, sendIdentification_Request, sendIdentification_Response, updateGprsLocation_Request, updateGprsLocation_Response, purgeMS_Request, purgeMS_Response, RestoreData_Request, RestoreData_Response, reset_Request, forwardCheckSSIndication_Request, provideSubscriberInfo_Request, provideSubscriberInfo_Response, authenticationFailureReport_Request, authenticationFailureReport_Response,

    // -- supplementary
    processUnstructuredSSRequest_Request, processUnstructuredSSRequest_Response, unstructuredSSRequest_Request, unstructuredSSRequest_Response, unstructuredSSNotify_Request, unstructuredSSNotify_Response, registerSS_Request, registerSS_Response, eraseSS_Request, eraseSS_Response, activateSS_Request, activateSS_Response, deactivateSS_Request, deactivateSS_Response, interrogateSS_Request, interrogateSS_Response, getPasswordRequest_Request, getPasswordRequest_Response, registerPasswordRequest_Request, registerPasswordRequest_Response,

    // -- sms
    sendRoutingInfoForSM_Request, sendRoutingInfoForSM_Response, reportSM_DeliveryStatus_Request, reportSM_DeliveryStatus_Response, InformServiceCentre_Request, forwardSM_Request, forwardSM_Response, moForwardSM_Request, moForwardSM_Response, mtForwardSM_Request, mtForwardSM_Response, MtForwardSM_VGCS_Request, MtForwardSM_VGCS_Response, alertServiceCentreWithoutResult_Request, alertServiceCentre_Request, alertServiceCentre_Response, noteSubscriberPresent_Request, noteSubscriberPresent_Response, readyForSM_Request, readyForSM_Response,

    // -- lms
    sendRoutingInfoForLCS_Request, sendRoutingInfoForLCS_Response, provideSubscriberLocation_Request, provideSubscriberLocation_Response, subscriberLocationReport_Request, subscriberLocationReport_Response,

    // -- call handling
    sendRoutingInfo_Request, sendRoutingInfo_Response, provideRoamingNumber_Request, privideRoamingNumber_Response, istCommand_Request, istCommand_Response,

    // -- oam
    sendIMSI_Request, sendIMSI_Response, activateTraceMode_Request, activateTraceMode_Response,

    // -- pdpContextActivation
    sendRoutingInfoForGprs_Request, sendRoutingInfoForGprs_Response;

}
