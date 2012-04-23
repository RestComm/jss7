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
public interface CAPOperationCode {

	// -- gsmSCF activation Package
	public static final int initialDP = 0;

	// -- gsmSCF/gsmSRF activation of assist Package
	public static final int assistRequestInstructions = 16;

	// -- Assist connection establishment Package
	public static final int establishTemporaryConnection = 17;

	// -- Generic disconnect resource Package
	public static final int disconnectForwardConnection = 18;
	public static final int dFCWithArgument = 86;

	// -- Non-assisted connection establishment Package
	public static final int connectToResource = 19;

	// -- Connect Package (elementary gsmSSF function)
	public static final int connect = 20;

	// -- Call handling Package (elementary gsmSSF function)
	public static final int releaseCall = 22;

	// -- BCSM Event handling Package
	public static final int requestReportBCSMEvent = 23;
	public static final int eventReportBCSM = 24;
	
	// -- gsmSSF call processing Package
	public static final int collectInformation = 27;
	public static final int continueCode = 31;

	// -- gsmSCF call initiation Package
	public static final int initiateCallAttempt = 32;

	// -- Timer Package
	public static final int resetTimer = 33;

	// -- Billing Package
	public static final int furnishChargingInformation = 34;

	// -- Charging Package
	public static final int applyCharging = 35;
	public static final int applyChargingReport = 36;

	// -- Traffic management Package
	public static final int callGap = 41;

	// -- Call report Package
	public static final int callInformationReport = 44;
	public static final int callInformationRequest = 45;

	// -- Signalling control Package
	public static final int sendChargingInformation = 46;

	// -- Specialized resource control Package
	public static final int playAnnouncement = 47;
	public static final int promptAndCollectUserInformation = 48;
	public static final int specializedResourceReport = 49;

	// -- Cancel Package
	public static final int cancelCode = 53;

	// -- Activity Test Package
	public static final int activityTest = 55;

	// -- CPH Response Package
	public static final int continueWithArgument = 88;
	public static final int disconnectLeg = 90;
	public static final int moveLeg = 93;
	public static final int splitLeg = 95;

	// -- Exception Inform Package
	public static final int entityReleased = 96;

	// -- Play Tone Package
	public static final int playTone = 97;

	// -- Sms Activation Package
	public static final int initialDPSMS = 60;

	// -- Sms Billing Package
	public static final int furnishChargingInformationSMS = 61;

	// -- Sms Connect Package
	public static final int connectSMS = 62;

	// -- Sms Event Handling Package
	public static final int requestReportSMSEvent = 63;
	public static final int eventReportSMS = 64;

	// -- Sms Processing Package
	public static final int continueSMS = 65;

	// -- Sms Release Package
	public static final int releaseSMS = 66;

	// -- Sms Timer Package
	public static final int resetTimerSMS = 67;

	// -- Gprs Activity Test Package
	public static final int activityTestGPRS = 70;
	// -- Gprs Charging Package
	public static final int applyChargingGPRS = 71;
	public static final int applyChargingReportGPRS = 72;

	// -- Gprs Cancel Package
	public static final int cancelGPRS = 73;

	// -- Gprs Connect Package
	public static final int connectGPRS = 74;

	// -- Gprs Processing Package
	public static final int continueGPRS = 75;

	// -- Gprs Exception Information Package
	public static final int entityReleasedGPRS = 76;

	// -- Gprs Billing Package
	public static final int furnishChargingInformationGPRS = 77;

	// -- Gprs Scf Activation Package
	public static final int initialDPGPRS = 78;

	// -- Gprs Release Package
	public static final int releaseGPRS = 79;

	// -- Gprs Event Handling Package
	public static final int eventReportGPRS = 80;
	public static final int requestReportGPRSEvent = 81;

	// -- Gprs Timer Package
	public static final int resetTimerGPRS = 82;

	// -- Gprs Charge Advice Package
	public static final int sendChargingInformationGPRS = 83;
}
