package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.MAPMessageImpl;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDService;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * @author amit bhayani
 * 
 */
public abstract class USSDServiceImpl extends MAPMessageImpl implements
		USSDService {
	private byte ussdDataCodingSch;

	private USSDString ussdString;

	public USSDServiceImpl(byte ussdDataCodingSch, USSDString ussdString) {
		this.ussdDataCodingSch = ussdDataCodingSch;
		this.ussdString = ussdString;
	}

	public byte getUSSDDataCodingScheme() {
		return ussdDataCodingSch;
	}

	public USSDString getUSSDString() {
		return this.ussdString;
	}

	public void setUSSDDataCodingScheme(byte ussdDataCodingSch) {
		this.ussdDataCodingSch = ussdDataCodingSch;
	}

	public void setUSSDString(USSDString ussdString) {
		this.ussdString = ussdString;
	}
	

}
