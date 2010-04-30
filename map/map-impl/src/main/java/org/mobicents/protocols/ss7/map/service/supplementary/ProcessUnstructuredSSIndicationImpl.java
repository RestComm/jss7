package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

public class ProcessUnstructuredSSIndicationImpl extends USSDServiceImpl
		implements ProcessUnstructuredSSIndication {

	public ProcessUnstructuredSSIndicationImpl(byte ussdDataCodingSch,
			USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
