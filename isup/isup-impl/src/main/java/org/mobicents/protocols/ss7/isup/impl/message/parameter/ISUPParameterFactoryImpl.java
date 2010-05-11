/**
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.accessTransport.*;
import org.mobicents.protocols.ss7.isup.message.parameter.*;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.*;

/**
 * Simple factory.
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

	public ContinuitiyIndicators createContinuitiyIndicators() {
		
		return new ContinuitiyIndicatorsImpl();
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

	
	
	
}
