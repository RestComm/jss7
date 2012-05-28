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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

/**
*
RequestingNodeType ::= ENUMERATED {
	vlr  (0),
	sgsn  (1),
	...,
	s-cscf  (2),
	bsf  (3),
	gan-aaa-server  (4),
	wlan-aaa-server  (5),
	mme		(16),
	mme-sgsn	(17)
	}
	-- the values 2, 3, 4 and 5 shall not be used on the MAP-D or Gr interfaces
	-- exception handling:
	-- received values in the range (6-15) shall be treated as "vlr"
	-- received values greater than 17 shall be treated as "sgsn"

* 
* @author sergey vetyutnev
* 
*/
public enum RequestingNodeType {
	vlr(0), 
	sgsn(1), 
	sCscf(2), 
	bsf(3), 
	ganAaaServer(4), 
	wlanAaaaServer(5), 
	mme(16), 
	mmeSgsn(17);

	private int code;

	private RequestingNodeType(int code) {
		this.code = code;
	}

	public static RequestingNodeType getInstance(int code) {
		switch (code) {
		case 0:
			return RequestingNodeType.vlr;
		case 1:
			return RequestingNodeType.sgsn;
		case 2:
			return RequestingNodeType.sCscf;
		case 3:
			return RequestingNodeType.bsf;
		case 4:
			return RequestingNodeType.ganAaaServer;
		case 5:
			return RequestingNodeType.wlanAaaaServer;
		case 16:
			return RequestingNodeType.mme;
		case 17:
			return RequestingNodeType.mmeSgsn;
		default:
			return null;
		}
	}

	public int getCode() {
		return this.code;
	}

}
