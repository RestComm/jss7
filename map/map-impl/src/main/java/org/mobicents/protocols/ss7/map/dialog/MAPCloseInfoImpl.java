package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPCloseInfoImpl implements MAPCloseInfo {
	
	private MAPDialog mapDialog = null;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

}
