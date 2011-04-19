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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AddressStringImpl implements AddressString {

	protected int NO_EXTENSION_MASK = 0x80;
	protected int NATURE_OF_ADD_IND_MASK = 0x70;
	protected int NUMBERING_PLAN_IND_MASK = 0x0F;

	private int DIGIT_1_MASK = 0x0F;
	private int DIGIT_2_MASK = 0xF0;

	private AddressNature addressNature;
	private NumberingPlan numberingPlan;
	private String address;

	private boolean isExtension;

	public AddressStringImpl() {
	}

	public AddressStringImpl(AddressNature addressNature,
			NumberingPlan numberingPlan, String address) {
		super();
		this.addressNature = addressNature;
		this.numberingPlan = numberingPlan;
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public AddressNature getAddressNature() {
		return this.addressNature;
	}

	public NumberingPlan getNumberingPlan() {
		return this.numberingPlan;
	}

	public boolean isExtension() {
		return isExtension;
	}

	public void decode(AsnInputStream ansIS) throws MAPException, IOException {

		// The first byte has extension, nature of address indicator and
		// numbering plan indicator
		int nature = ansIS.read();

		if ((nature & NO_EXTENSION_MASK) == 0x80) {
			this.isExtension = false;
		} else {
			this.isExtension = true;
		}

		int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);

		this.addressNature = AddressNature.getInstance(natureOfAddInd);

		int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);

		this.numberingPlan = NumberingPlan.getInstance(numbPlanInd);

		StringBuffer s = new StringBuffer();

		while (ansIS.available() > 0) {
			int b = ansIS.read();

			int digit1 = (b & DIGIT_1_MASK);
			s.append(this.decodeNumber(digit1));

			int digit2 = ((b & DIGIT_2_MASK) >> 4);

			if (digit2 == 15) {
				// this is mask
			} else {
				s.append(this.decodeNumber(digit2));
			}
		}

		this.address = s.toString();

	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		int nature = 1;

		if (this.isExtension) {
			nature = 0;
		}

		nature = nature << 7;

		nature = nature | (this.addressNature.getIndicator() << 4);

		nature = nature | (this.numberingPlan.getIndicator());

		asnOs.write(nature);

		char[] chars = this.address.toCharArray();
		for (int i = 0; i < chars.length; i = i + 2) {
			char a = chars[i];

			int digit1 = this.encodeNumber(a);
			int digit2;
			if ((i + 1) == chars.length) {
				// add the filler instead
				digit2 = 15;
			} else {
				char b = chars[i + 1];
				digit2 = this.encodeNumber(b);
			}

			int digit = (digit2 << 4) | digit1;

			asnOs.write(digit);
		}

	}

	private int encodeNumber(char c) throws MAPException {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case '*':
			return 10;
		case '#':
			return 11;
		case 'a':
			return 12;
		case 'b':
			return 13;
		case 'c':
			return 14;
		default:
			throw new MAPException(
					"char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received "
							+ c);

		}
	}

	private char decodeNumber(int i) throws MAPException {
		switch (i) {
		case 0:
			return '0';
		case 1:
			return '1';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		case 10:
			return '*';
		case 11:
			return '#';
		case 12:
			return 'a';
		case 13:
			return 'b';
		case 14:
			return 'c';
			// case 15:
			// return 'd';
		default:
			throw new MAPException(
					"Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received "
							+ i);

		}
	}

}
