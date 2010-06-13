package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface ProcessUnstructuredSSIndication extends USSDService {

	/**
	 * Set the {@link AddressString} representing the MSISDN which sent this
	 * USSD String
	 * 
	 * @param msisdnAddr
	 */
	public void setMSISDNAddressString(AddressString msisdnAddr);

	/**
	 * Get the {@link AddressString} representing the MSISDN
	 * 
	 * @return
	 */
	public AddressString getMSISDNAddressString();
}
