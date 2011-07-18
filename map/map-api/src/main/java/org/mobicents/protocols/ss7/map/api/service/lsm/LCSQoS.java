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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;

/**
 * 
 * LCS-QoS ::= SEQUENCE {
 *   horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL,
 *   verticalCoordinateRequest [1] NULL OPTIONAL,
 *   vertical-accuracy [2] Vertical-Accuracy OPTIONAL,
 *   responseTime [3] ResponseTime OPTIONAL,
 *   extensionContainer [4] ExtensionContainer OPTIONAL,
 *   ...}
 *
 * @author amit bhayani
 *
 */
public interface LCSQoS extends MAPPrimitive {
	
	/**
	 * Horizontal-Accuracy ::= OCTET STRING (SIZE (1))
     *          -- bit 8 = 0
     *          -- bits 7-1 = 7 bit Uncertainty Code defined in 3GPP TS 23.032. The horizontal location
     *          -- error should be less than the error indicated by the uncertainty code with 67%
     *          -- confidence.
	 * @return
	 */
	public Integer getHorizontalAccuracy();
	
	/**
	 * NULL
	 * @return
	 */
	public Boolean getVerticalCoordinateRequest();
	
	/**
	 * Vertical-Accuracy ::= OCTET STRING (SIZE (1))
     *     -- bit 8 = 0
     *     -- bits 7-1 = 7 bit Vertical Uncertainty Code defined in 3GPP TS 23.032.
     *     -- The vertical location error should be less than the error indicated
     *     -- by the uncertainty code with 67% confidence.
     *
	 * @return
	 */
	public Integer getVerticalAccuracy();
	
	public ResponseTime getResponseTime();
	
	public MAPExtensionContainer getExtensionContainer();
}
