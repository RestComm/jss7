package org.mobicents.protocols.ss7.management.shell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author amit bhayani
 * 
 */
public class Utils {

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

	/**
	 * Validate ip address with regular expression
	 * 
	 * @param ip
	 *            ip address for validation
	 * @return true valid ip address, false invalid ip address
	 */
	public static boolean validateIp(final String ip) {
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static int validatePort(final String port) {
		int iPort = -1;
		try {
			iPort = Integer.parseInt(port);
		} catch (NumberFormatException nfe) {
			return -1;
		}

		if (iPort > 0 && iPort <= 65535) {
			return iPort;
		}

		return -1;
	}

	public static int validateChannel(final String strChannel) {
		int channel = -1;
		try {
			channel = Integer.parseInt(strChannel);
		} catch (NumberFormatException nfe) {
			return -1;
		}

		if (channel > 0 && channel <= 31) {
			return channel;
		}

		return -1;
	}

	public static int validateSpan(final String strSpan) {
		int span = -1;
		try {
			span = Integer.parseInt(strSpan);
		} catch (NumberFormatException nfe) {
			return span;
		}

		// Max span's 16?
		if (span > 0 && span <= 16) {
			return span;
		}

		return -1;
	}

	public static int validateCode(final String strCode) {
		int code = -1;
		try {
			code = Integer.parseInt(strCode);
		} catch (NumberFormatException nfe) {
			return -1;
		}

		// Max span's 16 * 32 = 512?
		if (code > 0 && code <= 512) {
			return code;
		}

		return -1;
	}
}
