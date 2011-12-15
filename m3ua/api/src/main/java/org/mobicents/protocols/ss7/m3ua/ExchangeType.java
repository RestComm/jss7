package org.mobicents.protocols.ss7.m3ua;

public enum ExchangeType {
	SE("SE"), DE("DE");

	private static final String TYPE_SE = "SE";
	private static final String TYPE_DE = "DE";

	private String type = null;

	private ExchangeType(String type) {
		this.type = type;
	}

	public static ExchangeType getExchangeType(String type) {
		if (TYPE_SE.equals(type)) {
			return SE;
		} else if (TYPE_DE.equals(type)) {
			return DE;
		} else {
			return null;
		}
	}
	
	public String getType() {
		return this.type;
	}
}
