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
 * LCSClientInternalID ::= ENUMERATED {
 *    broadcastService (0),
 *    o-andM-HPLMN (1),
 *    o-andM-VPLMN (2),
 *    anonymousLocation (3),
 *    targetMSsubscribedService (4),
 *    ... }
 * -- for a CAMEL phase 3 PLMN operator client, the value targetMSsubscribedService shall be used
 * 
 * @author amit bhayani
 *
 */
public enum LCSClientInternalID {
	
	broadcastService(0), oandMHPLMN(1), oandMVPLMN(2), anonymousLocation(3), targetMSsubscribedService(4);
	
	private final int id;
	
	private LCSClientInternalID(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static LCSClientInternalID getLCSClientInternalID(int type){
		switch(type){
		case 0:
			return broadcastService;
		case 1:
			return oandMHPLMN;
		case 2:
			return oandMVPLMN;
		case 3:
			return anonymousLocation;
		case 4:
			return targetMSsubscribedService;
		default:
			return null;
		}
	}

}
