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

package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class MAPDialogSupplementaryImpl extends MAPDialogImpl implements MAPDialogSupplementary {

	protected MAPDialogSupplementaryImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl, MAPServiceSupplementary mapService,
			AddressString origReference, AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	public Long addProcessUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AlertingPattern alertingPatter, AddressString msisdn)
			throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
		invoke.setOperationCode(oc);

		ProcessUnstructuredSSRequestIndicationImpl req = new ProcessUnstructuredSSRequestIndicationImpl(ussdDataCodingScheme, ussdString, alertingPatter,
				msisdn);
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

	public void addProcessUnstructuredSSResponse(long invokeId, byte ussdDataCodingScheme, USSDString ussdString) throws MAPException {
		Return returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		returnResult.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
		returnResult.setOperationCode(oc);

		ProcessUnstructuredSSResponseIndicationImpl req = new ProcessUnstructuredSSResponseIndicationImpl(ussdDataCodingScheme, ussdString);
		AsnOutputStream aos = new AsnOutputStream();
		req.encodeData(aos);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(req.getTagClass());
		p.setPrimitive(req.getIsPrimitive());
		p.setTag(req.getTag());
		p.setData(aos.toByteArray());
		returnResult.setParameter(p);

		this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
	}

	public Long addUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AlertingPattern alertingPatter, AddressString msisdn)
			throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.unstructuredSS_Request);
		invoke.setOperationCode(oc);

		UnstructuredSSRequestIndicationImpl req = new UnstructuredSSRequestIndicationImpl(ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
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

	public void addUnstructuredSSResponse(long invokeId, byte ussdDataCodingScheme, USSDString ussdString) throws MAPException {

		Return returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

		returnResult.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long) MAPOperationCode.unstructuredSS_Request);
		returnResult.setOperationCode(oc);

		UnstructuredSSResponseIndicationImpl req = new UnstructuredSSResponseIndicationImpl(ussdDataCodingScheme, ussdString);
		AsnOutputStream aos = new AsnOutputStream();
		req.encodeData(aos);

		Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
		p.setTagClass(req.getTagClass());
		p.setPrimitive(req.getIsPrimitive());
		p.setTag(req.getTag());
		p.setData(aos.toByteArray());
		returnResult.setParameter(p);

		this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
	}

}
