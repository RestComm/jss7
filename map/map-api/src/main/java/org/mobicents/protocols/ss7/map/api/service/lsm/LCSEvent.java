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
 * LCS-Event ::= ENUMERATED {
 *		emergencyCallOrigination (0),
 *		emergencyCallRelease (1),
 *		mo-lr (2),
 *		...,
 *		deferredmt-lrResponse (3) }
 *		-- exception handling:
 *		-- a SubscriberLocationReport-Arg containing an unrecognized LCS-Event
 *		-- shall be rejected by a receiver with a return error cause of unexpected data value
 * 
 * @author amit bhayani
 *
 */
public enum LCSEvent {

	emergencyCallOrigination(0), emergencyCallRelease(1), molr(2), deferredmtlrResponse(3);
	
	private final int event;
	
	
	private LCSEvent(int event){
		this.event = event;
	}
	
	public int getEvent(){
		return this.event;
	}
	
	public static LCSEvent getLCSEvent(int event){
		switch(event){
		case 0:
			return emergencyCallOrigination;
		case 1:
			return emergencyCallRelease;
		case 2:
			return molr;
		case 3:
			return deferredmtlrResponse;
		default:
			return null;
		}
	}
}
