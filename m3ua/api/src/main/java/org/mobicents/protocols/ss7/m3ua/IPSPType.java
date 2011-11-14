package org.mobicents.protocols.ss7.m3ua;

public enum IPSPType {
	CLIENT("CLIENT"), SERVER("SERVER");

	private static final String TYPE_CLIENT = "CLIENT";
	private static final String TYPE_SERVER = "SERVER";

	private String type = null;

	private IPSPType(String type) {
		this.type = type;
	}

	public static IPSPType getIPSPType(String type) {
		if (TYPE_CLIENT.equals(type)) {
			return CLIENT;
		} else if (TYPE_SERVER.equals(type)) {
			return SERVER;
		}

		return null;
	}

	public String getType() {
		return this.type;
	}
}
