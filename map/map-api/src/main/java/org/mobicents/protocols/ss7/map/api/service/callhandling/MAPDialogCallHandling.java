package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;


/*
 * 
 * @author cristian veliscu
 * 
 */
public interface MAPDialogCallHandling extends MAPDialog {
	public Long addSendRoutingInformationRequest(ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
			InterrogationType interrogationType, MAPExtensionContainer extensionContainer, 
			CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason, 
			ExtBasicServiceCode basicServiceGroup, ExtBasicServiceCode basicServiceGroup2, 
			ExternalSignalInfo networkSignalInfo, ExternalSignalInfo networkSignalInfo2, 
			ExtExternalSignalInfo additionalSignalInfo, AlertingPattern alertingPattern, 
			IstSupportIndicator istSupportIndicator) throws MAPException;
	public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
			InterrogationType interrogationType, MAPExtensionContainer extensionContainer, 
			CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason, 
			ExtBasicServiceCode basicServiceGroup, ExtBasicServiceCode basicServiceGroup2, 
			ExternalSignalInfo networkSignalInfo, ExternalSignalInfo networkSignalInfo2, 
			ExtExternalSignalInfo additionalSignalInfo, AlertingPattern alertingPattern, 
			IstSupportIndicator istSupportIndicator) throws MAPException;
	public void addSendRoutingInformationResponse(long invokeId, IMSI imsi, ExtendedRoutingInfo extRoutingInfo, 
			SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer, 
			ExtBasicServiceCode basicService, ExtBasicServiceCode basicService2, 
			RoutingInfo routingInfo2, ISDNAddressString vmscAddress, ISDNAddressString msisdn, 
			NumberPortabilityStatus nrPortabilityStatus, SupportedCamelPhases supportedCamelPhases, 
			OfferedCamel4CSIs offeredCamel4CSIs, UnavailabilityCause unavailabilityCause, 
			ExternalSignalInfo gsmBearerCapability) throws MAPException;
}