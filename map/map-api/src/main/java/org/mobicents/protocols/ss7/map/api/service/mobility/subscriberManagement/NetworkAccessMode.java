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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 * 

NetworkAccessMode ::= ENUMERATED {
	packetAndCircuit	(0),
	onlyCircuit		(1),
	onlyPacket		(2),
	...}
	-- if unknown values are received in NetworkAccessMode
	-- they shall be discarded.

 * 
 * @author sergey vetyutnev
 * 
 */
public enum NetworkAccessMode {
	packetAndCircuit(0), 
	onlyCircuit(1), 
	onlyPacket(2);

	private int code;

	private NetworkAccessMode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static NetworkAccessMode getInstance(int code) {
		switch (code) {
		case 0:
			return NetworkAccessMode.packetAndCircuit;
		case 1:
			return NetworkAccessMode.onlyCircuit;
		case 2:
			return NetworkAccessMode.onlyPacket;
		default:
			return null;
		}
	}
}

