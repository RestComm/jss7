/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl.mgmt;

/**
 * @author baranowb
 * 
 */
public enum Mtp3PrimitiveMessageType {
	
	MTP3_PAUSE(3),MTP3_RESUME(4),MTP3_STATUS(5);
	
	Mtp3PrimitiveMessageType(int x) {
		this.t = x;
	}

	private int t;

	public int getType() {
		return t;
	}

	public static final Mtp3PrimitiveMessageType fromInt(int v) {
		switch (v) {
		case 3:
			return MTP3_PAUSE;
		case 4:
			return MTP3_RESUME;
		case 5:
			return MTP3_STATUS;
		
		default:
			return null;

		}
	}
}
