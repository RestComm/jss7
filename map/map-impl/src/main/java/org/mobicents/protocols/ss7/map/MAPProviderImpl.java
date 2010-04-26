package org.mobicents.protocols.ss7.map;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.OpenServiceIndication;
import org.mobicents.protocols.ss7.map.dialog.OpenServiceIndicationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderImpl implements MAPProvider, TCListener {

	private Set<MAPDialogListener> dialogListeners = new HashSet<MAPDialogListener>();

	private TCAPProvider tcapProvider = null;

	public MAPProviderImpl(TCAPProvider tcapProvider) {
		this.tcapProvider = tcapProvider;
		this.tcapProvider.addTCListener(this);
	}

	public void addMAPDialogListener(MAPDialogListener mapDialogListener) {
		dialogListeners.add(mapDialogListener);
	}

	public void addMAPServiceListener(MAPServiceListener mapServiceListener) {
		// TODO Auto-generated method stub

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
		MAPDialog dialog = new MAPDialogImpl(appCntx);
		return dialog;
	}

	public MapServiceFactory getMapServiceFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeMAPDialogListener(MAPDialogListener mapDialogListener) {
		dialogListeners.remove(mapDialogListener);
	}

	public void removeMAPServiceListener(MAPServiceListener mapServiceListener) {
		// TODO Auto-generated method stub

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
				// TODO send TC-U-ABORT without intimating MAP User
				return;
			}

			UserInformation userInfo = tcBeginIndication.getUserInformation();

			// FIXME : UserInformation is encoded here it-self, should we have a
			// different Object for this? Like MAP-DialogueAS and
			// MAP-DialoguePDU?
			
			byte[] data = null;//userInfo.getUserData();
			
			AsnInputStream asnis = new AsnInputStream(new ByteArrayInputStream(data));
			
			try {
				
				//Now what is this TAG and LENGTH for?
				int tag = asnis.readTag();
				
				if(tag != 0x08){
				int length = asnis.readLength();
				}
				
				
				tag = asnis.readTag();
				
				if(tag != Tag.OBJECT_IDENTIFIER){
					throw new ParseException("Expected OID TAG, but is "+ tag);
				}
				
				long[] map_dialog_oid = asnis.readObjectIdentifier(); 
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (AsnException e) {
				e.printStackTrace();
			}
			

			// TODO Check for UserInformation correctness

			OpenServiceIndication map_OpenService = new OpenServiceIndicationImpl(
					mapAppCtx, null);

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

}
