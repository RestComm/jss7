package org.mobicents.protocols.ss7.map;

public class Utils {

	public final static String dump(byte[] buff, int size, boolean asBits) {
		String s = "";
		for (int i = 0; i < size; i++) {
			String ss = null;
			if(!asBits)
			{
				ss = Integer.toHexString(buff[i] & 0xff);
			}
			else
			{
				ss = Integer.toBinaryString(buff[i] & 0xff); 
			}
			ss = fillInZeroPrefix(ss,asBits);
			s += " " + ss;
		}
		return s;
	}
	
	public final static String fillInZeroPrefix(String ss, boolean asBits) {
		if (asBits) {
			if (ss.length() < 8) {
				for (int j = ss.length(); j < 8; j++) {
					ss = "0" + ss;
				}
			}
		} else {
			// hex
			if (ss.length() < 2) {

				ss = "0" + ss;
			}
		}

		return ss;
	}
}
