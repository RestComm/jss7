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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * 

EPS-SubscriptionData ::= SEQUENCE {
	apn-oi-Replacement	[0]	APN-OI-Replacement	OPTIONAL,
	-- this apn-oi-Replacement refers to the UE level apn-oi-Replacement.
	rfsp-id		[2]	RFSP-ID	OPTIONAL,
	ambr			[3]	AMBR		OPTIONAL,
	apn-ConfigurationProfile	[4]	APN-ConfigurationProfile	OPTIONAL,
	stn-sr		[6]	ISDN-AddressString	OPTIONAL,
	extensionContainer	[5]	ExtensionContainer	OPTIONAL,
	...,
	mps-CSPriority	[7]	NULL		OPTIONAL,
	mps-EPSPriority	[8]	NULL		OPTIONAL }
	-- mps-CSPriority by its presence indicates that the UE is subscribed to the eMLPP in 
	-- the CS domain, referring to the 3GPP TS 29.272 [144] for details.
	-- mps-EPSPriority by its presence indicates that the UE is subscribed to the MPS in 
	-- the EPS domain, referring to the 3GPP TS 29.272 [144] for details.

RFSP-ID ::=  INTEGER (1..256)

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface EPSSubscriptionData {

	public APNOIReplacement getApnOiReplacement();

	public Integer getRfspId();

	public AMBR getAmbr();

	public APNConfigurationProfile getAPNConfigurationProfile();

	public ISDNAddressString getStnSr();

	public MAPExtensionContainer getExtensionContainer();

	public boolean getMpsCSPriority();

	public boolean getMpsEPSPriority();

}
