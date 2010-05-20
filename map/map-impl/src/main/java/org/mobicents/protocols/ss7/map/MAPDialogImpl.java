package org.mobicents.protocols.ss7.map;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogueAS;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
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
 * MAP-DialoguePDU ::= CHOICE {
 *    map-open                 [0] MAP-OpenInfo,
 *   map-accept               [1] MAP-AcceptInfo,
 *   map-close                [2] MAP-CloseInfo,
 *   map-refuse               [3] MAP-RefuseInfo,
 *   map-userAbort            [4] MAP-UserAbortInfo,
 *   map-providerAbort        [5] MAP-ProviderAbortInfo}
 * 
 * @author amit bhayani
 * 
 */
public class MAPDialogImpl implements MAPDialog {

	private Dialog tcapDialog = null;
	private MAPProviderImpl mapProviderImpl = null;

	// Application Context of this Dialog
	private MAPApplicationContext appCntx;

	private AddressString destReference;
	private AddressString origReference;

	protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog,
			MAPProviderImpl mapProviderImpl) {
		this.appCntx = appCntx;
		this.tcapDialog = tcapDialog;
		this.mapProviderImpl = mapProviderImpl;
	}

	protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog,
			MAPProviderImpl mapProviderImpl, AddressString origReference,
			AddressString destReference) {
		this(appCntx, tcapDialog, mapProviderImpl);

		this.destReference = destReference;
		this.origReference = origReference;
	}

	public Long getDialogId() {
		return tcapDialog.getDialogId();
	}

	public void abort(int userReason) {
		TCUserAbortRequest userAbort = this.mapProviderImpl.getTCAPProvider()
				.getDialogPrimitiveFactory().createUAbort(this.tcapDialog);

		// TODO : Take care of userReason

		// try {
		// this.tcapDialog.send(userAbort);
		// } catch (TCAPSendException e) {
		// throw new MAPException(e.getMessage(), e);
		// }
	}

	public void close(boolean prearrangedEnd) throws MAPException {
		TCEndRequest endRequest = this.mapProviderImpl.getTCAPProvider()
				.getDialogPrimitiveFactory().createEnd(this.tcapDialog);
		if (!prearrangedEnd) {
			endRequest.setTermination(TerminationType.Basic);
		} else {
			endRequest.setTermination(TerminationType.PreArranged);
		}

		ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider()
				.getDialogPrimitiveFactory().createApplicationContextName(
						this.appCntx.getOID());

		endRequest.setApplicationContextName(acn);

		try {
			this.tcapDialog.send(endRequest);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}

	public void send() throws MAPException {
		// Its Idle, send TC-BEGIN
		if (this.tcapDialog.getState() == TRPseudoState.Idle) {
			TCBeginRequest tcBeginReq = this.mapProviderImpl.getTCAPProvider()
					.getDialogPrimitiveFactory().createBegin(this.tcapDialog);

			ApplicationContextName acn = this.mapProviderImpl
					.getTCAPProvider()
					.getDialogPrimitiveFactory()
					.createApplicationContextName(
							MAPApplicationContext.networkUnstructuredSsContextV2
									.getOID());

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

			UserInformation userInformation = TcapFactory
					.createUserInformation();

			userInformation.setOid(true);
			userInformation.setOidValue(MAPDialogueAS.MAP_DialogueAS.getOID());

			userInformation.setAsn(true);
			userInformation.setEncodeType(localasnOs.toByteArray());

			tcBeginReq.setUserInformation(userInformation);

			try {
				this.tcapDialog.send(tcBeginReq);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}

		} else if (this.tcapDialog.getState() == TRPseudoState.Active) {
			// Its Active send TC-CONTINUE

			TCContinueRequest tcContinueReq = this.mapProviderImpl
					.getTCAPProvider().getDialogPrimitiveFactory()
					.createContinue(this.tcapDialog);

			try {
				this.tcapDialog.send(tcContinueReq);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}

		} else if (this.tcapDialog.getState() == TRPseudoState.InitialReceived) {
			// Its first Reply to TC-Begin

			TCContinueRequest tcContinueReq = this.mapProviderImpl
					.getTCAPProvider().getDialogPrimitiveFactory()
					.createContinue(this.tcapDialog);

			ApplicationContextName acn = this.mapProviderImpl
					.getTCAPProvider()
					.getDialogPrimitiveFactory()
					.createApplicationContextName(
							MAPApplicationContext.networkUnstructuredSsContextV2
									.getOID());

			tcContinueReq.setApplicationContextName(acn);

			try {
				this.tcapDialog.send(tcContinueReq);
			} catch (TCAPSendException e) {
				throw new MAPException(e.getMessage(), e);
			}

		}

		// TODO state where it should raise exception?
	}

	public void addProcessUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {

		Invoke invoke = this.mapProviderImpl.getTCAPProvider()
				.getComponentPrimitiveFactory().createTCInvokeRequest();

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

	public void addProcessUnstructuredSSResponse(long invokeId,
			boolean lastResult, byte ussdDataCodingScheme, USSDString ussdString)
			throws MAPException {
		try {
			Return returnResult = null;

			if (lastResult) {
				returnResult = this.mapProviderImpl.getTCAPProvider()
						.getComponentPrimitiveFactory()
						.createTCResultLastRequest();
			} else {
				returnResult = this.mapProviderImpl.getTCAPProvider()
						.getComponentPrimitiveFactory().createTCResultRequest();
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

	public void addUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
		Invoke invoke = this.mapProviderImpl.getTCAPProvider()
				.getComponentPrimitiveFactory().createTCInvokeRequest();

		try {
			invoke.setInvokeId(this.tcapDialog.getNewInvokeId());

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false,
					(long) MAPOperationCode.unstructuredSS_Request);
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

	public void addUnstructuredSSResponse(long invokeId, boolean lastResult,
			byte ussdDataCodingScheme, USSDString ussdString)
			throws MAPException {

		try {
			Return returnResult = null;

			if (lastResult) {
				returnResult = this.mapProviderImpl.getTCAPProvider()
						.getComponentPrimitiveFactory()
						.createTCResultLastRequest();
			} else {
				returnResult = this.mapProviderImpl.getTCAPProvider()
						.getComponentPrimitiveFactory().createTCResultRequest();
			}

			returnResult.setInvokeId(invokeId);

			// Operation Code
			OperationCode oc = TcapFactory.createOperationCode(false,
					(long) MAPOperationCode.unstructuredSS_Request);
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

}
