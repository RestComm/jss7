package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.MAPMessageImpl;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDService;

/**
 * @author amit bhayani
 * 
 */
public abstract class USSDServiceImpl extends MAPMessageImpl implements
		USSDService {
	private byte ussdDataCodingSch;

	private byte[] ussdString;

	public USSDServiceImpl(byte ussdDataCodingSch, byte[] ussdString) {
		this.ussdDataCodingSch = ussdDataCodingSch;
		this.ussdString = ussdString;
	}

	public byte getUSSDDataCodingScheme() {
		return ussdDataCodingSch;
	}

	public byte[] getUSSDString() {
		return this.ussdString;
	}

	public void setUSSDDataCodingScheme(byte ussdDataCodingSch) {
		this.ussdDataCodingSch = ussdDataCodingSch;
	}

	public void setUSSDString(byte[] ussdString) {
		this.ussdString = ussdString;
	}
	

}
