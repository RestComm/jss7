package org.mobicents.protocols.ss7.m3ua;

public enum Functionality {
	AS("AS"), IPSP("IPSP"), SGW("SGW");

	private static final String TYPE_AS = "AS";
	private static final String TYPE_IPSP = "IPSP";
	private static final String TYPE_SGW = "SGW";

	private String type = null;

	private Functionality(String type) {
		this.type = type;
	}

	public static Functionality getFunctionality(String type) {
		if (TYPE_AS.equals(type)) {
			return AS;
		} else if (TYPE_IPSP.equals(type)) {
			return IPSP;
		} else if (TYPE_SGW.equals(type)) {
			return SGW;
		}
		return null;
	}

	public String getType() {
		return this.type;
	}

}
