package org.mobicents.protocols.ss7.map.api;

import java.util.Arrays;

/**
 * 
 * @author amit bhayani
 * 
 */
public enum MAPApplicationContext {
	


	/**
	 * Look at http://www.oid-info.com/get/0.4.0.0.1.0.19.2
	 */
	networkUnstructuredSsContextV2(new long[] { 0, 4, 0, 0, 1, 0, 19, 2 }, 1);

	private long[] oid;
	private int applicationContext;

	private MAPApplicationContext(long[] oid, int applicationContext) {
		this.oid = oid;
		this.applicationContext = applicationContext;
	}

	public long[] getOID() {
		return this.oid;
	}

	public int getApplicationContext() {
		return this.applicationContext;
	}

	public static MAPApplicationContext getInstance(int applicationContext) {
		switch (applicationContext) {
		case 1:
			return networkUnstructuredSsContextV2;
		default:
			return null;
		}
	}

	public static MAPApplicationContext getInstance(long[] oid) {
		long[] temp = networkUnstructuredSsContextV2.getOID();
		if (Arrays.equals(temp, oid)) {
			return networkUnstructuredSsContextV2;
		}

		return null;
	}

}
