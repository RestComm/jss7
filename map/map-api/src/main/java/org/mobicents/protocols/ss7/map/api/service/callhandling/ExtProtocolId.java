package org.mobicents.protocols.ss7.map.api.service.callhandling;

/*
 * Ext-ProtocolId ::= ENUMERATED {
 * ets_300356 (1),
 * ...}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public enum ExtProtocolId {
	ets_300356 (1);
	 
	private int code;

	private ExtProtocolId(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static ExtProtocolId getExtProtocolId(int code) {
		switch (code) {
		case 1:
			return ets_300356;
		default:
			return null;
		}
	}
}