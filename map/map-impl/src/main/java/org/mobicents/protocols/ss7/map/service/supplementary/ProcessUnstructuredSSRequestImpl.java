package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;

public class ProcessUnstructuredSSRequestImpl extends USSDServiceImpl implements
		ProcessUnstructuredSSRequest {

	public ProcessUnstructuredSSRequestImpl(byte ussdDataCodingSch,
			byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}
	


}
