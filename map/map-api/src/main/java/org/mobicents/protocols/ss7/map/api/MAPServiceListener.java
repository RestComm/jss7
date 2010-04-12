package org.mobicents.protocols.ss7.map.api;

public interface MAPServiceListener {

	
	public void onServiceIndication(ServiceIndication serviceIndication);
	
	public void onServiceConfirm(ServiceConfirm serviceConfirm);
	
}
