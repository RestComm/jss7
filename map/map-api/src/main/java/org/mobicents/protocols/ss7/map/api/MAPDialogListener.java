package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPDialogListener {

	public void onMAPOpenInfo(MAPOpenInfo mapOpenInfo);
	
	public void onMAPAcceptInfo(MAPAcceptInfo mapAccptInfo);
	
	public void onMAPCloseInfo(MAPCloseInfo mapCloseInfo);
	
	public void onMAPRefuseInfo(MAPRefuseInfo mapRefuseInfo);
	
	public void onMAPUserAbortInfo(MAPUserAbortInfo mapUserAbortInfo);
	
	public void onMAPProviderAbortInfo(MAPProviderAbortInfo mapProviderAbortInfo);

}
