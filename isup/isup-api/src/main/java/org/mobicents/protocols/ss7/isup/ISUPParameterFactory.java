/**
 * 
 */
package org.mobicents.protocols.ss7.isup;

import org.mobicents.protocols.ss7.isup.message.parameter.*;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Factory for parameters.
 * @author baranowb
 *
 */
public interface ISUPParameterFactory {

	AccessDeliveryInformation createAccessDeliveryInformation();
	
	ApplicationTransportParameter createApplicationTransportParameter();
	
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
	
	CircuitAssigmentMap createCircuitAssigmentMap();
	
	CircuitGroupSuperVisionMessageType createCircuitGroupSuperVisionMessageType();
	
	CircuitIdentificationCode createCircuitIdentificationCode();
	
	CircuitStateIndicator createCircuitStateIndicator();
	
	ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode();
	
	CollectCallRequest createCollectCallRequest();
	
	ConferenceTreatmentIndicators createConferenceTreatmentIndicators();
	
	ConnectedNumber createConnectedNumber();
	
	ConnectionRequest createConnectionRequest();
	
	ContinuitiyIndicators createContinuitiyIndicators();
	
	CorrelationID createCorrelationID();
	
	DisplayInformation createDisplayInformation();
	
	EchoControlInformation createEchoControlInformation();
	
	EventInformation createEventInformation();
	
	FacilityIndicator createFacilityIndicator();
	
	ForwardCallIndicators createForwardCallIndicators();
	
	ForwardGVNS createForwardGVNS();
	
	GenericNotificationIndicator createGenericNotificationIndicator();
	
	GenericNumber createGenericNumber();
	
	GenericReference createGenericReference();
	
	GVNSUserGroup createGVNSUserGroup();
	
	HopCounter createHopCounter();
	
	HTRInformation createHTRInformation();
	
	InformationIndicators createInformationIndicators();
	
	InformationRequestIndicators createInformationRequestIndicators();
	
	InstructionIndicators createInstructionIndicators();
	
	InvokingPivotReason createInvokingPivotReason();
	
	LocationNumber createLocationNumber();
	
	LoopPreventionIndicators createLoopPreventionIndicators();
	
	MCIDRequestIndicators createMCIDRequestIndicators();
	
	MCIDResponseIndicators createMCIDResponseIndicators();
	
	MLPPPrecedence createMLPPPrecedence();
	
	NatureOfConnectionIndicators createNatureOfConnectionIndicators();
	
	NetworkManagementControls createNetworkManagementControls();
	
	NetworkRoutingNumber createNetworkRoutingNumber();
	
	NetworkSpecificFacility createNetworkSpecificFacility();
	
	OptionalBackwardCallIndicators createOptionalBackwardCallIndicators();
	
	OptionalForwardCallIndicators createOptionalForwardCallIndicators();
	
	OriginalCalledINNumber createOriginalCalledINNumber();
	
	OriginalCalledNumber createOriginalCalledNumber();
	
	OriginatingISCPointCode createOriginatingISCPointCode();
	
	OriginatingParticipatingServiceProvider createOriginatingParticipatingServiceProvider();
	
	ParameterCompatibilityInformation createParameterCompatibilityInformation();
	
	PerformingPivotIndicator createPerformingPivotIndicator();
	
	PivotCapability createPivotCapability();
	
	PivotCounter createPivotCounter();
	
	PivotRoutingBackwardInformation createPivotRoutingBackwardInformation();
	
	PivotRoutingForwardInformation createPivotRoutingForwardInformation();
	
	PivotRoutingIndicators createPivotRoutingIndicators();
	
	PropagationDelayCounter createPropagationDelayCounter();
	
	QueryOnReleaseCapability createQueryOnReleaseCapability();
	
	RangeAndStatus createRangeAndStatus();
	
	RedirectBackwardInformation createRedirectBackwardInformation();
	
	RedirectCapability createRedirectCapability();
	
	RedirectCounter createRedirectCounter();
	
	RedirectingNumber createRedirectingNumber();
	
	RedirectionInformation createRedirectionInformation();
	
	RedirectionNumber createRedirectionNumber();
	
	RedirectionNumberRestriction createRedirectionNumberRestriction();
	
	RedirectStatus createRedirectStatus();
	
	RemoteOperations createRemoteOperations();
	
	Reserved createReserved();
	
	ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier();
	
	ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible();
	
	SCFID createSCFID();
	
	ServiceActivation createServiceActivation();
	
	SignalingPointCode createSignalingPointCode();
	
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
	
	AccessTransport createAccessTransport();
	

}
