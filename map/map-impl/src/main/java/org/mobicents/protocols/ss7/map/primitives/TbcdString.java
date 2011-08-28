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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public abstract class TbcdString implements MAPAsnPrimitive {

	protected int DIGIT_1_MASK = 0x0F;
	protected int DIGIT_2_MASK = 0xF0;

	protected String decodeString(AsnInputStream ansIS, int length) throws IOException, MAPParsingComponentException {
		StringBuilder s = new StringBuilder();
		for (int i1 = 0; i1 < length; i1++) {
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

		return s.toString();
	}
	
	protected void encodeString(AsnOutputStream asnOs, String data) throws MAPException {
		char[] chars = data.toCharArray();
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
	
	protected int encodeNumber(char c) throws MAPException {
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

	protected char decodeNumber(int i) throws MAPParsingComponentException {
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
			throw new MAPParsingComponentException(
					"Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received "
							+ i, MAPParsingComponentExceptionReason.MistypedParameter);

		}
	}
}

