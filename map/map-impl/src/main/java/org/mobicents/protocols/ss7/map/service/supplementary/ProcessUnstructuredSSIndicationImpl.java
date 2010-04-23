package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;

public class ProcessUnstructuredSSIndicationImpl extends USSDServiceImpl
		implements ProcessUnstructuredSSIndication {

	public ProcessUnstructuredSSIndicationImpl(byte ussdDataCodingSch,
			byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
