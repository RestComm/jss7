package org.mobicents.protocols.ss7.map;

import java.util.HashSet;
import java.util.Set;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderImpl implements MAPProvider {

	Set<MAPDialogListener> dialogListeners = new HashSet<MAPDialogListener>();

	public MAPProviderImpl() {
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

}
