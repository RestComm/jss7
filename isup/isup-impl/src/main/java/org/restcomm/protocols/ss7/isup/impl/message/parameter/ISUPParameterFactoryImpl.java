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
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
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
import org.restcomm.protocols.ss7.isup.message.parameter.Status;
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
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReasonType;


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

    public ApplicationTransport createApplicationTransport() {

        return new ApplicationTransportImpl();
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

    public CallDiversionInformation createCallDiversionInformation() {

        return new CallDiversionInformationImpl();
    }

    public CallDiversionTreatmentIndicators createCallDiversionTreatmentIndicators() {

        return new CallDiversionTreatmentIndicatorsImpl();
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

    public CallHistoryInformation createCallHistoryInformation() {

        return new CallHistoryInformationImpl();
    }

    public CallingPartyCategory createCallingPartyCategory() {

        return new CallingPartyCategoryImpl();
    }

    public CallingPartyNumber createCallingPartyNumber() {

        return new CallingPartyNumberImpl();
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

    public CauseIndicators createCauseIndicators() {

        return new CauseIndicatorsImpl();
    }

    public CCNRPossibleIndicator createCCNRPossibleIndicator() {

        return new CCNRPossibleIndicatorImpl();
    }

    public CCSS createCCSS() {

        return new CCSSImpl();
    }

    public ChargedPartyIdentification createChargedPartyIdentification() {
        return new ChargedPartyIdentificationImpl();
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

    @Override
    public ErrorCode createErrorCode() {
        return new ErrorCodeImpl();
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

    public GenericDigits createGenericDigits() {
        return new GenericDigitsImpl();
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

    public GVNSUserGroup createGVNSUserGroup() {

        return new GVNSUserGroupImpl();
    }

    public HopCounter createHopCounter() {

        return new HopCounterImpl();
    }

    public HTRInformation createHTRInformation() {

        return new HTRInformationImpl();
    }

    public InformationIndicators createInformationIndicators() {

        return new InformationIndicatorsImpl();
    }

    public InformationRequestIndicators createInformationRequestIndicators() {

        return new InformationRequestIndicatorsImpl();
    }

    @Override
    public Invoke createInvoke() {
        return new InvokeImpl();
    }

    public InvokingPivotReason createInvokingPivotReason(InvokingPivotReasonType type) {
        int tag = -1;
        switch(type){
            case PivotRoutingBackwardInformation:
                tag = PivotRoutingBackwardInformation.INFORMATION_INVOKING_PIVOT_REASON;
                break;
            case PivotRoutingForwardInformation:
                tag = PivotRoutingForwardInformation.INFORMATION_INVOKING_PIVOT_REASON;
                break;
            default:
                throw new IllegalArgumentException();
        }
        InvokingPivotReasonImpl reason = new InvokingPivotReasonImpl();
        reason.setTag(tag);
        return reason;
    }

    @Override
    public InvokingRedirectReason createInvokingRedirectReason(InvokingRedirectReasonType type) {
        int tag = -1;
        switch(type){
            case RedirectBackwardInformation:
                tag = RedirectBackwardInformation.INFORMATION_INVOKING_REDIRECT_REASON;
                break;
            case RedirectForwardInformation:
                tag = RedirectForwardInformation.INFORMATION_INVOKING_REDIRECT_REASON;
                break;
            default:
                throw new IllegalArgumentException();
        }
        InvokingRedirectReasonImpl reason = new InvokingRedirectReasonImpl();
        reason.setTag(tag);
        return reason;
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

    public MessageCompatibilityInformation createMessageCompatibilityInformation() {
        return new MessageCompatibilityInformationImpl();
    }

    @Override
    public MessageCompatibilityInstructionIndicator createMessageCompatibilityInstructionIndicator() {
        return new MessageCompatibilityInstructionIndicatorImpl();
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

    @Override
    public OperationCode createOperationCode() {
        return new OperationCodeImpl();
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

    @Override
    public Parameter createParameter() {
        return new ParameterImpl();
    }

    public ParameterCompatibilityInformation createParameterCompatibilityInformation() {

        return new ParameterCompatibilityInformationImpl();
    }

    public ParameterCompatibilityInstructionIndicators createParameterCompatibilityInstructionIndicators() {

        return new ParameterCompatibilityInstructionIndicatorsImpl();
    }

    public PerformingPivotIndicator createPerformingPivotIndicator() {
        PerformingPivotIndicatorImpl indicator = new PerformingPivotIndicatorImpl();
        indicator.setTag(PivotRoutingForwardInformation.INFORMATION_PERFORMING_PIVOT_INDICATOR);
        return indicator;
    }

    @Override
    public PerformingRedirectIndicator createPerformingRedirectIndicator() {
        PerformingRedirectIndicatorImpl indicator = new PerformingRedirectIndicatorImpl();
        indicator.setTag(RedirectForwardInformation.INFORMATION_PERFORMING_REDIRECT_INDICATOR);
        return indicator;
    }

    public PivotCapability createPivotCapability() {

        return new PivotCapabilityImpl();
    }

    public PivotCounter createPivotCounter() {

        return new PivotCounterImpl();
    }

    @Override
    public PivotReason createPivotReason() {
        return new PivotReasonImpl();
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

    public PivotStatus createPivotStatus() {
        return new PivotStatusImpl();
    }

    @Override
    public Problem createProblem() {
        return new ProblemImpl();
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

    @Override
    public RedirectForwardInformation createRedirectForwardformation() {
        return new RedirectForwardInformationImpl();
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

    @Override
    public RedirectReason createRedirectReason() {
        return new RedirectReasonImpl();
    }

    public RedirectStatus createRedirectStatus() {

        return new RedirectStatusImpl();
    }

    @Override
    public Reject createReject() {
        return new RejectImpl();
    }

    public RemoteOperations createRemoteOperations() {

        return new RemoteOperationsImpl();
    }

    public Reserved createReserved() {

        return new ReservedImpl();
    }

    @Override
    public ReturnError createReturnError() {
        return new ReturnErrorImpl();
    }

    @Override
    public ReturnResult createReturnResult() {
        return new ReturnResultImpl();
    }

    public ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier() {
        ReturnToInvokingExchangeCallIdentifierImpl cid = new ReturnToInvokingExchangeCallIdentifierImpl();
        cid.setTag(PivotRoutingForwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_CALL_ID);
        return cid;
    }

    @Override
    public ReturnToInvokingExchangeDuration createReturnToInvokingExchangeDuration() {
        ReturnToInvokingExchangeDurationImpl duration = new ReturnToInvokingExchangeDurationImpl();
        duration.setTag(PivotRoutingBackwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_DURATION);
        return duration;
    }

    public ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible() {
        ReturnToInvokingExchangePossibleImpl iWonderWhenSomeoneWillNoticeThisLongishName = new ReturnToInvokingExchangePossibleImpl();
        iWonderWhenSomeoneWillNoticeThisLongishName.setTag(PivotRoutingForwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_POSSIBLE);
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

    public Status createStatus() {
        return new StatusImpl();
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

    @Override
    public RedirectForwardInformation createRedirectForwardInformation() {
        return new RedirectForwardInformationImpl();
    }
}
