/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import java.io.Serializable;

/**
 *
 * SupportedLCS-CapabilitySets ::= BIT STRING { lcsCapabilitySet1 (0), lcsCapabilitySet2 (1), lcsCapabilitySet3 (2),
 * lcsCapabilitySet4 (3) , lcsCapabilitySet5 (4) } (SIZE (2..16)) -- Core network signalling capability set1 indicates LCS
 * Release98 or Release99 version. -- Core network signalling capability set2 indicates LCS Release4. -- Core network signalling
 * capability set3 indicates LCS Release5. -- Core network signalling capability set4 indicates LCS Release6. -- Core network
 * signalling capability set5 indicates LCS Release7 or later version. -- A node shall mark in the BIT STRING all LCS capability
 * sets it supports. -- If no bit is set then the sending node does not support LCS. -- If the parameter is not sent by an VLR
 * then the VLR may support at most capability set1. -- If the parameter is not sent by an SGSN then no support for LCS is
 * assumed. -- An SGSN is not allowed to indicate support of capability set1. -- Other bits than listed above shall be
 * discarded.
 *
 * @author amit bhayani
 *
 */
public interface SupportedLCSCapabilitySets extends Serializable {

    boolean getCapabilitySetRelease98_99();

    boolean getCapabilitySetRelease4();

    boolean getCapabilitySetRelease5();

    boolean getCapabilitySetRelease6();

    boolean getCapabilitySetRelease7();

}
