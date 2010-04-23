package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSConfirm;

public class ProcessUnstructuredSSConfirmImpl extends USSDServiceImpl implements
		ProcessUnstructuredSSConfirm {

	public ProcessUnstructuredSSConfirmImpl(byte ussdDataCodingSch,
			byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
