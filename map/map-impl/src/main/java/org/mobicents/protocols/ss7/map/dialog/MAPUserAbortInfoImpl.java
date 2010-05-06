package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPUserAbortInfoImpl implements MAPUserAbortInfo {
	
	private MAPDialog mapDialog = null;
	private MAPUserAbortChoice mapUserAbortChoice = null;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public MAPUserAbortChoice getMAPUserAbortChoice() {
		return this.mapUserAbortChoice;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setMAPUserAbortChoice(MAPUserAbortChoice mapUsrAbrtChoice) {
		this.mapUserAbortChoice = mapUsrAbrtChoice;
	}

}
