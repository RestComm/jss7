package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPMessage;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPMessageImpl implements MAPMessage {
	private int invokeId;
	private MAPDialog mapDialog;

	public int getInvokeId() {
		return this.invokeId;
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public void setInvokeId(int invokeId) {
		this.invokeId = invokeId;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

}
