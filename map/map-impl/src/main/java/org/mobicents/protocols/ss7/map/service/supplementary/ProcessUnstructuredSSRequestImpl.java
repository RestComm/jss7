package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

public class ProcessUnstructuredSSRequestImpl extends USSDServiceImpl implements
		ProcessUnstructuredSSRequest {

	private AddressString msisdnAddressString = null;
	
	public ProcessUnstructuredSSRequestImpl(byte ussdDataCodingSch,
			USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}
	
	public AddressString getMSISDNAddressString() {
		return this.msisdnAddressString;
	}

	public void setMSISDNAddressString(AddressString msisdnAddr) {
		this.msisdnAddressString = msisdnAddr;
	}

}
