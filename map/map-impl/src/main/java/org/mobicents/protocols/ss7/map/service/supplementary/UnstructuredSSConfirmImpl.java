package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSConfirm;

public class UnstructuredSSConfirmImpl extends USSDServiceImpl implements
		UnstructuredSSConfirm {

	public UnstructuredSSConfirmImpl(byte ussdDataCodingSch, byte[] ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
