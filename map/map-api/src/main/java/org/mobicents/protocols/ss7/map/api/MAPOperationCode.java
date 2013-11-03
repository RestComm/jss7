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
 * Standard Operation Code included in Invoke. ETS 300 974: December 2000 (GSM 09.02 version 5.15.1)
 *
 * @author amit bhayani
 *
 */
public interface MAPOperationCode {

    // -- Mobility Services
    // --- Location management services
    int updateLocation = 2;
    int cancelLocation = 3;
    int sendIdentification = 55;
    int purgeMS = 67;
    int updateGprsLocation = 23;
    int noteMMEvent = 89;

    // --- Handover services
    int performHandover = 28;
    int prepareHandover = 68;
    int sendEndSignal = 29;
    int processAccessSignalling = 33;
    int forwardAccessSignalling = 34;
    int performSubsequentHandover = 30;
    int prepareSubsequentHandover = 69;
    int noteInternalHandover = 35;

    // --- Authentication management services
    int sendParameters = 9;
    int sendAuthenticationInfo = 56;
    int authenticationFailureReport = 15;

    // --- IMEI management services
    int checkIMEI = 43;

    // --- Subscriber management services
    int insertSubscriberData = 7;
    int deleteSubscriberData = 8;

    // --- Fault recovery services
    int reset = 37;
    int forwardCheckSsIndication = 38;
    int restoreData = 57;

    // --- Subscriber Information services
    int anyTimeInterrogation = 71;
    int provideSubscriberInfo = 70;
    int anyTimeSubscriptionInterrogation = 62;
    int anyTimeModification = 65;
    int noteSubscriberDataModified = 5;

    // -- oam
    int activateTraceMode = 50;
    int deactivateTraceMode = 51;
    int traceSubscriberActivity = 52;
    int sendIMSI = 58;

    // -- Call Handling Services
    int sendRoutingInfo = 22;
    int provideRoamingNumber = 4;
    int resumeCallHandling = 6;
    int prepareGroupCall = 39;
    int processGroupCallSignalling = 41;
    int forwardGroupCallSignalling = 42;
    int sendGroupCallEndSignal = 40;
    int sendGroupCallInfo = 84;
    int setReportingState = 73;
    int statusReport = 74;
    int remoteUserFree = 75;
    int istAlert = 87;
    int istCommand = 88;
    int releaseResources = 20;

    // -- Supplementary services
    int processUnstructuredSSData = 19;
    int beginSubscriberActivity = 54;
    int registerSS = 10;
    int eraseSS = 11;
    int activateSS = 12;
    int deactivateSS = 13;
    int interrogateSS = 14;
    int registerPassword = 17;
    int getPassword = 18;
    int processUnstructuredSS_Request = 59;
    int unstructuredSS_Request = 60;
    int unstructuredSS_Notify = 61;
    int ssInvocationNotification = 72;
    int registerCCEntry = 76;
    int eraseCCEntry = 77;

    // -- short message service
    int sendRoutingInfoForSM = 45;
    int reportSM_DeliveryStatus = 47;
    int informServiceCentre = 63;
    // int forwardSM = 46; // the same as mo_forwardSM
    int mo_forwardSM = 46;
    int mt_forwardSM = 44;
    int mt_forwardSM_VGCS = 21;
    int alertServiceCentreWithoutResult = 49;
    int alertServiceCentre = 64;
    int noteSubscriberPresent = 48;
    int readyForSM = 66;

    // -- Network-Requested PDP Context Activation services
    int sendRoutingInfoForGprs = 24;
    int failureReport = 25;
    int noteMsPresentForGprs = 26;

    // -- location service (lms)
    int provideSubscriberLocation = 83;
    int subscriberLocationReport = 86;
    int sendRoutingInfoForLCS = 85;
}
