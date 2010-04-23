package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;

public class UnstructuredSSIndicationImpl extends USSDServiceImpl implements
		UnstructuredSSIndication {

	public UnstructuredSSIndicationImpl(byte ussdDataCodingSch,
			byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
