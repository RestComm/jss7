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
 * TerminationCause ::= ENUMERATED {
 *		normal (0),
 *		errorundefined (1),
 *		internalTimeout (2),
 *		congestion (3),
 *		mt-lrRestart (4),
 *		privacyViolation (5),
 *		...,
 *		shapeOfLocationEstimateNotSupported (6) }
 *	-- mt-lrRestart shall be used to trigger the GMLC to restart the location procedure,
 *	-- either because the sending node knows that the terminal has moved under coverage
 *	-- of another MSC or SGSN (e.g. Send Identification received), or because the subscriber
 *	-- has been deregistered due to a Cancel Location received from HLR.
 *	--
 *	-- exception handling
 *	-- an unrecognized value shall be treated the same as value 1 (errorundefined)
 * 
 * @author amit bhayani
 *
 */
public enum TerminationCause {
	
	normal (0),
	errorundefined (1),
	internalTimeout (2),
	congestion (3),
	mtlrRestart (4),
	privacyViolation (5);

	private final int cause;
	
	private TerminationCause(int cause){
		this.cause = cause;
	}
	
	public int getCause(){
		return this.cause;
	}
	
	public static TerminationCause getTerminationCause(int cause){
		switch(cause){
		case 0:
			return normal;
		case 1:
			return errorundefined;
		case 2:
			return internalTimeout;
		case 3:
			return congestion;
		case 4:
			return mtlrRestart;
		case 5:
			return privacyViolation;
		default:
			return null;
		}
	}
}
