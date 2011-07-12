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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 * 
 * NetworkResource ::= ENUMERATED {
 *	plmn  (0),
 *	hlr  (1),
 *	vlr  (2),
 *	pvlr  (3),
 *	controllingMSC  (4),
 *	vmsc  (5),
 *	eir  (6),
 *	rss  (7)}
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public enum NetworkResource {
	plmn(0), hlr(1), vlr(2), pvlr(3), controllingMSC(4), vmsc(5), eir(6), rss(7);

	private int code;

	private NetworkResource(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public static NetworkResource getInstance(int code) {
		switch (code) {
		case 0:
			return plmn;
		case 1:
			return hlr;
		case 2:
			return vlr;
		case 3:
			return pvlr;
		case 4:
			return controllingMSC;
		case 5:
			return vmsc;
		case 6:
			return eir;
		case 7:
			return rss;
		default:
			return null;
		}
	}	


}
