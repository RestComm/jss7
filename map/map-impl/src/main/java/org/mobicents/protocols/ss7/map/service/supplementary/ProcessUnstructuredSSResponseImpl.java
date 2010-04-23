package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;

public class ProcessUnstructuredSSResponseImpl extends USSDServiceImpl
		implements ProcessUnstructuredSSResponse {

	public ProcessUnstructuredSSResponseImpl(byte ussdDataCodingSch,
			byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
