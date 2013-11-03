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

package org.mobicents.protocols.ss7.cap.api;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPOperationCode {

    // -- gsmSCF activation Package
    int initialDP = 0;

    // -- gsmSCF/gsmSRF activation of assist Package
    int assistRequestInstructions = 16;

    // -- Assist connection establishment Package
    int establishTemporaryConnection = 17;

    // -- Generic disconnect resource Package
    int disconnectForwardConnection = 18;
    int dFCWithArgument = 86;

    // -- Non-assisted connection establishment Package
    int connectToResource = 19;

    // -- Connect Package (elementary gsmSSF function)
    int connect = 20;

    // -- Call handling Package (elementary gsmSSF function)
    int releaseCall = 22;

    // -- BCSM Event handling Package
    int requestReportBCSMEvent = 23;
    int eventReportBCSM = 24;

    // -- gsmSSF call processing Package
    int collectInformation = 27;
    int continueCode = 31;

    // -- gsmSCF call initiation Package
    int initiateCallAttempt = 32;

    // -- Timer Package
    int resetTimer = 33;

    // -- Billing Package
    int furnishChargingInformation = 34;

    // -- Charging Package
    int applyCharging = 35;
    int applyChargingReport = 36;

    // -- Traffic management Package
    int callGap = 41;

    // -- Call report Package
    int callInformationReport = 44;
    int callInformationRequest = 45;

    // -- Signalling control Package
    int sendChargingInformation = 46;

    // -- Specialized resource control Package
    int playAnnouncement = 47;
    int promptAndCollectUserInformation = 48;
    int specializedResourceReport = 49;

    // -- Cancel Package
    int cancelCode = 53;

    // -- Activity Test Package
    int activityTest = 55;

    // -- CPH Response Package
    int continueWithArgument = 88;
    int disconnectLeg = 90;
    int moveLeg = 93;
    int splitLeg = 95;

    // -- Exception Inform Package
    int entityReleased = 96;

    // -- Play Tone Package
    int playTone = 97;

    // -- Sms Activation Package
    int initialDPSMS = 60;

    // -- Sms Billing Package
    int furnishChargingInformationSMS = 61;

    // -- Sms Connect Package
    int connectSMS = 62;

    // -- Sms Event Handling Package
    int requestReportSMSEvent = 63;
    int eventReportSMS = 64;

    // -- Sms Processing Package
    int continueSMS = 65;

    // -- Sms Release Package
    int releaseSMS = 66;

    // -- Sms Timer Package
    int resetTimerSMS = 67;

    // -- Gprs Activity Test Package
    int activityTestGPRS = 70;
    // -- Gprs Charging Package
    int applyChargingGPRS = 71;
    int applyChargingReportGPRS = 72;

    // -- Gprs Cancel Package
    int cancelGPRS = 73;

    // -- Gprs Connect Package
    int connectGPRS = 74;

    // -- Gprs Processing Package
    int continueGPRS = 75;

    // -- Gprs Exception Information Package
    int entityReleasedGPRS = 76;

    // -- Gprs Billing Package
    int furnishChargingInformationGPRS = 77;

    // -- Gprs Scf Activation Package
    int initialDPGPRS = 78;

    // -- Gprs Release Package
    int releaseGPRS = 79;

    // -- Gprs Event Handling Package
    int eventReportGPRS = 80;
    int requestReportGPRSEvent = 81;

    // -- Gprs Timer Package
    int resetTimerGPRS = 82;

    // -- Gprs Charge Advice Package
    int sendChargingInformationGPRS = 83;
}
