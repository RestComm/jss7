package org.mobicents.protocols.ss7.map.api;

/**
 * This is super interface for all service message in MAP
 * 
 * @author amit bhayani
 * 
 */
public interface MAPMessage {

	public int getInvokeId();

	public void setInvokeId(int invokeId);

	public MAPDialog getMAPDialog();

	public void setMAPDialog(MAPDialog mapDialog);

}
