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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * AreaType ::= ENUMERATED {
 *  countryCode (0),
 *  plmnId (1),
 *  locationAreaId (2),
 *  routingAreaId (3),
 *  cellGlobalId (4),
 *  ... ,
 *  utranCellId (5) }
 * 
 * @author amit bhayani
 *
 */
public enum AreaType {
	
	countryCode(0), plmnId(1), locationAreaId(2), routingAreaId(3), cellGlobalId(4), utranCellId(5);
	
	private final int type;
	
	private AreaType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public static AreaType getAreaType(int type){
		switch(type){
		case 0:
			return countryCode;
		case 1:
			return plmnId;
		case 2:
			return locationAreaId;
		case 3:
			return routingAreaId;
		case 4:
			return cellGlobalId;
		case 5:
			return utranCellId;			
		default:
			return null;
		}
	}
}
