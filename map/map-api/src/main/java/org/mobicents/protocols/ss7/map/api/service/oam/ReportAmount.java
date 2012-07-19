/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 * 

ReportAmount ::= ENUMERATED {
	d1 (0),
	d2 (1),
	d4 (2),
	d8 (3),
	d16 (4),
	d32 (5),
	d64 (6),
	infinity (7)}

 * 
 * @author sergey vetyutnev
 * 
 */
public enum ReportAmount {
	d1(0), 
	d2(1), 
	d4(2), 
	d8(3), 
	d16(4), 
	d32(5), 
	d64(6), 
	infinity(7);

	private int code;

	private ReportAmount(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static ReportAmount getInstance(int code) {
		switch (code) {
		case 0:
			return ReportAmount.d1;
		case 1:
			return ReportAmount.d2;
		case 2:
			return ReportAmount.d4;
		case 3:
			return ReportAmount.d8;
		case 4:
			return ReportAmount.d16;
		case 5:
			return ReportAmount.d32;
		case 6:
			return ReportAmount.d64;
		case 7:
			return ReportAmount.infinity;
		default:
			return null;
		}
	}
}

