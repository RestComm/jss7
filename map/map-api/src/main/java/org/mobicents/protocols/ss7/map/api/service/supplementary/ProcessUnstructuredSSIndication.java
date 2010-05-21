package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;

/**
 * 
 * @author amit bhayani
 *
 */
public interface ProcessUnstructuredSSIndication extends  USSDService {
	
	public void setMSISDNAddressString(AddressString msisdnAddr);
	
	public AddressString getMSISDNAddressString();
}
