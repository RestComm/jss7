package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

/**
 * LIPA-Permission ::= ENUMERATED {
 *	lipaProhibited (0),
 *	lipaOnly (1),
 *	lipaConditional (2)
 *	}
 *
 * @author amit bhayani
 * 
 */
public enum LIPAPermission {
	lipaProhibited(0), lipaOnly(1), lipaConditional(2);

	private int code;

	private LIPAPermission(int code) {
		this.code = code;
	}

	public static LIPAPermission getInstance(int code) {
		switch (code) {
		case 0:
			return lipaProhibited;
		case 1:
			return lipaOnly;
		case 2:
			return lipaConditional;
		default:
			return null;
		}
	}

	public int getCode() {
		return code;
	}
}
