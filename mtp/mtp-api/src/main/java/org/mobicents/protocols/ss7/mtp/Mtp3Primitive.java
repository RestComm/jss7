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
package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 * 
 */
public abstract class Mtp3Primitive {

	// SI flag is 0
	public static final int SERVICE_INDICATOR = 0;

	// Type definition
	public static final int PAUSE = 3;
	public static final int RESUME = 4;
	public static final int STATUS = 5;

	protected int type;
	protected int affectedDpc;
	
	public Mtp3Primitive(){
		
	}

	/**
	 * 
	 */
	public Mtp3Primitive(int type, int affectedDpc) {
		this.type = type;
		this.affectedDpc = affectedDpc;
	}

	public int getAffectedDpc() {
		return affectedDpc;
	}

	public int getType() {
		return type;
	}

	public abstract byte[] getValue();

}
