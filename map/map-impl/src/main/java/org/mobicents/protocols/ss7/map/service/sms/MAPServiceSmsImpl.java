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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSIndicationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.Tag;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPServiceSmsImpl extends MAPServiceBaseImpl implements MAPServiceSms {
	
	public MAPServiceSmsImpl(MAPProviderImpl mapProviderImpl) {
		super(mapProviderImpl);
	}

	
	/*
	 * Creating a new outgoing MAP SMS dialog and adding it to the
	 * MAPProvider.dialog collection
	 * 
	 */
	@Override
	public MAPDialogSms createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
			AddressString destReference) throws MAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new MAPException(
					"Cannot create MAPDialogSms because MAPServiceSms is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		MAPDialogSmsImpl dialog = new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl,
				this, origReference, destReference);

		this.PutMADDialogIntoCollection(dialog);

		return dialog;
	}

	@Override
	protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
		return new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
	}

	@Override
	public void addMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
		super.addMAPServiceListener(mapServiceListener);
	}

	@Override
	public void removeMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
		super.removeMAPServiceListener(mapServiceListener);
	}

	
	@Override
	public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
		MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
		int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();
		
		switch(ctx) {
		case shortMsgAlertContext:
			if (vers <= 2)
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			else {
				long[] altOid = dialogApplicationContext.getOID();
				altOid[7] = 2;
				ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
				return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
			}
			
		case shortMsgMORelayContext:
		case shortMsgMTRelayContext:
		case shortMsgGatewayContext:
			if (vers <= 3)
				return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			else {
				long[] altOid = dialogApplicationContext.getOID();
				altOid[7] = 3;
				ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
				return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
			}
		}
		
		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}
	
	@Override
	public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog, Long invokeId, Long linkedId)
			throws MAPParsingComponentException {
		MAPDialogSmsImpl mapDialogSmsImpl = (MAPDialogSmsImpl) mapDialog;

		Long ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
		
		int ocValueInt = (int) (long)ocValue;
		switch (ocValueInt) {
		case MAPOperationCode.mo_forwardSM:
			if (compType == ComponentType.Invoke)
				this.moForwardShortMessageRequest(parameter, mapDialogSmsImpl, invokeId);
			else
				this.moForwardShortMessageResponse(parameter, mapDialogSmsImpl, invokeId);
			break;

		case MAPOperationCode.mt_forwardSM:
			if (compType == ComponentType.Invoke)
				this.mtForwardShortMessageRequest(parameter, mapDialogSmsImpl, invokeId);
			else
				this.mtForwardShortMessageResponse(parameter, mapDialogSmsImpl, invokeId);
			break;

		case MAPOperationCode.sendRoutingInfoForSM:
			if (compType == ComponentType.Invoke)
				this.sendRoutingInfoForSMRequest(parameter, mapDialogSmsImpl, invokeId);
			else
				this.sendRoutingInfoForSMResponse(parameter, mapDialogSmsImpl, invokeId);
			break;

		case MAPOperationCode.reportSM_DeliveryStatus:
			if (compType == ComponentType.Invoke)
				this.reportSMDeliveryStatusRequest(parameter, mapDialogSmsImpl, invokeId);
			else
				this.reportSMDeliveryStatusResponse(parameter, mapDialogSmsImpl, invokeId);
			break;

		case MAPOperationCode.informServiceCentre:
			if (compType == ComponentType.Invoke)
				this.informServiceCentreRequest(parameter, mapDialogSmsImpl, invokeId);
			break;

		case MAPOperationCode.alertServiceCentre:
			if (compType == ComponentType.Invoke)
				this.alertServiceCentreRequest(parameter, mapDialogSmsImpl, invokeId);
			else
				this.alertServiceCentreResponse(parameter, mapDialogSmsImpl, invokeId);
			break;
			
		default:
			new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
		}
	}

	@Override
	public Boolean checkInvokeTimeOut(MAPDialog dialog, Invoke invoke) {
		return false;
	}

	private void moForwardShortMessageRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter == null)
			throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter is mandatory but not found",
					MAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			if (parameters.length < 3)
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Needs at least 3 mandatory parameters, found"
						+ parameters.length, MAPParsingComponentExceptionReason.MistypedParameter);

			// SM_RP_DA
			Parameter p = parameters[0];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive())
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 0 bad tag class or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			SM_RP_DAImpl sm_RP_DA = new SM_RP_DAImpl();
			sm_RP_DA.decode(p);

			// SM_RP_OA
			p = parameters[1];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive())
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 1 bad tag class or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
			sm_RP_OA.decode(p);

			// sm-RP-UI
			p = parameters[2];
			if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive())
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 2 bad tag class or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			if (p.getTag() != Tag.STRING_OCTET)
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 2 tag must be STRING_OCTET, found: "
						+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			byte[] sm_RP_UI = p.getData();

			// ExtensionContainer
			MAPExtensionContainerImpl extensionContainer = null;
			if (parameters.length >= 4 && parameters[3].getTagClass() == Tag.CLASS_UNIVERSAL && parameters[3].getTag() == Tag.SEQUENCE) {
				p = parameters[3];
				extensionContainer = new MAPExtensionContainerImpl();
				extensionContainer.decode(p);
			}

			// imsi
			IMSIImpl imsi = null;
			p = parameters[parameters.length - 1];
			if (parameters.length >= 4 && p.getTagClass() == Tag.CLASS_UNIVERSAL && p.getTag() == Tag.STRING_OCTET) {
				imsi = new IMSIImpl();
				imsi.decode(p);
			}

			MoForwardShortMessageRequestIndicationImpl ind = new MoForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer,
					imsi);

			ind.setInvokeId(invokeId);
			ind.setMAPDialog(mapDialogImpl);

			for (MAPServiceListener serLis : this.serviceListeners) {
				((MAPServiceSmsListener) serLis).onMoForwardShortMessageIndication(ind);
			}
		} else {
			throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Expected Parameter tag as SEQUENCE but received "
					+ parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void moForwardShortMessageResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		if (parameter != null) {
			if (parameter.getTag() == Tag.SEQUENCE) {
				Parameter[] parameters = parameter.getParameters();

				byte[] sm_RP_UI = null;
				MAPExtensionContainerImpl extensionContainer = null;

				for (Parameter p : parameters) {
					// sm-RP-UI
					if (p.getTagClass() == Tag.CLASS_UNIVERSAL && p.isPrimitive() && p.getTag() == Tag.STRING_OCTET && sm_RP_UI == null) {
						sm_RP_UI = p.getData();
					}

					// ExtensionContainer
					if (p.getTagClass() == Tag.CLASS_UNIVERSAL && !p.isPrimitive() && p.getTag() == Tag.SEQUENCE && extensionContainer == null) {
						extensionContainer = new MAPExtensionContainerImpl();
						extensionContainer.decode(p);
					}
				}

				MoForwardShortMessageResponseIndicationImpl ind = new MoForwardShortMessageResponseIndicationImpl(sm_RP_UI, extensionContainer);

				ind.setInvokeId(invokeId);
				ind.setMAPDialog(mapDialogImpl);

				for (MAPServiceListener serLis : this.serviceListeners) {
					((MAPServiceSmsListener) serLis).onMoForwardShortMessageRespIndication(ind);
				}
			} else {
				throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Expected Parameter tag as SEQUENCE but received "
						+ parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}
	
	private void mtForwardShortMessageRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void mtForwardShortMessageResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void sendRoutingInfoForSMRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void sendRoutingInfoForSMResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void reportSMDeliveryStatusRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void reportSMDeliveryStatusResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void informServiceCentreRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void alertServiceCentreRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}
	
	private void alertServiceCentreResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
		// TODO: implement it
	}

}


