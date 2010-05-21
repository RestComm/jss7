package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

public class ProcessUnstructuredSSIndicationImpl extends USSDServiceImpl
		implements ProcessUnstructuredSSIndication {
	
	private AddressString msisdnAddressString = null;

	public ProcessUnstructuredSSIndicationImpl(byte ussdDataCodingSch,
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
