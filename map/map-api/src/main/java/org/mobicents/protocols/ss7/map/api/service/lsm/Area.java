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
 * Area ::= SEQUENCE {
 *   areaType [0] AreaType,
 *   areaIdentification [1] AreaIdentification,
 *   ...}
 *    
 * @author amit bhayani
 *
 */
public interface Area {
	
	public AreaType getAreaType();
	
	/**
	 * AreaIdentification ::= OCTET STRING (SIZE (2..7))
     *      -- The internal structure is defined as follows:
     *      -- octet 1 bits 4321 Mobile Country Code 1st digit
     *      -- bits 8765 Mobile Country Code 2nd digit
     *      -- octet 2 bits 4321 Mobile Country Code 3rd digit
     *      -- bits 8765 Mobile Network Code 3rd digit if 3 digit MNC included
     *      -- or filler (1111)
     *      -- octet 3 bits 4321 Mobile Network Code 1st digit
     *      -- bits 8765 Mobile Network Code 2nd digit
     *      -- octets 4 and 5 Location Area Code (LAC) for Local Area Id,
     *      -- Routing Area Id and Cell Global Id
     *      -- octet 6 Routing Area Code (RAC) for Routing Area Id
     *      -- octets 6 and 7 Cell Identity (CI) for Cell Global Id
     *      -- octets 4 until 7 Utran Cell Identity (UC-Id) for Utran Cell Id
     *      
	 * @return
	 */
	public String getAreaIdentification();
	
}
