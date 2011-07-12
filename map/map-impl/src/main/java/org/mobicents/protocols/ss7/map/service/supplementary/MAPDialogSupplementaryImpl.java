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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class MAPDialogSupplementaryImpl extends MAPDialogImpl implements MAPDialogSupplementary {

	protected MAPDialogSupplementaryImpl(MAPApplicationContext appCntx, Dialog tcapDialog,
			MAPProviderImpl mapProviderImpl, MAPServiceSupplementary mapService, AddressString origReference,
			AddressString destReference) {
		super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
	}

	public Long addProcessUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AddressString msisdn)
			throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.processUnstructuredSS_Request);
			invoke.setOperationCode(oc);

			// Sequence of Parameter
			Parameter p1 = TcapFactory.createParameter();
			p1.setTagClass(Tag.CLASS_UNIVERSAL);
			p1.setTag(Tag.STRING_OCTET);
			p1.setData(new byte[] { ussdDataCodingScheme });

			ussdString.encode();
			Parameter p2 = TcapFactory.createParameter();
			p2.setTagClass(Tag.CLASS_UNIVERSAL);
			p2.setTag(Tag.STRING_OCTET);
			p2.setData(ussdString.getEncodedString());

			Parameter p3 = null;
			if (msisdn != null) {
				AsnOutputStream asnOs = new AsnOutputStream();
				((AddressStringImpl) msisdn).encode(asnOs);
				byte[] msisdndata = asnOs.toByteArray();

				p3 = TcapFactory.createParameter();
				p3.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p3.setTag(0x00);
				p3.setData(msisdndata);
			}

			Parameter p = TcapFactory.createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);

			if (p3 != null) {
				p.setParameters(new Parameter[] { p1, p2, p3 });
			} else {
				p.setParameters(new Parameter[] { p1, p2 });
			}

			invoke.setParameter(p);

			//this.tcapDialog.sendComponent(invoke);
			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}

	}

	public void addProcessUnstructuredSSResponse(long invokeId, boolean lastResult, byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
			Return returnResult = null;

		if (lastResult) {
			returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		} else {
			returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultRequest();
		}

		returnResult.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long)MAPOperationCode.processUnstructuredSS_Request);
		returnResult.setOperationCode(oc);

		// Sequence of Parameter
		Parameter p1 = TcapFactory.createParameter();
		p1.setTagClass(Tag.CLASS_UNIVERSAL);
		p1.setTag(Tag.STRING_OCTET);
		p1.setData(new byte[] { ussdDataCodingScheme });

		ussdString.encode();
		Parameter p2 = TcapFactory.createParameter();
		p2.setTagClass(Tag.CLASS_UNIVERSAL);
		p2.setTag(Tag.STRING_OCTET);
		p2.setData(ussdString.getEncodedString());

		Parameter p = TcapFactory.createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		p.setParameters(new Parameter[] { p1, p2 });

		returnResult.setParameter(p);

		// this.tcapDialog.sendComponent(returnResult);
		if (lastResult)
			this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
		else
			this.sendReturnResultComponent((ReturnResult) returnResult);
	}

	public Long addUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString) throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			Long invokeId = this.tcapDialog.getNewInvokeId();
			invoke.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode();
			oc.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Request);
			invoke.setOperationCode(oc);

			// Sequence of Parameter
			Parameter p1 = TcapFactory.createParameter();
			p1.setTagClass(Tag.CLASS_UNIVERSAL);
			p1.setTag(Tag.STRING_OCTET);
			p1.setData(new byte[] { ussdDataCodingScheme });

			ussdString.encode();
			Parameter p2 = TcapFactory.createParameter();
			p2.setTagClass(Tag.CLASS_UNIVERSAL);
			p2.setTag(Tag.STRING_OCTET);
			p2.setData(ussdString.getEncodedString());

			Parameter p = TcapFactory.createParameter();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);
			p.setParameters(new Parameter[] { p1, p2 });

			invoke.setParameter(p);

//			this.tcapDialog.sendComponent(invoke);
			this.sendInvokeComponent(invoke);
			
			return invokeId;

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void addUnstructuredSSResponse(long invokeId, boolean lastResult, byte ussdDataCodingScheme, USSDString ussdString) throws MAPException {

		Return returnResult = null;

		if (lastResult) {
			returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();
		} else {
			returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultRequest();
		}

		returnResult.setInvokeId(invokeId);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode();
		oc.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Request);
		returnResult.setOperationCode(oc);

		// Sequence of Parameter
		Parameter p1 = TcapFactory.createParameter();
		p1.setTagClass(Tag.CLASS_UNIVERSAL);
		p1.setTag(Tag.STRING_OCTET);
		p1.setData(new byte[] { ussdDataCodingScheme });

		ussdString.encode();
		Parameter p2 = TcapFactory.createParameter();
		p2.setTagClass(Tag.CLASS_UNIVERSAL);
		p2.setTag(Tag.STRING_OCTET);
		p2.setData(ussdString.getEncodedString());

		Parameter p = TcapFactory.createParameter();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		p.setParameters(new Parameter[] { p1, p2 });

		returnResult.setParameter(p);

		// this.tcapDialog.sendComponent(returnResult);
		if (lastResult)
			this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
		else
			this.sendReturnResultComponent((ReturnResult) returnResult);
	}
}
