package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/*
 *SendRoutingInfoRes ::= [3] SEQUENCE {
 *  imsi [9] IMSI OPTIONAL,
 *  extendedRoutingInfo ExtendedRoutingInfo OPTIONAL,
 *  cug-CheckInfo [3] CUG-CheckInfo OPTIONAL,
 *  cugSubscriptionFlag [6] NULL OPTIONAL*,
 *  subscriberInfo [7] SubscriberInfo OPTIONAL,
 *  ss-List [1] SS-List OPTIONAL,
 *  basicService [5] Ext-BasicServiceCode OPTIONAL,
 *  forwardingInterrogationRequired [4] NULL OPTIONAL*,
 *  vmsc-Address [2] ISDN-AddressString OPTIONAL,
 *  extensionContainer [0] ExtensionContainer OPTIONAL,
 *  ... ,
 *  naea-PreferredCI [10] NAEA-PreferredCI OPTIONAL,
 *  ccbs-Indicators [11] CCBS-Indicators OPTIONAL,
 *  msisdn [12] ISDN-AddressString OPTIONAL,
 *  numberPortabilityStatus [13] NumberPortabilityStatus OPTIONAL,
 *  istAlertTimer [14] IST-AlertTimerValue OPTIONAL,
 *  supportedCamelPhasesInVMSC [15] SupportedCamelPhases OPTIONAL,
 *  offeredCamel4CSIsInVMSC [16] OfferedCamel4CSIs OPTIONAL,
 *  routingInfo2 [17] RoutingInfo OPTIONAL,
 *  ss-List2 [18] SS-List OPTIONAL,
 *  basicService2 [19] Ext-BasicServiceCode OPTIONAL,
 *  allowedServices [20] AllowedServices OPTIONAL,
 *  unavailabilityCause [21] UnavailabilityCause OPTIONAL,
 *  releaseResourcesSupported [22] NULL OPTIONAL*,
 *  gsm-BearerCapability [23] ExternalSignalInfo OPTIONAL }
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public interface SendRoutingInformationResponse extends CallHandlingMessage {			
	public IMSI getIMSI();
	public ISDNAddressString getMsisdn(); //OCTET STRING
	public ExtendedRoutingInfo getExtendedRoutingInfo(); //CHOICE
	public RoutingInfo getRoutingInfo2(); //CHOICE
	public SubscriberInfo getSubscriberInfo(); //SEQUENCE
	public ExtBasicServiceCode getBasicService(); //CHOICE
	public ExtBasicServiceCode getBasicService2(); //CHOICE
	public ISDNAddressString getVmscAddress(); //OCTET STRING
	public NumberPortabilityStatus getNumberPortabilityStatus(); //ENUMERATED
	public SupportedCamelPhases getSupportedCamelPhases(); //BIT STRING
	public OfferedCamel4CSIs getOfferedCamel4CSIs(); //BIT STRING
	public ExternalSignalInfo getGsmBearerCapability(); //SEQUENCE
	public UnavailabilityCause getUnavailabilityCause(); //ENUMERATED
	public MAPExtensionContainer getExtensionContainer(); //SEQUENCE
}