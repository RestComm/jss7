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

package org.mobicents.protocols.ss7.map.api.primitives;


/**
 * -- 000 unknown 
 * -- 001 international number 
 * -- 010 national significant number 
 * -- 011 network specific number 
 * -- 100 subscriber number 
 * -- 101 reserved 
 * -- 110 abbreviated number 
 * -- 111 reserved for extension
 * 
 * See also {@link AddressString}
 * 
 * @author amit bhayani
 * 
 */
public enum AddressNature {

	unknown(0), 
	international_number(1), 
	national_significant_number(2), 
	network_specific_number(3), 
	subscriber_number(4), 
	reserved(5), 
	abbreviated_number(6), 
	reserved_for_extension(7);

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
