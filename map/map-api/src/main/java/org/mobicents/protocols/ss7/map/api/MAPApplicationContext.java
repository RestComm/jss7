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

package org.mobicents.protocols.ss7.map.api;

import java.util.Arrays;

/**
 * 
 * @author amit bhayani
 * 
 */
public enum MAPApplicationContext {
	


	/**
	 * Look at http://www.oid-info.com/get/0.4.0.0.1.0.19.2
	 */
	networkUnstructuredSsContextV2(new long[] { 0, 4, 0, 0, 1, 0, 19, 2 }, 1);

	private long[] oid;
	private int applicationContext;

	private MAPApplicationContext(long[] oid, int applicationContext) {
		this.oid = oid;
		this.applicationContext = applicationContext;
	}

	public long[] getOID() {
		return this.oid;
	}

	public int getApplicationContext() {
		return this.applicationContext;
	}

	public static MAPApplicationContext getInstance(int applicationContext) {
		switch (applicationContext) {
		case 1:
			return networkUnstructuredSsContextV2;
		default:
			return null;
		}
	}

	public static MAPApplicationContext getInstance(long[] oid) {
		long[] temp = networkUnstructuredSsContextV2.getOID();
		if (Arrays.equals(temp, oid)) {
			return networkUnstructuredSsContextV2;
		}

		return null;
	}
	
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		for(long l :  this.oid){
			s.append(l).append(", ");
		}
		return s.toString();
	}

}
