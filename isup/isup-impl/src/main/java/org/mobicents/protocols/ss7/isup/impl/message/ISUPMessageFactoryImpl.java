/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:12:19:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.ChargeInformationMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ConfusionMessage;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityCheckRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityAcceptedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ForwardTransferMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.protocols.ss7.isup.message.InformationMessage;
import org.mobicents.protocols.ss7.isup.message.InformationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.LoopPreventionMessage;
import org.mobicents.protocols.ss7.isup.message.LoopbackAckMessage;
import org.mobicents.protocols.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.protocols.ss7.isup.message.OverloadMessage;
import org.mobicents.protocols.ss7.isup.message.PassAlongMessage;
import org.mobicents.protocols.ss7.isup.message.PreReleaseInformationMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.ResumeMessage;
import org.mobicents.protocols.ss7.isup.message.SegmentationMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentDirectoryNumberMessage;
import org.mobicents.protocols.ss7.isup.message.SuspendMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.UnequippedCICMessage;
import org.mobicents.protocols.ss7.isup.message.UserToUserInformationMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartTestMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CCSS;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ChargedPartyIdentification;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.ContinuityIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CorrelationID;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.HopCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LoopPreventionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.QueryOnReleaseCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.SCFID;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.SubsequentNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDCapabilityIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:12:19:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPMessageFactoryImpl implements ISUPMessageFactory {

    private static class MessageIndexingPlaceHolder {
        int commandCode;
        // magic
        Set<Integer> mandatoryCodes;
        Set<Integer> mandatoryVariableCodes;
        Set<Integer> optionalCodes;

        Map<Integer, Integer> mandatoryCodeToIndex;
        Map<Integer, Integer> mandatoryVariableCodeToIndex;
        Map<Integer, Integer> optionalCodeToIndex;

    }

    private ISUPParameterFactory parameterFactory;

    // ACM
    private static final MessageIndexingPlaceHolder _ACM_HOLDER;
    // ANM
    private static final MessageIndexingPlaceHolder _ANM_HOLDER;
    // APT
    private static final MessageIndexingPlaceHolder _APT_HOLDER;
    // BLO
    private static final MessageIndexingPlaceHolder _BLO_HOLDER;
    // BLA
    private static final MessageIndexingPlaceHolder _BLA_HOLDER;
    // CPG
    private static final MessageIndexingPlaceHolder _CPG_HOLDER;
    // CGB
    private static final MessageIndexingPlaceHolder _CGB_HOLDER;
    // CGBA
    private static final MessageIndexingPlaceHolder _CGBA_HOLDER;
    // CQM
    private static final MessageIndexingPlaceHolder _CQM_HOLDER;
    // CQR
    private static final MessageIndexingPlaceHolder _CQR_HOLDER;
    // GRS
    private static final MessageIndexingPlaceHolder _GRS_HOLDER;
    // GRA
    private static final MessageIndexingPlaceHolder _GRA_HOLDER;
    // CGU
    private static final MessageIndexingPlaceHolder _CGU_HOLDER;
    // CGUA
    private static final MessageIndexingPlaceHolder _CGUA_HOLDER;
    // CNF
    private static final MessageIndexingPlaceHolder _CNF_HOLDER;
    // CON
    private static final MessageIndexingPlaceHolder _CON_HOLDER;
    // COT
    private static final MessageIndexingPlaceHolder _COT_HOLDER;
    // CCR
    private static final MessageIndexingPlaceHolder _CCR_HOLDER;
    // FAC
    private static final MessageIndexingPlaceHolder _FAC_HOLDER;
    // FAA
    private static final MessageIndexingPlaceHolder _FAA_HOLDER;
    // FRJ
    private static final MessageIndexingPlaceHolder _FRJ_HOLDER;
    // FAR
    private static final MessageIndexingPlaceHolder _FAR_HOLDER;
    // FOT
    private static final MessageIndexingPlaceHolder _FOT_HOLDER;
    // IDR
    private static final MessageIndexingPlaceHolder _IDR_HOLDER;
    // IRS
    private static final MessageIndexingPlaceHolder _IRS_HOLDER;
    // INR
    private static final MessageIndexingPlaceHolder _INR_HOLDER;
    // INF
    private static final MessageIndexingPlaceHolder _INF_HOLDER;
    // IAM
    private static final MessageIndexingPlaceHolder _IAM_HOLDER;
    // LPA
    private static final MessageIndexingPlaceHolder _LPA_HOLDER;
    // LPP
    private static final MessageIndexingPlaceHolder _LPP_HOLDER;
    // NRM
    private static final MessageIndexingPlaceHolder _NRM_HOLDER;
    // OLM
    private static final MessageIndexingPlaceHolder _OLM_HOLDER;
    // PAM -- pam does not need that.
    // PRI
    private static final MessageIndexingPlaceHolder _PRI_HOLDER;
    // REL
    private static final MessageIndexingPlaceHolder _REL_HOLDER;
    // RLC
    private static final MessageIndexingPlaceHolder _RLC_HOLDER;
    // RSC
    private static final MessageIndexingPlaceHolder _RSC_HOLDER;
    // RES
    private static final MessageIndexingPlaceHolder _RES_HOLDER;
    // SGM
    private static final MessageIndexingPlaceHolder _SGM_HOLDER;
    // SAM
    private static final MessageIndexingPlaceHolder _SAM_HOLDER;
    // SDN
    private static final MessageIndexingPlaceHolder _SDN_HOLDER;
    // SUS
    private static final MessageIndexingPlaceHolder _SUS_HOLDER;
    // UBL
    private static final MessageIndexingPlaceHolder _UBL_HOLDER;
    // UBA
    private static final MessageIndexingPlaceHolder _UBA_HOLDER;
    // UCIC
    private static final MessageIndexingPlaceHolder _UCIC_HOLDER;
    // UPA
    private static final MessageIndexingPlaceHolder _UPA_HOLDER;
    // UPT
    private static final MessageIndexingPlaceHolder _UPT_HOLDER;
    // U2UI
    private static final MessageIndexingPlaceHolder _U2U_HOLDER;

    // TODO: remove this, change to use arrays.
    static {
        Set<Integer> mandatoryCodes = new HashSet<Integer>();
        Set<Integer> mandatoryVariableCodes = new HashSet<Integer>();
        Set<Integer> optionalCodes = new HashSet<Integer>();

        Map<Integer, Integer> mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        Map<Integer, Integer> mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        Map<Integer, Integer> optionalCodeToIndex = new HashMap<Integer, Integer>();

        // ACM
        mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);
        mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_F_BackwardCallIndicators);

        optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(CauseIndicators._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
        optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(CallDiversionInformation._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
        optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(UIDActionIndicators._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);
        optionalCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
        optionalCodes.add(HTRInformation._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);

        optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CauseIndicators);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_TransmissionMediumUsed);
        optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_EchoControlInformation);
        optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_AccessDeliveryInformation);
        optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectionNumber);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_CallDiversionInformation);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_NetworkSpecificFacility);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_RedirectionNumberRestriction);
        optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UIDActionIndicators);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_ApplicationTransportParameter);
        optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_CCNRPossibleIndicator);
        optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_HTRInformation);
        optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
                AddressCompleteMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectStatus);

        MessageIndexingPlaceHolder ACM_HOLDER = new MessageIndexingPlaceHolder();
        ACM_HOLDER.commandCode = AddressCompleteMessage.MESSAGE_CODE;
        ACM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        ACM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        ACM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        ACM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        ACM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        ACM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _ACM_HOLDER = ACM_HOLDER;

        // ANM
        optionalCodes.add(BackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
        optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);
        optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
        optionalCodes.add(DisplayInformation._PARAMETER_CODE);
        optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);

        optionalCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_BackwardCallIndicators);
        optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ConnectedNumber);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex
                .put(AccessDeliveryInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_AccessDeliveryInformation);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_BackwardGVNS);
        optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_CallHistoryInformation);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_GenericNumber);
        optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_TransmissionMediumUsed);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_NetworkSpecificFacility);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectionNumber);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_EchoControlInformation);
        optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_RedirectionNumberRestriction);
        optionalCodeToIndex.put(DisplayInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_DisplayInformation);
        optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_ApplicationTransportParameter);
        optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
                AnswerMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectStatus);

        MessageIndexingPlaceHolder ANM_HOLDER = new MessageIndexingPlaceHolder();
        ANM_HOLDER.commandCode = AnswerMessage.MESSAGE_CODE;
        ANM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        ANM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        ANM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        ANM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        ANM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        ANM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _ANM_HOLDER = ANM_HOLDER;

        // APT

        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);

        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE,ApplicationTransportMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,ApplicationTransportMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,ApplicationTransportMessageImpl._INDEX_O_ApplicationTransportParameter);
        MessageIndexingPlaceHolder APT_HOLDER = new MessageIndexingPlaceHolder();
        APT_HOLDER.commandCode = ApplicationTransportMessage.MESSAGE_CODE;
        APT_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        APT_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        APT_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        APT_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        APT_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        APT_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);


        _APT_HOLDER=APT_HOLDER;
        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        // BLO
        MessageIndexingPlaceHolder BLO_HOLDER = new MessageIndexingPlaceHolder();
        BLO_HOLDER.commandCode = BlockingMessage.MESSAGE_CODE;
        BLO_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        BLO_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        BLO_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        BLO_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        BLO_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        BLO_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _BLO_HOLDER = BLO_HOLDER;
        // BLA
        MessageIndexingPlaceHolder BLA_HOLDER = new MessageIndexingPlaceHolder();
        BLA_HOLDER.commandCode = BlockingAckMessage.MESSAGE_CODE;
        BLA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        BLA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        BLA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        BLA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        BLA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        BLA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _BLA_HOLDER = BLA_HOLDER;

        // CPG
        mandatoryCodes.add(EventInformation._PARAMETER_CODE);
        mandatoryCodeToIndex.put(EventInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_F_EventInformation);

        optionalCodes.add(CauseIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(BackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
        optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(CallDiversionInformation._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
        optionalCodes.add(CallTransferNumber._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
        optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
        optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);
        optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
        optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(UIDActionIndicators._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);
        optionalCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);

        optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CauseIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex
                .put(BackwardCallIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_BackwardCallIndicators);
        optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectionNumber);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_NetworkSpecificFacility);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex
                .put(TransmissionMediumUsed._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_TransmissionMediumUsed);
        optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_AccessDeliveryInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_CallDiversionInformation);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_RedirectionNumberRestriction);
        optionalCodeToIndex.put(CallTransferNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallTransferNumber);
        optionalCodeToIndex
                .put(EchoControlInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_EchoControlInformation);
        optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ConnectedNumber);
        optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_BackwardGVNS);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_GenericNumber);
        optionalCodeToIndex
                .put(CallHistoryInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallHistoryInformation);
        optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UIDActionIndicators);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_ApplicationTransportParameter);
        optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CCNRPossibleIndicator);
        optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
                CallProgressMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectStatus);

        MessageIndexingPlaceHolder CPG_HOLDER = new MessageIndexingPlaceHolder();
        CPG_HOLDER.commandCode = CallProgressMessage.MESSAGE_CODE;
        CPG_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CPG_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CPG_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CPG_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CPG_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CPG_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CPG_HOLDER = CPG_HOLDER;

        // CGB
        mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
        mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
                CircuitGroupBlockingMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupBlockingMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder CGB_HOLDER = new MessageIndexingPlaceHolder();
        CGB_HOLDER.commandCode = CircuitGroupBlockingMessage.MESSAGE_CODE;
        CGB_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CGB_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CGB_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CGB_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CGB_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CGB_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CGB_HOLDER = CGB_HOLDER;

        // CGBA
        mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
        mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
                CircuitGroupBlockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupBlockingAckMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder CGBA_HOLDER = new MessageIndexingPlaceHolder();
        CGBA_HOLDER.commandCode = CircuitGroupBlockingAckMessage.MESSAGE_CODE;
        CGBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CGBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CGBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CGBA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CGBA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CGBA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CGBA_HOLDER = CGBA_HOLDER;

        // CQM
        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupQueryMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder CQM_HOLDER = new MessageIndexingPlaceHolder();
        CQM_HOLDER.commandCode = CircuitGroupQueryResponseMessage.MESSAGE_CODE;
        CQM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CQM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CQM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CQM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CQM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CQM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CQM_HOLDER = CQM_HOLDER;

        // CQR
        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodes.add(CircuitStateIndicator._PARAMETER_CODE);

        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupQueryResponseMessageImpl._INDEX_V_RangeAndStatus);
        mandatoryVariableCodeToIndex.put(CircuitStateIndicator._PARAMETER_CODE,
                CircuitGroupQueryResponseMessageImpl._INDEX_V_CircuitStateIndicator);

        MessageIndexingPlaceHolder CQR_HOLDER = new MessageIndexingPlaceHolder();
        CQR_HOLDER.commandCode = CircuitGroupQueryMessage.MESSAGE_CODE;
        CQR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CQR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CQR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CQR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CQR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CQR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CQR_HOLDER = CQR_HOLDER;

        // GRS
        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupResetMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder GRS_HOLDER = new MessageIndexingPlaceHolder();
        GRS_HOLDER.commandCode = CircuitGroupResetMessage.MESSAGE_CODE;
        GRS_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        GRS_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        GRS_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        GRS_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        GRS_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        GRS_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _GRS_HOLDER = GRS_HOLDER;

        // GRA
        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupResetAckMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder GRA_HOLDER = new MessageIndexingPlaceHolder();
        GRA_HOLDER.commandCode = CircuitGroupResetAckMessage.MESSAGE_CODE;
        GRA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        GRA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        GRA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        GRA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        GRA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        GRA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _GRA_HOLDER = GRA_HOLDER;

        // CGU
        mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
        mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
                CircuitGroupUnblockingMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupUnblockingMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder CGU_HOLDER = new MessageIndexingPlaceHolder();
        CGU_HOLDER.commandCode = CircuitGroupUnblockingMessage.MESSAGE_CODE;
        CGU_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CGU_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CGU_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CGU_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CGU_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CGU_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CGU_HOLDER = CGU_HOLDER;

        // CGUA
        mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
        mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
                CircuitGroupUnblockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

        mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE,
                CircuitGroupUnblockingAckMessageImpl._INDEX_V_RangeAndStatus);

        MessageIndexingPlaceHolder CGUA_HOLDER = new MessageIndexingPlaceHolder();
        CGUA_HOLDER.commandCode = CircuitGroupUnblockingAckMessage.MESSAGE_CODE;
        CGUA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CGUA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CGUA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CGUA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CGUA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CGUA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CGUA_HOLDER = CGUA_HOLDER;

        // CNF
        mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
        mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
                CircuitGroupUnblockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

        mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ConfusionMessageImpl._INDEX_V_CauseIndicators);

        MessageIndexingPlaceHolder CNF_HOLDER = new MessageIndexingPlaceHolder();
        CNF_HOLDER.commandCode = ConfusionMessage.MESSAGE_CODE;
        CNF_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CNF_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CNF_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CNF_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CNF_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CNF_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CNF_HOLDER = CNF_HOLDER;

        // CON
        MessageIndexingPlaceHolder CON_HOLDER = new MessageIndexingPlaceHolder();
        mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);

        optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
        optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
        optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
        optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
        optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);
        optionalCodes.add(HTRInformation._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);

        mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE, ConnectMessageImpl._INDEX_F_BackwardCallIndicators);

        optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
        optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_BackwardGVNS);
        optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_ConnectedNumber);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_NetworkSpecificFacility);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_TransmissionMediumUsed);
        optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_EchoControlInformation);
        optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_AccessDeliveryInformation);
        optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_CallHistoryInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_GenericNumber);
        optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_RedirectionNumberRestriction);
        optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_HTRInformation);
        optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
                ConnectMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, ConnectMessageImpl._INDEX_O_RedirectStatus);

        CON_HOLDER.commandCode = ConnectMessage.MESSAGE_CODE;
        CON_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CON_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CON_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CON_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CON_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CON_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _CON_HOLDER = CON_HOLDER;

        // COT
        mandatoryCodes.add(ContinuityIndicators._PARAMETER_CODE);

        mandatoryCodeToIndex.put(ContinuityIndicators._PARAMETER_CODE, ContinuityMessageImpl._INDEX_F_ContinuityIndicators);

        MessageIndexingPlaceHolder COT_HOLDER = new MessageIndexingPlaceHolder();
        COT_HOLDER.commandCode = ContinuityMessage.MESSAGE_CODE;
        COT_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        COT_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        COT_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        COT_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        COT_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        COT_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _COT_HOLDER = COT_HOLDER;

        // CCR
        MessageIndexingPlaceHolder CCR_HOLDER = new MessageIndexingPlaceHolder();
        CCR_HOLDER.commandCode = ContinuityCheckRequestMessage.MESSAGE_CODE;
        CCR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        CCR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        CCR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        CCR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        CCR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        CCR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _CCR_HOLDER = CCR_HOLDER;
        // FAC
        MessageIndexingPlaceHolder FAC_HOLDER = new MessageIndexingPlaceHolder();
        FAC_HOLDER.commandCode = FacilityMessage.MESSAGE_CODE;

        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(CallTransferNumber._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingIndicators._PARAMETER_CODE);
        optionalCodes.add(PivotStatus._PARAMETER_CODE);
        optionalCodes.add(PivotCounter._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);
        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(CallTransferNumber._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_CallTransferNumber);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_RedirectionNumber);
        optionalCodeToIndex.put(PivotRoutingIndicators._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_PivotRoutingIndicators);
        optionalCodeToIndex.put(PivotStatus._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_PivotStatus);
        optionalCodeToIndex.put(PivotCounter._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_PivotCounter);
        optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, FacilityMessageImpl._INDEX_O_RedirectStatus);


        FAC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        FAC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        FAC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        FAC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        FAC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        FAC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _FAC_HOLDER = FAC_HOLDER;
        // FAA
        MessageIndexingPlaceHolder FAA_HOLDER = new MessageIndexingPlaceHolder();
        FAA_HOLDER.commandCode = FacilityAcceptedMessage.MESSAGE_CODE;

        mandatoryCodes.add(FacilityIndicator._PARAMETER_CODE);
        mandatoryCodeToIndex.put(FacilityIndicator._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_F_FacilityIndicator);

        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_ConnectionRequest);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_ParameterCompatibilityInformation);


        FAA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        FAA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        FAA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        FAA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        FAA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        FAA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _FAA_HOLDER = FAA_HOLDER;
        // FRJ
        MessageIndexingPlaceHolder FRJ_HOLDER = new MessageIndexingPlaceHolder();
        FRJ_HOLDER.commandCode = FacilityRejectedMessage.MESSAGE_CODE;

        mandatoryCodes.add(FacilityIndicator._PARAMETER_CODE);
        mandatoryCodeToIndex.put(FacilityIndicator._PARAMETER_CODE, FacilityRejectedMessageImpl._INDEX_F_FacilityIndicator);

        mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, FacilityRejectedMessageImpl._INDEX_V_CauseIndicators);

        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, FacilityRejectedMessageImpl._INDEX_O_UserToUserIndicators);

        FRJ_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        FRJ_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        FRJ_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        FRJ_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        FRJ_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        FRJ_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _FRJ_HOLDER = FRJ_HOLDER;

        // FAR
        MessageIndexingPlaceHolder FAR_HOLDER = new MessageIndexingPlaceHolder();
        FAR_HOLDER.commandCode = FacilityRequestMessage.MESSAGE_CODE;

        mandatoryCodes.add(FacilityIndicator._PARAMETER_CODE);
        mandatoryCodeToIndex.put(FacilityIndicator._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_F_FacilityIndicator);

        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_UserToUserIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_ConnectionRequest);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, AbstractFacilityMessageImpl._INDEX_O_ParameterCompatibilityInformation);

        FAR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        FAR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        FAR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        FAR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        FAR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        FAR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _FAR_HOLDER = FAR_HOLDER;
        // FOT
        MessageIndexingPlaceHolder FOT_HOLDER = new MessageIndexingPlaceHolder();
        FOT_HOLDER.commandCode = ForwardTransferMessage.MESSAGE_CODE;

        optionalCodes.add(CallReference._PARAMETER_CODE);

        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, ForwardTransferMessageImpl._INDEX_O_CallReference);

        FOT_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        FOT_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        FOT_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        FOT_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        FOT_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        FOT_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _FOT_HOLDER = FOT_HOLDER;
        // IDR
        MessageIndexingPlaceHolder IDR_HOLDER = new MessageIndexingPlaceHolder();
        IDR_HOLDER.commandCode = IdentificationRequestMessage.MESSAGE_CODE;

        optionalCodes.add(MCIDRequestIndicators._PARAMETER_CODE);
        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);

        optionalCodeToIndex.put(MCIDRequestIndicators._PARAMETER_CODE, IdentificationRequestMessageImpl._INDEX_O_MCIDRequestIndicators);
        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE, IdentificationRequestMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, IdentificationRequestMessageImpl._INDEX_O_ParameterCompatibilityInformation);


        IDR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        IDR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        IDR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        IDR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        IDR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        IDR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _IDR_HOLDER = IDR_HOLDER;
        // IRS
        MessageIndexingPlaceHolder IRS_HOLDER = new MessageIndexingPlaceHolder();
        IRS_HOLDER.commandCode = IdentificationResponseMessage.MESSAGE_CODE;

        optionalCodes.add(MCIDResponseIndicators._PARAMETER_CODE);
        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(CallingPartyNumber._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);
        optionalCodes.add(ChargedPartyIdentification._PARAMETER_CODE);

        optionalCodeToIndex.put(MCIDResponseIndicators._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_MCIDResponseIndicators);
        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(CallingPartyNumber._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_CallingPartyNumber);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_GenericNumber);
        optionalCodeToIndex.put(ChargedPartyIdentification._PARAMETER_CODE, IdentificationResponseMessageImpl._INDEX_O_ChargedPartyIdentification);


        IRS_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        IRS_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        IRS_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        IRS_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        IRS_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        IRS_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _IRS_HOLDER = IRS_HOLDER;
        // FIXME: INR
        MessageIndexingPlaceHolder INR_HOLDER = new MessageIndexingPlaceHolder();

        mandatoryCodes.add(InformationRequestIndicators._PARAMETER_CODE);

        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);

        mandatoryCodeToIndex.put(InformationRequestIndicators._PARAMETER_CODE,
                InformationRequestMessageImpl._INDEX_F_InformationRequestIndicators);

        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, InformationRequestMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                InformationRequestMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,
                InformationRequestMessageImpl._INDEX_O_NetworkSpecificFacility);

        INR_HOLDER.commandCode = InformationRequestMessage.MESSAGE_CODE;
        INR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        INR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        INR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        INR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        INR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        INR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _INR_HOLDER = INR_HOLDER;
        // FIXME: INF
        MessageIndexingPlaceHolder INF_HOLDER = new MessageIndexingPlaceHolder();

        mandatoryCodes.add(InformationIndicators._PARAMETER_CODE);

        optionalCodes.add(CallingPartyCategory._PARAMETER_CODE);
        optionalCodes.add(CallingPartyNumber._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);

        mandatoryCodeToIndex.put(InformationIndicators._PARAMETER_CODE, InformationMessageImpl._INDEX_F_InformationIndicators);

        optionalCodeToIndex.put(CallingPartyCategory._PARAMETER_CODE, InformationMessageImpl._INDEX_O_CallingPartyCategory);
        optionalCodeToIndex.put(CallingPartyNumber._PARAMETER_CODE, InformationMessageImpl._INDEX_O_CallingPartyNumber);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, InformationMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, InformationMessageImpl._INDEX_O_ConnectionRequest);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                InformationMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,
                InformationMessageImpl._INDEX_O_NetworkSpecificFacility);

        INF_HOLDER.commandCode = InformationMessage.MESSAGE_CODE;
        INF_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        INF_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        INF_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        INF_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        INF_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        INF_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _INF_HOLDER = INF_HOLDER;
        // IAM
        mandatoryCodes.add(NatureOfConnectionIndicators._PARAMETER_CODE);
        mandatoryCodes.add(ForwardCallIndicators._PARAMETER_CODE);
        mandatoryCodes.add(CallingPartyCategory._PARAMETER_CODE);
        mandatoryCodes.add(TransmissionMediumRequirement._PARAMETER_CODE);

        mandatoryCodeToIndex.put(NatureOfConnectionIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_F_NatureOfConnectionIndicators);
        mandatoryCodeToIndex.put(ForwardCallIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_F_ForwardCallIndicators);
        mandatoryCodeToIndex.put(CallingPartyCategory._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_CallingPartyCategory);
        mandatoryCodeToIndex.put(TransmissionMediumRequirement._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_F_TransmissionMediumRequirement);

        mandatoryVariableCodes.add(CalledPartyNumber._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(CalledPartyNumber._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_V_CalledPartyNumber);

        optionalCodes.add(TransitNetworkSelection._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        optionalCodes.add(CallingPartyNumber._PARAMETER_CODE);
        optionalCodes.add(OptionalForwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(RedirectingNumber._PARAMETER_CODE);
        optionalCodes.add(RedirectionInformation._PARAMETER_CODE);
        optionalCodes.add(ClosedUserGroupInterlockCode._PARAMETER_CODE);
        optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
        optionalCodes.add(OriginalCalledNumberImpl._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(UserServiceInformation._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);
        optionalCodes.add(PropagationDelayCounter._PARAMETER_CODE);
        optionalCodes.add(UserServiceInformationPrime._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(GenericDigits._PARAMETER_CODE);
        optionalCodes.add(OriginatingISCPointCode._PARAMETER_CODE);
        optionalCodes.add(UserTeleserviceInformation._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(ServiceActivation._PARAMETER_CODE);
        optionalCodes.add(GenericReference._PARAMETER_CODE);
        optionalCodes.add(MLPPPrecedence._PARAMETER_CODE);
        optionalCodes.add(TransimissionMediumRequierementPrime._PARAMETER_CODE);
        optionalCodes.add(LocationNumber._PARAMETER_CODE);
        optionalCodes.add(ForwardGVNS._PARAMETER_CODE);
        optionalCodes.add(CCSS._PARAMETER_CODE);
        optionalCodes.add(NetworkManagementControls._PARAMETER_CODE);
        optionalCodes.add(CircuitAssigmentMap._PARAMETER_CODE);
        optionalCodes.add(CorrelationID._PARAMETER_CODE);
        optionalCodes.add(CallDiversionTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(CalledINNumber._PARAMETER_CODE);
        optionalCodes.add(CallOfferingTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
        optionalCodes.add(SCFID._PARAMETER_CODE);
        optionalCodes.add(UIDCapabilityIndicators._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
        optionalCodes.add(HopCounter._PARAMETER_CODE);
        optionalCodes.add(CollectCallRequest._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);
        optionalCodes.add(PivotCapability._PARAMETER_CODE);
        optionalCodes.add(CalledDirectoryNumber._PARAMETER_CODE);
        optionalCodes.add(OriginalCalledINNumber._PARAMETER_CODE);
        optionalCodes.add(NetworkRoutingNumber._PARAMETER_CODE);
        optionalCodes.add(QueryOnReleaseCapability._PARAMETER_CODE);
        optionalCodes.add(PivotCounter._PARAMETER_CODE);
        optionalCodes.add(PivotRoutingForwardInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectCapability._PARAMETER_CODE);
        optionalCodes.add(RedirectCounter._PARAMETER_CODE);
        optionalCodes.add(RedirectStatus._PARAMETER_CODE);
        optionalCodes.add(RedirectForwardInformation._PARAMETER_CODE);

        optionalCodeToIndex.put(TransitNetworkSelection._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_TransitNetworkSelection);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CallReference);
        optionalCodeToIndex.put(CallingPartyNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CallingPartyNumber);
        optionalCodeToIndex.put(OptionalForwardCallIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_OptionalForwardCallIndicators);
        optionalCodeToIndex.put(RedirectingNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectingNumber);
        optionalCodeToIndex.put(RedirectionInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_RedirectionInformation);
        optionalCodeToIndex.put(ClosedUserGroupInterlockCode._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_ClosedUserGroupInterlockCode);
        optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ConnectionRequest);
        optionalCodeToIndex.put(OriginalCalledNumberImpl._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_OriginalCalledNumber);
        optionalCodeToIndex
                .put(UserToUserInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(UserServiceInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_UserServiceInformation);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_User2UIndicators);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericNumber);
        optionalCodeToIndex.put(PropagationDelayCounter._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_PropagationDelayCounter);
        optionalCodeToIndex.put(UserServiceInformationPrime._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_UserServiceInformationPrime);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_NetworkSPecificFacility);
        optionalCodeToIndex.put(GenericDigits._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericDigits);
        optionalCodeToIndex.put(OriginatingISCPointCode._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_OriginatingISCPointCode);
        optionalCodeToIndex.put(UserTeleserviceInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_UserTeleserviceInformation);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ServiceActivation);
        optionalCodeToIndex.put(GenericReference._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericReference);
        optionalCodeToIndex.put(MLPPPrecedence._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_MLPPPrecedence);
        optionalCodeToIndex.put(TransimissionMediumRequierementPrime._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_TransimissionMediumRequierementPrime);
        optionalCodeToIndex.put(LocationNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_LocationNumber);
        optionalCodeToIndex.put(ForwardGVNS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ForwardGVNS);
        optionalCodeToIndex.put(CCSS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CCSS);
        optionalCodeToIndex.put(NetworkManagementControls._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_NetworkManagementControls);
        optionalCodeToIndex.put(CircuitAssigmentMap._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_CircuitAssigmentMap);
        optionalCodeToIndex.put(CorrelationID._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CorrelationID);
        optionalCodeToIndex.put(CallDiversionTreatmentIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_CallDiversionTreatmentIndicators);
        optionalCodeToIndex.put(CalledINNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CalledINNumber);
        optionalCodeToIndex.put(CallOfferingTreatmentIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_CallOfferingTreatmentIndicators);
        optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
        optionalCodeToIndex.put(SCFID._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_SCFID);
        optionalCodeToIndex.put(UIDCapabilityIndicators._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_UIDCapabilityIndicators);
        optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_EchoControlInformation);
        optionalCodeToIndex.put(HopCounter._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_HopCounter);
        optionalCodeToIndex.put(CollectCallRequest._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_CollectCallRequest);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_ApplicationTransport);
        optionalCodeToIndex.put(PivotCapability._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_PivotCapability);
        optionalCodeToIndex.put(CalledDirectoryNumber._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_CalledDirectoryNumber);
        optionalCodeToIndex.put(OriginalCalledINNumber._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_OriginalCalledINNumber);
        optionalCodeToIndex.put(NetworkRoutingNumber._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_NetworkRoutingNumber);
        optionalCodeToIndex.put(QueryOnReleaseCapability._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_QueryOnReleaseCapability);
        optionalCodeToIndex.put(PivotCounter._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_PivotCounter);
        optionalCodeToIndex.put(PivotRoutingForwardInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_PivotRoutingForwardInformation);
        optionalCodeToIndex.put(RedirectCapability._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_RedirectCapability);
        optionalCodeToIndex.put(RedirectCounter._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectCounter);
        optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectStatus);
        optionalCodeToIndex.put(RedirectForwardInformation._PARAMETER_CODE,
                InitialAddressMessageImpl._INDEX_O_RedirectForwardInformation);


        MessageIndexingPlaceHolder IAM_HOLDER = new MessageIndexingPlaceHolder();
        IAM_HOLDER.commandCode = InitialAddressMessage.MESSAGE_CODE;
        IAM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        IAM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        IAM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        IAM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        IAM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        IAM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _IAM_HOLDER = IAM_HOLDER;

        // LPA
        MessageIndexingPlaceHolder LPA_HOLDER = new MessageIndexingPlaceHolder();
        LPA_HOLDER.commandCode = LoopbackAckMessage.MESSAGE_CODE;
        LPA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        LPA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        LPA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        LPA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        LPA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        LPA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _LPA_HOLDER = LPA_HOLDER;
        // LPP
        MessageIndexingPlaceHolder LPP_HOLDER = new MessageIndexingPlaceHolder();
        LPP_HOLDER.commandCode = LoopPreventionMessage.MESSAGE_CODE;

        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(CallTransferReference._PARAMETER_CODE);
        optionalCodes.add(LoopPreventionIndicators._PARAMETER_CODE);

        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE,LoopPreventionMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,LoopPreventionMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(CallTransferReference._PARAMETER_CODE,LoopPreventionMessageImpl._INDEX_O_CallTransferReference);
        optionalCodeToIndex.put(LoopPreventionIndicators._PARAMETER_CODE,LoopPreventionMessageImpl._INDEX_O_LoopPreventionIndicators);

        LPP_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        LPP_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        LPP_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        LPP_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        LPP_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        LPP_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _LPP_HOLDER = LPP_HOLDER;
        // NRM
        MessageIndexingPlaceHolder NRM_HOLDER = new MessageIndexingPlaceHolder();
        NRM_HOLDER.commandCode = NetworkResourceManagementMessage.MESSAGE_CODE;

        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(EchoControlInformation._PARAMETER_CODE);


        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE,NetworkResourceManagementMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,NetworkResourceManagementMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE,NetworkResourceManagementMessageImpl._INDEX_O_EchoControlInformation);


        NRM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        NRM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        NRM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        NRM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        NRM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        NRM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _NRM_HOLDER = NRM_HOLDER;
        // OLM
        MessageIndexingPlaceHolder OLM_HOLDER = new MessageIndexingPlaceHolder();
        OLM_HOLDER.commandCode = OverloadMessage.MESSAGE_CODE;
        OLM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        OLM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        OLM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        OLM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        OLM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        OLM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _OLM_HOLDER = OLM_HOLDER;
        // PAM - no need for this
        // FIXME: PRI
        MessageIndexingPlaceHolder PRI_HOLDER = new MessageIndexingPlaceHolder();
        PRI_HOLDER.commandCode = PreReleaseInformationMessage.MESSAGE_CODE;

        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(OptionalForwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
        optionalCodes.add(ApplicationTransport._PARAMETER_CODE);

        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE,PreReleaseInformationMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,PreReleaseInformationMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(OptionalForwardCallIndicators._PARAMETER_CODE,PreReleaseInformationMessageImpl._INDEX_O_OptionalForwardCallIndicators);
        optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,PreReleaseInformationMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
        optionalCodeToIndex.put(ApplicationTransport._PARAMETER_CODE,PreReleaseInformationMessageImpl._INDEX_O_ApplicationTransport);

        PRI_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        PRI_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        PRI_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        PRI_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        PRI_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        PRI_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _PRI_HOLDER = PRI_HOLDER;
        // REL
        mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ReleaseMessageImpl._INDEX_V_CauseIndicators);

        optionalCodes.add(RedirectionInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(SignalingPointCode._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(AutomaticCongestionLevel._PARAMETER_CODE);
        optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
        optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
        optionalCodes.add(DisplayInformation._PARAMETER_CODE);
        optionalCodes.add(RemoteOperations._PARAMETER_CODE);
        optionalCodes.add(HTRInformation._PARAMETER_CODE);
        optionalCodes.add(RedirectCounter._PARAMETER_CODE);
        optionalCodes.add(RedirectBackwardInformation._PARAMETER_CODE);

        optionalCodeToIndex.put(RedirectionInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectionInformation);
        optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectionNumber);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(SignalingPointCode._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_SignalingPointCode);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_U2UInformation);
        optionalCodeToIndex.put(AutomaticCongestionLevel._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_AutomaticCongestionLevel);
        optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_NetworkSpecificFacility);
        optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE,
                ReleaseMessageImpl._INDEX_O_AccessDeliveryInformation);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
                ReleaseMessageImpl._INDEX_O_ParameterCompatibilityInformation);
        optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_U2UIndicators);
        optionalCodeToIndex.put(DisplayInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_DisplayInformation);
        optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RemoteOperations);
        optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_HTRInformation);
        optionalCodeToIndex.put(RedirectCounter._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectCounter);
        optionalCodeToIndex.put(RedirectBackwardInformation._PARAMETER_CODE,
                ReleaseMessageImpl._INDEX_O_RedirectBackwardInformation);

        MessageIndexingPlaceHolder REL_HOLDER = new MessageIndexingPlaceHolder();
        REL_HOLDER.commandCode = ReleaseMessage.MESSAGE_CODE;
        REL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        REL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        REL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        REL_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        REL_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        REL_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _REL_HOLDER = REL_HOLDER;

        // RLC
        optionalCodes.add(CauseIndicators._PARAMETER_CODE);
        optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ReleaseCompleteMessageImpl._INDEX_O_CauseIndicators);

        MessageIndexingPlaceHolder RLC_HOLDER = new MessageIndexingPlaceHolder();
        RLC_HOLDER.commandCode = ReleaseCompleteMessage.MESSAGE_CODE;
        RLC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        RLC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        RLC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        RLC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        RLC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        RLC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _RLC_HOLDER = RLC_HOLDER;

        // RSC
        MessageIndexingPlaceHolder RSC_HOLDER = new MessageIndexingPlaceHolder();
        RSC_HOLDER.commandCode = ResetCircuitMessage.MESSAGE_CODE;
        RSC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        RSC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        RSC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        RSC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        RSC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        RSC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _RSC_HOLDER = RSC_HOLDER;
        // RES
        mandatoryCodes.add(SuspendResumeIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        mandatoryCodeToIndex.put(SuspendResumeIndicators._PARAMETER_CODE, ResumeMessageImpl._INDEX_F_SuspendResumeIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE,ResumeMessageImpl._INDEX_O_CallReference);

        MessageIndexingPlaceHolder RES_HOLDER = new MessageIndexingPlaceHolder();
        RES_HOLDER.commandCode = ResumeMessage.MESSAGE_CODE;
        RES_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        RES_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        RES_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        RES_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        RES_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        RES_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _RES_HOLDER = RES_HOLDER;
        // SGM
        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);
        optionalCodes.add(GenericDigits._PARAMETER_CODE);
        optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
        optionalCodes.add(GenericNumber._PARAMETER_CODE);

        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_AccessTransport);
        optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_UserToUserInformation);
        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_MessageCompatibilityInformation);
        optionalCodeToIndex.put(GenericDigits._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_GenericDigits);
        optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_GenericNotificationIndicator);
        optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE,SegmentationMessageImpl._INDEX_O_GenericNumber);


        MessageIndexingPlaceHolder SGM_HOLDER = new MessageIndexingPlaceHolder();
        SGM_HOLDER.commandCode = SegmentationMessage.MESSAGE_CODE;
        SGM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        SGM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        SGM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        SGM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        SGM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        SGM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _SGM_HOLDER = SGM_HOLDER;
        // SAM
        MessageIndexingPlaceHolder SAM_HOLDER = new MessageIndexingPlaceHolder();

        mandatoryVariableCodes.add(SubsequentNumber._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(SubsequentNumber._PARAMETER_CODE,
                SubsequentAddressMessageImpl._INDEX_V_SubsequentNumber);

        SAM_HOLDER.commandCode = SubsequentAddressMessage.MESSAGE_CODE;
        SAM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        SAM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        SAM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        SAM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        SAM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        SAM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _SAM_HOLDER = SAM_HOLDER;
        // SDN
        MessageIndexingPlaceHolder SDN_HOLDER = new MessageIndexingPlaceHolder();

        optionalCodes.add(SubsequentNumber._PARAMETER_CODE);
        optionalCodes.add(MessageCompatibilityInformation._PARAMETER_CODE);

        optionalCodeToIndex.put(SubsequentNumber._PARAMETER_CODE, SubsequentDirectoryNumberMessageImpl._INDEX_O_SubsequentNumber);
        optionalCodeToIndex.put(MessageCompatibilityInformation._PARAMETER_CODE, SubsequentDirectoryNumberMessageImpl._INDEX_O_MessageCompatibilityInformation);

        SDN_HOLDER.commandCode = SubsequentDirectoryNumberMessage.MESSAGE_CODE;
        SDN_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        SDN_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        SDN_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        SDN_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        SDN_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        SDN_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();
        _SDN_HOLDER = SDN_HOLDER;
        // SUS
        mandatoryCodes.add(SuspendResumeIndicators._PARAMETER_CODE);
        optionalCodes.add(CallReference._PARAMETER_CODE);
        mandatoryCodeToIndex.put(SuspendResumeIndicators._PARAMETER_CODE, SuspendMessageImpl._INDEX_F_SuspendResumeIndicators);
        optionalCodeToIndex.put(CallReference._PARAMETER_CODE,SuspendMessageImpl._INDEX_O_CallReference);

        MessageIndexingPlaceHolder SUS_HOLDER = new MessageIndexingPlaceHolder();
        SUS_HOLDER.commandCode = SuspendMessage.MESSAGE_CODE;
        SUS_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        SUS_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        SUS_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        SUS_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        SUS_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        SUS_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _SUS_HOLDER = SUS_HOLDER;
        // UBL
        MessageIndexingPlaceHolder UBL_HOLDER = new MessageIndexingPlaceHolder();
        UBL_HOLDER.commandCode = UnblockingMessage.MESSAGE_CODE;
        UBL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        UBL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        UBL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        UBL_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        UBL_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        UBL_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _UBL_HOLDER = UBL_HOLDER;
        // UBA
        MessageIndexingPlaceHolder UBA_HOLDER = new MessageIndexingPlaceHolder();
        UBA_HOLDER.commandCode = UnblockingAckMessage.MESSAGE_CODE;
        UBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        UBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        UBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        UBA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        UBA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        UBA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _UBA_HOLDER = UBA_HOLDER;
        // UCIC
        MessageIndexingPlaceHolder UCIC_HOLDER = new MessageIndexingPlaceHolder();
        UCIC_HOLDER.commandCode = UnequippedCICMessage.MESSAGE_CODE;
        UCIC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        UCIC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        UCIC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        UCIC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        UCIC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        UCIC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _UCIC_HOLDER = UCIC_HOLDER;
        // UPA
        MessageIndexingPlaceHolder UPA_HOLDER = new MessageIndexingPlaceHolder();
        UPA_HOLDER.commandCode = UserPartAvailableMessage.MESSAGE_CODE;

        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,UserPartAvailableMessageImpl._INDEX_O_ParameterCompatibilityInformation);

        UPA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        UPA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        UPA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        UPA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        UPA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        UPA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _UPA_HOLDER = UPA_HOLDER;
        // UPT
        MessageIndexingPlaceHolder UPT_HOLDER = new MessageIndexingPlaceHolder();
        UPT_HOLDER.commandCode = UserPartTestMessage.MESSAGE_CODE;

        optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
        optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,UserPartAvailableMessageImpl._INDEX_O_ParameterCompatibilityInformation);

        UPT_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        UPT_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        UPT_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        UPT_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        UPT_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        UPT_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _UPT_HOLDER = UPT_HOLDER;
        // U2UI
        MessageIndexingPlaceHolder U2UI_HOLDER = new MessageIndexingPlaceHolder();
        U2UI_HOLDER.commandCode = UserToUserInformationMessage.MESSAGE_CODE;

        optionalCodes.add(AccessTransport._PARAMETER_CODE);
        optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE,UserToUserInformationMessageImpl._INDEX_O_AccessTransport);

        mandatoryVariableCodes.add(UserToUserInformation._PARAMETER_CODE);
        mandatoryVariableCodeToIndex.put(UserToUserInformation._PARAMETER_CODE,UserToUserInformationMessageImpl._INDEX_V_User2UserInformation);

        U2UI_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
        U2UI_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
        U2UI_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
        U2UI_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
        U2UI_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
        U2UI_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

        mandatoryCodes = new HashSet<Integer>();
        mandatoryVariableCodes = new HashSet<Integer>();
        optionalCodes = new HashSet<Integer>();
        mandatoryCodeToIndex = new HashMap<Integer, Integer>();
        mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
        optionalCodeToIndex = new HashMap<Integer, Integer>();

        _U2U_HOLDER = U2UI_HOLDER;

    }

    public ISUPMessageFactoryImpl(ISUPParameterFactory parameterFactory) {

        this.parameterFactory = parameterFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createACM()
     */
    public AddressCompleteMessage createACM() {

        AddressCompleteMessageImpl acm = new AddressCompleteMessageImpl(_ACM_HOLDER.mandatoryCodes,
                _ACM_HOLDER.mandatoryVariableCodes, _ACM_HOLDER.optionalCodes, _ACM_HOLDER.mandatoryCodeToIndex,
                _ACM_HOLDER.mandatoryVariableCodeToIndex, _ACM_HOLDER.optionalCodeToIndex);

        return acm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createACM(int)
     */
    @Override
    public AddressCompleteMessage createACM(int cic) {
        AddressCompleteMessage acm = createACM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        acm.setCircuitIdentificationCode(code);
        return acm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createANM(int cic)
     */
    public AnswerMessage createANM() {

        AnswerMessageImpl acm = new AnswerMessageImpl(_ANM_HOLDER.mandatoryCodes, _ANM_HOLDER.mandatoryVariableCodes,
                _ANM_HOLDER.optionalCodes, _ANM_HOLDER.mandatoryCodeToIndex, _ANM_HOLDER.mandatoryVariableCodeToIndex,
                _ANM_HOLDER.optionalCodeToIndex);

        return acm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createANM(int)
     */
    @Override
    public AnswerMessage createANM(int cic) {
        AnswerMessage msg = createANM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createAPT()
     */
    @Override
    public ApplicationTransportMessage createAPT() {
        ApplicationTransportMessageImpl apt = new ApplicationTransportMessageImpl(_APT_HOLDER.mandatoryCodes, _APT_HOLDER.mandatoryVariableCodes,
                _APT_HOLDER.optionalCodes, _APT_HOLDER.mandatoryCodeToIndex, _APT_HOLDER.mandatoryVariableCodeToIndex,
                _APT_HOLDER.optionalCodeToIndex);

        return apt;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createAPT(int cic)
     */
    public ApplicationTransportMessage createAPT(int cic) {
        ApplicationTransportMessage msg = createAPT();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLA(int cic)
     */
    public BlockingAckMessage createBLA() {

        BlockingAckMessageImpl bla = new BlockingAckMessageImpl(_BLA_HOLDER.mandatoryCodes, _BLA_HOLDER.mandatoryVariableCodes,
                _BLA_HOLDER.optionalCodes, _BLA_HOLDER.mandatoryCodeToIndex, _BLA_HOLDER.mandatoryVariableCodeToIndex,
                _BLA_HOLDER.optionalCodeToIndex);

        return bla;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLA(int)
     */
    @Override
    public BlockingAckMessage createBLA(int cic) {
        BlockingAckMessage msg = createBLA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLO()
     */
    @Override
    public BlockingMessage createBLO() {
        BlockingMessageImpl blo = new BlockingMessageImpl(_BLO_HOLDER.mandatoryCodes, _BLO_HOLDER.mandatoryVariableCodes,
                _BLO_HOLDER.optionalCodes, _BLO_HOLDER.mandatoryCodeToIndex, _BLO_HOLDER.mandatoryVariableCodeToIndex,
                _BLO_HOLDER.optionalCodeToIndex);
        return blo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLO(int cic)
     */
    public BlockingMessage createBLO(int cic) {
        BlockingMessage msg = createBLO();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCCR()
     */
    @Override
    public ContinuityCheckRequestMessage createCCR() {
        ContinuityCheckRequestMessageImpl ccr = new ContinuityCheckRequestMessageImpl(_CCR_HOLDER.mandatoryCodes,
                _CCR_HOLDER.mandatoryVariableCodes, _CCR_HOLDER.optionalCodes, _CCR_HOLDER.mandatoryCodeToIndex,
                _CCR_HOLDER.mandatoryVariableCodeToIndex, _CCR_HOLDER.optionalCodeToIndex);
        return ccr;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCCR(int cic)
     */
    public ContinuityCheckRequestMessage createCCR(int cic) {
        ContinuityCheckRequestMessage msg = createCCR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGB()
     */
    @Override
    public CircuitGroupBlockingMessage createCGB() {
        CircuitGroupBlockingMessage cgb = new CircuitGroupBlockingMessageImpl(_CGB_HOLDER.mandatoryCodes,
                _CGB_HOLDER.mandatoryVariableCodes, _CGB_HOLDER.optionalCodes, _CGB_HOLDER.mandatoryCodeToIndex,
                _CGB_HOLDER.mandatoryVariableCodeToIndex, _CGB_HOLDER.optionalCodeToIndex);
        return cgb;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGB(int cic)
     */
    public CircuitGroupBlockingMessage createCGB(int cic) {
        CircuitGroupBlockingMessage msg = createCGB();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGBA(int cic)
     */
    public CircuitGroupBlockingAckMessage createCGBA() {

        CircuitGroupBlockingAckMessageImpl cgba = new CircuitGroupBlockingAckMessageImpl(_CGBA_HOLDER.mandatoryCodes,
                _CGBA_HOLDER.mandatoryVariableCodes, _CGBA_HOLDER.optionalCodes, _CGBA_HOLDER.mandatoryCodeToIndex,
                _CGBA_HOLDER.mandatoryVariableCodeToIndex, _CGBA_HOLDER.optionalCodeToIndex);

        return cgba;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGBA(int)
     */
    @Override
    public CircuitGroupBlockingAckMessage createCGBA(int cic) {
        CircuitGroupBlockingAckMessage msg = createCGBA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGU()
     */
    @Override
    public CircuitGroupUnblockingMessage createCGU() {
        CircuitGroupUnblockingMessage msg = new CircuitGroupUnblockingMessageImpl(_CGU_HOLDER.mandatoryCodes,
                _CGU_HOLDER.mandatoryVariableCodes, _CGU_HOLDER.optionalCodes, _CGU_HOLDER.mandatoryCodeToIndex,
                _CGU_HOLDER.mandatoryVariableCodeToIndex, _CGU_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGU(int cic)
     */
    public CircuitGroupUnblockingMessage createCGU(int cic) {
        CircuitGroupUnblockingMessage msg = createCGU();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGUA(int cic)
     */
    public CircuitGroupUnblockingAckMessage createCGUA() {

        CircuitGroupUnblockingAckMessage msg = new CircuitGroupUnblockingAckMessageImpl(_CGUA_HOLDER.mandatoryCodes,
                _CGUA_HOLDER.mandatoryVariableCodes, _CGUA_HOLDER.optionalCodes, _CGUA_HOLDER.mandatoryCodeToIndex,
                _CGUA_HOLDER.mandatoryVariableCodeToIndex, _CGUA_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGUA(int)
     */
    @Override
    public CircuitGroupUnblockingAckMessage createCGUA(int cic) {
        CircuitGroupUnblockingAckMessage msg = createCGUA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCIM()
     */
    @Override
    public ChargeInformationMessage createCIM() {
        // TODO:
        // ChargeInformationMessage msg = new ChargeInformationMessageImpl();
        // return msg;
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCIM(int cic)
     */
    public ChargeInformationMessage createCIM(int cic) {
        ChargeInformationMessage msg = createCIM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCNF()
     */
    @Override
    public ConfusionMessage createCNF() {
        ConfusionMessage msg = new ConfusionMessageImpl(_CNF_HOLDER.mandatoryCodes, _CNF_HOLDER.mandatoryVariableCodes,
                _CNF_HOLDER.optionalCodes, _CNF_HOLDER.mandatoryCodeToIndex, _CNF_HOLDER.mandatoryVariableCodeToIndex,
                _CNF_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCNF(int cic)
     */
    public ConfusionMessage createCNF(int cic) {
        ConfusionMessage msg = createCNF();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCommand(int)
     */
    public ISUPMessage createCommand(int commandCode) {
        switch (commandCode) {
            case InitialAddressMessage.MESSAGE_CODE:
                InitialAddressMessage IAM = createIAM();
                return IAM;
            case AddressCompleteMessage.MESSAGE_CODE:
                AddressCompleteMessage ACM = createACM();
                return ACM;
            case ReleaseMessage.MESSAGE_CODE:
                ReleaseMessage REL = createREL(0);
                return REL;
            case ReleaseCompleteMessage.MESSAGE_CODE:
                ReleaseCompleteMessage RLC = createRLC();
                return RLC;

            case ApplicationTransportMessage.MESSAGE_CODE:
                ApplicationTransportMessage APT = createAPT();
                return APT;

            case AnswerMessage.MESSAGE_CODE:
                AnswerMessage ANM = createANM();
                return ANM;

            case CallProgressMessage.MESSAGE_CODE:
                CallProgressMessage CPG = createCPG();
                return CPG;

            case CircuitGroupResetAckMessage.MESSAGE_CODE:
                CircuitGroupResetAckMessage GRA = createGRA();
                return GRA;

            case ConfusionMessage.MESSAGE_CODE:
                ConfusionMessage CFN = createCNF();
                return CFN;

            case ConnectMessage.MESSAGE_CODE:
                ConnectMessage CON = createCON();
                return CON;

            case ContinuityMessage.MESSAGE_CODE:
                ContinuityMessage COT = createCOT();
                return COT;

            case FacilityRejectedMessage.MESSAGE_CODE:
                FacilityRejectedMessage FRJ = createFRJ();
                return FRJ;

            case InformationMessage.MESSAGE_CODE:
                InformationMessage INF = createINF();
                return INF;

            case InformationRequestMessage.MESSAGE_CODE:
                InformationRequestMessage INR = createINR();
                return INR;

            case SubsequentAddressMessage.MESSAGE_CODE:
                SubsequentAddressMessage SAM = createSAM();
                return SAM;

            case SubsequentDirectoryNumberMessage.MESSAGE_CODE:
                SubsequentDirectoryNumberMessage SDN = createSDN();
                return SDN;

            case ForwardTransferMessage.MESSAGE_CODE:
                ForwardTransferMessage FOT = createFOT();
                return FOT;

            case ResumeMessage.MESSAGE_CODE:
                ResumeMessage RES = createRES();
                return RES;
            case BlockingMessage.MESSAGE_CODE:
                BlockingMessage BLO = createBLO();
                return BLO;

            case BlockingAckMessage.MESSAGE_CODE:
                BlockingAckMessage BLA = createBLA();
                return BLA;

            case ContinuityCheckRequestMessage.MESSAGE_CODE:
                ContinuityCheckRequestMessage CCR = createCCR();
                return CCR;

            case LoopbackAckMessage.MESSAGE_CODE:
                LoopbackAckMessage LPA = createLPA();
                return LPA;

            case LoopPreventionMessage.MESSAGE_CODE:
                LoopPreventionMessage LPP = createLPP();
                return LPP;

            case OverloadMessage.MESSAGE_CODE:
                OverloadMessage OLM = createOLM();
                return OLM;

            case SuspendMessage.MESSAGE_CODE:
                SuspendMessage SUS = createSUS();
                return SUS;

            case ResetCircuitMessage.MESSAGE_CODE:
                ResetCircuitMessage RSC = createRSC();
                return RSC;

            case UnblockingMessage.MESSAGE_CODE:
                UnblockingMessage UBL = createUBL();
                return UBL;

            case UnblockingAckMessage.MESSAGE_CODE:
                UnblockingAckMessage UBA = createUBA();
                return UBA;

            case UnequippedCICMessage.MESSAGE_CODE:
                UnequippedCICMessage UCIC = createUCIC();
                return UCIC;

            case CircuitGroupBlockingMessage.MESSAGE_CODE:
                CircuitGroupBlockingMessage CGB = createCGB();
                return CGB;

            case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
                CircuitGroupBlockingAckMessage CGBA = createCGBA();
                return CGBA;

            case CircuitGroupUnblockingMessage.MESSAGE_CODE:
                CircuitGroupUnblockingMessage CGU = createCGU();
                return CGU;

            case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
                CircuitGroupUnblockingAckMessage CGUA = createCGUA();
                return CGUA;

            case CircuitGroupResetMessage.MESSAGE_CODE:
                CircuitGroupResetMessage GRS = createGRS();
                return GRS;

            case CircuitGroupQueryResponseMessage.MESSAGE_CODE:
                CircuitGroupQueryResponseMessage CQR = createCQR();
                return CQR;

            case CircuitGroupQueryMessage.MESSAGE_CODE:
                CircuitGroupQueryMessage CQM = createCQM();
                return CQM;

            case FacilityAcceptedMessage.MESSAGE_CODE:
                FacilityAcceptedMessage FAA = createFAA();
                return FAA;

            case FacilityRequestMessage.MESSAGE_CODE:
                FacilityRequestMessage FAR = createFAR();
                return FAR;

            case PassAlongMessage.MESSAGE_CODE:
                PassAlongMessage PAM = createPAM();
                return PAM;

            case PreReleaseInformationMessage.MESSAGE_CODE:
                PreReleaseInformationMessage PRI = createPRI();
                return PRI;

            case FacilityMessage.MESSAGE_CODE:
                FacilityMessage FAC = createFAC();
                return FAC;

            case NetworkResourceManagementMessage.MESSAGE_CODE:
                NetworkResourceManagementMessage NRM = createNRM();
                return NRM;

            case IdentificationRequestMessage.MESSAGE_CODE:
                IdentificationRequestMessage IDR = createIDR();
                return IDR;

            case IdentificationResponseMessage.MESSAGE_CODE:
                IdentificationResponseMessage IRS = createIRS();
                return IRS;

            case SegmentationMessage.MESSAGE_CODE:
                SegmentationMessage SGM = createSGM();
                return SGM;

            case ChargeInformationMessage.MESSAGE_CODE:
                ChargeInformationMessage CIM = createCIM();
                return CIM;

            case UserPartAvailableMessage.MESSAGE_CODE:
                UserPartAvailableMessage UPA = createUPA();
                return UPA;

            case UserPartTestMessage.MESSAGE_CODE:
                UserPartTestMessage UPT = createUPT();
                return UPT;

            case UserToUserInformationMessage.MESSAGE_CODE:
                UserToUserInformationMessage USR = createUSR();
                return USR;
            default:
                throw new IllegalArgumentException("Not supported comamnd code: " + commandCode);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCommand(int, int)
     */
    @Override
    public ISUPMessage createCommand(int commandCode, int cic) {
        ISUPMessage msg = createCommand(commandCode);
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCON(int cic)
     */
    public ConnectMessage createCON() {

        ConnectMessage msg = new ConnectMessageImpl(_CON_HOLDER.mandatoryCodes, _CON_HOLDER.mandatoryVariableCodes,
                _CON_HOLDER.optionalCodes, _CON_HOLDER.mandatoryCodeToIndex, _CON_HOLDER.mandatoryVariableCodeToIndex,
                _CON_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCON(int)
     */
    @Override
    public ConnectMessage createCON(int cic) {
        ConnectMessage msg = createCON();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCOT(int cic)
     */
    public ContinuityMessage createCOT() {

        ContinuityMessage msg = new ContinuityMessageImpl(_COT_HOLDER.mandatoryCodes, _COT_HOLDER.mandatoryVariableCodes,
                _COT_HOLDER.optionalCodes, _COT_HOLDER.mandatoryCodeToIndex, _COT_HOLDER.mandatoryVariableCodeToIndex,
                _COT_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCOT(int)
     */
    @Override
    public ContinuityMessage createCOT(int cic) {
        ContinuityMessage msg = createCOT();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCPG()
     */
    @Override
    public CallProgressMessage createCPG() {
        CallProgressMessage msg = new CallProgressMessageImpl(_CPG_HOLDER.mandatoryCodes, _CPG_HOLDER.mandatoryVariableCodes,
                _CPG_HOLDER.optionalCodes, _CPG_HOLDER.mandatoryCodeToIndex, _CPG_HOLDER.mandatoryVariableCodeToIndex,
                _CPG_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCPG(int cic)
     */
    public CallProgressMessage createCPG(int cic) {
        CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
        c.setCIC(cic);
        CallProgressMessage msg = this.createCPG();
        msg.setCircuitIdentificationCode(c);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQM()
     */
    @Override
    public CircuitGroupQueryMessage createCQM() {
        CircuitGroupQueryMessage msg = new CircuitGroupQueryMessageImpl(_CQM_HOLDER.mandatoryCodes,
                _CQM_HOLDER.mandatoryVariableCodes, _CQM_HOLDER.optionalCodes, _CQM_HOLDER.mandatoryCodeToIndex,
                _CQM_HOLDER.mandatoryVariableCodeToIndex, _CQM_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQM(int cic)
     */
    public CircuitGroupQueryMessage createCQM(int cic) {
        CircuitGroupQueryMessage msg = createCQM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQR(int cic)
     */
    public CircuitGroupQueryResponseMessage createCQR() {
        CircuitGroupQueryResponseMessage msg = new CircuitGroupQueryResponseMessageImpl(_CQR_HOLDER.mandatoryCodes,
                _CQR_HOLDER.mandatoryVariableCodes, _CQR_HOLDER.optionalCodes, _CQR_HOLDER.mandatoryCodeToIndex,
                _CQR_HOLDER.mandatoryVariableCodeToIndex, _CQR_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQR(int)
     */
    @Override
    public CircuitGroupQueryResponseMessage createCQR(int cic) {
        CircuitGroupQueryResponseMessage msg = createCQR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAA(int cic)
     */
    public FacilityAcceptedMessage createFAA() {
        FacilityAcceptedMessage msg = new FacilityAcceptedMessageImpl(_FAA_HOLDER.mandatoryCodes,
                _FAA_HOLDER.mandatoryVariableCodes, _FAA_HOLDER.optionalCodes, _FAA_HOLDER.mandatoryCodeToIndex,
                _FAA_HOLDER.mandatoryVariableCodeToIndex, _FAA_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAA(int)
     */
    @Override
    public FacilityAcceptedMessage createFAA(int cic) {
        FacilityAcceptedMessage msg = createFAA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAC()
     */
    @Override
    public FacilityMessage createFAC() {
        FacilityMessage msg = new FacilityMessageImpl(_FAC_HOLDER.mandatoryCodes,
                _FAC_HOLDER.mandatoryVariableCodes, _FAC_HOLDER.optionalCodes, _FAC_HOLDER.mandatoryCodeToIndex,
                _FAC_HOLDER.mandatoryVariableCodeToIndex, _FAC_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAC(int cic)
     */
    public FacilityMessage createFAC(int cic) {
        FacilityMessage msg = createFAC();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAR()
     */
    @Override
    public FacilityRequestMessage createFAR() {
        FacilityRequestMessage msg = new FacilityRequestMessageImpl(_FAR_HOLDER.mandatoryCodes,
                _FAR_HOLDER.mandatoryVariableCodes, _FAR_HOLDER.optionalCodes, _FAR_HOLDER.mandatoryCodeToIndex,
                _FAR_HOLDER.mandatoryVariableCodeToIndex, _FAR_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAR(int cic)
     */
    public FacilityRequestMessage createFAR(int cic) {
        FacilityRequestMessage msg = createFAR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFOT()
     */
    @Override
    public ForwardTransferMessage createFOT() {
        ForwardTransferMessage msg = new ForwardTransferMessageImpl(_FOT_HOLDER.mandatoryCodes,
                _FOT_HOLDER.mandatoryVariableCodes, _FOT_HOLDER.optionalCodes, _FOT_HOLDER.mandatoryCodeToIndex,
                _FOT_HOLDER.mandatoryVariableCodeToIndex, _FOT_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFOT(int cic)
     */
    public ForwardTransferMessage createFOT(int cic) {
        ForwardTransferMessage msg = createFOT();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFRJ()
     */
    @Override
    public FacilityRejectedMessage createFRJ() {
        FacilityRejectedMessage msg = new FacilityRejectedMessageImpl(_FRJ_HOLDER.mandatoryCodes,
                _FRJ_HOLDER.mandatoryVariableCodes, _FRJ_HOLDER.optionalCodes, _FRJ_HOLDER.mandatoryCodeToIndex,
                _FRJ_HOLDER.mandatoryVariableCodeToIndex, _FRJ_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFRJ(int cic)
     */
    public FacilityRejectedMessage createFRJ(int cic) {
        FacilityRejectedMessage msg = createFRJ();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRA(int cic)
     */
    public CircuitGroupResetAckMessage createGRA() {
        CircuitGroupResetAckMessage msg = new CircuitGroupResetAckMessageImpl(_GRA_HOLDER.mandatoryCodes,
                _GRA_HOLDER.mandatoryVariableCodes, _GRA_HOLDER.optionalCodes, _GRA_HOLDER.mandatoryCodeToIndex,
                _GRA_HOLDER.mandatoryVariableCodeToIndex, _GRA_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRA(int)
     */
    @Override
    public CircuitGroupResetAckMessage createGRA(int cic) {
        CircuitGroupResetAckMessage msg = createGRA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRS()
     */
    @Override
    public CircuitGroupResetMessage createGRS() {
        CircuitGroupResetMessage msg = new CircuitGroupResetMessageImpl(_GRS_HOLDER.mandatoryCodes,
                _GRS_HOLDER.mandatoryVariableCodes, _GRS_HOLDER.optionalCodes, _GRS_HOLDER.mandatoryCodeToIndex,
                _GRS_HOLDER.mandatoryVariableCodeToIndex, _GRS_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRS(int cic)
     */
    public CircuitGroupResetMessage createGRS(int cic) {
        CircuitGroupResetMessage msg = createGRS();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIAM()
     */
    @Override
    public InitialAddressMessage createIAM() {
        InitialAddressMessage msg = new InitialAddressMessageImpl(_IAM_HOLDER.mandatoryCodes,
                _IAM_HOLDER.mandatoryVariableCodes, _IAM_HOLDER.optionalCodes, _IAM_HOLDER.mandatoryCodeToIndex,
                _IAM_HOLDER.mandatoryVariableCodeToIndex, _IAM_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIAM(int cic)
     */
    public InitialAddressMessage createIAM(int cic) {
        InitialAddressMessage msg = createIAM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIDR()
     */
    @Override
    public IdentificationRequestMessage createIDR() {
        IdentificationRequestMessage msg = new IdentificationRequestMessageImpl(_IDR_HOLDER.mandatoryCodes,
                _IDR_HOLDER.mandatoryVariableCodes, _IDR_HOLDER.optionalCodes, _IDR_HOLDER.mandatoryCodeToIndex,
                _IDR_HOLDER.mandatoryVariableCodeToIndex, _IDR_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIDR(int cic)
     */
    public IdentificationRequestMessage createIDR(int cic) {
        IdentificationRequestMessage msg = createIDR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINF(int cic)
     */
    public InformationMessage createINF() {
        InformationMessage msg = new InformationMessageImpl(_INF_HOLDER.mandatoryCodes, _INF_HOLDER.mandatoryVariableCodes,
                _INF_HOLDER.optionalCodes, _INF_HOLDER.mandatoryCodeToIndex, _INF_HOLDER.mandatoryVariableCodeToIndex,
                _INF_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINF(int)
     */
    @Override
    public InformationMessage createINF(int cic) {
        InformationMessage msg = createINF();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINR()
     */
    @Override
    public InformationRequestMessage createINR() {
        InformationRequestMessage msg = new InformationRequestMessageImpl(_INR_HOLDER.mandatoryCodes,
                _INR_HOLDER.mandatoryVariableCodes, _INR_HOLDER.optionalCodes, _INR_HOLDER.mandatoryCodeToIndex,
                _INR_HOLDER.mandatoryVariableCodeToIndex, _INR_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINR(int cic)
     */
    public InformationRequestMessage createINR(int cic) {
        InformationRequestMessage msg = createINR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIRS(int cic)
     */
    public IdentificationResponseMessage createIRS() {
        IdentificationResponseMessage msg = new IdentificationResponseMessageImpl(_IRS_HOLDER.mandatoryCodes,
                _IRS_HOLDER.mandatoryVariableCodes, _IRS_HOLDER.optionalCodes, _IRS_HOLDER.mandatoryCodeToIndex,
                _IRS_HOLDER.mandatoryVariableCodeToIndex, _IRS_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIRS(int)
     */
    @Override
    public IdentificationResponseMessage createIRS(int cic) {
        IdentificationResponseMessage msg = createIRS();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPA(int cic)
     */
    public LoopbackAckMessage createLPA() {

        LoopbackAckMessage msg = new LoopbackAckMessageImpl(_LPA_HOLDER.mandatoryCodes, _LPA_HOLDER.mandatoryVariableCodes,
                _LPA_HOLDER.optionalCodes, _LPA_HOLDER.mandatoryCodeToIndex, _LPA_HOLDER.mandatoryVariableCodeToIndex,
                _LPA_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPA(int)
     */
    @Override
    public LoopbackAckMessage createLPA(int cic) {
        LoopbackAckMessage msg = createLPA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPP()
     */
    @Override
    public LoopPreventionMessage createLPP() {
        LoopPreventionMessage msg = new LoopPreventionMessageImpl(_LPP_HOLDER.mandatoryCodes, _LPP_HOLDER.mandatoryVariableCodes,
                _LPP_HOLDER.optionalCodes, _LPP_HOLDER.mandatoryCodeToIndex, _LPP_HOLDER.mandatoryVariableCodeToIndex,
                _LPP_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPP(int cic)
     */
    public LoopPreventionMessage createLPP(int cic) {
        LoopPreventionMessage msg = createLPP();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createNRM()
     */
    @Override
    public NetworkResourceManagementMessage createNRM() {
        NetworkResourceManagementMessage msg = new NetworkResourceManagementMessageImpl(_NRM_HOLDER.mandatoryCodes, _NRM_HOLDER.mandatoryVariableCodes,
                _NRM_HOLDER.optionalCodes, _NRM_HOLDER.mandatoryCodeToIndex, _NRM_HOLDER.mandatoryVariableCodeToIndex,
                _NRM_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createNRM(int cic)
     */
    public NetworkResourceManagementMessage createNRM(int cic) {
        NetworkResourceManagementMessage msg = createNRM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createOLM()
     */
    @Override
    public OverloadMessage createOLM() {
        OverloadMessage msg = new OverloadMessageImpl(_OLM_HOLDER.mandatoryCodes, _OLM_HOLDER.mandatoryVariableCodes,
                _OLM_HOLDER.optionalCodes, _OLM_HOLDER.mandatoryCodeToIndex, _OLM_HOLDER.mandatoryVariableCodeToIndex,
                _OLM_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createOLM(int cic)
     */
    public OverloadMessage createOLM(int cic) {
        OverloadMessage msg = createOLM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPAM()
     */
    @Override
    public PassAlongMessage createPAM() {
        return new PassAlongMessageImpl();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPAM(int cic)
     */
    public PassAlongMessage createPAM(int cic) {
        PassAlongMessage msg = createPAM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPRI()
     */
    @Override
    public PreReleaseInformationMessage createPRI() {
        PreReleaseInformationMessage msg = new PreReleaseInformationMessageImpl(_PRI_HOLDER.mandatoryCodes, _PRI_HOLDER.mandatoryVariableCodes,
                _PRI_HOLDER.optionalCodes, _PRI_HOLDER.mandatoryCodeToIndex, _PRI_HOLDER.mandatoryVariableCodeToIndex,
                _PRI_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPRI(int cic)
     */
    public PreReleaseInformationMessage createPRI(int cic) {
        PreReleaseInformationMessage msg = createPRI();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createREL()
     */
    @Override
    public ReleaseMessage createREL() {
        ReleaseMessage msg = new ReleaseMessageImpl(_REL_HOLDER.mandatoryCodes, _REL_HOLDER.mandatoryVariableCodes,
                _REL_HOLDER.optionalCodes, _REL_HOLDER.mandatoryCodeToIndex, _REL_HOLDER.mandatoryVariableCodeToIndex,
                _REL_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createREL(int cic)
     */
    public ReleaseMessage createREL(int cic) {
        ReleaseMessage msg = createREL();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRES()
     */
    @Override
    public ResumeMessage createRES() {
        ResumeMessage msg = new ResumeMessageImpl(_RES_HOLDER.mandatoryCodes, _RES_HOLDER.mandatoryVariableCodes,
                _RES_HOLDER.optionalCodes, _RES_HOLDER.mandatoryCodeToIndex, _RES_HOLDER.mandatoryVariableCodeToIndex,
                _RES_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRES(int cic)
     */
    public ResumeMessage createRES(int cic) {
        ResumeMessage msg = createRES();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRLC(int cic)
     */
    public ReleaseCompleteMessage createRLC() {

        ReleaseCompleteMessage msg = new ReleaseCompleteMessageImpl(_RLC_HOLDER.mandatoryCodes,
                _RLC_HOLDER.mandatoryVariableCodes, _RLC_HOLDER.optionalCodes, _RLC_HOLDER.mandatoryCodeToIndex,
                _RLC_HOLDER.mandatoryVariableCodeToIndex, _RLC_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRLC(int)
     */
    @Override
    public ReleaseCompleteMessage createRLC(int cic) {
        ReleaseCompleteMessage msg = createRLC();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRSC()
     */
    @Override
    public ResetCircuitMessage createRSC() {
        ResetCircuitMessage msg = new ResetCircuitMessageImpl(_RSC_HOLDER.mandatoryCodes, _RSC_HOLDER.mandatoryVariableCodes,
                _RSC_HOLDER.optionalCodes, _RSC_HOLDER.mandatoryCodeToIndex, _RSC_HOLDER.mandatoryVariableCodeToIndex,
                _RSC_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRSC(int cic)
     */
    public ResetCircuitMessage createRSC(int cic) {
        ResetCircuitMessage msg = createRSC();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSAM()
     */
    @Override
    public SubsequentAddressMessage createSAM() {
        SubsequentAddressMessage msg = new SubsequentAddressMessageImpl(_SAM_HOLDER.mandatoryCodes,
                _SAM_HOLDER.mandatoryVariableCodes, _SAM_HOLDER.optionalCodes, _SAM_HOLDER.mandatoryCodeToIndex,
                _SAM_HOLDER.mandatoryVariableCodeToIndex, _SAM_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSAM(int cic)
     */
    public SubsequentAddressMessage createSAM(int cic) {
        SubsequentAddressMessage msg = createSAM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSDN()
     */
    @Override
    public SubsequentDirectoryNumberMessage createSDN() {
        SubsequentDirectoryNumberMessage msg = new SubsequentDirectoryNumberMessageImpl(_SDN_HOLDER.mandatoryCodes,
                _SDN_HOLDER.mandatoryVariableCodes, _SDN_HOLDER.optionalCodes, _SDN_HOLDER.mandatoryCodeToIndex,
                _SDN_HOLDER.mandatoryVariableCodeToIndex, _SDN_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSDN(int cic)
     */
    public SubsequentDirectoryNumberMessage createSDN(int cic) {
        SubsequentDirectoryNumberMessage msg = createSDN();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSGM()
     */
    @Override
    public SegmentationMessage createSGM() {
        SegmentationMessage msg = new SegmentationMessageImpl(_SGM_HOLDER.mandatoryCodes,
                _SGM_HOLDER.mandatoryVariableCodes, _SGM_HOLDER.optionalCodes, _SGM_HOLDER.mandatoryCodeToIndex,
                _SGM_HOLDER.mandatoryVariableCodeToIndex, _SGM_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSGM(int cic)
     */
    public SegmentationMessage createSGM(int cic) {
        SegmentationMessage msg = createSGM();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSUS()
     */
    @Override
    public SuspendMessage createSUS() {
        SuspendMessage msg = new SuspendMessageImpl(_SUS_HOLDER.mandatoryCodes, _SUS_HOLDER.mandatoryVariableCodes,
                _SUS_HOLDER.optionalCodes, _SUS_HOLDER.mandatoryCodeToIndex, _SUS_HOLDER.mandatoryVariableCodeToIndex,
                _SUS_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSUS(int cic)
     */
    public SuspendMessage createSUS(int cic) {
        SuspendMessage msg = createSUS();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBA(int cic)
     */
    public UnblockingAckMessage createUBA() {
        UnblockingAckMessage msg = new UnblockingAckMessageImpl(_UBA_HOLDER.mandatoryCodes, _UBA_HOLDER.mandatoryVariableCodes,
                _UBA_HOLDER.optionalCodes, _UBA_HOLDER.mandatoryCodeToIndex, _UBA_HOLDER.mandatoryVariableCodeToIndex,
                _UBA_HOLDER.optionalCodeToIndex);

        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBA(int)
     */
    @Override
    public UnblockingAckMessage createUBA(int cic) {
        UnblockingAckMessage msg = createUBA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBL()
     */
    @Override
    public UnblockingMessage createUBL() {
        UnblockingMessage msg = new UnblockingMessageImpl(_UBL_HOLDER.mandatoryCodes, _UBL_HOLDER.mandatoryVariableCodes,
                _UBL_HOLDER.optionalCodes, _UBL_HOLDER.mandatoryCodeToIndex, _UBL_HOLDER.mandatoryVariableCodeToIndex,
                _UBL_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBL(int cic)
     */
    public UnblockingMessage createUBL(int cic) {
        UnblockingMessage msg = createUBL();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUCIC()
     */
    @Override
    public UnequippedCICMessage createUCIC() {
        UnequippedCICMessage msg = new UnequippedCICMessageImpl(_UCIC_HOLDER.mandatoryCodes,
                _UCIC_HOLDER.mandatoryVariableCodes, _UCIC_HOLDER.optionalCodes, _UCIC_HOLDER.mandatoryCodeToIndex,
                _UCIC_HOLDER.mandatoryVariableCodeToIndex, _UCIC_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUCIC(int cic)
     */
    public UnequippedCICMessage createUCIC(int cic) {
        UnequippedCICMessage msg = createUCIC();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPA()
     */
    @Override
    public UserPartAvailableMessage createUPA() {
        UserPartAvailableMessage msg = new UserPartAvailableMessageImpl(_UPA_HOLDER.mandatoryCodes,
                _UPA_HOLDER.mandatoryVariableCodes, _UPA_HOLDER.optionalCodes, _UPA_HOLDER.mandatoryCodeToIndex,
                _UPA_HOLDER.mandatoryVariableCodeToIndex, _UPA_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPA(int cic)
     */
    public UserPartAvailableMessage createUPA(int cic) {
        UserPartAvailableMessage msg = createUPA();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPT()
     */
    @Override
    public UserPartTestMessage createUPT() {
        UserPartTestMessage msg = new UserPartTestMessageImpl(_UPT_HOLDER.mandatoryCodes,
                _UPT_HOLDER.mandatoryVariableCodes, _UPT_HOLDER.optionalCodes, _UPT_HOLDER.mandatoryCodeToIndex,
                _UPT_HOLDER.mandatoryVariableCodeToIndex, _UPT_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPT(int cic)
     */
    public UserPartTestMessage createUPT(int cic) {
        UserPartTestMessage msg = createUPT();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUSR()
     */
    @Override
    public UserToUserInformationMessage createUSR() {
        UserToUserInformationMessage msg = new UserToUserInformationMessageImpl(_U2U_HOLDER.mandatoryCodes,
                _U2U_HOLDER.mandatoryVariableCodes, _U2U_HOLDER.optionalCodes, _U2U_HOLDER.mandatoryCodeToIndex,
                _U2U_HOLDER.mandatoryVariableCodeToIndex, _U2U_HOLDER.optionalCodeToIndex);
        return msg;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUSR(int cic)
     */
    public UserToUserInformationMessage createUSR(int cic) {
        UserToUserInformationMessage msg = createUSR();
        CircuitIdentificationCode code = this.parameterFactory.createCircuitIdentificationCode();
        code.setCIC(cic);
        msg.setCircuitIdentificationCode(code);
        return msg;
    }
}
