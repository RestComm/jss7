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
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

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
		this.tcapProvider.addTCListener(this);

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

	public MAPDialog createNewDialog(MAPApplicationContext appCntx,
			SccpAddress origAddress, AddressString origReference,
			SccpAddress destAddress, AddressString destReference)
			throws MAPException {

		Dialog tcapDialog;
		try {
			tcapDialog = tcapProvider.getNewDialog(origAddress, destAddress);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
		MAPDialogImpl dialog = new MAPDialogImpl(appCntx, tcapDialog, this,
				origReference, destReference);

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

	private void unstructuredSSRequest(Parameter parameter,
			MAPDialogImpl mapDialogImpl, Long invokeId) throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();
			USSDString ussdString = mapServiceFactory.createUSSDString(data,
					null);
			ussdString.decode();

			UnstructuredSSIndicationImpl unSSInd = new UnstructuredSSIndicationImpl(
					ussd_DataCodingScheme, ussdString);

			unSSInd.setInvokeId(invokeId);
			unSSInd.setMAPDialog(mapDialogImpl);

			for (MAPServiceListener serLis : this.serviceListeners) {
				serLis.onUnstructuredSSIndication(unSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			loger.error("Expected Parameter tag as SEQUENCE but received "
					+ parameter.getTag());
			throw new MAPException(
					"Expected Parameter tag as SEQUENCE but received "
							+ parameter.getTag());
		}
	}

	private void processUnstructuredSSRequest(Parameter parameter,
			MAPDialogImpl mapDialogImpl, Long invokeId) throws MAPException {
		if (parameter.getTag() == Tag.SEQUENCE) {
			Parameter[] parameters = parameter.getParameters();

			byte[] data = parameters[0].getData();

			// First Parameter is ussd-DataCodingScheme
			byte ussd_DataCodingScheme = data[0];

			// Second Parameter is ussd-String
			data = parameters[1].getData();

			USSDString ussdString = mapServiceFactory.createUSSDString(data,
					null);
			ussdString.decode();

			ProcessUnstructuredSSIndicationImpl procUnSSInd = new ProcessUnstructuredSSIndicationImpl(
					ussd_DataCodingScheme, ussdString);

			procUnSSInd.setInvokeId(invokeId);
			procUnSSInd.setMAPDialog(mapDialogImpl);

			for (MAPServiceListener serLis : this.serviceListeners) {
				serLis.onProcessUnstructuredSSIndication(procUnSSInd);
			}
		} else {
			// TODO This is Error, what do we do next? Or should it even happen?
			loger.error("Expected Parameter tag as SEQUENCE but received "
					+ parameter.getTag());
			throw new MAPException(
					"Expected Parameter tag as SEQUENCE but received "
							+ parameter.getTag());
		}
	}

	public void onTCBegin(TCBeginIndication tcBeginIndication) {
		ApplicationContextName acn = tcBeginIndication
				.getApplicationContextName();
		Component[] comps = tcBeginIndication.getComponents();

		// ETS 300 974 Section 12.1.3
		// On receipt of a TC-BEGIN indication primitive, the MAP PM shall:
		//			
		// - if no application-context-name is included in the primitive and if
		// the "Components present" indicator indicates "no components", issue a
		// TC-U-ABORT request primitive (note 2). The local MAP-User is not
		// informed;

		if (acn == null && comps == null) {
			loger
					.error("Both ApplicationContextName and Component[] are null. Send TC-U-ABORT to peer and not notifying the User");
			// TODO send back TC-U-ABORT
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

			// TODO send TC-U-ABORT without intimating the MAP-User
			loger
					.error("ApplicationContextName is null, we dont support deriving the version 1 application-context-name as we only support networkUnstructuredSsContextV2. Send TC-U-ABORT to peer and not notifying the User");

			return;

		} else {
			MAPApplicationContext mapAppCtx = MAPApplicationContext
					.getInstance(acn.getOid());

			// if an application-context-name different from version 1 is
			// received in a syntactically correct TC-BEGIN indication primitive
			// and if it is acceptable from a load control point of view but the
			// application-context-name is not supported, the MAP PM shall issue
			// a TC-U-ABORT request primitive with abort-reason indicating
			// "application-context-not-supported".
			if (mapAppCtx == null) {
				StringBuffer s = new StringBuffer();
				s.append("Expected networkUnstructuredSsContextV2 ").append(
						MAPApplicationContext.getInstance(1).toString())
						.append(" But received");
				for (long l : acn.getOid()) {
					s.append(l).append(", ");
				}
				// TODO send TC-U-ABORT without intimating MAP User
				return;
			}

			UserInformation userInfo = tcBeginIndication.getUserInformation();

			if (userInfo != null) {

				if (!userInfo.isOid()) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					return;
				}

				long[] oid = userInfo.getOidValue();

				MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);

				if (mapDialAs == null) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					return;
				}

				if (!userInfo.isAsn()) {
					// TODO : This is Error Send back TC-U-ABORT without
					// intimating User
					return;
				}

				try {
					byte[] asnData = userInfo.getEncodeType();

					AsnInputStream ais = new AsnInputStream(
							new ByteArrayInputStream(asnData));

					int tag = ais.readTag();

					// It should be MAP_OPEN Tag
					if (tag != 0x00) {
						// TODO : This is Error Send back TC-U-ABORT without
						// intimating User
						return;
					}

					MAPDialogImpl mapDialogImpl = new MAPDialogImpl(mapAppCtx,
							tcBeginIndication.getDialog(), this);

					dialogs.put(mapDialogImpl.getDialogId(), mapDialogImpl);

					MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
					mapOpenInfoImpl.decode(ais);

					for (MAPDialogListener listener : this.dialogListeners) {
						listener.onMAPOpenInfo(mapOpenInfoImpl);
					}

					// Now let us decode the Components
					for (Component c : comps) {
						ComponentType compType = c.getType();
						if (compType != ComponentType.Invoke) {
							// TODO This is Error, what do we do next? Or should
							// it even happen?
							loger
									.error("Expected ComponentType.Invoke but received "
											+ compType);
							return;
						}
						Invoke invoke = (Invoke) c;

						long invokeId = invoke.getInvokeId();
						OperationCode oc = invoke.getOperationCode();

						if (oc.getCode() == MAPOperationCode.processUnstructuredSS_Request) {

							Parameter parameter = invoke.getParameter();
							this.processUnstructuredSSRequest(parameter,
									mapDialogImpl, invokeId);

						} else if (oc.getCode() == MAPOperationCode.unstructuredSS_Request) {

							Parameter parameter = invoke.getParameter();
							this.unstructuredSSRequest(parameter,
									mapDialogImpl, invokeId);

						} else {
							loger
									.error("Expected OC is MAPOperationCode.processUnstructuredSS_Request or MAPOperationCode.unstructuredSS_Request but received "
											+ oc.getCode());
							return;
						}
					} // end of for (Component c : comps)

				} catch (AsnException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (MAPException e) {
					e.printStackTrace();
				}

			} else {
				// TODO : This is Error Send back TC-U-ABORT without
				// intimating User
				return;
			}

		}

	}

	private void fireTCUAbort(Dialog tcapDialog, MAPDialogImpl mapDialog)
			throws MAPException {

		this.dialogs.remove(mapDialog.getDialogId());

		TCUserAbortRequest tcUserAbort = this.getTCAPProvider()
				.getDialogPrimitiveFactory().createUAbort(tcapDialog);

		MAPProviderAbortInfoImpl mapProviderAbortInfo = new MAPProviderAbortInfoImpl();
		mapProviderAbortInfo.setMAPDialog(mapDialog);
		mapProviderAbortInfo
				.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);

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

		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPProviderAbortInfo(mapProviderAbortInfo);
		}

	}

	public void onTCContinue(TCContinueIndication tcContinueIndication) {

		ApplicationContextName acn = tcContinueIndication
				.getApplicationContextName();

		Dialog tcapDialog = tcContinueIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs
				.get(tcapDialog.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id "
					+ tcapDialog.getDialogId());
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
		if (!mapDialogImpl.isMapAcceptInfoFired()) {
			if (acn == null) {
				try {
					fireTCUAbort(tcapDialog, mapDialogImpl);
				} catch (MAPException e) {
					this.loger.error(e);
				}
			} else {
				MAPApplicationContext mapAcn = MAPApplicationContext
						.getInstance(acn.getOid());
				if (mapAcn == null | mapAcn != mapDialogImpl.getAppCntx()) {
					try {
						fireTCUAbort(tcapDialog, mapDialogImpl);
					} catch (MAPException e) {
						this.loger.error(e);
					}
				} else {
					
					// Fire MAPAcceptInfo
					MAPAcceptInfoImpl mapAcceptInfo = new MAPAcceptInfoImpl();
					mapAcceptInfo.setMAPDialog(mapDialogImpl);

					for (MAPDialogListener listener : this.dialogListeners) {
						listener.onMAPAcceptInfo(mapAcceptInfo);
					}
					
					mapDialogImpl.setMapAcceptInfoFired(true);
				}
			}// end of if (acn == null)

		}// end of if(!mapDialogImpl.isMapAcceptInfoFired())

		Component[] components = tcContinueIndication.getComponents();

		// Now let us decode the Components
		for (Component c : components) {

			try {

				ComponentType compType = c.getType();

				long invokeId = c.getInvokeId();

				// TODO Does it make any difference if its Invoke, ReturnResult
				// or ReturnResultLast?
				if (compType == ComponentType.Invoke
						|| compType == ComponentType.ReturnResult
						|| compType == ComponentType.ReturnResultLast) {
					Invoke invoke = (Invoke) c;

					OperationCode oc = invoke.getOperationCode();

					if (oc.getCode() == MAPOperationCode.processUnstructuredSS_Request) {

						Parameter parameter = invoke.getParameter();
						this.processUnstructuredSSRequest(parameter,
								mapDialogImpl, invokeId);

					} else if (oc.getCode() == MAPOperationCode.unstructuredSS_Request) {

						Parameter parameter = invoke.getParameter();
						this.unstructuredSSRequest(parameter, mapDialogImpl,
								invokeId);

					} else {
						loger
								.error("Expected OC is MAPOperationCode.processUnstructuredSS_Request or MAPOperationCode.unstructuredSS_Request but received "
										+ oc.getCode());
						return;
					}
				}// end of if
			} catch (MAPException e) {
				e.printStackTrace();
			}

		} // end of for (Component c : comps)

	}

	public void onTCEnd(TCEndIndication tcEndIndication) {

		ApplicationContextName acn = tcEndIndication
				.getApplicationContextName();

		Dialog tcapDialog = tcEndIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog
				.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id "
					+ tcapDialog.getDialogId());
			return;
		}

		MAPApplicationContext mapAcn = MAPApplicationContext.getInstance(acn
				.getOid());

		if (mapAcn == null | mapAcn != mapDialogImpl.getAppCntx()) {
			// 12.1.6 Receipt of a TC-END ind
			// On receipt of a TC-END indication primitive in the dialogue
			// initiated state, the MAP PM shall check the value of the
			// application-context-name parameter. If this value does not match
			// the one used in the MAP-OPEN request primitive, the MAP PM shall
			// discard any following component handling primitive and shall
			// issue a MAP-P-ABORT indication primitive with the
			// "provider-reason" parameter indicating "abnormal dialogue".

			MAPProviderAbortInfoImpl abortInfo = new MAPProviderAbortInfoImpl();
			abortInfo.setMAPDialog(mapDialogImpl);
			abortInfo
					.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);

			for (MAPDialogListener listener : this.dialogListeners) {
				listener.onMAPProviderAbortInfo(abortInfo);
			}

			return;
		}

		// Otherwise it shall issue a MAP-OPEN confirm primitive with the result
		// parameter set to "accepted" and process the following TC component
		// handling indication primitives as described in clause 12.6;

		// TODO : Shoudl we issue MAP-OPEN here? Sounds illogical

		Component[] components = tcEndIndication.getComponents();

		if (components != null) {
			// Now let us decode the Components
			for (Component c : components) {

				try {

					ComponentType compType = c.getType();

					long invokeId = c.getInvokeId();

					// TODO Does it make any difference if its Invoke,
					// ReturnResult
					// or ReturnResultLast?
					if (compType == ComponentType.Invoke
							|| compType == ComponentType.ReturnResult
							|| compType == ComponentType.ReturnResultLast) {
						Invoke invoke = (Invoke) c;

						OperationCode oc = invoke.getOperationCode();

						if (oc.getCode() == MAPOperationCode.processUnstructuredSS_Request) {

							Parameter parameter = invoke.getParameter();
							this.processUnstructuredSSRequest(parameter,
									mapDialogImpl, invokeId);

						} else if (oc.getCode() == MAPOperationCode.unstructuredSS_Request) {

							Parameter parameter = invoke.getParameter();
							this.unstructuredSSRequest(parameter,
									mapDialogImpl, invokeId);

						} else {
							loger
									.error("Expected OC is MAPOperationCode.processUnstructuredSS_Request or MAPOperationCode.unstructuredSS_Request but received "
											+ oc.getCode());
							return;
						}
					}// end of if
				} catch (MAPException e) {
					e.printStackTrace();
				}

			} // end of for (Component c : comps)
		}// end of if (components != null)

		// then it shall issue a MAP-CLOSE indication primitive and return to
		// idle all state machines associated with the dialogue.
		MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
		closeInfo.setMAPDialog(mapDialogImpl);

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

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog
				.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id "
					+ tcapDialog.getDialogId());
			return;
		}

		PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();

		MAPProviderAbortInfoImpl prAbortInfoImpl = new MAPProviderAbortInfoImpl();
		prAbortInfoImpl.setMAPDialog(mapDialogImpl);
		// TODO Mapping of MAPProviderAbortReason with PAbortCauseType ???
		prAbortInfoImpl
				.setMAPProviderAbortReason(MAPProviderAbortReason.abnormalDialogue);

		for (MAPDialogListener listener : this.dialogListeners) {
			listener.onMAPProviderAbortInfo(prAbortInfoImpl);
		}

	}

	public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
		Dialog tcapDialog = tcUserAbortIndication.getDialog();

		MAPDialogImpl mapDialogImpl = this.dialogs.remove(tcapDialog
				.getDialogId());

		if (mapDialogImpl == null) {
			loger.error("MAP Dialog not found for Dialog Id "
					+ tcapDialog.getDialogId());
			return;
		}

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

	public MAPDialog getMAPDialog(Long dialogId) {
		return this.dialogs.get(dialogId);
	}

}
