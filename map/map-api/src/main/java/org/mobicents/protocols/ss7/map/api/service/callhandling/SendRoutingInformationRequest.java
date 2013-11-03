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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;

/**
 *
 *<pre>
 *  MAP V1-2-3:
 *
 *  MAP V3:
 *  sendRoutingInfo  OPERATION ::= {--Timer m
 *  -- The timer is set to the upper limit of the range if the GMSC supports pre-paging.
 *  ARGUMENT
 *  SendRoutingInfoArg
 *  RESULT
 *  SendRoutingInfoRes
 *  ERRORS {
 *  systemFailure |
 *  dataMissing |
 *  unexpectedDataValue |
 *  facilityNotSupported |
 *  or-NotAllowed |
 *  unknownSubscriber |
 *  numberChanged |
 *  bearerServiceNotProvisioned |
 *  teleserviceNotProvisioned |
 *  absentSubscriber |
 *  busySubscriber |
 *  noSubscriberReply |
 *  callBarred |
 *  cug-Reject |
 *  forwardingViolation}
 *  CODElocal:22 }
 *
 *  MAP V2:
 *  SendRoutingInfo ::= OPERATION--Timer m
 *  ARGUMENT
 *  sendRoutingInfoArgSendRoutingInfoArg
 *  RESULT
 *  sendRoutingInfoResSendRoutingInfoRes
 *  ERRORS {
 *  SystemFailure,
 *  DataMissing,
 *  UnexpectedDataValue,
 *  FacilityNotSupported,
 *  UnknownSubscriber,
 *  NumberChanged,
 *  -- NumberChanged must not be used in version 1
 *  BearerServiceNotProvisioned,
 *  TeleserviceNotProvisioned,
 *  AbsentSubscriber,
 *  CallBarred,
 *  CUG-Reject,
 *  -- CUG-Reject must not be used in version 1
 *  ForwardingViolation}
 *
 *  MAP V3:
 *  SendRoutingInfoArg ::= SEQUENCE {
 *  msisdn [0] ISDN-AddressString,
 *  cug-CheckInfo [1] CUG-CheckInfo OPTIONAL,
 *  numberOfForwarding [2] NumberOfForwarding OPTIONAL,
 *  interrogationType [3] InterrogationType,
 *  or-Interrogation [4] NULL OPTIONAL*,
 *  or-Capability [5] OR-Phase OPTIONAL,
 *  gmsc-OrGsmSCF-Address [6] ISDN-AddressString,
 *  callReferenceNumber [7] CallReferenceNumber OPTIONAL,
 *  forwardingReason [8] ForwardingReason OPTIONAL,
 *  basicServiceGroup [9] Ext-BasicServiceCode OPTIONAL,
 *  networkSignalInfo [10] ExternalSignalInfo OPTIONAL,
 *  camelInfo [11] CamelInfo OPTIONAL,
 *  suppressionOfAnnouncement [12] SuppressionOfAnnouncement OPTIONAL,
 *  extensionContainer [13] ExtensionContainer OPTIONAL,
 *  ...,
 *  alertingPattern [14] AlertingPattern OPTIONAL,
 *  ccbs-Call [15] NULL OPTIONAL*,
 *  supportedCCBS-Phase [16] SupportedCCBS-Phase OPTIONAL,
 *  additionalSignalInfo [17] Ext-ExternalSignalInfo OPTIONAL,
 *  istSupportIndicator [18] IST-SupportIndicator OPTIONAL,
 *  pre-pagingSupported [19] NULL OPTIONAL*,
 *  callDiversionTreatmentIndicator [20] CallDiversionTreatmentIndicator OPTIONAL,
 *  longFTN-Supported [21] NULL OPTIONAL*,
 *  suppress-VT-CSI [22] NULL OPTIONAL*,
 *  suppressIncomingCallBarring [23] NULL OPTIONAL*,
 *  gsmSCF-InitiatedCall [24] NULL OPTIONAL*,
 *  basicServiceGroup2 [25] Ext-BasicServiceCode OPTIONAL,
 *  networkSignalInfo2 [26] ExternalSignalInfo OPTIONAL,
 *  suppressMTSS [27] SuppressMTSS OPTIONAL,
 *  mtRoamingRetrySupported [28] NULL OPTIONAL*,
 *  callPriority [29] EMLPP-Priority OPTIONAL }
 *
 *  MAP V2:
 *  SendRoutingInfoArg ::= SEQUENCE {
 *  msisdn [0] ISDN-AddressString,
 *  cug-CheckInfo [1] CUG-CheckInfo OPTIONAL,
 *  -- cug-CheckInfo must be absent in version 1
 *  numberOfForwarding [2] NumberOfForwarding OPTIONAL,
 *  networkSignalInfo [10] ExternalSignalInfo OPTIONAL,
 *  ...}
 *
 *  SuppressionOfAnnouncement ::= NULL
 *
 *  NumberOfForwarding ::= INTEGER (1..5)
 *
 *  OR-Phase ::= INTEGER (1..127)
 *
 *  SupportedCCBS-Phase ::= INTEGER (1..127)
 *  -- exception handling:
 *  -- Only value 1 is used.
 *  -- Values in the ranges 2-127 are reserved for future use.
 *  -- If received values 2-127 shall be mapped on to value 1.
 * </pre>
 *
 * @author cristian veliscu
 */
public interface SendRoutingInformationRequest extends CallHandlingMessage {
    ISDNAddressString getMsisdn(); // OCTET STRING

    CUGCheckInfo getCUGCheckInfo(); // SEQUENCE

    Integer getNumberOfForwarding(); // INTEGER

    InterrogationType getInterogationType(); // ENUMERATED

    boolean getORInterrogation(); // NULL

    Integer getORCapability(); // NULL

    ISDNAddressString getGmscOrGsmSCFAddress(); // OCTET STRING

    CallReferenceNumber getCallReferenceNumber(); // OCTET STRING

    ForwardingReason getForwardingReason(); // ENUMERATED

    ExtBasicServiceCode getBasicServiceGroup(); // CHOICE

    ExternalSignalInfo getNetworkSignalInfo(); // SEQUENCE

    CamelInfo getCamelInfo(); // SEQUENCE

    boolean getSuppressionOfAnnouncement(); // NULL

    MAPExtensionContainer getExtensionContainer(); // SEQUENCE

    AlertingPattern getAlertingPattern(); // OCTET STRING

    boolean getCCBSCall(); // NULL

    Integer getSupportedCCBSPhase(); // INTEGER

    ExtExternalSignalInfo getAdditionalSignalInfo(); // SEQUENCE

    ISTSupportIndicator getIstSupportIndicator(); // ENUMERATED

    boolean getPrePagingSupported(); // NULL

    CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator(); // OCTET STRING

    boolean getLongFTNSupported(); // NULL

    boolean getSuppressVtCSI(); // NULL

    boolean getSuppressIncomingCallBarring(); // NULL

    boolean getGsmSCFInitiatedCall(); // NULL

    ExtBasicServiceCode getBasicServiceGroup2(); // CHOICE

    ExternalSignalInfo getNetworkSignalInfo2(); // SEQUENCE

    SuppressMTSS getSuppressMTSS(); // BIT STRING

    boolean getMTRoamingRetrySupported(); // NULL

    EMLPPPriority getCallPriority(); // INTEGER

    long getMapProtocolVersion();
}
