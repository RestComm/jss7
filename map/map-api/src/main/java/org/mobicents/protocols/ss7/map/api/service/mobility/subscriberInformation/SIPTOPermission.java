package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

/**
 * SIPTO-Permission ::= ENUMERATED {
 *	siptoAllowed (0),
 *	siptoNotAllowed (1)
 *	}
 *
 * @author amit bhayani
 * 
 */
public enum SIPTOPermission {
	siptoAllowed(0), siptoNotAllowed(1);

	private int code;

	private SIPTOPermission(int code) {
		this.code = code;
	}

	public static SIPTOPermission getInstance(int code) {
		switch (code) {
		case 0:
			return siptoAllowed;
		case 1:
			return siptoNotAllowed;
		default:
			return null;
		}
	}

	public int getCode() {
		return code;
	}

}
