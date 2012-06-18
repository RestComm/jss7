package org.mobicents.protocols.ss7.map.service.callhandling;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class MAPDialogCallHandlingImpl extends MAPDialogImpl implements MAPDialogCallHandling {
	// Include these constants in MAPApplicationContextName and MAPOperationCode
	// sendRoutingInfo_Request: add constant to MAPMessageType
	// sendRoutingInfo_Response: add constant to MAPMessageType
	protected static final int locationInfoRetrievalContext = 5;
	protected static final int sendRoutingInfo = 22;
	protected static final int version = 3;
	
	
	protected MAPDialogCallHandlingImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
			  MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	// TODO: implement methods to send request/response
	@Override
	public Long addSendRoutingInformationRequest(ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
			InterrogationType interrogationType, MAPExtensionContainer extensionContainer, 
			CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason, 
			ExtBasicServiceCode basicServiceGroup, ExtBasicServiceCode basicServiceGroup2, 
			ExternalSignalInfo networkSignalInfo, ExternalSignalInfo networkSignalInfo2, 
			ExtExternalSignalInfo additionalSignalInfo, AlertingPattern alertingPattern, 
			IstSupportIndicator istSupportIndicator) throws MAPException {
		return this.addSendRoutingInformationRequest(_Timer_Default, msisdn, gmscAddress, 
				interrogationType, extensionContainer, callReferenceNumber, forwardingReason, 
			    basicServiceGroup, basicServiceGroup2, networkSignalInfo, networkSignalInfo2, 
				additionalSignalInfo, alertingPattern, istSupportIndicator);
	}

	@Override
	public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
			InterrogationType interrogationType, MAPExtensionContainer extensionContainer, 
			CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason, 
			ExtBasicServiceCode basicServiceGroup, ExtBasicServiceCode basicServiceGroup2, 
			ExternalSignalInfo networkSignalInfo, ExternalSignalInfo networkSignalInfo2, 
			ExtExternalSignalInfo additionalSignalInfo, AlertingPattern alertingPattern, 
			IstSupportIndicator istSupportIndicator) throws MAPException {
		if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
		 || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
			throw new MAPException("Bad application context name for addSendRoutingInformationRequest: must be locationInfoRetrievalContext_V3");
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
		if (customInvokeTimeout == _Timer_Default)
			invoke.setTimeout(_Timer_ml);
		else invoke.setTimeout(customInvokeTimeout);
		
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfo);
		invoke.setOperationCode(oc);
		
		if(true) { // validate parameters here...
		  SendRoutingInformationRequestImpl req = new SendRoutingInformationRequestImpl(msisdn, gmscAddress, 
					interrogationType, extensionContainer, callReferenceNumber, forwardingReason, 
					basicServiceGroup, basicServiceGroup2, networkSignalInfo, networkSignalInfo2, 
					additionalSignalInfo, alertingPattern, istSupportIndicator);
		  AsnOutputStream aos = new AsnOutputStream();
		  req.encodeData(aos);
		
		  Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
	  	  p.setTagClass(req.getTagClass());
		  p.setPrimitive(req.getIsPrimitive());
		  p.setTag(req.getTag());
		  p.setData(aos.toByteArray());
		  invoke.setParameter(p);
		}
		
		Long invokeId;
		try {
			invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}

		this.sendInvokeComponent(invoke);
		return invokeId;
	}

	@Override
	public void addSendRoutingInformationResponse(long invokeId, IMSI imsi, ExtendedRoutingInfo extRoutingInfo, 
			SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer, 
			ExtBasicServiceCode basicService, ExtBasicServiceCode basicService2, 
			RoutingInfo routingInfo2, ISDNAddressString vmscAddress, ISDNAddressString msisdn, 
			NumberPortabilityStatus nrPortabilityStatus, SupportedCamelPhases supportedCamelPhases, 
			OfferedCamel4CSIs offeredCamel4CSIs, UnavailabilityCause unavailabilityCause, 
			ExternalSignalInfo gsmBearerCapability) throws MAPException {
		if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
		 || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
			throw new MAPException("Bad application context name for addSendRoutingInformationResponse: must be locationInfoRetrievalContext_V3");
	
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfo);
		resultLast.setOperationCode(oc);
		
		if(true) { // validate parameters here...
			SendRoutingInformationResponseImpl res = new SendRoutingInformationResponseImpl(imsi, extRoutingInfo, 
					subscriberInfo, extensionContainer, basicService, basicService2, 
					routingInfo2, vmscAddress, msisdn, nrPortabilityStatus, supportedCamelPhases, 
				    offeredCamel4CSIs, unavailabilityCause, gsmBearerCapability); 
			AsnOutputStream aos = new AsnOutputStream();
			res.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(res.getTagClass());
			p.setPrimitive(res.getIsPrimitive());
			p.setTag(res.getTag());
			p.setData(aos.toByteArray());
			resultLast.setParameter(p);
		}

		this.sendReturnResultLastComponent(resultLast);
	}
}