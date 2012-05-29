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
package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import java.io.Serializable;

/**
 * 
 *	SupportedLCS-CapabilitySets ::= BIT STRING {
 *		lcsCapabilitySet1 (0),
 *		lcsCapabilitySet2 (1),
 *		lcsCapabilitySet3 (2),
 *		lcsCapabilitySet4 (3) ,
 *		lcsCapabilitySet5 (4) } (SIZE (2..16)) 
 *	-- Core network signalling capability set1 indicates LCS Release98 or Release99 version.
 *	-- Core network signalling capability set2 indicates LCS Release4.
 *	-- Core network signalling capability set3 indicates LCS Release5.
 *	-- Core network signalling capability set4 indicates LCS Release6.
 *	-- Core network signalling capability set5 indicates LCS Release7 or later version.
 *	-- A node shall mark in the BIT STRING all LCS capability sets it supports. 
 *	-- If no bit is set then the sending node does not support LCS.
 *	-- If the parameter is not sent by an VLR then the VLR may support at most capability set1.
 *	-- If the parameter is not sent by an SGSN then no support for LCS is assumed.
 *	-- An SGSN is not allowed to indicate support of capability set1.
 *	-- Other bits than listed above shall be discarded.
 *
 * @author amit bhayani
 *
 */
public interface SupportedLCSCapabilitySets extends Serializable {

	public boolean getCapabilitySetRelease98_99();

	public boolean getCapabilitySetRelease4();

	public boolean getCapabilitySetRelease5();

	public boolean getCapabilitySetRelease6();

	public boolean getCapabilitySetRelease7();

}
