package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;


/*
 * 
 * @author cristian veliscu
 * 
 */
public interface MAPServiceCallHandling extends MAPServiceBase {
    public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, 
    		SccpAddress origAddress, AddressString origReference, 
    		SccpAddress destAddress, AddressString destReference) throws MAPException;
	public void addMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener);
    public void removeMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener);
}