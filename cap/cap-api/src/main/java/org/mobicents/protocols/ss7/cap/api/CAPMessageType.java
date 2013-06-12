/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.api;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum CAPMessageType {

    // Voice
    initialDP_Request, connect_Request, releaseCall_Request, eventReportBCSM_Request, requestReportBCSMEvent_Request, continue_Request, activityTest_Request, activityTest_Response, assistRequestInstructions_Request, establishTemporaryConnection_Request, disconnectForwardConnection_Request, disconnectForwardConnectionWithArgument_Request, connectToResource_Request, resetTimer_Request, furnishChargingInformation_Request, applyChargingReport_Request, applyCharging_Request, callInformationReport_Request, callInformationRequest_Request, sendChargingInformation_Request, specializedResourceReport_Request, playAnnouncement_Request, promptAndCollectUserInformation_Request, promptAndCollectUserInformation_Response, cancel_Request, continueWithArgument_Request, collectInformation_Request, callGap_Request, entityReleased_Request, disconnectLeg_Request, moveLeg_Request, splitLeg_Request, playTone_Request, initiateCallAttempt_Request,

    // GPRS
    initialDPGPRS_Request, requestReportGPRSEvent_Request, eventReportGPRS_Request, applyChargingGPRS_Request, applyChargingReportGPRS_Request, entityReleasedGPRS_Request, connectGPRS_Request, continueGPRS_Request, releaseGPRS_Request, resetTimerGPRS_Request, furnishChargingInformationGPRS_Request, cancelGPRS_Request, sendChargingInformationGPRS_Request, activityTestGPRS_Request,

    // SMS
    initialDPSMS_Request, connectSMS_Request, releaseSMS_Request, requestReportSMSEvent_Request, eventReportSMS_Request, resetTimerSMS_Request, furnishChargingInformationSMS_Request, continueSMS_Request,

}
