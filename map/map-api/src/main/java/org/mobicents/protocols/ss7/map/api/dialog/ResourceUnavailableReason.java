package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * See ETS 300974 Table 5.3/7
 * 
 * ResourceUnavailableReason ::= ENUMERATED {
 *    shortTermResourceLimitation (0),
 *    longTermResourceLimitation (1)}
 *
 * 
 * @author amit bhayani
 * 
 */
public enum ResourceUnavailableReason {

	shortTermResourceLimitation(0), longTermResourceLimitation(1);

	private int code;

	private ResourceUnavailableReason(int code) {
		this.code = code;
	}

	public static ResourceUnavailableReason getInstance(int code) {
		switch (code) {
		case 0:
			return shortTermResourceLimitation;
		case 1:
			return longTermResourceLimitation;
		default:
			return null;
		}
	}

	public int getCode() {
		return code;
	}

}
