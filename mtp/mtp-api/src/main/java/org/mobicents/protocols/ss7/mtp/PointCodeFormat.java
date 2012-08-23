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
package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 * 
 */
public enum PointCodeFormat {
	ANSI("ANSI"), ITU("ITU"), Japan_TTC_DDI("Japan_TTC_DDI"), China("China"), Japan_NTT("Japan_NTT");

	private final String format;

	private static final String FORMAT_ITU = "ITU";
	private static final String FORMAT_ANSI = "ANSI";
	private static final String FORMAT_Japan_TTC_DDI = "Japan_TTC_DDI";
	private static final String FROMAT_China = "China";
	private static final String FORMAT_Japan_NTT = "Japan_NTT";

	private PointCodeFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return this.format;
	}

	public static PointCodeFormat getInstance(String format) {
		if (FORMAT_ITU.equals(format)) {
			return ITU;
		} else if (FORMAT_ANSI.equals(format)) {
			return ANSI;
		} else if (FORMAT_Japan_TTC_DDI.equals(format)) {
			return Japan_TTC_DDI;
		} else if (FROMAT_China.equals(format)) {
			return China;
		} else if (FORMAT_Japan_NTT.equals(format)) {
			return Japan_NTT;
		}

		return null;
	}

}
