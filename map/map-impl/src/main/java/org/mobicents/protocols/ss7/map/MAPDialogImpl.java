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
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
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
			MAPProviderImpl mapProviderImpl, AddressString destReference,
			AddressString origReference) {
		this(appCntx, tcapDialog, mapProviderImpl);

		this.destReference = destReference;
		this.origReference = origReference;
	}

	public Long getDialogId() {
		return tcapDialog.getDialogId();
	}

	public void abort(int userReason) {
		// TODO Auto-generated method stub

	}

	public void close(boolean prearrangedEnd) {

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

		} else {

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
			p1.setTag(0x04);
			p1.setData(new byte[] { ussdDataCodingScheme });

			ussdString.encode();
			Parameter p2 = TcapFactory.createParameter();
			p2.setTagClass(Tag.CLASS_UNIVERSAL);
			p2.setTag(0x04);
			p2.setData(ussdString.getEncodedString());

			Parameter[] params = new Parameter[] { p1, p2 };
			invoke.setParameters(params);

			this.tcapDialog.sendComponent(invoke);

		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}

	}

	public void addProcessUnstructuredSSResponse(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
		// TODO Auto-generated method stub

	}

	public void addUnstructuredSSRequest(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
		// TODO Auto-generated method stub

	}

	public void addUnstructuredSSResponse(byte ussdDataCodingScheme,
			USSDString ussdString) throws MAPException {
		// TODO Auto-generated method stub

	}

}
