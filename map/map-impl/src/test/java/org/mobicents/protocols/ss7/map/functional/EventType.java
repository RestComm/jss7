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
package org.mobicents.protocols.ss7.map.functional;

/**
 *
 * @author amit bhayani
 *
 */
public enum EventType {
    // Dialog EventType
    DialogDelimiter, DialogEricssonRequest, DialogRequest, DialogAccept, DialogReject, DialogUserAbort, DialogProviderAbort, DialogClose, DialogNotice, DialogRelease, DialogTimeout,

    // Service EventType
    ErrorComponent, ProviderErrorComponent, RejectComponent, InvokeTimeout,

    // Supplementary EventType
    ProcessUnstructuredSSRequestIndication, ProcessUnstructuredSSResponseIndication, UnstructuredSSRequestIndication, UnstructuredSSResponseIndication, UnstructuredSSNotifyRequestIndication,

    // SMS EventType
    ForwardShortMessageIndication, ForwardShortMessageRespIndication, MoForwardShortMessageIndication, MoForwardShortMessageRespIndication, MtForwardShortMessageIndication, MtForwardShortMessageRespIndication, SendRoutingInfoForSMIndication, SendRoutingInfoForSMRespIndication, ReportSMDeliveryStatusIndication, ReportSMDeliveryStatusRespIndication, InformServiceCentreIndication, AlertServiceCentreIndication, AlertServiceCentreRespIndication,

    // mobility.authentication EventType
    SendAuthenticationInfo_V3, SendAuthenticationInfo_V2, SendAuthenticationInfoResp_V2, SendAuthenticationInfoResp_V3,

    // mobility.locationManagement EventType
    UpdateLocation, UpdateLocationResp, CancelLocation, CancelLocationResp, SendIdentification, SendIdentificationResp, UpdateGprsLocation, UpdateGprsLocationResp, PurgeMS, PurgeMSResp,

    // mobility.subscriberInformation EventType
    AnyTimeInterrogation, AnyTimeInterrogationResp,

    // mobility.SubscriberManagementServices EventType
    InsertSubscriberData, InsertSubscriberDataResp,

    // mobility.imei EventType
    CheckImei, CheckImeiResp,

    // Call Handling Service
    ProvideRoamingNumber, ProvideRoamingNumberResp, SendRoutingInformation, SendRoutingInformationResp,

    // lsm EventType
    ProvideSubscriberLocation, ProvideSubscriberLocationResp, SubscriberLocationReport, SubscriberLocationReportResp, SendRoutingInfoForLCS, SendRoutingInfoForLCSResp;
}
