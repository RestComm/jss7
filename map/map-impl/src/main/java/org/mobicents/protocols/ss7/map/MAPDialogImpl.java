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

package org.mobicents.protocols.ss7.map;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogueAS;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.dialog.AddressStringImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortInfoImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;

/**
 * 
 * MAP-DialoguePDU ::= CHOICE { map-open [0] MAP-OpenInfo, map-accept [1]
 * MAP-AcceptInfo, map-close [2] MAP-CloseInfo, map-refuse [3] MAP-RefuseInfo,
 * map-userAbort [4] MAP-UserAbortInfo, map-providerAbort [5]
 * MAP-ProviderAbortInfo}
 * 
 * @author amit bhayani
 * @author baranowb
 */
public class MAPDialogImpl implements MAPDialog {
	private static final Logger logger = Logger.getLogger(MAPDialogImpl.class);
	private Dialog tcapDialog = null;
	private MAPProviderImpl mapProviderImpl = null;

	// Application Context of this Dialog
	private MAPApplicationContext appCntx;

	private AddressString destReference;
	private AddressString origReference;

	private MAPDialogState state = MAPDialogState.Idle;

	protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl) {
		this.appCntx = appCntx;
		this.tcapDialog = tcapDialog;
		this.mapProviderImpl = mapProviderImpl;
	}

	protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
			AddressString origReference, AddressString destReference) {
		this(appCntx, tcapDialog, mapProviderImpl);

		this.destReference = destReference;
		this.origReference = origReference;
	}

	public Long getDialogId() {
		return tcapDialog.getDialogId();
	}

	public void abort(MAPUserAbortChoice mapUserAbortChoice) throws MAPException {
		TCUserAbortRequest tcUserAbort = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
				.createUAbort(this.tcapDialog);

		MAPUserAbortInfoImpl mapUserAbortInfoImpl = new MAPUserAbortInfoImpl();
		mapUserAbortInfoImpl.setMAPUserAbortChoice(mapUserAbortChoice);

		AsnOutputStream localasnOs = new AsnOutputStream();
		try {
			mapUserAbortInfoImpl.encode(localasnOs);
		} catch (IOException e) {
			throw new MAPException(e.getMessage(), e);
		}

		UserInformation userInformation = TcapFactory.createUserInformation();

		userInformation.setOid(true);
		userInformation.setOidValue(MAPDialogueAS.MAP_DialogueAS.getOID());

		userInformation.setAsn(true);
		userInformation.setEncodeType(localasnOs.toByteArray());

		tcUserAbort.setUserInformation(userInformation);

		try {
			this.tcapDialog.send(tcUserAbort);
			this.setState(MAPDialogState.Expunged);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void close(boolean prearrangedEnd) throws MAPException {

		switch (this.tcapDialog.getState()) {
		case InitialReceived:
			TCEndRequest endRequest = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createEnd(this.tcapDialog);
			if (!prearrangedEnd) {
				endRequest.setTermination(TerminationType.Basic);
			} else {
				endRequest.setTermination(TerminationType.PreArranged);
			}

			ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			endRequest.setApplicationContextName(acn);

			try {
				this.tcapDialog.send(endRequest);
				this.setState(MAPDialogState.Expunged);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}
			break;

		case Active:
			TCEndRequest endRequest1 = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createEnd(this.tcapDialog);
			if (!prearrangedEnd) {
				endRequest1.setTermination(TerminationType.Basic);
			} else {
				endRequest1.setTermination(TerminationType.PreArranged);
			}

			try {
				this.tcapDialog.send(endRequest1);
				this.setState(MAPDialogState.Expunged);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}
			break;
		case Idle:
			throw new MAPException("Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!");
		case InitialSent: // we have sent TC-BEGIN already, need to wait
			throw new MAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
		case Expunged: // dialog has been terminated on TC level, cant send
			throw new MAPException("Dialog has been terminated, can not send primitives!");
		}
	}

	public void send() throws MAPException {

		switch (this.tcapDialog.getState()) {
		case Idle:
			TCBeginRequest tcBeginReq = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createBegin(this.tcapDialog);

			ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			tcBeginReq.setApplicationContextName(acn);

			MAPOpenInfoImpl mapOpn = new MAPOpenInfoImpl();
			mapOpn.setDestReference(this.destReference);
			mapOpn.setOrigReference(this.origReference);

			AsnOutputStream localasnOs = new AsnOutputStream();
			try {
				mapOpn.encode(localasnOs);
			} catch (IOException e) {
				throw new MAPException(e.getMessage(), e);
			}

			UserInformation userInformation = TcapFactory.createUserInformation();

			userInformation.setOid(true);
			userInformation.setOidValue(MAPDialogueAS.MAP_DialogueAS.getOID());

			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			tcBeginReq.setUserInformation(userInformation);

			try {
				this.tcapDialog.send(tcBeginReq);
				this.setState(MAPDialogState.InitialSent);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}
			break;

		case Active:
			// Its Active send TC-CONTINUE

			TCContinueRequest tcContinueReq = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createContinue(this.tcapDialog);

			try {
				this.tcapDialog.send(tcContinueReq);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}
			break;

		case InitialReceived:
			// Its first Reply to TC-Begin

			TCContinueRequest tcContinueReq1 = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createContinue(this.tcapDialog);

			ApplicationContextName acn1 = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			tcContinueReq1.setApplicationContextName(acn1);

			try {
				this.tcapDialog.send(tcContinueReq1);
				this.setState(MAPDialogState.Active);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}
			break;

		case InitialSent: // we have sent TC-BEGIN already, need to wait
			throw new MAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
		case Expunged: // dialog has been terminated on TC level, cant send
			throw new MAPException("Dialog has been terminated, can not send primitives!");
		}

	}

	public void addProcessUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString, AddressString msisdn)
			throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			invoke.setInvokeId(this.tcapDialog.getNewInvokeId());

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false,
					(long) MAPOperationCode.processUnstructuredSS_Request);
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

			this.tcapDialog.sendComponent(invoke);

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}

	}

	public void addProcessUnstructuredSSResponse(long invokeId, boolean lastResult, byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
		try {
			Return returnResult = null;

			if (lastResult) {
				returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
						.createTCResultLastRequest();
			} else {
				returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
						.createTCResultRequest();
			}

			returnResult.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false,
					(long) MAPOperationCode.processUnstructuredSS_Request);
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

			this.tcapDialog.sendComponent(returnResult);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}

	}

	public void addUnstructuredSSRequest(byte ussdDataCodingScheme, USSDString ussdString) throws MAPException {
		Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			invoke.setInvokeId(this.tcapDialog.getNewInvokeId());

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false, (long) MAPOperationCode.unstructuredSS_Request);
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

			this.tcapDialog.sendComponent(invoke);

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void addUnstructuredSSResponse(long invokeId, boolean lastResult, byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {

		try {
			Return returnResult = null;

			if (lastResult) {
				returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
						.createTCResultLastRequest();
			} else {
				returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
						.createTCResultRequest();
			}

			returnResult.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false, (long) MAPOperationCode.unstructuredSS_Request);
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

			this.tcapDialog.sendComponent(returnResult);

		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public MAPApplicationContext getAppCntx() {
		return appCntx;
	}

	public MAPDialogState getState() {
		return state;
	}

	protected synchronized void setState(MAPDialogState newState) {
		// add checks?
		if (this.state == MAPDialogState.Expunged) {
			return;
		}
		this.state = newState;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DialogId=").append(this.getDialogId()).append("MAPDialogState=").append(this.getState())
				.append("MAPApplicationContext=").append(this.appCntx).append("TCAPDialogState=")
				.append(this.tcapDialog.getState());
		return sb.toString();
	}

}
