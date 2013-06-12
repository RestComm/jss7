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

/**
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;

import org.mobicents.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;
import org.mobicents.protocols.ss7.isup.message.parameter.HopCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangePossible;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;
import org.mobicents.protocols.ss7.isup.message.parameter.SCFID;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.mobicents.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.mobicents.protocols.ss7.isup.message.parameter.InstructionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PerformingPivotIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.InvokingPivotReason;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.SubsequentNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.LoopPreventionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ContinuityIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CorrelationID;
import org.mobicents.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.QueryOnReleaseCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDCapabilityIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.FacilityIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferReference;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.CCSS;
import org.mobicents.protocols.ss7.isup.message.parameter.GVNSUserGroup;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.Reserved;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Simple factory.
 *
 * @author baranowb
 *
 */
public class ISUPParameterFactoryImpl implements ISUPParameterFactory {

    public AccessDeliveryInformation createAccessDeliveryInformation() {

        return new AccessDeliveryInformationImpl();
    }

    public AccessTransport createAccessTransport() {

        return new AccessTransportImpl();
    }

    public ApplicationTransportParameter createApplicationTransportParameter() {

        return new ApplicationTransportParameterImpl();
    }

    public AutomaticCongestionLevel createAutomaticCongestionLevel() {

        return new AutomaticCongestionLevelImpl();
    }

    public BackwardCallIndicators createBackwardCallIndicators() {

        return new BackwardCallIndicatorsImpl();
    }

    public BackwardGVNS createBackwardGVNS() {

        return new BackwardGVNSImpl();
    }

    public CCNRPossibleIndicator createCCNRPossibleIndicator() {

        return new CCNRPossibleIndicatorImpl();
    }

    public CCSS createCCSS() {

        return new CCSSImpl();
    }

    public CallDiversionInformation createCallDiversionInformation() {

        return new CallDiversionInformationImpl();
    }

    public CallDiversionTreatmentIndicators createCallDiversionTreatmentIndicators() {

        return new CallDiversionTreatmentIndicatorsImpl();
    }

    public CallHistoryInformation createCallHistoryInformation() {

        return new CallHistoryInformationImpl();
    }

    public CallOfferingTreatmentIndicators createCallOfferingTreatmentIndicators() {

        return new CallOfferingTreatmentIndicatorsImpl();
    }

    public CallReference createCallReference() {

        return new CallReferenceImpl();
    }

    public CallTransferNumber createCallTransferNumber() {

        return new CallTransferNumberImpl();
    }

    public CallTransferReference createCallTransferReference() {

        return new CallTransferReferenceImpl();
    }

    public CalledDirectoryNumber createCalledDirectoryNumber() {

        return new CalledDirectoryNumberImpl();
    }

    public CalledINNumber createCalledINNumber() {

        return new CalledINNumberImpl();
    }

    public CalledPartyNumber createCalledPartyNumber() {

        return new CalledPartyNumberImpl();
    }

    public CallingPartyCategory createCallingPartyCategory() {

        return new CallingPartyCategoryImpl();
    }

    public CallingPartyNumber createCallingPartyNumber() {

        return new CallingPartyNumberImpl();
    }

    public CauseIndicators createCauseIndicators() {

        return new CauseIndicatorsImpl();
    }

    public CircuitAssigmentMap createCircuitAssigmentMap() {

        return new CircuitAssigmentMapImpl();
    }

    public CircuitGroupSuperVisionMessageType createCircuitGroupSuperVisionMessageType() {

        return new CircuitGroupSuperVisionMessageTypeImpl();
    }

    public CircuitIdentificationCode createCircuitIdentificationCode() {

        return new CircuitIdentificationCodeImpl();
    }

    public CircuitStateIndicator createCircuitStateIndicator() {

        return new CircuitStateIndicatorImpl();
    }

    public ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode() {

        return new ClosedUserGroupInterlockCodeImpl();
    }

    public CollectCallRequest createCollectCallRequest() {

        return new CollectCallRequestImpl();
    }

    public ConferenceTreatmentIndicators createConferenceTreatmentIndicators() {

        return new ConferenceTreatmentIndicatorsImpl();
    }

    public ConnectedNumber createConnectedNumber() {

        return new ConnectedNumberImpl();
    }

    public ConnectionRequest createConnectionRequest() {

        return new ConnectionRequestImpl();
    }

    public ContinuityIndicators createContinuityIndicators() {

        return new ContinuityIndicatorsImpl();
    }

    public CorrelationID createCorrelationID() {

        return new CorrelationIDImpl();
    }

    public DisplayInformation createDisplayInformation() {

        return new DisplayInformationImpl();
    }

    public EchoControlInformation createEchoControlInformation() {

        return new EchoControlInformationImpl();
    }

    public EventInformation createEventInformation() {

        return new EventInformationImpl();
    }

    public FacilityIndicator createFacilityIndicator() {

        return new FacilityIndicatorImpl();
    }

    public ForwardCallIndicators createForwardCallIndicators() {

        return new ForwardCallIndicatorsImpl();
    }

    public ForwardGVNS createForwardGVNS() {

        return new ForwardGVNSImpl();
    }

    public GVNSUserGroup createGVNSUserGroup() {

        return new GVNSUserGroupImpl();
    }

    public GenericNotificationIndicator createGenericNotificationIndicator() {

        return new GenericNotificationIndicatorImpl();
    }

    public GenericNumber createGenericNumber() {

        return new GenericNumberImpl();
    }

    public GenericReference createGenericReference() {

        return new GenericReferenceImpl();
    }

    public HTRInformation createHTRInformation() {

        return new HTRInformationImpl();
    }

    public HopCounter createHopCounter() {

        return new HopCounterImpl();
    }

    public InformationIndicators createInformationIndicators() {

        return new InformationIndicatorsImpl();
    }

    public InformationRequestIndicators createInformationRequestIndicators() {

        return new InformationRequestIndicatorsImpl();
    }

    public InstructionIndicators createInstructionIndicators() {

        return new InstructionIndicatorsImpl();
    }

    public InvokingPivotReason createInvokingPivotReason() {

        return new InvokingPivotReasonImpl();
    }

    public LocationNumber createLocationNumber() {

        return new LocationNumberImpl();
    }

    public LoopPreventionIndicators createLoopPreventionIndicators() {

        return new LoopPreventionIndicatorsImpl();
    }

    public MCIDRequestIndicators createMCIDRequestIndicators() {

        return new MCIDRequestIndicatorsImpl();
    }

    public MCIDResponseIndicators createMCIDResponseIndicators() {

        return new MCIDResponseIndicatorsImpl();
    }

    public MLPPPrecedence createMLPPPrecedence() {

        return new MLPPPrecedenceImpl();
    }

    public NatureOfConnectionIndicators createNatureOfConnectionIndicators() {

        return new NatureOfConnectionIndicatorsImpl();
    }

    public NetworkManagementControls createNetworkManagementControls() {

        return new NetworkManagementControlsImpl();
    }

    public NetworkRoutingNumber createNetworkRoutingNumber() {

        return new NetworkRoutingNumberImpl();
    }

    public NetworkSpecificFacility createNetworkSpecificFacility() {

        return new NetworkSpecificFacilityImpl();
    }

    public OptionalBackwardCallIndicators createOptionalBackwardCallIndicators() {

        return new OptionalBackwardCallIndicatorsImpl();
    }

    public OptionalForwardCallIndicators createOptionalForwardCallIndicators() {

        return new OptionalForwardCallIndicatorsImpl();
    }

    public OriginalCalledINNumber createOriginalCalledINNumber() {

        return new OriginalCalledINNumberImpl();
    }

    public OriginalCalledNumber createOriginalCalledNumber() {

        return new OriginalCalledNumberImpl();
    }

    public OriginatingISCPointCode createOriginatingISCPointCode() {

        return new OriginatingISCPointCodeImpl();
    }

    public OriginatingParticipatingServiceProvider createOriginatingParticipatingServiceProvider() {

        return new OriginatingParticipatingServiceProviderImpl();
    }

    public ParameterCompatibilityInformation createParameterCompatibilityInformation() {

        return new ParameterCompatibilityInformationImpl();
    }

    public PerformingPivotIndicator createPerformingPivotIndicator() {

        return new PerformingPivotIndicatorImpl();
    }

    public PivotCapability createPivotCapability() {

        return new PivotCapabilityImpl();
    }

    public PivotCounter createPivotCounter() {

        return new PivotCounterImpl();
    }

    public PivotRoutingBackwardInformation createPivotRoutingBackwardInformation() {

        return new PivotRoutingBackwardInformationImpl();
    }

    public PivotRoutingForwardInformation createPivotRoutingForwardInformation() {

        return new PivotRoutingForwardInformationImpl();
    }

    public PivotRoutingIndicators createPivotRoutingIndicators() {

        return new PivotRoutingIndicatorsImpl();
    }

    public PropagationDelayCounter createPropagationDelayCounter() {

        return new PropagationDelayCounterImpl();
    }

    public QueryOnReleaseCapability createQueryOnReleaseCapability() {

        return new QueryOnReleaseCapabilityImpl();
    }

    public RangeAndStatus createRangeAndStatus() {

        return new RangeAndStatusImpl();
    }

    public RedirectBackwardInformation createRedirectBackwardInformation() {

        return new RedirectBackwardInformationImpl();
    }

    public RedirectCapability createRedirectCapability() {

        return new RedirectCapabilityImpl();
    }

    public RedirectCounter createRedirectCounter() {

        return new RedirectCounterImpl();
    }

    public RedirectStatus createRedirectStatus() {

        return new RedirectStatusImpl();
    }

    public RedirectingNumber createRedirectingNumber() {

        return new RedirectingNumberImpl();
    }

    public RedirectionInformation createRedirectionInformation() {

        return new RedirectionInformationImpl();
    }

    public RedirectionNumber createRedirectionNumber() {

        return new RedirectionNumberImpl();
    }

    public RedirectionNumberRestriction createRedirectionNumberRestriction() {

        return new RedirectionNumberRestrictionImpl();
    }

    public RemoteOperations createRemoteOperations() {

        return new RemoteOperationsImpl();
    }

    public Reserved createReserved() {

        return new ReservedImpl();
    }

    public ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier() {

        return new ReturnToInvokingExchangeCallIdentifierImpl();
    }

    public ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible() {

        return new ReturnToInvokingExchangePossibleImpl();
    }

    public SCFID createSCFID() {

        return new SCFIDImpl();
    }

    public ServiceActivation createServiceActivation() {

        return new ServiceActivationImpl();
    }

    public SignalingPointCode createSignalingPointCode() {

        return new SignalingPointCodeImpl();
    }

    public SubsequentNumber createSubsequentNumber() {

        return new SubsequentNumberImpl();
    }

    public SuspendResumeIndicators createSuspendResumeIndicators() {

        return new SuspendResumeIndicatorsImpl();
    }

    public TerminatingNetworkRoutingNumber createTerminatingNetworkRoutingNumber() {

        return new TerminatingNetworkRoutingNumberImpl();
    }

    public TransimissionMediumRequierementPrime createTransimissionMediumRequierementPrime() {

        return new TransimissionMediumRequierementPrimeImpl();
    }

    public TransitNetworkSelection createTransitNetworkSelection() {

        return new TransitNetworkSelectionImpl();
    }

    public TransmissionMediumRequirement createTransmissionMediumRequirement() {

        return new TransmissionMediumRequirementImpl();
    }

    public TransmissionMediumUsed createTransmissionMediumUsed() {

        return new TransmissionMediumUsedImpl();
    }

    public UIDActionIndicators createUIDActionIndicators() {

        return new UIDActionIndicatorsImpl();
    }

    public UIDCapabilityIndicators createUIDCapabilityIndicators() {

        return new UIDCapabilityIndicatorsImpl();
    }

    public UserServiceInformation createUserServiceInformation() {

        return new UserServiceInformationImpl();
    }

    public UserServiceInformationPrime createUserServiceInformationPrime() {

        return new UserServiceInformationPrimeImpl();
    }

    public UserTeleserviceInformation createUserTeleserviceInformation() {

        return new UserTeleserviceInformationImpl();
    }

    public UserToUserIndicators createUserToUserIndicators() {

        return new UserToUserIndicatorsImpl();
    }

    public UserToUserInformation createUserToUserInformation() {

        return new UserToUserInformationImpl();
    }

    public GenericDigits createGenericDigits() {
        return new GenericDigitsImpl();
    }

}
