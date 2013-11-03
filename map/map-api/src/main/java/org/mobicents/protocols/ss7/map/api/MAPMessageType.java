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

package org.mobicents.protocols.ss7.map.api;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum MAPMessageType {

    // -- mobility
    anyTimeInterrogation_Request, anyTimeInterrogation_Response, sendAuthenticationInfo_Request, sendAuthenticationInfo_Response, updateLocation_Request, updateLocation_Response, checkIMEI_Request, checkIMEI_Response, cancelLocation_Request, cancelLocation_Response, insertSubscriberData_Request, insertSubscriberData_Response, sendIdentification_Request, sendIdentification_Response, updateGprsLocation_Request, updateGprsLocation_Response, purgeMS_Request, purgeMS_Response,

    // -- supplementary
    processUnstructuredSSRequest_Request, processUnstructuredSSRequest_Response, unstructuredSSRequest_Request, unstructuredSSRequest_Response, unstructuredSSNotify_Request, unstructuredSSNotify_Response,

    // -- sms
    sendRoutingInfoForSM_Request, sendRoutingInfoForSM_Response, reportSM_DeliveryStatus_Request, reportSM_DeliveryStatus_Response, InformServiceCentre_Request, forwardSM_Request, forwardSM_Response, moForwardSM_Request, moForwardSM_Response, mtForwardSM_Request, mtForwardSM_Response, MtForwardSM_VGCS_Request, MtForwardSM_VGCS_Response, alertServiceCentreWithoutResult_Request, alertServiceCentre_Request, alertServiceCentre_Response, noteSubscriberPresent_Request, noteSubscriberPresent_Response, readyForSM_Request, readyForSM_Response,

    // -- lms
    sendRoutingInfoForLCS_Request, sendRoutingInfoForLCS_Response, provideSubscriberLocation_Request, provideSubscriberLocation_Response, subscriberLocationReport_Request, subscriberLocationReport_Response,

    // -- call handling
    sendRoutingInfo_Request, sendRoutingInfo_Response, provideRoamingNumber_Request, privideRoamingNumber_Response;
}
