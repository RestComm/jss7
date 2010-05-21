package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;


/**
 * This service is used between the MSC and the VLR and between the VLR and the
 * HLR to relay information in order to allow unstructured supplementary service
 * operation.
 * 
 * @author amit bhayani
 * 
 */
public interface ProcessUnstructuredSSRequest extends  USSDService {
	
	public void setMSISDNAddressString(AddressString msisdnAddr);
	
	public AddressString getMSISDNAddressString();
	
}
