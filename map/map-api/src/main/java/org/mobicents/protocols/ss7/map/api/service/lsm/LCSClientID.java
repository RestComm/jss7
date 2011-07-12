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

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;

/**
 * LCS-ClientID ::= SEQUENCE {
 *    lcsClientType [0] LCSClientType,
 *    lcsClientExternalID [1] LCSClientExternalID OPTIONAL,
 *    lcsClientDialedByMS [2] AddressString OPTIONAL,
 *    lcsClientInternalID [3] LCSClientInternalID OPTIONAL,
 *    lcsClientName [4] LCSClientName OPTIONAL,
 *    ...,
 *    lcsAPN [5] APN OPTIONAL,
 *    lcsRequestorID [6] LCSRequestorID OPTIONAL }
 *    
 * @author amit bhayani
 *
 */
public interface LCSClientID {
	LCSClientType getLCSClientType();
	
	LCSClientExternalID getLCSClientExternalID();
	
	AddressString getLCSClientDialedByMS();
	
	LCSClientInternalID getLCSClientInternalID();
	
	LCSClientName getLCSClientName();
	
	/**
	 * APN ::= OCTET STRING (SIZE (2..63))
     *      -- Octets are coded according to TS 3GPP TS 23.003 [17]
	 * @return
	 */
	String getLCSAPN();
	
	LCSRequestorID getLCSRequestorID();
}
