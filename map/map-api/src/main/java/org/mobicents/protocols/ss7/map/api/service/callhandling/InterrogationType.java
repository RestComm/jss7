package org.mobicents.protocols.ss7.map.api.service.callhandling;


/*
 * InterrogationType ::= ENUMERATED {
 * basicCall (0),
 * forwarding (1)}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public enum InterrogationType {
	basicCall(0), forwarding(1);

	private int code;

	private InterrogationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static InterrogationType getInterrogationType(int code) {
		switch (code) {
		case 0:
			return basicCall;
		case 1:
			return forwarding;
		default:
			return null;
		}
	}
}