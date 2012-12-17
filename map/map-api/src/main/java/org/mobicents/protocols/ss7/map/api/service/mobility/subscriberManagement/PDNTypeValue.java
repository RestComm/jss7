package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

public enum PDNTypeValue {
	IPv4(0x01), 
	IPv6(0x02), 
	IPv4v6(0x03); 

	private int code;

	private PDNTypeValue(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static PDNTypeValue getInstance(int code) {
		switch (code) {
		case 0x01:
			return PDNTypeValue.IPv4;
		case 0x02:
			return PDNTypeValue.IPv6;
		case 0x03:
			return PDNTypeValue.IPv4v6;
		default:
			return null;
		}
	}
}
