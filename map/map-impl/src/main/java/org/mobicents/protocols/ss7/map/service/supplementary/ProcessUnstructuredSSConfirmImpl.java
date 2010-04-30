package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSConfirm;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

public class ProcessUnstructuredSSConfirmImpl extends USSDServiceImpl implements
		ProcessUnstructuredSSConfirm {

	public ProcessUnstructuredSSConfirmImpl(byte ussdDataCodingSch,
			USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
