package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPProviderAbortInfoImpl implements MAPProviderAbortInfo {

	private MAPDialog mapDialog = null;
	private MAPProviderAbortReason mapProviderAbortReason = null;

	public MAPProviderAbortInfoImpl() {
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public MAPProviderAbortReason getMAPProviderAbortReason() {
		return this.mapProviderAbortReason;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
		this.mapProviderAbortReason = mapProvAbrtReas;
	}

}
