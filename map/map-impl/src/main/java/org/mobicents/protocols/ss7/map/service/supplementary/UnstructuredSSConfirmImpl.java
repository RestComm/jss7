package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSConfirm;

public class UnstructuredSSConfirmImpl extends USSDServiceImpl implements
		UnstructuredSSConfirm {

	public UnstructuredSSConfirmImpl(byte ussdDataCodingSch, USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

}
