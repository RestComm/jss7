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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

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
public interface LCSQoS extends Serializable {
	
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
	public boolean getVerticalCoordinateRequest();
	
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
