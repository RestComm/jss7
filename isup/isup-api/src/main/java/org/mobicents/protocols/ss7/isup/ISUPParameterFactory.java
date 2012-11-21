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
package org.mobicents.protocols.ss7.isup;

import java.io.Serializable;
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
	
	ContinuityIndicators createContinuityIndicators();
	
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

	GenericDigits createGenericDigits();
	

}
