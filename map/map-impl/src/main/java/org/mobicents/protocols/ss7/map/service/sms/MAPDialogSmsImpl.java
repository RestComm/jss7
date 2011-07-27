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

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
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

		MoForwardShortMessageRequestIndicationImpl ind = new MoForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi);
		Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());		
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);
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
			MoForwardShortMessageResponseIndicationImpl ind = new MoForwardShortMessageResponseIndicationImpl(sm_RP_UI, extensionContainer);
			Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
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

			MtForwardShortMessageRequestIndicationImpl ind = new MtForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, moreMessagesToSend,
					extensionContainer);
			Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());		
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
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
			MtForwardShortMessageResponseIndicationImpl ind = new MtForwardShortMessageResponseIndicationImpl(sm_RP_UI, extensionContainer);
			Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
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
			SendRoutingInfoForSMRequestIndicationImpl ind = new SendRoutingInfoForSMRequestIndicationImpl(msisdn, sm_RP_PRI, serviceCentreAddress,
					extensionContainer, gprsSupportIndicator, sM_RP_MTI, sM_RP_SMEA);
			Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());		
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
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

		SendRoutingInfoForSMResponseIndicationImpl ind = new SendRoutingInfoForSMResponseIndicationImpl(imsi, locationInfoWithLMSI, extensionContainer);
		Parameter p = ind.encode(this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory());		
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);
		resultLast.setParameter(p);

		this.sendReturnResultLastComponent(resultLast);
	}

	@Override
	public Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
			Integer sbsentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, Boolean deliveryOutcomeIndicator,
			SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
		
		if (msisdn == null || serviceCentreAddress == null || sMDeliveryOutcome == null)
			throw new MAPException("msisdn, serviceCentreAddress and sMDeliveryOutcome must not be null");
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.reportSM_DeliveryStatus);
			invoke.setOperationCode(oc);
			
			// Sequence of Parameter
			AsnOutputStream aos = new AsnOutputStream();
			ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

			// msisdn
			Parameter p = ((ISDNAddressStringImpl) msisdn).encode();
			lstPar.add(p);

			// serviceCentreAddress
			p = ((AddressStringImpl) serviceCentreAddress).encode();
			lstPar.add(p);
			
			// sm-DeliveryOutcome
			p = ((AddressStringImpl) serviceCentreAddress).encode();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.ENUMERATED);
			p.setData(new byte[]{ (byte)sMDeliveryOutcome.getCode() });
			lstPar.add(p);
			
			// absentSubscriberDiagnosticSM
			if (sbsentSubscriberDiagnosticSM != null) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_AbsentSubscriberDiagnosticSM);
				aos.reset();
				aos.writeIntegerData(sbsentSubscriberDiagnosticSM);
				p.setData(aos.toByteArray());
				lstPar.add(p);
			}

			if (extensionContainer != null) {
				p = ((MAPExtensionContainerImpl) extensionContainer).encode();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_ExtensionContainer);
				lstPar.add(p);
			}

			if (gprsSupportIndicator != null && gprsSupportIndicator == true) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_GprsSupportIndicator);
				p.setData(new byte[0]);
				lstPar.add(p);
			}

			if (deliveryOutcomeIndicator != null && deliveryOutcomeIndicator == true) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_DeliveryOutcomeIndicator);
				p.setData(new byte[0]);
				lstPar.add(p);
			}

			if (additionalSMDeliveryOutcome != null) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_AdditionalSMDeliveryOutcome);
				p.setData(new byte[] { (byte) additionalSMDeliveryOutcome.getCode() });
				lstPar.add(p);
			}
			
			if (additionalAbsentSubscriberDiagnosticSM != null) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(ReportSMDeliveryStatusRequestIndicationImpl._TAG_AdditionalAbsentSubscriberDiagnosticSM);
				aos.reset();
				aos.writeIntegerData(additionalAbsentSubscriberDiagnosticSM);
				p.setData(aos.toByteArray());
				lstPar.add(p);
			}

			p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
			
			Parameter[] pp = new Parameter[lstPar.size()];
			lstPar.toArray(pp);
			p.setParameters(pp);

			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		} catch (IOException e) {
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

		// if (sm_RP_UI != null || extensionContainer != null) {

		// Sequence of Parameter
		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		Parameter p;
		if (storedMSISDN != null) {
			p = ((ISDNAddressStringImpl) storedMSISDN).encode();
			lstPar.add(p);
		}

		if (extensionContainer != null) {
			p = ((MAPExtensionContainerImpl) extensionContainer).encode();
			lstPar.add(p);
		}

		p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);

		Parameter[] pp = new Parameter[lstPar.size()];
		lstPar.toArray(pp);
		p.setParameters(pp);

		resultLast.setParameter(p);

		// }

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
			
			// Sequence of Parameter
			AsnOutputStream aos = new AsnOutputStream();
			ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

			// storedMSISDN
			Parameter p;
			if (storedMSISDN != null) {
				p = ((ISDNAddressStringImpl) storedMSISDN).encode();
				lstPar.add(p);
			}
			
			// mw-Status
			if (mwStatus != null) {
				p = ((MWStatusImpl) mwStatus).encode();
				lstPar.add(p);
			}

			// extensionContainer
			if (extensionContainer != null) {
				p = ((MAPExtensionContainerImpl) extensionContainer).encode();
				lstPar.add(p);
			}
			
			// absentSubscriberDiagnosticSM
			if (absentSubscriberDiagnosticSM != null) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_UNIVERSAL);
				p.setTag(Tag.INTEGER);
				aos.reset();
				aos.writeIntegerData(absentSubscriberDiagnosticSM);
				p.setData(aos.toByteArray());
				lstPar.add(p);
			}
			
			// additionalAbsentSubscriberDiagnosticSM
			if (additionalAbsentSubscriberDiagnosticSM != null) {
				p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(InformServiceCentreRequestIndicationImpl._TAG_AdditionalAbsentSubscriberDiagnosticSM);
				aos.reset();
				aos.writeIntegerData(additionalAbsentSubscriberDiagnosticSM);
				p.setData(aos.toByteArray());
				lstPar.add(p);
			}

			p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
			
			Parameter[] pp = new Parameter[lstPar.size()];
			lstPar.toArray(pp);
			p.setParameters(pp);

			invoke.setParameter(p);

			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public Long addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress) throws MAPException {
		
		if (msisdn == null || serviceCentreAddress == null)
			throw new MAPException("msisdn and serviceCentreAddress must not be null");
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			// Operation Code
			OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.alertServiceCentre);
			invoke.setOperationCode(oc);
			
			// Sequence of Parameter
			ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

			// msisdn
			Parameter p;
			p = ((ISDNAddressStringImpl) msisdn).encode();
			lstPar.add(p);

			// serviceCentreAddress
			p = ((AddressStringImpl) serviceCentreAddress).encode();
			lstPar.add(p);

			p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);
			
			Parameter[] pp = new Parameter[lstPar.size()];
			lstPar.toArray(pp);
			p.setParameters(pp);

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

		// if (sm_RP_UI != null || extensionContainer != null) {

		// Sequence of Parameter
		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		Parameter p;

		p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);

		Parameter[] pp = new Parameter[lstPar.size()];
		lstPar.toArray(pp);
		p.setParameters(pp);

		resultLast.setParameter(p);

		// }

		this.sendReturnResultLastComponent(resultLast);
	}

}


