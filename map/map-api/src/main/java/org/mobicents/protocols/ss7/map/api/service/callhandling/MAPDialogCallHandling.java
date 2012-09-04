/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;


/*
 * 
 * @author cristian veliscu
 * 
 */
public interface MAPDialogCallHandling extends MAPDialog {
	public Long addSendRoutingInformationRequest(
			long mapProtocolVersion, ISDNAddressString msisdn, 
			CUGCheckInfo cugCheckInfo, Integer numberOfForwarding,
			InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
			ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber,
			ForwardingReason forwardingReason, ExtBasicServiceCode basicServiceGroup,
			ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo, boolean suppressionOfAnnouncement,
			MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
			boolean ccbsCall, Integer supportedCCBSPhase, 
			ExtExternalSignalInfo additionalSignalInfo,
			ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
			CallDiversionTreatmentIndicator callDiversionTreatmentIndicator,
			boolean longFTNSupported, boolean suppressVtCSI, boolean suppressIncomingCallBarring,
			boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
			ExternalSignalInfo networkSignalInfo2, SuppressMTSS supressMTSS,
			boolean mtRoamingRetrySupported, EMLPPPriority callPriority) throws MAPException;
	
	public Long addSendRoutingInformationRequest(
			int customInvokeTimeout,
			long mapProtocolVersion, ISDNAddressString msisdn, 
			CUGCheckInfo cugCheckInfo, Integer numberOfForwarding,
			InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
			ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber,
			ForwardingReason forwardingReason, ExtBasicServiceCode basicServiceGroup,
			ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo, boolean suppressionOfAnnouncement,
			MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
			boolean ccbsCall, Integer supportedCCBSPhase, 
			ExtExternalSignalInfo additionalSignalInfo,
			ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
			CallDiversionTreatmentIndicator callDiversionTreatmentIndicator,
			boolean longFTNSupported, boolean suppressVtCSI, boolean suppressIncomingCallBarring,
			boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
			ExternalSignalInfo networkSignalInfo2, SuppressMTSS supressMTSS,
			boolean mtRoamingRetrySupported, EMLPPPriority callPriority) throws MAPException;
	
	public void addSendRoutingInformationResponse(
			long invokeId, long mapProtocolVersion, 
			IMSI imsi, ExtendedRoutingInfo extRoutingInfo, CUGCheckInfo cugCheckInfo,
			boolean cugSubscriptionFlag, SubscriberInfo subscriberInfo, ArrayList<SSCode> ssList,
			ExtBasicServiceCode basicService, boolean forwardingInterrogationRequired,
			ISDNAddressString vmscAddress, MAPExtensionContainer extensionContainer, 
			NAEAPreferredCI naeaPreferredCI, CCBSIndicators ccbsIndicators,
			ISDNAddressString msisdn, NumberPortabilityStatus nrPortabilityStatus, 
			Integer istAlertTimer, SupportedCamelPhases supportedCamelPhases, 
			OfferedCamel4CSIs offeredCamel4CSIs, RoutingInfo routingInfo2, ArrayList<SSCode> ssList2,
			ExtBasicServiceCode basicService2, AllowedServices allowedServices,
			UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported,
			ExternalSignalInfo gsmBearerCapability) throws MAPException;
	
	
	public Long addProvideRoamingNumberRequest( IMSI imsi, ISDNAddressString mscNumber,
			ISDNAddressString msisdn, LMSI lmsi,
			ExternalSignalInfo gsmBearerCapability,
			ExternalSignalInfo networkSignalInfo,
			boolean suppressionOfAnnouncement, ISDNAddressString gmscAddress,
			CallReferenceNumber callReferenceNumber, boolean orInterrogation,
			MAPExtensionContainer extensionContainer,
			AlertingPattern alertingPattern, boolean ccbsCall,
			SupportedCamelPhases supportedCamelPhasesInInterrogatingNode,
			ExtExternalSignalInfo additionalSignalInfo,
			boolean orNotSupportedInGMSC, boolean prePagingSupported,
			boolean longFTNSupported, boolean suppressVtCsi,
			OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode,
			boolean mtRoamingRetrySupported, PagingArea pagingArea,
			EMLPPPriority callPriority, boolean mtrfIndicator,
			ISDNAddressString oldMSCNumber) throws MAPException;
	
	public Long addProvideRoamingNumberRequest(int customInvokeTimeout,
			IMSI imsi, ISDNAddressString mscNumber,
			ISDNAddressString msisdn, LMSI lmsi,
			ExternalSignalInfo gsmBearerCapability,
			ExternalSignalInfo networkSignalInfo,
			boolean suppressionOfAnnouncement, ISDNAddressString gmscAddress,
			CallReferenceNumber callReferenceNumber, boolean orInterrogation,
			MAPExtensionContainer extensionContainer,
			AlertingPattern alertingPattern, boolean ccbsCall,
			SupportedCamelPhases supportedCamelPhasesInInterrogatingNode,
			ExtExternalSignalInfo additionalSignalInfo,
			boolean orNotSupportedInGMSC, boolean prePagingSupported,
			boolean longFTNSupported, boolean suppressVtCsi,
			OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode,
			boolean mtRoamingRetrySupported, PagingArea pagingArea,
			EMLPPPriority callPriority, boolean mtrfIndicator,
			ISDNAddressString oldMSCNumber) throws MAPException;

	public void addProvideRoamingNumberResponse(long invokeId,
			long mapProtocolVersion, ISDNAddressString roamingNumber,
			MAPExtensionContainer extensionContainer,
			boolean releaseResourcesSupported, ISDNAddressString vmscAddress)
			throws MAPException;
}