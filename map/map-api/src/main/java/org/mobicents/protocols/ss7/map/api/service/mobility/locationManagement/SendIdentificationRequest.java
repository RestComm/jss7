/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.TMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V3:
SendIdentificationArg ::= SEQUENCE {
	tmsi			TMSI,
	numberOfRequestedVectors	NumberOfRequestedVectors 	OPTIONAL,
	-- within a dialogue numberOfRequestedVectors shall be present in 
	-- the first service request and shall not be present in subsequent service requests. 
	-- If received in a subsequent service request it shall be discarded. 
	segmentationProhibited	NULL			OPTIONAL,
	extensionContainer	ExtensionContainer	OPTIONAL,
	...,
	msc-Number	ISDN-AddressString 	OPTIONAL,
	previous-LAI	[0] LAIFixedLength	OPTIONAL,
	hopCounter	[1] HopCounter	OPTIONAL,
	mtRoamingForwardingSupported	[2] NULL		OPTIONAL,
	newVLR-Number	[3] ISDN-AddressString	OPTIONAL,
	new-lmsi		[4] LMSI		OPTIONAL }

MAP V2:
tmsi	TMSI

NumberOfRequestedVectors ::= INTEGER (1..5)
HopCounter ::= INTEGER (0..3)

 * 
 * @author sergey vetyutnev
 * 
 */
public interface SendIdentificationRequest extends MobilityMessage {

	public TMSI getTmsi();

	public Integer getNumberOfRequestedVectors();

	public boolean getSegmentationProhibited();

	public MAPExtensionContainer getExtensionContainer();

	public ISDNAddressString getMscNumber();

	public LAIFixedLength getPreviousLAI();

	public Integer getHopCounter();

	public boolean getMtRoamingForwardingSupported();

	public ISDNAddressString getNewVLRNumber();

	public LMSI getLmsi();

}

