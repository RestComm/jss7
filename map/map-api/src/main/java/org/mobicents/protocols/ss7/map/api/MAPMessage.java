package org.mobicents.protocols.ss7.map.api;

public interface MAPMessage {

	public int getInvokeId();
	
	public void setInvokeId();
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);
	
}
