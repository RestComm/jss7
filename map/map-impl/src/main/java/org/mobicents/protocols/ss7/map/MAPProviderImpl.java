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
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPDialogueAS;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSIndicationImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSIndicationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
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


	public MAPProviderImpl(TCAPProvider tcapProvider) {
		this.tcapProvider = tcapProvider;
		this.tcapProvider.addTCListener(this);

	}
	
	protected TCAPProvider getTCAPProvider(){
		return this.tcapProvider;
	}

	public void addMAPDialogListener(MAPDialogListener mapDialogListener) {
		this.dialogListeners.add(mapDialogListener);
	}

	public void addMAPServiceListener(MAPServiceListener mapServiceListener) {
		this.serviceListeners.add(mapServiceListener);
	}

	public MAPDialog createNewDialog(int applicationCntx,
			SccpAddress destAddress, byte[] destReference,
			SccpAddress origAddress, byte[] origReference) throws MAPException {
		
		
		MAPApplicationContext appCntx = MAPApplicationContext
				.getInstance(applicationCntx);

		if (appCntx == null) {
			throw new MAPException(
					"No Application Context matching for passed applicationCntx ");
		}

		Dialog tcapDialog;
		try {
			tcapDialog = tcapProvider.getNewDialog(origAddress, destAddress);
		} catch (TCAPException e) {
			throw new MAPException(e.getMessage(), e);
		}
		MAPDialog dialog = new MAPDialogImpl(appCntx, tcapDialog, this);
		return dialog;
	}

	public MapServiceFactory getMapServiceFactory() {
		// TODO Auto-generated method stub
		return null;
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

							Parameter[] parameters = invoke.getParameters();

							byte[] data = parameters[0].getData();

							// First Parameter is ussd-DataCodingScheme
							byte ussd_DataCodingScheme = data[0];

							// Second Parameter is ussd-String
							data = parameters[1].getData();

							ProcessUnstructuredSSIndicationImpl procUnSSInd = new ProcessUnstructuredSSIndicationImpl(
									ussd_DataCodingScheme, data);

							procUnSSInd.setInvokeId(invokeId);

							for (MAPServiceListener serLis : this.serviceListeners) {
								serLis
										.onProcessUnstructuredSSIndication(procUnSSInd);
							}

						} else if (oc.getCode() == MAPOperationCode.unstructuredSS_Request) {

							Parameter[] parameters = invoke.getParameters();

							byte[] data = parameters[0].getData();

							// First Parameter is ussd-DataCodingScheme
							byte ussd_DataCodingScheme = data[0];

							// Second Parameter is ussd-String
							data = parameters[1].getData();

							UnstructuredSSIndicationImpl procUnSSInd = new UnstructuredSSIndicationImpl(
									ussd_DataCodingScheme, data);

							procUnSSInd.setInvokeId(invokeId);

							for (MAPServiceListener serLis : this.serviceListeners) {
								serLis.onUnstructuredSSIndication(procUnSSInd);
							}
						}
					}

				} catch (AsnException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				// TODO : This is Error Send back TC-U-ABORT without
				// intimating User
				return;
			}

		}

	}

	public void onTCContinue(TCContinueIndication arg0) {
		// TODO Auto-generated method stub

	}

	public void onTCEnd(TCEndIndication arg0) {
		// TODO Auto-generated method stub

	}

	public void onTCUni(TCUniIndication arg0) {
		// TODO Auto-generated method stub

	}

	public void onInvokeTimeout(Invoke arg0) {
		// TODO Auto-generated method stub

	}

}
