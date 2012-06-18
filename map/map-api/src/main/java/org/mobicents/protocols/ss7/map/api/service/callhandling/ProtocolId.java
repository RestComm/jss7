package org.mobicents.protocols.ss7.map.api.service.callhandling;

/*
 * ProtocolId ::= ENUMERATED {
 * gsm-0408 (1),
 * gsm-0806 (2),
 * gsm-BSSMAP (3),
 * -- Value 3 is reserved and must not be used
 * ets-300102-1 (4)}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public enum ProtocolId {
	gsm_0408 (1),
	gsm_0806 (2),
	gsm_BSSMAP (3),
	ets_300102_1 (4);
	 
	private int code;

	private ProtocolId(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static ProtocolId getProtocolId(int code) {
		switch (code) {
		case 1:
			return gsm_0408;
		case 2:
			return gsm_0806;
		case 3:
			return gsm_BSSMAP;
		case 4:
			return ets_300102_1;
		default:
			return null;
		}
	}
}