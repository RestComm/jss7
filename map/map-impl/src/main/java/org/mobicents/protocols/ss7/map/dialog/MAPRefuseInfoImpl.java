package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPRefuseInfoImpl implements MAPRefuseInfo {
	
	private MAPDialog mapDialog = null;
	private Reason reason;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public Reason getReason() {
		return this.reason;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

}
