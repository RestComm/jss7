/**
 * Start time:17:31:22 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;

/**
 * Start time:17:31:22 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ClosedUserGroupInterlockCodeImpl extends AbstractParameter implements ClosedUserGroupInterlockCode {

	// XXX: this parameter is weird, it does not follow general convention of
	// parameters :/
	private byte[] niDigits = null;
	private int binaryCode = 0;

	public ClosedUserGroupInterlockCodeImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public ClosedUserGroupInterlockCodeImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param niDigits
	 *            - arrays of NetworkIdentiti digits, it must be of length 4, if
	 *            only 2 digits are required - last two must be empty (zero,
	 *            default int value)
	 * @param binaryCode
	 */
	public ClosedUserGroupInterlockCodeImpl(byte[] niDigits, int binaryCode) {
		super();

		// FIXME: add check for range ?
		this.setNiDigits(niDigits);
		this.binaryCode = binaryCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 4) {
			throw new ParameterRangeInvalidException("byte[] must not be null and must have length of 4");
		}
		int v = 0;
		this.niDigits = new byte[4];

		for (int i = 0; i < 2; i++) {
			v = 0;
			v = b[i];
			this.niDigits[i * 2] = (byte) (v & 0x0F);
			this.niDigits[i * 2 + 1] = (byte) ((v >> 4) & 0x0F);
		}

		this.binaryCode = b[2] << 8;
		this.binaryCode |= b[3];

		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[4];
		int v = 0;
		for (int i = 0; i < 2; i++) {
			v = 0;

			v |= (this.niDigits[i * 2] & 0x0F) << 4;
			v |= (this.niDigits[i * 2 + 1] & 0x0F);

			b[i] = (byte) v;
		}
		b[2] = (byte) (this.binaryCode >> 8);
		b[3] = (byte) this.binaryCode;

		return b;
	}

	public byte[] getNiDigits() {
		return niDigits;
	}

	public void setNiDigits(byte[] niDigits) {
		if (niDigits == null || niDigits.length != 4) {
			throw new IllegalArgumentException();
		}

		this.niDigits = niDigits;
	}

	public int getBinaryCode() {
		return binaryCode;
	}

	public void setBinaryCode(int binaryCode) {
		this.binaryCode = binaryCode;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
