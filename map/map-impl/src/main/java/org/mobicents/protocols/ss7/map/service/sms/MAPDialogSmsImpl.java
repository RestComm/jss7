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

import org.mobicents.protocols.asn.AsnException;
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
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
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
		
		if (sm_RP_DA == null || sm_RP_OA == null || sm_RP_UI == null)
			throw new MAPException("sm_RP_DA,sm_RP_OA and sm_RP_UI must not be null");
		
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.mo_forwardSM);
			invoke.setOperationCode(oc);
			
			// Sequence of Parameter
			
			ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

			Parameter p1 = ((SM_RP_DAImpl) sm_RP_DA).encode();
			lstPar.add(p1);

			Parameter p2 = ((SM_RP_OAImpl) sm_RP_OA).encode();
			lstPar.add(p2);

			Parameter p3 = TcapFactory.createParameter();
			p3.setTagClass(Tag.CLASS_UNIVERSAL);
			p3.setTag(Tag.STRING_OCTET);
			p3.setData(sm_RP_UI);
			lstPar.add(p3);

			Parameter p4 = null;
			if (extensionContainer != null) {
				p4 = ((MAPExtensionContainerImpl) extensionContainer).encode();
				lstPar.add(p4);
			}

			Parameter p5 = null;
			if (imsi != null) {
				p5 = ((IMSIImpl) imsi).encode();
				p5.setTagClass(Tag.CLASS_UNIVERSAL);
				p5.setTag(Tag.STRING_OCTET);
				lstPar.add(p5);
			}

			Parameter p = TcapFactory.createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(false);
			p.setTag(Tag.SEQUENCE);

			Parameter[] pp = new Parameter[lstPar.size()];
			lstPar.toArray(pp);
			p.setParameters(pp);

			invoke.setParameter(p);

			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	@Override
	public void addMoForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException {
		
		ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		resultLast.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.mo_forwardSM);
		resultLast.setOperationCode(oc);

		// if (sm_RP_UI != null || extensionContainer != null) {

		// Sequence of Parameter
		AsnOutputStream aos = new AsnOutputStream();

		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		Parameter p1 = null;
		if (sm_RP_UI != null) {
			aos.reset();
			p1 = TcapFactory.createParameter();
			p1.setTagClass(Tag.CLASS_UNIVERSAL);
			p1.setTag(Tag.STRING_OCTET);
			p1.setData(sm_RP_UI);
			lstPar.add(p1);
		}

		Parameter p2 = null;
		if (extensionContainer != null) {
			p2 = ((MAPExtensionContainerImpl) extensionContainer).encode();
			lstPar.add(p2);
		}

		Parameter p = TcapFactory.createParameter();
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
	public Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, Boolean moreMessagesToSend,
			MAPExtensionContainer extensionContainer) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMtForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, Boolean sm_RP_PRI, AddressString serviceCentreAddress,
			MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, byte[] sM_RP_SMEA) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI, MAPExtensionContainer extensionContainer)
			throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString derviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
			Integer sbsentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, Boolean deliveryOutcomeIndicator,
			SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN, MAPExtensionContainer extensionContainer) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus, MAPExtensionContainer extensionContainer,
			Integer absentSubscriberDiagnosticSM, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long addAlertServiceCentreRequest(ISDNAddressString getMsisdn, AddressString serviceCentreAddress) throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAlertServiceCentreResponse(long invokeId) throws MAPException {
		// TODO Auto-generated method stub
		
	}

}


