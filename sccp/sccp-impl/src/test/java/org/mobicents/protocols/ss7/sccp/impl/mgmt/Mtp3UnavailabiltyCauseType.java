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
public enum Mtp3UnavailabiltyCauseType {
	
	CAUSE_UNKNOWN(0),CAUSE_UNEQUIPED(1),CAUSE_INACCESSIBLE(2);
	
	Mtp3UnavailabiltyCauseType(int x) {
		this.t = x;
	}

	private int t;

	public int getType() {
		return t;
	}

	public static final Mtp3UnavailabiltyCauseType fromInt(int v) {
		switch (v) {
		case 0:
			return CAUSE_UNKNOWN;
		case 1:
			return CAUSE_UNEQUIPED;
		case 2:
			return CAUSE_INACCESSIBLE;
		
		default:
			return null;

		}
	}
}
