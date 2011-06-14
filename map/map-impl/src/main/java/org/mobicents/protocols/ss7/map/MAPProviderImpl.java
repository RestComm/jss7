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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPDialogueAS;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.dialog.AddressStringImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPAcceptInfoImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPCloseInfoImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPProviderAbortInfoImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;
import org.mobicents.protocols.ss7.map.dialog.MAPUserAbortInfoImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSIndicationImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSIndicationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderImpl implements MAPProvider, TCListener {

	Logger loger = Logger.getLogger(MAPProviderImpl.class);

	private Set<MAPDialogListener> dialogListeners = new HashSet<MAPDialogListener>();
	private Set<MAPServiceListener> serviceListeners = new HashSet<MAPServiceListener>();

	private Map<Long, MAPDialogImpl> dialogs = new HashMap<Long, MAPDialogImpl>();

	private TCAPProvider tcapProvider = null;

	private final MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

	public MAPProviderImpl(TCAPProvider tcapProvider) {
		this.tcapProvider = tcapProvider;

	}

	protected TCAPProvider getTCAPProvider() {
		return this.tcapProvider;
	}

	public void addMAPDialogListener(MAPDialogListener mapDialogListener) {
		this.dialogListeners.add(mapDialogListener);
	}

	public void addMAPServiceListener(MAPServiceListener mapServiceListener) {
		this.serviceListeners.add(mapServiceListener);
	}

	public MAPDialog createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress,
			AddressString origReference, SccpAddress destAddress, AddressString destReference) throws MAPException {

		Dialog tcapDialog;
		try {
			tcapDialog = tcapProvider.getNewDialog(origAddress, destAddress);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
		MAPDialogImpl dialog = new MAPDialogImpl(appCntx, tcapDialog, this, origReference, destReference);

		dialogs.put(dialog.getDialogId(), dialog);

		return dialog;
	}

	public MapServiceFactory getMapServiceFactory() {
		return mapServiceFactory;
	}

	public void removeMAPDialogListener(MAPDialogListener mapDialogListener) {
		this.dialogListeners.remove(mapDialogListener);
	}

	public void removeMAPServiceListener(MAPServiceListener mapServiceListener) {
		this.serviceListeners.remove(mapServiceListener);
	}

	/**
	 * Listener methods of TCListener
	 */

	public void dialogReleased(Dialog arg0) {
		// TODO Auto-generated method stub

	}

	private void unstructuredSSRequest(Parameter parameter, MAPDialogImpl mapDialogImpl, Long invokeId)
			throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();
			USSDString ussdString = mapServiceFactory.createUSSDString(data, null);
			ussdString.decode();

			UnstructuredSSIndicationImpl unSSInd = new UnstructuredSSIndicationImpl(ussd_DataCodingScheme, ussdString);

			unSSInd.setInvokeId(invokeId);
			unSSInd.setMAPDialog(mapDialogImpl);

			for (MAPServiceListener serLis : this.serviceListeners) {
				serLis.onUnstructuredSSIndication(unSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			loger.error("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
			throw new MAPException("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
		}
	}

	private void processUnstructuredSSRequest(Parameter parameter, MAPDialogImpl mapDialogImpl, Long invokeId)
			throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();

			USSDString ussdString = mapServiceFactory.createUSSDString(data, null);
			ussdString.decode();

			ProcessUnstructuredSSIndicationImpl procUnSSInd = new ProcessUnstructuredSSIndicationImpl(
					ussd_DataCodingScheme, ussdString);

			procUnSSInd.setInvokeId(invokeId);
			procUnSSInd.setMAPDialog(mapDialogImpl);

			// MSISDN
			if (parameters.length > 2) {
				Parameter msisdnParam = parameters[2];
				if (msisdnParam.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && msisdnParam.getTag() == 0x00) {
					byte[] msisdnData = msisdnParam.getData();

					AsnInputStream ansIS = new AsnInputStream(new ByteArrayInputStream(msisdnData));

					AddressStringImpl msisdnAddStr = new AddressStringImpl();
					try {
						msisdnAddStr.decode(ansIS);
						procUnSSInd.setMSISDNAddressString(msisdnAddStr);
					} catch (IOException e) {
						loger.error("Error while decoding the MSISDN AddressString ", e);
					}
				}
			}

			for (MAPServiceListener serLis : this.serviceListeners) {
				serLis.onProcessUnstructuredSSIndication(procUnSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			loger.error("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
			throw new MAPException("Expected Parameter tag as SEQUENCE but received " + parameter.getTag());
		}
	}

	public void onTCBegin(TCBeginIndication tcBeginIndication) {
		ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
		Component[] comps = tcBeginIndication.getComponents();

		// ETS 300 974 Section 12.1.3
		// On receipt of a TC-BEGIN indication primitive, the MAP PM shall:
		//
		// - if no application-context-name is included in the primitive and if
		// the "Components present" indicator indicates "no components", issue a
		// TC-U-ABORT request primitive (note 2). The local MAP-User is not
		// informed;

		if (acn == null && comps == null) {
			loger.error(String
					.format("Received TCBeginIndication=%s, both ApplicationContextName and Component[] are null. Send TC-U-ABORT to peer and not notifying the User",
							tcBeginIndication));

			try {
				fireTCUAbort(tcBeginIndication.getDialog(), null, MAPProviderAbortReason.invalidPDU);
			} catch (MAPException e) {
				loger.error("Error while firing TC-U-ABORT. ", e);
			}
			return;
		} else if (acn == null) {
			// - if no application-context-name is included in the primitive and
			// if presence of components is indicated, wait for the first
			// TC-INVOKE primitive, and derive a version 1
			// application-context-name from the operation code according to
			// table 12.1/1 (note 1);

			// Derive Application Context name
			// for(Component c : comps){
			// if(c.getType() == ComponentType.Invoke){
			// Invoke invoke = (Invoke)c;
			// OperationCode oc = invoke.getOperationCode();
			//
			//
			// }
			// }

			// As of now we only support networkUnstructuredSsContextV2 and

			// a) if no application-context-name can be derived (i.e. the
			// operation code does not exist in MAP V1 specifications), the MAP
			// PM shall issue a TC-U-ABORT request primitive (note 2). The local
			// MAP-User is not informed.

			loger.error(String
					.format("Received TCBeginIndication=%s ApplicationContextName is null, we dont support deriving the version 1 application-context-name as we only support networkUnstructuredSsContextV2. Send TC-U-ABORT to peer and not notifying the User",
							tcBeginIndication));
			// TODO What should be the MAPProviderAbortReason for TC-U-ABORT
			// here?
			try {
				this.fireTCUAbort(tcBeginIndication.getDialog(), null, MAPProviderAbortReason.abnormalDialogue);
			} catch (MAPException e) {
				loger.error("Error while firing TC-U-ABORT. ", e);
			}

			return;

		} else {
			MAPApplicationContext mapAppCtx = MAPApplicationContext.getInstance(acn.getOid());

			// if an application-context-name different from version 1 is
			// received in a syntactically correct TC-BEGIN indication primitive
			// and if it is acceptable from a load control point of view but the
			// application-context-name is not supported, the MAP PM shall issue
			// a TC-U-ABORT request primitive with abort-reason indicating
			// "application-context-not-supported".
			if (mapAppCtx == null) {
				StringBuffer s = new StringBuffer();
				s.append("Expected networkUnstructuredSsContextV2 ")
						.append(MAPApplicationContext.getInstance(1).toString()).append(" But received");
				for (long l : acn.getOid()) {
					s.append(l).append(", ");
				}

				loger.error(String.format("Received TCBeginIndication=%s %s", tcBeginIndication, s.toString()));
				// send TC-U-ABORT without intimating MAP User
				TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory()
						.createUAbort(tcBeginIndication.getDialog());
				tcUserAbort.setApplicationContextName(acn);
				tcUserAbort.setDialogServiceUserType(DialogServiceUserType.AcnNotSupported);

				try {
					tcBeginIndication.getDialog().send(tcUserAbort);
				} catch (TCAPSendException e) {
					loger.error("Error while firing TC-U-ABORT. ", e);
				}

				return;
			}

			UserInformation userInfo = tcBeginIndication.getUserInformation();

			MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();

			// Page 146 - if no User-information is present it is checked
			// whether presence of User Information in the TC-BEGIN indication
			// primitive is required for the received application-context-name.
			// If User Information is required but not present, a TC-U-ABORT
			// request primitive with abort-reason "User-specific" and
			// user-information "MAP-ProviderAbortInfo" indicating
			// "abnormalDialogue" shall be issued. The local MAP-user shall not
			// be informed.

			// TODO : From where do we know id userInfo is required for a give
			// application-context-name?
			if (userInfo != null) {

				if (!userInfo.isOid()) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					loger.error("userInfo.isOid() check failed");
					return;
				}

				long[] oid = userInfo.getOidValue();

				MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);

				if (mapDialAs == null) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					loger.error("Expected MAPDialogueAS.MAP_DialogueAS but is null");
					return;
				}

				if (!userInfo.isAsn()) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					loger.error("userInfo.isAsn() check failed");
					return;
				}

				try {
					byte[] asnData = userInfo.getEncodeType();

					AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(asnData));

					int tag = ais.readTag();

					// It should be MAP_OPEN Tag
					if (tag != 0x00) {
						// TODO : This is Error Send back TC-U-ABORT without
						// intimating User
						return;
					}

					mapOpenInfoImpl.decode(ais);

				} catch (AsnException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (MAPException e) {
					e.printStackTrace();
				}

			}// if (userInfo != null)

			MAPDialogImpl mapDialogImpl = new MAPDialogImpl(mapAppCtx, tcBeginIndication.getDialog(), this);
			this.dialogs.put(mapDialogImpl.getDialogId(), mapDialogImpl);
			mapOpenInfoImpl.setMAPDialog(mapDialogImpl);
			mapDialogImpl.setState(MAPDialogState.InitialReceived);

			for (MAPDialogListener listener : this.dialogListeners) {
				listener.onMAPOpenInfo(mapOpenInfoImpl);
			}

			// Now let us decode the Components
			if (comps != null) {
				processComponents(mapDialogImpl, comps);
			}
			// end of for (Component c : comps)
		}

	}

	private MAPProviderAbortInfoImpl fireTCUAbort(Dialog tcapDialog, MAPDialogImpl mapDialog,
			MAPProviderAbortReason mAPProviderAbortReason) throws MAPException {

		TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

		MAPProviderAbortInfoImpl mapProviderAbortInfo = new MAPProviderAbortInfoImpl();
		mapProviderAbortInfo.setMAPDialog(mapDialog);
		mapProviderAbortInfo.setMAPProviderAbortReason(mAPProviderAbortReason);

		AsnOutputStream localasnOs = new AsnOutputStream();
		try {
			mapProviderAbortInfo.encode(localasnOs);
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
			tcapDialog.send(tcUserAbort);
		} catch (TCAPSendException e) {
			throw new MAPException(e.getMessage(), e);
		}

		return mapProviderAbortInfo;
	}

	public void onTCContinue(TCContinueIndication tcContinueIndication) {

		Dialog tcapDialog = tcContinueIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.get(tcapDialog.getDialogId());

		if (mapDialogImpl == null) {
			// TODO : ABort TCAP?
			loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}

		// On receipt of the first TC-CONTINUE indication primitive for
		// a dialogue, the MAP PM shall check the value of the
		// application-context-name parameter. If this value matches the
		// one used in the MAP-OPEN request primitive, the MAP PM shall
		// issue a MAP-OPEN confirm primitive with the result parameter
		// indicating "accepted", then process the following TC
		// component handling indication primitives as described in
		// clause 12.6, and then waits for a request primitive from its
		// user or an indication primitive from TC, otherwise it shall
		// issue a TC-U-ABORT request primitive with a MAP-providerAbort
		// PDU indicating "abnormal dialogue" and a MAP-P-ABORT
		// indication primitive with the "provider-reason" parameter
		// indicating "abnormal dialogue".
		if (mapDialogImpl.getState() == MAPDialogState.InitialSent) {
			ApplicationContextName acn = tcContinueIndication.getApplicationContextName();

			if (acn == null) {
				loger.error(String.format(
						"Received first TC-CONTINUE for MAPDialog=%s. But no application-context-name included",
						mapDialogImpl));
				try {
					this.dialogs.remove(mapDialogImpl.getDialogId());
					MAPProviderAbortInfoImpl mapProviderAbortInfo = fireTCUAbort(tcapDialog, mapDialogImpl,
							MAPProviderAbortReason.abnormalDialogue);
					mapDialogImpl.setState(MAPDialogState.Expunged);
					for (MAPDialogListener listener : this.dialogListeners) {
						listener.onMAPProviderAbortInfo(mapProviderAbortInfo);
					}
				} catch (MAPException e) {
					this.loger.error(e);
				}

				return;
			} else {
				MAPApplicationContext mapAcn = MAPApplicationContext.getInstance(acn.getOid());
				if (mapAcn == null | mapAcn != mapDialogImpl.getAppCntx()) {
					loger.error(String.format("Received first TC-CONTINUE. MAPDialog=%s. But MAPApplicationContext=%s",
							mapDialogImpl, mapAcn));
					try {
						this.dialogs.remove(mapDialogImpl.getDialogId());
						MAPProviderAbortInfoImpl mapProviderAbortInfo = fireTCUAbort(tcapDialog, mapDialogImpl,
								MAPProviderAbortReason.abnormalDialogue);
						mapDialogImpl.setState(MAPDialogState.Expunged);
						for (MAPDialogListener listener : this.dialogListeners) {
							listener.onMAPProviderAbortInfo(mapProviderAbortInfo);
						}
					} catch (MAPException e) {
						this.loger.error(e);
					}

					return;
				} else {

					// Fire MAPAcceptInfo
					MAPAcceptInfoImpl mapAcceptInfo = new MAPAcceptInfoImpl();
					mapAcceptInfo.setMAPDialog(mapDialogImpl);
					mapDialogImpl.setState(MAPDialogState.Active);

					for (MAPDialogListener listener : this.dialogListeners) {
						listener.onMAPAcceptInfo(mapAcceptInfo);
					}

				}
			}// end of if (acn == null)

		}

		if (mapDialogImpl.getState() == MAPDialogState.InitialSent || mapDialogImpl.getState() == MAPDialogState.Active) {
			Component[] components = tcContinueIndication.getComponents();
			if (components != null) {
				processComponents(mapDialogImpl, components);
			}
		} else {
			// This should never happen
			loger.error(String.format("Received TC-CONTINUE. MAPDialog=%s. But state is neither InitialSent or Active",
					mapDialogImpl));
		}

	}

	public void onTCEnd(TCEndIndication tcEndIndication) {

		Dialog tcapDialog = tcEndIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog.getDialogId());

		if (mapDialogImpl == null) {			
			loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}

		if (mapDialogImpl.getState() == MAPDialogState.InitialSent) {
			// On receipt of a TC-END indication primitive in the dialogue
			// initiated state, the MAP PM shall check the value of the
			// application-context-name parameter. If this value does not match
			// the one used in the MAPOPEN request primitive, the MAP PM shall
			// discard any following component handling primitive and shall
			// issue a MAP-P-ABORT indication primitive with the
			// "provider-reason" parameter indicating "abnormal dialogue".
			ApplicationContextName acn = tcEndIndication.getApplicationContextName();

			if (acn == null) {				
				MAPProviderAbortInfoImpl abortInfo = new MAPProviderAbortInfoImpl();
				abortInfo.setMAPDialog(mapDialogImpl);
				abortInfo.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
				mapDialogImpl.setState(MAPDialogState.Expunged);
				for (MAPDialogListener listener : this.dialogListeners) {
					listener.onMAPProviderAbortInfo(abortInfo);
				}

				return;
			}

			MAPApplicationContext mapAcn = MAPApplicationContext.getInstance(acn.getOid());

			if (mapAcn == null | mapAcn != mapDialogImpl.getAppCntx()) {

				MAPProviderAbortInfoImpl abortInfo = new MAPProviderAbortInfoImpl();
				abortInfo.setMAPDialog(mapDialogImpl);
				abortInfo.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
				mapDialogImpl.setState(MAPDialogState.Expunged);
				for (MAPDialogListener listener : this.dialogListeners) {
					listener.onMAPProviderAbortInfo(abortInfo);
				}

				return;
			}
		}

		// Otherwise it shall issue a MAP-OPEN confirm primitive with the result
		// parameter set to "accepted" and process the following TC component
		// handling indication primitives as described in clause 12.6;

		// Fire MAPAcceptInfo
		MAPAcceptInfoImpl mapAcceptInfo = new MAPAcceptInfoImpl();
		mapAcceptInfo.setMAPDialog(mapDialogImpl);
		
		//TODO set expunged here itself?
		mapDialogImpl.setState(MAPDialogState.Active);

		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPAcceptInfo(mapAcceptInfo);
		}		

		Component[] components = tcEndIndication.getComponents();

		if (components != null) {
			// Now let us decode the Components
			processComponents(mapDialogImpl, components);
		}// end of if (components != null)

		// then it shall issue a MAP-CLOSE indication primitive and return to
		// idle all state machines associated with the dialogue.
		MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
		closeInfo.setMAPDialog(mapDialogImpl);
		mapDialogImpl.setState(MAPDialogState.Expunged);
		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPCloseInfo(closeInfo);
		}

	}

	public void onTCUni(TCUniIndication arg0) {
		// TODO Throw Exception or Ignore? This should never happen for MAP

	}

	public void onInvokeTimeout(Invoke arg0) {
		// TODO Auto-generated method stub

	}

	public void onTCPAbort(TCPAbortIndication tcPAbortIndication) {
		Dialog tcapDialog = tcPAbortIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}

		mapDialogImpl.setState(MAPDialogState.Expunged);
		
		MAPProviderAbortInfoImpl prAbortInfoImpl = new MAPProviderAbortInfoImpl();
		prAbortInfoImpl.setMAPDialog(mapDialogImpl);

		PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();

		// TODO Mapping of MAPProviderAbortReason with PAbortCauseType ???
		switch (pAbortCause) {
		case UnrecogniedMessageType:
			prAbortInfoImpl.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
			break;
		case UnrecognizedTxID:
			prAbortInfoImpl.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
			break;
		case BadlyFormattedTxPortion:
			prAbortInfoImpl.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
			break;
		case IncorrectTxPortion:
			prAbortInfoImpl.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
			break;
		case ResourceLimitation:
			prAbortInfoImpl.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);
			break;
		}

		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPProviderAbortInfo(prAbortInfoImpl);
		}

	}

	public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
		Dialog tcapDialog = tcUserAbortIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getDialogId());
			return;
		}
		
		mapDialogImpl.setState(MAPDialogState.Expunged);
		
		MAPUserAbortInfo mapUserAbortInfoImpl = new MAPUserAbortInfoImpl();
		mapUserAbortInfoImpl.setMAPDialog(mapDialogImpl);

		// TODO mapping between the MAPUserAbortChoice and getAbortReason
		MAPUserAbortChoiceImpl userAbortChoice = new MAPUserAbortChoiceImpl();
		userAbortChoice.setUserSpecificReason();

		mapUserAbortInfoImpl.setMAPUserAbortChoice(userAbortChoice);

		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPUserAbortInfo(mapUserAbortInfoImpl);
		}

	}

	private void processComponents(MAPDialogImpl mapDialogImpl, Component[] components) {
		// FIXME: Amit DOUBLE CHECK!
		// Now let us decode the Components
		for (Component c : components) {

			try {

				ComponentType compType = c.getType();

				long invokeId = c.getInvokeId();

				// TODO Does it make any difference if its Invoke, ReturnResult
				// or ReturnResultLast?
				Parameter parameter;
				OperationCode oc;
				if (compType == ComponentType.Invoke) {
					Invoke comp = (Invoke) c;
					oc = comp.getOperationCode();
					parameter = comp.getParameter();
				} else if (compType == ComponentType.ReturnResult) {
					ReturnResult comp = (ReturnResult) c;
					oc = comp.getOperationCode();
					parameter = comp.getParameter();
				} else if (compType == ComponentType.ReturnResultLast) {
					ReturnResultLast comp = (ReturnResultLast) c;
					oc = comp.getOperationCode();
					parameter = comp.getParameter();
				} else {
					// FIXME: add rest
					continue;
				}

				if (oc.getCode() == MAPOperationCode.processUnstructuredSS_Request) {

					this.processUnstructuredSSRequest(parameter, mapDialogImpl, invokeId);

				} else if (oc.getCode() == MAPOperationCode.unstructuredSS_Request) {

					this.unstructuredSSRequest(parameter, mapDialogImpl, invokeId);

				} else {
					loger.error("Expected OC is MAPOperationCode.processUnstructuredSS_Request or MAPOperationCode.unstructuredSS_Request but received "
							+ oc.getCode());
					// TODO : Probably add reject?
					return;
				}// end of if

			} catch (MAPException e) {
				e.printStackTrace();
			}
		} // end of for (Component c : comps)

	}

	public MAPDialog getMAPDialog(Long dialogId) {
		return this.dialogs.get(dialogId);
	}

	public void start() {
		this.tcapProvider.addTCListener(this);
	}

	public void stop() {
		this.tcapProvider.removeTCListener(this);

	}

}
