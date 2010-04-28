package org.mobicents.protocols.ss7.map.api;

import java.util.Arrays;

/**
 * 
 * @author amit bhayani
 *
 */
public enum MAPDialogueAS {

	/**
	 * Look at http://www.oid-info.com/get/0.4.0.0.1.1.1.1
	 */
	MAP_DialogueAS(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 }, 1);

	private long[] oid;
	private int dialogAS;

	private MAPDialogueAS(long[] oid, int dialogAS) {
		this.oid = oid;
		this.dialogAS = dialogAS;
	}
	
	
	
	public long[] getOID() {
		return this.oid;
	}

	public int getDialogAS() {
		return this.dialogAS;
	}

	public static MAPDialogueAS getInstance(int dialogAS) {
		switch (dialogAS) {
		case 1:
			return MAP_DialogueAS;
		default:
			return null;
		}
	}

	public static MAPDialogueAS getInstance(long[] oid) {
		long[] temp = MAP_DialogueAS.getOID();
		if (Arrays.equals(temp, oid)) {
			return MAP_DialogueAS;
		}

		return null;
	}
	
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		for(long l :  this.oid){
			s.append(l).append(", ");
		}
		return s.toString();
	}
}
