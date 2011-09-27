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
public enum MAPDialogueAS {

	/**
	 * Look at http://www.oid-info.com/get/0.4.0.0.1.1.1.1
	 */
	MAP_DialogueAS(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 }, 1);

	private long[] oid;
	private int dialogAS;

	private MAPDialogueAS(long[] oid, int dialogAS) {
		this.oid = oid;
		this.dialogAS = dialogAS;
	}
	
	
	
	public long[] getOID() {
		return this.oid;
	}

	public int getDialogAS() {
		return this.dialogAS;
	}

	public static MAPDialogueAS getInstance(int dialogAS) {
		switch (dialogAS) {
		case 1:
			return MAP_DialogueAS;
		default:
			return null;
		}
	}

	public static MAPDialogueAS getInstance(long[] oid) {
		long[] temp = MAP_DialogueAS.getOID();
		if (Arrays.equals(temp, oid)) {
			return MAP_DialogueAS;
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
