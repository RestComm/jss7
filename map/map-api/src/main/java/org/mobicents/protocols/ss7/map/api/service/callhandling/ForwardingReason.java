package org.mobicents.protocols.ss7.map.api.service.callhandling;


/*
 *	ForwardingReason ::= ENUMERATED {
 *  notReachable (0),
 *  busy (1),
 *  noReply (2)}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public enum ForwardingReason {
	notReachable (0),
	busy (1),
    noReply (2);

	private int code;

	private ForwardingReason(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static ForwardingReason getForwardingReason(int code) {
		switch (code) {
		case 0:
			return notReachable;
		case 1:
			return busy;
		case 2:
			return noReply;
		default:
			return null;
		}
	}
}