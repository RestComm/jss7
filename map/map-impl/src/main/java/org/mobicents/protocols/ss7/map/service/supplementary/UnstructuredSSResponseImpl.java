package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;

public class UnstructuredSSResponseImpl extends USSDServiceImpl implements
		UnstructuredSSResponse {

	public UnstructuredSSResponseImpl(byte ussdDataCodingSch, USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
