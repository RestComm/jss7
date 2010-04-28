package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPMessage;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class MAPMessageImpl implements MAPMessage {
	private long invokeId;
	private MAPDialog mapDialog;

	public long getInvokeId() {
		return this.invokeId;
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public void setInvokeId(long invokeId) {
		this.invokeId = invokeId;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

}
