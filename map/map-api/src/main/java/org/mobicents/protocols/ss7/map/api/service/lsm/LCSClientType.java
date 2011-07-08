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
package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * LCSClientType ::= ENUMERATED {
 *     emergencyServices (0),
 *     valueAddedServices (1),
 *     plmnOperatorServices (2),
 *     lawfulInterceptServices (3),
 *     ... }
 *  -- exception handling:
 *  -- unrecognized values may be ignored if the LCS client uses the privacy override
 *  -- otherwise, an unrecognized value shall be treated as unexpected data by a receiver
 *  -- a return error shall then be returned if received in a MAP invoke
 *  
 * @author amit bhayani
 *
 */
public enum LCSClientType {

	emergencyServices(0), valueAddedServices(1), plmnOperatorServices(2), lawfulInterceptServices(3);
	
	private final int type;
	
	private LCSClientType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public static LCSClientType getLCSClientType(int type){
		switch(type){
		case 0:
			return emergencyServices;
		case 1:
			return valueAddedServices;
		case 2:
			return plmnOperatorServices;
		case 3:
			return lawfulInterceptServices;
		default:
			return null;
		}
	}
}
