package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * -- 000 unknown -- 001 international number -- 010 national significant number --
 * 011 network specific number -- 100 subscriber number -- 101 reserved -- 110
 * abbreviated number -- 111 reserved for extension
 * 
 * See also {@link AddressString}
 * 
 * @author amit bhayani
 * 
 */
public enum AddressNature {

	unknown(0), international_number(1), national_significant_number(2), network_specific_number(
			3), subscriber_number(4), reserved(5), abbreviated_number(6), reserved_for_extension(
			7);

	private int indicator;

	private AddressNature(int indicator) {
		this.indicator = indicator;
	}
	

	public int getIndicator() {
		return indicator;
	}

	public static AddressNature getInstance(int indication) {
		switch (indication) {
		case 0:
			return unknown;
		case 1:
			return international_number;
		case 2:
			return national_significant_number;
		case 3:
			return network_specific_number;
		case 4:
			return subscriber_number;
		case 5:
			return reserved;
		case 6:
			return abbreviated_number;
		case 7:
			return reserved_for_extension;
		default:
			return null;
		}
	}

}
