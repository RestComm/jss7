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

import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * LCSCodeword ::= SEQUENCE {
 *   dataCodingScheme [0] USSD-DataCodingScheme,
 *   lcsCodewordString [1] LCSCodewordString,
 *   ...}
 * 
 * @author amit bhayani
 *
 */
public interface LCSCodeword extends MAPPrimitive {
	
	/**
	 * USSD-DataCodingScheme ::= OCTET STRING (SIZE (1))
	 *		-- The structure of the USSD-DataCodingScheme is defined by
	 *		-- the Cell Broadcast Data Coding Scheme as described in
	 *		-- TS 3GPP TS 23.038 [25]
	 *
	 * @return
	 */
	public byte getDataCodingScheme();
	
	/**
	 * LCSCodewordString ::= USSD-String (SIZE (1..maxLCSCodewordStringLength))
	 * 
	 * maxLCSCodewordStringLength INTEGER ::= 20
	 * 
	 * @return
	 */
	public USSDString getLCSCodewordString();
}
