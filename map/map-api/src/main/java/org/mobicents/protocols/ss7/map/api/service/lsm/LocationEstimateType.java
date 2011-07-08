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
 * 
 * LocationEstimateType ::= ENUMERATED {
 *    currentLocation (0),
 *    currentOrLastKnownLocation (1),
 *    initialLocation (2),
 *    ...,
 *    activateDeferredLocation (3),
 *    cancelDeferredLocation (4) }
 * -- exception handling:
 * -- a ProvideSubscriberLocation-Arg containing an unrecognized LocationEstimateType
 * -- shall be rejected by the receiver with a return error cause of unexpected data value
 *
 * @author amit bhayani
 * 
 */
public enum LocationEstimateType {

	currentLocation(0), currentOrLastKnownLocation(1), initialLocation(2), activateDeferredLocation(3), cancelDeferredLocation(4);

	private final int type;

	private LocationEstimateType(int type) {
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public static LocationEstimateType getLocationEstimateType(int type){
		switch(type){
		case 0:
			return currentLocation;
		case 1:
			return currentOrLastKnownLocation;
		case 2:
			return initialLocation;
		case 3:
			return activateDeferredLocation;
		case 4:
			return cancelDeferredLocation;
		default:
			return null;
		}
	}
}
