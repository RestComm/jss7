/**
 * Start time:12:24:47 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;

/**
 * Start time:12:24:47 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GenericDigitsImpl extends AbstractParameter implements GenericDigits {

	private int encodignScheme;
	private int typeOfDigits;
	private int[] digits;

	public GenericDigitsImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public GenericDigitsImpl(int encodignScheme, int typeOfDigits, int[] digits) {
		super();
		this.encodignScheme = encodignScheme;
		this.typeOfDigits = typeOfDigits;
		this.setDigits(digits);
	}

	public GenericDigitsImpl() {
		super();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length < 2) {
			throw new ParameterRangeInvalidException("byte[] must not be null or has size less than 2");
		}
		this.typeOfDigits = b[0] & 0x1F;
		this.encodignScheme = (b[0] >> 5) & 0x07;
		this.digits = new int[b.length - 1];

		for (int index = 1; index < b.length; index++) {
			this.digits[index - 1] = b[index];
		}
		return 1 + this.digits.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		byte[] b = new byte[this.digits.length + 1];

		b[0] |= this.typeOfDigits & 0x1F;
		b[0] |= ((this.encodignScheme & 0x07) << 5);

		for (int index = 1; index < b.length; index++) {
			b[index] = (byte) this.digits[index - 1];
		}
		return b;

	}

	public int getEncodignScheme() {
		return encodignScheme;
	}

	public void setEncodignScheme(int encodignScheme) {
		this.encodignScheme = encodignScheme;
	}

	public int getTypeOfDigits() {
		return typeOfDigits;
	}

	public void setTypeOfDigits(int typeOfDigits) {
		this.typeOfDigits = typeOfDigits;
	}

	public int[] getDigits() {
		return digits;
	}

	public void setDigits(int[] digits) {
		if (digits == null)
			throw new IllegalArgumentException("Digits must not be null");
		this.digits = digits;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
