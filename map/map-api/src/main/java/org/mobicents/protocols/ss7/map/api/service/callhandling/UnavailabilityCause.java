package org.mobicents.protocols.ss7.map.api.service.callhandling;


/*
 *	UnavailabilityCause ::= ENUMERATED {
 *  bearerServiceNotProvisioned (1),
 *  teleserviceNotProvisioned (2),
 *  absentSubscriber (3),
 *  busySubscriber (4),
 *  callBarred (5),
 *  cug-Reject (6),
 *  ...}
 */
 
/*
 * 
 * @author cristian veliscu
 * 
 */
public enum UnavailabilityCause {
	bearerServiceNotProvisioned (1),
    teleserviceNotProvisioned (2),
    absentSubscriber (3),
    busySubscriber (4),
    callBarred (5),
    cugReject (6);

	private int code;

	private UnavailabilityCause(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static UnavailabilityCause getUnavailabilityCause(int code) {
		switch (code) {
		case 1:
			return bearerServiceNotProvisioned;
		case 2:
			return teleserviceNotProvisioned;
		case 3:
			return absentSubscriber;
		case 4:
			return busySubscriber;
		case 5:
			return callBarred;
		case 6:
			return cugReject;
		default:
			return null;
		}
	}
}