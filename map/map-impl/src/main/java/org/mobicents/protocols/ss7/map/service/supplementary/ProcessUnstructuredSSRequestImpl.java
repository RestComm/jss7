package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

public class ProcessUnstructuredSSRequestImpl extends USSDServiceImpl implements
		ProcessUnstructuredSSRequest {

	public ProcessUnstructuredSSRequestImpl(byte ussdDataCodingSch,
			USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}
	


}
