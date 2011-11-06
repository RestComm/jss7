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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.CAPServiceBaseImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPServiceCircuitSwitchedCallImpl extends CAPServiceBaseImpl implements CAPServiceCircuitSwitchedCall {

	protected Logger loger = Logger.getLogger(CAPServiceCircuitSwitchedCallImpl.class);

	public CAPServiceCircuitSwitchedCallImpl(CAPProviderImpl capProviderImpl) {
		super(capProviderImpl);
	}

	@Override
	public CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new CAPException(
					"Cannot create CAPDialogCircuitSwitchedCall because CAPServiceCircuitSwitchedCall is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress);
		CAPDialogCircuitSwitchedCallImpl dialog = new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog, this.capProviderImpl, this);

		this.putCAPDialogIntoCollection(dialog);

		return dialog;
	}

	@Override
	public void addCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
		super.addCAPServiceListener(capServiceListener);
	}

	@Override
	public void removeCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
		super.removeCAPServiceListener(capServiceListener);
	}

	@Override
	protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
		return new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog, this.capProviderImpl, this);
	}

	@Override
	public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
		
		switch(dialogApplicationContext) {
		case CapV1_gsmSSF_to_gsmSCF:
		case CapV2_gsmSSF_to_gsmSCF:
		case CapV2_assistGsmSSF_to_gsmSCF:
		case CapV2_gsmSRF_to_gsmSCF:
		case CapV3_gsmSSF_scfGeneric:
		case CapV3_gsmSSF_scfAssistHandoff:
		case CapV3_gsmSRF_gsmSCF:
		case CapV4_gsmSSF_scfGeneric:
		case CapV4_gsmSSF_scfAssistHandoff:
		case CapV4_scf_gsmSSFGeneric:
		case CapV4_gsmSRF_gsmSCF:
			return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
		}
		
		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}

	@Override
	public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog, Long invokeId, Long linkedId)
			throws CAPParsingComponentException {

		CAPDialogCircuitSwitchedCallImpl capDialogCircuitSwitchedCallImpl = (CAPDialogCircuitSwitchedCallImpl) capDialog;

		Long ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
		CAPApplicationContext acn = capDialog.getApplicationContext();
		int ocValueInt = (int) (long) ocValue;

		switch (ocValueInt) {
		case CAPOperationCode.initialDP:
			if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
					|| acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric) {
				if (compType == ComponentType.Invoke) {
					this.initialDpRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
				}
			}
			break;

		case CAPOperationCode.requestReportBCSMEvent:
			if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
					|| acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric) {
				if (compType == ComponentType.Invoke) {
					this.requestReportBCSMEventRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
				}
			}
			break;

		case CAPOperationCode.applyCharging:
			if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
					|| acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric) {
				if (compType == ComponentType.Invoke) {
					this.applyChargingRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
				}
			}
			break;

		case CAPOperationCode.eventReportBCSM:
			if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
					|| acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric) {
				if (compType == ComponentType.Invoke) {
					eventReportBCSMRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
				}
			}
			break;

		case CAPOperationCode.continueCode:
			if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
					|| acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric) {
				if (compType == ComponentType.Invoke) {
					continueRequest(parameter, capDialogCircuitSwitchedCallImpl, invokeId);
				}
			}
			break;

		default:
			new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
		}
	}

	private void initialDpRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId) throws CAPParsingComponentException {

		if (parameter == null)
			throw new CAPParsingComponentException("Error while decoding initialDpRequest: Parameter is mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new CAPParsingComponentException("Error while decoding initialDpRequest: Bad tag or tagClass or parameter is primitive, received tag="
					+ parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);
		InitialDPRequestIndicationImpl ind = new InitialDPRequestIndicationImpl();
		ind.decodeData(ais, buf.length);

		ind.setInvokeId(invokeId);
		ind.setCAPDialog(capDialogImpl);

		for (CAPServiceListener serLis : this.serviceListeners) {
			try {
				((CAPServiceCircuitSwitchedCallListener) serLis).onInitialDPRequestIndication(ind);
			} catch (Exception e) {
				loger.error("Error processing initialDpRequest: " + e.getMessage(), e);
			}
		}
	}

	private void requestReportBCSMEventRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId)
			throws CAPParsingComponentException {

		if (parameter == null)
			throw new CAPParsingComponentException("Error while decoding requestReportBCSMEventRequest: Parameter is mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new CAPParsingComponentException(
					"Error while decoding requestReportBCSMEventRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
					CAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);
		RequestReportBCSMEventRequestIndicationImpl ind = new RequestReportBCSMEventRequestIndicationImpl();
		ind.decodeData(ais, buf.length);

		ind.setInvokeId(invokeId);
		ind.setCAPDialog(capDialogImpl);

		for (CAPServiceListener serLis : this.serviceListeners) {
			try {
				((CAPServiceCircuitSwitchedCallListener) serLis).onRequestReportBCSMEventRequestIndication(ind);
			} catch (Exception e) {
				loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
			}
		}
	}

	private void applyChargingRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId) throws CAPParsingComponentException {

		if (parameter == null)
			throw new CAPParsingComponentException("Error while decoding applyChargingRequest: Parameter is mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new CAPParsingComponentException(
					"Error while decoding applyChargingRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
					CAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);
		ApplyChargingRequestIndicationImpl ind = new ApplyChargingRequestIndicationImpl();
		ind.decodeData(ais, buf.length);

		ind.setInvokeId(invokeId);
		ind.setCAPDialog(capDialogImpl);

		for (CAPServiceListener serLis : this.serviceListeners) {
			try {
				((CAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingRequestIndication(ind);
			} catch (Exception e) {
				loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
			}
		}
	}

	private void eventReportBCSMRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId) throws CAPParsingComponentException {

		if (parameter == null)
			throw new CAPParsingComponentException("Error while decoding eventReportBCSM: Parameter is mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);

		if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
			throw new CAPParsingComponentException(
					"Error while decoding eventReportBCSM: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
					CAPParsingComponentExceptionReason.MistypedParameter);

		byte[] buf = parameter.getData();
		AsnInputStream ais = new AsnInputStream(buf);
		EventReportBCSMRequestIndicationImpl ind = new EventReportBCSMRequestIndicationImpl();
		ind.decodeData(ais, buf.length);

		ind.setInvokeId(invokeId);
		ind.setCAPDialog(capDialogImpl);

		for (CAPServiceListener serLis : this.serviceListeners) {
			try {
				((CAPServiceCircuitSwitchedCallListener) serLis).onEventReportBCSMRequestIndication(ind);
			} catch (Exception e) {
				loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
			}
		}
	}

	private void continueRequest(Parameter parameter, CAPDialogCircuitSwitchedCallImpl capDialogImpl, Long invokeId) throws CAPParsingComponentException {

		ContinueRequestIndicationImpl ind = new ContinueRequestIndicationImpl();

		ind.setInvokeId(invokeId);
		ind.setCAPDialog(capDialogImpl);

		for (CAPServiceListener serLis : this.serviceListeners) {
			try {
				((CAPServiceCircuitSwitchedCallListener) serLis).onContinueRequestIndication(ind);
			} catch (Exception e) {
				loger.error("Error processing continueRequest: " + e.getMessage(), e);
			}
		}
	}
}

