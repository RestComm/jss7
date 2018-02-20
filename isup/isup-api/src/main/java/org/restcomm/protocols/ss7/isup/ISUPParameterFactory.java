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
 *
 */
package org.restcomm.protocols.ss7.isup;


import org.restcomm.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.restcomm.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CCSS;
import org.restcomm.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.CallReference;
import org.restcomm.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallTransferReference;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledINNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ChargedPartyIdentification;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.restcomm.protocols.ss7.isup.message.parameter.CollectCallRequest;
import org.restcomm.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.restcomm.protocols.ss7.isup.message.parameter.ContinuityIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.CorrelationID;
import org.restcomm.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.EventInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.restcomm.protocols.ss7.isup.message.parameter.GVNSUserGroup;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericReference;
import org.restcomm.protocols.ss7.isup.message.parameter.HTRInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.HopCounter;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingPivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.LoopPreventionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MCIDRequestIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.restcomm.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.restcomm.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInstructionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingPivotIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotCapability;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotCounter;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotStatus;
import org.restcomm.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.restcomm.protocols.ss7.isup.message.parameter.QueryOnReleaseCapability;
import org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectCapability;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.restcomm.protocols.ss7.isup.message.parameter.Reserved;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangePossible;
import org.restcomm.protocols.ss7.isup.message.parameter.SCFID;
import org.restcomm.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.restcomm.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.restcomm.protocols.ss7.isup.message.parameter.SubsequentNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.restcomm.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.restcomm.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.restcomm.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.restcomm.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.UIDCapabilityIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReasonType;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingRedirectIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.ErrorCode;
import org.restcomm.protocols.ss7.isup.message.parameter.Problem;
import org.restcomm.protocols.ss7.isup.message.parameter.Parameter;
import org.restcomm.protocols.ss7.isup.message.parameter.Reject;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnResult;
import org.restcomm.protocols.ss7.isup.message.parameter.OperationCode;
import org.restcomm.protocols.ss7.isup.message.parameter.Invoke;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnError;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingPivotReasonType;
import org.restcomm.protocols.ss7.isup.message.parameter.Status;

/**
 * Factory for parameters.
 *
 * @author baranowb
 *
 */
public interface ISUPParameterFactory {

    AccessDeliveryInformation createAccessDeliveryInformation();

    AccessTransport createAccessTransport();

    ApplicationTransport createApplicationTransport();

    AutomaticCongestionLevel createAutomaticCongestionLevel();

    BackwardCallIndicators createBackwardCallIndicators();

    BackwardGVNS createBackwardGVNS();

    CallDiversionInformation createCallDiversionInformation();

    CallDiversionTreatmentIndicators createCallDiversionTreatmentIndicators();

    CalledDirectoryNumber createCalledDirectoryNumber();

    CalledINNumber createCalledINNumber();

    CalledPartyNumber createCalledPartyNumber();

    CallHistoryInformation createCallHistoryInformation();

    CallingPartyCategory createCallingPartyCategory();

    CallingPartyNumber createCallingPartyNumber();

    CallOfferingTreatmentIndicators createCallOfferingTreatmentIndicators();

    CallReference createCallReference();

    CallTransferNumber createCallTransferNumber();

    CallTransferReference createCallTransferReference();

    CauseIndicators createCauseIndicators();

    CCNRPossibleIndicator createCCNRPossibleIndicator();

    CCSS createCCSS();

    ChargedPartyIdentification createChargedPartyIdentification();

    CircuitAssigmentMap createCircuitAssigmentMap();

    CircuitGroupSuperVisionMessageType createCircuitGroupSuperVisionMessageType();

    CircuitIdentificationCode createCircuitIdentificationCode();

    CircuitStateIndicator createCircuitStateIndicator();

    ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode();

    CollectCallRequest createCollectCallRequest();

    ConferenceTreatmentIndicators createConferenceTreatmentIndicators();

    ConnectedNumber createConnectedNumber();

    ConnectionRequest createConnectionRequest();

    ContinuityIndicators createContinuityIndicators();

    CorrelationID createCorrelationID();

    DisplayInformation createDisplayInformation();

    EchoControlInformation createEchoControlInformation();

    ErrorCode createErrorCode();

    EventInformation createEventInformation();

    FacilityIndicator createFacilityIndicator();

    ForwardCallIndicators createForwardCallIndicators();

    ForwardGVNS createForwardGVNS();

    GenericDigits createGenericDigits();

    GenericNotificationIndicator createGenericNotificationIndicator();

    GenericNumber createGenericNumber();

    GenericReference createGenericReference();

    GVNSUserGroup createGVNSUserGroup();

    HopCounter createHopCounter();

    HTRInformation createHTRInformation();

    InformationIndicators createInformationIndicators();

    InformationRequestIndicators createInformationRequestIndicators();

    Invoke createInvoke();

    InvokingPivotReason createInvokingPivotReason(InvokingPivotReasonType type);

    InvokingRedirectReason createInvokingRedirectReason(InvokingRedirectReasonType type);

    LocationNumber createLocationNumber();

    LoopPreventionIndicators createLoopPreventionIndicators();

    MCIDRequestIndicators createMCIDRequestIndicators();

    MCIDResponseIndicators createMCIDResponseIndicators();

    MessageCompatibilityInformation createMessageCompatibilityInformation();

    MessageCompatibilityInstructionIndicator createMessageCompatibilityInstructionIndicator();

    MLPPPrecedence createMLPPPrecedence();

    NatureOfConnectionIndicators createNatureOfConnectionIndicators();

    NetworkManagementControls createNetworkManagementControls();

    NetworkRoutingNumber createNetworkRoutingNumber();

    NetworkSpecificFacility createNetworkSpecificFacility();

    OperationCode createOperationCode();

    OptionalBackwardCallIndicators createOptionalBackwardCallIndicators();

    OptionalForwardCallIndicators createOptionalForwardCallIndicators();

    OriginalCalledINNumber createOriginalCalledINNumber();

    OriginalCalledNumber createOriginalCalledNumber();

    OriginatingISCPointCode createOriginatingISCPointCode();

    OriginatingParticipatingServiceProvider createOriginatingParticipatingServiceProvider();

    Parameter createParameter();

    ParameterCompatibilityInformation createParameterCompatibilityInformation();

    ParameterCompatibilityInstructionIndicators createParameterCompatibilityInstructionIndicators();

    PerformingPivotIndicator createPerformingPivotIndicator();

    PerformingRedirectIndicator createPerformingRedirectIndicator();

    PivotCapability createPivotCapability();

    PivotCounter createPivotCounter();

    PivotReason createPivotReason();

    PivotRoutingBackwardInformation createPivotRoutingBackwardInformation();

    PivotRoutingForwardInformation createPivotRoutingForwardInformation();

    PivotRoutingIndicators createPivotRoutingIndicators();

    PivotStatus createPivotStatus();

    Problem createProblem();

    PropagationDelayCounter createPropagationDelayCounter();

    QueryOnReleaseCapability createQueryOnReleaseCapability();

    RangeAndStatus createRangeAndStatus();

    RedirectBackwardInformation createRedirectBackwardInformation();

    RedirectCapability createRedirectCapability();

    RedirectCounter createRedirectCounter();

    RedirectForwardInformation createRedirectForwardformation();

    RedirectingNumber createRedirectingNumber();

    RedirectionInformation createRedirectionInformation();

    RedirectionNumber createRedirectionNumber();

    RedirectionNumberRestriction createRedirectionNumberRestriction();

    RedirectReason createRedirectReason();

    RedirectStatus createRedirectStatus();

    Reject createReject();

    RemoteOperations createRemoteOperations();

    Reserved createReserved();

    ReturnError createReturnError();

    ReturnResult createReturnResult();

    ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier();

    ReturnToInvokingExchangeDuration createReturnToInvokingExchangeDuration();

    ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible();

    SCFID createSCFID();

    ServiceActivation createServiceActivation();

    SignalingPointCode createSignalingPointCode();
    Status createStatus();
    SubsequentNumber createSubsequentNumber();
    SuspendResumeIndicators createSuspendResumeIndicators();
    TerminatingNetworkRoutingNumber createTerminatingNetworkRoutingNumber();
    TransimissionMediumRequierementPrime createTransimissionMediumRequierementPrime();
    TransitNetworkSelection createTransitNetworkSelection();
    TransmissionMediumRequirement createTransmissionMediumRequirement();
    TransmissionMediumUsed createTransmissionMediumUsed();
    UIDActionIndicators createUIDActionIndicators();
    UIDCapabilityIndicators createUIDCapabilityIndicators();
    UserServiceInformation createUserServiceInformation();
    UserServiceInformationPrime createUserServiceInformationPrime();
    UserTeleserviceInformation createUserTeleserviceInformation();
    UserToUserIndicators createUserToUserIndicators();
    UserToUserInformation createUserToUserInformation();
    RedirectForwardInformation createRedirectForwardInformation();
}
