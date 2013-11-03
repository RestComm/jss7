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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;

/**
 *
 initialDP {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT InitialDPArg {bound}
   RETURN RESULT FALSE
   ERRORS {
     missingCustomerRecord | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
     unexpectedDataValue | unexpectedParameter} CODE opcode-initialDP
   }
   -- Direction: gsmSSF -> gsmSCF, Timer: Tidp
   -- This operation is used after a TDP to indicate request for service.

InitialDPArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  serviceKey                     [0] ServiceKey, (= Integer4)
  calledPartyNumber              [2] CalledPartyNumber {bound} OPTIONAL,
  callingPartyNumber             [3] CallingPartyNumber {bound} OPTIONAL,
  callingPartysCategory          [5] CallingPartysCategory OPTIONAL,
  cGEncountered                  [7] CGEncountered OPTIONAL,
  iPSSPCapabilities              [8] IPSSPCapabilities {bound} OPTIONAL, (OCTET STRING (1..4))
  locationNumber                 [10] LocationNumber {bound} OPTIONAL,
  originalCalledPartyID          [12] OriginalCalledPartyID {bound} OPTIONAL,
  extensions                     [15] Extensions {bound} OPTIONAL,
  highLayerCompatibility         [23] HighLayerCompatibility OPTIONAL,
  additionalCallingPartyNumber   [25] AdditionalCallingPartyNumber {bound} OPTIONAL,
  bearerCapability               [27] BearerCapability {bound} OPTIONAL,
  eventTypeBCSM                  [28] EventTypeBCSM OPTIONAL,
  redirectingPartyID             [29] RedirectingPartyID {bound} OPTIONAL,
  redirectionInformation         [30] RedirectionInformation OPTIONAL,
  cause                          [17] Cause {bound} OPTIONAL,
  serviceInteractionIndicatorsTwo [32] ServiceInteractionIndicatorsTwo OPTIONAL,
  carrier                        [37] Carrier {bound} OPTIONAL,
  cug-Index                      [45] CUG-Index OPTIONAL,
  cug-Interlock                  [46] CUG-Interlock OPTIONAL,
  cug-OutgoingAccess             [47] NULL OPTIONAL,
  iMSI                           [50] IMSI OPTIONAL,
  subscriberState                [51] SubscriberState OPTIONAL,
  locationInformation            [52] LocationInformation OPTIONAL,
  ext-basicServiceCode           [53] Ext-BasicServiceCode OPTIONAL,
  callReferenceNumber            [54] CallReferenceNumber OPTIONAL,
  mscAddress                     [55] ISDN-AddressString OPTIONAL,
  calledPartyBCDNumber           [56] CalledPartyBCDNumber {bound} OPTIONAL,
  timeAndTimezone                [57] TimeAndTimezone {bound} OPTIONAL,
  callForwardingSS-Pending       [58] NULL OPTIONAL,
  initialDPArgExtension          [59] InitialDPArgExtension {bound} OPTIONAL,
  ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDPRequest extends CircuitSwitchedCallMessage {

    int getServiceKey();

    CalledPartyNumberCap getCalledPartyNumber();

    CallingPartyNumberCap getCallingPartyNumber();

    CallingPartysCategoryInap getCallingPartysCategory();

    CGEncountered getCGEncountered();

    IPSSPCapabilities getIPSSPCapabilities();

    LocationNumberCap getLocationNumber();

    OriginalCalledNumberCap getOriginalCalledPartyID();

    CAPExtensions getExtensions();

    HighLayerCompatibilityInap getHighLayerCompatibility();

    /**
     * Use Digits.getGenericNumber() for AdditionalCallingPartyNumber
     *
     * @return
     */
    Digits getAdditionalCallingPartyNumber();

    BearerCapability getBearerCapability();

    EventTypeBCSM getEventTypeBCSM();

    RedirectingPartyIDCap getRedirectingPartyID();

    RedirectionInformationInap getRedirectionInformation();

    CauseCap getCause();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    Carrier getCarrier();

    CUGIndex getCugIndex();

    CUGInterlock getCugInterlock();

    boolean getCugOutgoingAccess();

    IMSI getIMSI();

    SubscriberState getSubscriberState();

    LocationInformation getLocationInformation();

    ExtBasicServiceCode getExtBasicServiceCode();

    CallReferenceNumber getCallReferenceNumber();

    ISDNAddressString getMscAddress();

    CalledPartyBCDNumber getCalledPartyBCDNumber();

    TimeAndTimezone getTimeAndTimezone();

    boolean getCallForwardingSSPending();

    InitialDPArgExtension getInitialDPArgExtension();
}