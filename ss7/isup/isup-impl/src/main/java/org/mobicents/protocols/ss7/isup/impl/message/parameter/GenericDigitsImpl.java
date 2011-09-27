/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;

/**
 * Start time:12:24:47 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GenericDigitsImpl extends AbstractISUPParameter implements GenericDigits {

	private int encodingScheme;
	private int typeOfDigits;
	private int[] digits;

	public GenericDigitsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public GenericDigitsImpl(int encodingScheme, int typeOfDigits, int[] digits) {
		super();
		this.encodingScheme = encodingScheme;
		this.typeOfDigits = typeOfDigits;
		this.setDigits(digits);
	}

	public GenericDigitsImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length < 2) {
			throw new ParameterException("byte[] must not be null or has size less than 2");
		}
		this.typeOfDigits = b[0] & 0x1F;
		this.encodingScheme = (b[0] >> 5) & 0x07;
		this.digits = new int[b.length - 1];

		for (int index = 1; index < b.length; index++) {
			this.digits[index - 1] = b[index];
		}
		return 1 + this.digits.length;
	}

	public byte[] encode() throws ParameterException {

		byte[] b = new byte[this.digits.length + 1];

		b[0] |= this.typeOfDigits & 0x1F;
		b[0] |= ((this.encodingScheme & 0x07) << 5);

		for (int index = 1; index < b.length; index++) {
			b[index] = (byte) this.digits[index - 1];
		}
		return b;

	}

	public int getEncodingScheme() {
		return encodingScheme;
	}

	public void setEncodingScheme(int encodingScheme) {
		this.encodingScheme = encodingScheme;
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
