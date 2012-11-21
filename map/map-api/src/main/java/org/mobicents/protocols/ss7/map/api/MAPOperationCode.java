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

package org.mobicents.protocols.ss7.map.api;

/**
 * Standard Operation Code included in Invoke. ETS 300 974: December 2000 (GSM
 * 09.02 version 5.15.1)
 * 
 * @author amit bhayani
 * 
 */
public interface MAPOperationCode {

	// -- Mobility Services
	// --- Location management services
	public static final int updateLocation = 2;
	public static final int cancelLocation = 3;
	public static final int sendIdentification = 55;
	public static final int purgeMS = 67;
	public static final int updateGprsLocation = 23;
	public static final int noteMMEvent = 89;

	// --- Handover services
	public static final int performHandover = 28;
	public static final int prepareHandover = 68;
	public static final int sendEndSignal = 29;
	public static final int processAccessSignalling = 33;
	public static final int forwardAccessSignalling = 34;
	public static final int performSubsequentHandover = 30;
	public static final int prepareSubsequentHandover = 69;
	public static final int noteInternalHandover = 35;

	// --- Authentication management services
	public static final int sendParameters = 9;
	public static final int sendAuthenticationInfo = 56;
	public static final int authenticationFailureReport = 15;

	// --- IMEI management services
	public static final int checkIMEI = 43;

	// --- Subscriber management services
	public static final int insertSubscriberData = 7;
	public static final int deleteSubscriberData = 8;

	// --- Fault recovery services
	public static final int reset = 37;
	public static final int forwardCheckSsIndication = 38;
	public static final int restoreData = 57;

	// --- Subscriber Information services
	public static final int anyTimeInterrogation = 71;
	public static final int provideSubscriberInfo = 70;
	public static final int anyTimeSubscriptionInterrogation = 62;
	public static final int anyTimeModification = 65;
	public static final int noteSubscriberDataModified = 5;

	
	// -- oam
	public static final int activateTraceMode = 50;
	public static final int deactivateTraceMode = 51;
	public static final int traceSubscriberActivity = 52;
	public static final int sendIMSI = 58;

	
	// -- Call Handling Services
	public static final int sendRoutingInfo = 22;
	public static final int provideRoamingNumber = 4;
	public static final int resumeCallHandling = 6;
	public static final int prepareGroupCall = 39;
	public static final int processGroupCallSignalling = 41;
	public static final int forwardGroupCallSignalling = 42;
	public static final int sendGroupCallEndSignal = 40;
	public static final int sendGroupCallInfo = 84;
	public static final int setReportingState = 73;
	public static final int statusReport = 74;
	public static final int remoteUserFree = 75;
	public static final int istAlert = 87;
	public static final int istCommand = 88;
	public static final int releaseResources = 20;

	
	// -- Supplementary services
	public static final int processUnstructuredSSData = 19;
	public static final int beginSubscriberActivity = 54;
	public static final int registerSS = 10;
	public static final int eraseSS = 11;
	public static final int activateSS = 12;
	public static final int deactivateSS = 13;
	public static final int interrogateSS = 14;
	public static final int registerPassword = 17;
	public static final int getPassword = 18;
	public static final int processUnstructuredSS_Request = 59;
	public static final int unstructuredSS_Request = 60;
	public static final int unstructuredSS_Notify = 61;
	public static final int ssInvocationNotification = 72;
	public static final int registerCCEntry = 76;
	public static final int eraseCCEntry = 77;


	//-- short message service
	public static final int sendRoutingInfoForSM = 45;
	public static final int reportSM_DeliveryStatus = 47;
	public static final int informServiceCentre = 63;
	// public static final int  forwardSM = 46; // the same as mo_forwardSM
	public static final int mo_forwardSM = 46;
	public static final int mt_forwardSM = 44;
	public static final int mt_forwardSM_VGCS = 21;
	public static final int alertServiceCentreWithoutResult = 49;
	public static final int alertServiceCentre = 64;
	public static final int noteSubscriberPresent = 48;
	public static final int readyForSM = 66;


	// -- Network-Requested PDP Context Activation services
	public static final int sendRoutingInfoForGprs = 24;
	public static final int failureReport = 25;
	public static final int noteMsPresentForGprs = 26;
	

	// -- location service (lms)
	public static final int provideSubscriberLocation = 83;
	public static final int subscriberLocationReport = 86;
	public static final int sendRoutingInfoForLCS = 85;
}
