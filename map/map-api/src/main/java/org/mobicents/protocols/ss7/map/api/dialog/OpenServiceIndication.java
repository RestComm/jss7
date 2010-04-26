package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface OpenServiceIndication {

	public MAPApplicationContext getMAPApplicationContext();

	public SccpAddress getDestAddress();

	public byte[] getDestReference();

	public void setDestReference(byte[] destReference);

	public SccpAddress getOrigAddress();

	public void setOrigAddress(SccpAddress origAddress);

	public byte[] getOrigReference();

	public void setOrigReference(byte[] origReference);

}
