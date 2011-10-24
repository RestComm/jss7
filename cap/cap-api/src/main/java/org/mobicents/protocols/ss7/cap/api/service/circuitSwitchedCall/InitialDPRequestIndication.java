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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.primitives.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.Extensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;


/**
*
initialDP {PARAMETERS-BOUND : bound} OPERATION ::= {
ARGUMENT InitialDPArg {bound}
RETURN RESULT FALSE
ERRORS {missingCustomerRecord |
missingParameter |
parameterOutOfRange |
systemFailure |
taskRefused |
unexpectedComponentSequence |
unexpectedDataValue |
unexpectedParameter}
CODE opcode-initialDP}
-- Direction: gsmSSF -> gsmSCF, Timer: Tidp
-- This operation is used after a TDP to indicate request for service. 

InitialDPArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
serviceKey [0] ServiceKey , (= Integer4)
calledPartyNumber [2] CalledPartyNumber {bound} OPTIONAL,
callingPartyNumber [3] CallingPartyNumber {bound} OPTIONAL,
callingPartysCategory [5] CallingPartysCategory OPTIONAL,
cGEncountered [7] CGEncountered OPTIONAL,
iPSSPCapabilities [8] IPSSPCapabilities {bound} OPTIONAL, (OCTET STRING (1..4))
locationNumber [10] LocationNumber {bound} OPTIONAL,
originalCalledPartyID [12] OriginalCalledPartyID {bound} OPTIONAL,
extensions [15] Extensions {bound} OPTIONAL,
highLayerCompatibility [23] HighLayerCompatibility OPTIONAL,
additionalCallingPartyNumber [25] AdditionalCallingPartyNumber {bound} OPTIONAL,
bearerCapability [27] BearerCapability {bound} OPTIONAL,
eventTypeBCSM [28] EventTypeBCSM OPTIONAL,
redirectingPartyID [29] RedirectingPartyID {bound} OPTIONAL,
redirectionInformation [30] RedirectionInformation OPTIONAL,
cause [17] Cause {bound} OPTIONAL,
serviceInteractionIndicatorsTwo [32] ServiceInteractionIndicatorsTwo OPTIONAL,
carrier [37] Carrier {bound} OPTIONAL,
cug-Index [45] CUG-Index OPTIONAL,
cug-Interlock [46] CUG-Interlock OPTIONAL,
cug-OutgoingAccess [47] NULL OPTIONAL,
iMSI [50] IMSI OPTIONAL,
subscriberState [51] SubscriberState OPTIONAL,
locationInformation [52] LocationInformation OPTIONAL,
ext-basicServiceCode [53] Ext-BasicServiceCode OPTIONAL,
callReferenceNumber [54] CallReferenceNumber OPTIONAL,
mscAddress [55] ISDN-AddressString OPTIONAL,
calledPartyBCDNumber [56] CalledPartyBCDNumber {bound} OPTIONAL,
timeAndTimezone [57] TimeAndTimezone {bound} OPTIONAL,
callForwardingSS-Pending [58] NULL OPTIONAL,
initialDPArgExtension [59] InitialDPArgExtension {bound} OPTIONAL,
...
} 
 
* 
* @author sergey vetyutnev
* 
*/
public interface InitialDPRequestIndication extends CircuitSwitchedCallMessage {

	public int getServiceKey();

	public byte[] getCalledPartyNumber();

	public CalledPartyNumber getCalledPartyNumberIsup();

	public byte[] getCallingPartyNumber();

	public CallingPartyNumber getCallingPartyNumberIsup();

	public byte[] getCallingPartysCategory();

	public CallingPartyCategory getCallingPartysCategoryIsup();

	public CGEncountered getCGEncountered();

	public IPSSPCapabilities getIPSSPCapabilities();

	public byte[] getLocationNumber();

	public LocationNumber getLocationNumberIsup();

	public byte[] getOriginalCalledPartyID();

	public OriginalCalledNumber getOriginalCalledPartyIDIsup();

	public Extensions getExtensions();

	public byte[] getHighLayerCompatibility(); // TODO: ISUP version

	public Digits getAdditionalCallingPartyNumber();

	public GenericNumber getAdditionalCallingPartyNumberIsup();
	
	public BearerCapability getBearerCapability();
	
	public EventTypeBCSM getEventTypeBCSM();

	public byte[] getRedirectingPartyID();

	public RedirectingNumber getRedirectingPartyIDIsup();

	public byte[] getRedirectionInformation();

	public RedirectionInformation getRedirectionInformationIsup();

	public byte[] getCause();

	public CauseIndicators getCauseIsup();

	// ..................................
}

