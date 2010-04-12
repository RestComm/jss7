package org.mobicents.protocols.ss7.map.api;

public interface MAPDialogListener {
	
	public void onDialogIndication(DialogIndication dialogIndication);
	
	public void onDialogConfirm(DialogConfirm dialogConfirm);

}
