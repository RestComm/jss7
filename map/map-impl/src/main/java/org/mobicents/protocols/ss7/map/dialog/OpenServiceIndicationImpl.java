package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.dialog.OpenServiceIndication;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class OpenServiceIndicationImpl implements OpenServiceIndication {

	private MAPApplicationContext appCtx;
	private SccpAddress destAddress;
	private byte[] destReference;

	private SccpAddress origAddress = null;
	private byte[] origReference;

	public OpenServiceIndicationImpl(MAPApplicationContext appCtx,
			SccpAddress destAddress) {
		this.appCtx = appCtx;
		this.destAddress = destAddress;
	}

	public SccpAddress getDestAddress() {
		return this.destAddress;
	}

	public byte[] getDestReference() {
		return this.destReference;
	}

	public MAPApplicationContext getMAPApplicationContext() {
		return this.appCtx;
	}

	public SccpAddress getOrigAddress() {
		return this.origAddress;
	}

	public byte[] getOrigReference() {
		return this.origReference;
	}

	public void setDestReference(byte[] destReference) {
		this.destReference = destReference;
	}

	public void setOrigAddress(SccpAddress origAddress) {
		this.origAddress = origAddress;
	}

	public void setOrigReference(byte[] origReference) {
		this.origReference = origReference;
	}

}
