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

package org.mobicents.protocols.ss7.map.api.service.mobility.handover;

import java.util.ArrayList;
import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V3:
PrepareHO-Res ::= [3] SEQUENCE {
	handoverNumber	[0] ISDN-AddressString	OPTIONAL,
	relocationNumberList	[1]	RelocationNumberList	OPTIONAL,
	an-APDU		[2]	AccessNetworkSignalInfo	OPTIONAL,
	multicallBearerInfo	[3]	MulticallBearerInfo	OPTIONAL,
	multipleBearerNotSupported	NULL			OPTIONAL,
	selectedUMTS-Algorithms	[5]	SelectedUMTS-Algorithms	OPTIONAL,
	chosenRadioResourceInformation	[6] ChosenRadioResourceInformation	 OPTIONAL,
	extensionContainer	[4]	ExtensionContainer	OPTIONAL,
	...,
	iuSelectedCodec	[7] Codec		OPTIONAL,
	iuAvailableCodecsList	[8] CodecList	OPTIONAL,
	aoipSelectedCodecTarget	[9] AoIPCodec	OPTIONAL,
	aoipAvailableCodecsListMap	[10] AoIPCodecsList	OPTIONAL }

MAP V2:
PrepareHO-Res ::= SEQUENCE {
	handoverNumber 	ISDN-AddressString OPTIONAL,
	bss-APDU 		ExternalSignalInfo OPTIONAL,
	...}

RelocationNumberList ::= SEQUENCE SIZE (1..7) OF RelocationNumber

MulticallBearerInfo ::= INTEGER (1..7)

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PrepareHandoverResponse extends MobilityMessage {

	public ISDNAddressString getHandoverNumber();

	public ArrayList<RelocationNumber> getRelocationNumberList();

	public AccessNetworkSignalInfo getAnAPDU();

	public Integer getMulticallBearerInfo();

	public boolean getMultipleBearerNotSupported();

	public SelectedUMTSAlgorithms getSelectedUMTSAlgorithms();

	public ChosenRadioResourceInformation getChosenRadioResourceInformation();

	public MAPExtensionContainer getExtensionContainer();

	public Codec getIuSelectedCodec();

	public CodecList getIuAvailableCodecsList();

	public AoIPCodec getAoipSelectedCodecTarget();

	public AoIPCodecsList getAoipAvailableCodecsListMap();

	// this parameter is for MAP V2 only
	public ExternalSignalInfo getBssAPDU();

}
