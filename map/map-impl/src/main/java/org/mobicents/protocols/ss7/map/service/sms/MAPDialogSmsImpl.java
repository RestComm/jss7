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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPDialogSmsImpl extends MAPDialogImpl implements MAPDialogSms {

	protected MAPDialogSmsImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl, MAPServiceSms mapService,
			AddressString origReference, AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	@Override
	public Long addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi)
			throws MAPException {
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long)MAPOperationCode.mo_forwardSM);
		invoke.setOperationCode(oc);

		MoForwardShortMessageRequestIndicationImpl req = new MoForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi);
		AsnOutputStream aos = new AsnOutputStream();
		req.encodeData(aos);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(req.getTagClass());
		p.setPrimitive(req.getIsPrimitive());
		p.setTag(req.getTag());
		p.setData(aos.toByteArray());
		invoke.setParameter(p);

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
	public void addMoForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.mo_forwardSM);
		resultLast.setOperationCode(oc);

		if (sm_RP_UI != null || extensionContainer != null) {

			MoForwardShortMessageResponseIndicationImpl req = new MoForwardShortMessageResponseIndicationImpl(sm_RP_UI, extensionContainer);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			resultLast.setParameter(p);
		}

		this.sendReturnResultLastComponent(resultLast);
	}

	@Override
	public Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, Boolean moreMessagesToSend,
			MAPExtensionContainer extensionContainer) throws MAPException {
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.mt_forwardSM);
			invoke.setOperationCode(oc);
			
			MtForwardShortMessageRequestIndicationImpl req = new MtForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, moreMessagesToSend,
					extensionContainer);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);
			
			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public void addMtForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.mt_forwardSM);
		resultLast.setOperationCode(oc);

		if (sm_RP_UI != null || extensionContainer != null) {

			MtForwardShortMessageResponseIndicationImpl resp = new MtForwardShortMessageResponseIndicationImpl(sm_RP_UI, extensionContainer);
			AsnOutputStream aos = new AsnOutputStream();
			resp.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(resp.getTagClass());
			p.setPrimitive(resp.getIsPrimitive());
			p.setTag(resp.getTag());
			p.setData(aos.toByteArray());
			resultLast.setParameter(p);
		}

		this.sendReturnResultLastComponent(resultLast);
	}

	@Override
	public Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, Boolean sm_RP_PRI, AddressString serviceCentreAddress,
			MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, byte[] sM_RP_SMEA) throws MAPException {
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
		
		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForSM);
		invoke.setOperationCode(oc);

		try {
			SendRoutingInfoForSMRequestIndicationImpl req = new SendRoutingInfoForSMRequestIndicationImpl(msisdn, sm_RP_PRI, serviceCentreAddress,
					extensionContainer, gprsSupportIndicator, sM_RP_MTI, sM_RP_SMEA);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI, MAPExtensionContainer extensionContainer)
			throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForSM);
		resultLast.setOperationCode(oc);

		SendRoutingInfoForSMResponseIndicationImpl resp = new SendRoutingInfoForSMResponseIndicationImpl(imsi, locationInfoWithLMSI, extensionContainer);
		AsnOutputStream aos = new AsnOutputStream();
		resp.encodeData(aos);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(resp.getTagClass());
		p.setPrimitive(resp.getIsPrimitive());
		p.setTag(resp.getTag());
		p.setData(aos.toByteArray());
		resultLast.setParameter(p);		
		
		this.sendReturnResultLastComponent(resultLast);
	}

	@Override
	public Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
			Integer absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, Boolean deliveryOutcomeIndicator,
			SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
		
		if (msisdn == null || serviceCentreAddress == null || sMDeliveryOutcome == null)
			throw new MAPException("msisdn, serviceCentreAddress and sMDeliveryOutcome must not be null");
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
		
		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.reportSM_DeliveryStatus);
			invoke.setOperationCode(oc);
			
			ReportSMDeliveryStatusRequestIndicationImpl req = new ReportSMDeliveryStatusRequestIndicationImpl(msisdn, serviceCentreAddress, sMDeliveryOutcome,
					absentSubscriberDiagnosticSM, extensionContainer, gprsSupportIndicator, deliveryOutcomeIndicator, additionalSMDeliveryOutcome,
					additionalAbsentSubscriberDiagnosticSM);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN, MAPExtensionContainer extensionContainer) throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.reportSM_DeliveryStatus);
		resultLast.setOperationCode(oc);

		if (storedMSISDN != null || extensionContainer != null) {

			ReportSMDeliveryStatusResponseIndicationImpl resp = new ReportSMDeliveryStatusResponseIndicationImpl(storedMSISDN, extensionContainer);
			AsnOutputStream aos = new AsnOutputStream();
			resp.encodeData(aos);

			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(resp.getTagClass());
			p.setPrimitive(resp.getIsPrimitive());
			p.setTag(resp.getTag());
			p.setData(aos.toByteArray());
			resultLast.setParameter(p);
		}

		this.sendReturnResultLastComponent(resultLast);
	}

	@Override
	public Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus, MAPExtensionContainer extensionContainer,
			Integer absentSubscriberDiagnosticSM, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest(InvokeClass.Class4);

		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.informServiceCentre);
			invoke.setOperationCode(oc);

			InformServiceCentreRequestIndicationImpl req = new InformServiceCentreRequestIndicationImpl(storedMSISDN, mwStatus, extensionContainer,
					absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);
			
			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public Long addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress) throws MAPException {
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.alertServiceCentre);
			invoke.setOperationCode(oc);

			AlertServiceCentreRequestIndicationImpl req = new AlertServiceCentreRequestIndicationImpl(msisdn, serviceCentreAddress);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);
			
			Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public void addAlertServiceCentreResponse(long invokeId) throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.alertServiceCentre);
		resultLast.setOperationCode(oc);

		this.sendReturnResultLastComponent(resultLast);
	}

}


