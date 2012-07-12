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

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 

MAP V3:
ProcessAccessSignalling-Arg ::= [3] SEQUENCE {
	an-APDU		AccessNetworkSignalInfo,
	selectedUMTS-Algorithms	[1]	SelectedUMTS-Algorithms	OPTIONAL,
	selectedGSM-Algorithm	[2]	SelectedGSM-Algorithm	OPTIONAL,
	chosenRadioResourceInformation	[3] ChosenRadioResourceInformation OPTIONAL,
	selectedRab-Id	[4] RAB-Id	OPTIONAL,
	extensionContainer	[0]	ExtensionContainer 	OPTIONAL,
	...,
	iUSelectedCodec	[5] Codec		OPTIONAL,
	iuAvailableCodecsList	[6] CodecList	OPTIONAL,
	aoipSelectedCodecTarget	[7] AoIPCodec	OPTIONAL,
	aoipAvailableCodecsListMap	[8] AoIPCodecsList	OPTIONAL }

MAP V2:
ProcessAccessSignalling ::= OPERATION--Timer s
ARGUMENT
bss-APDU	ExternalSignalInfo

RAB-Id ::= INTEGER (1..255)

 * 
 * @author sergey vetyutnev
 * 
 */
public interface ProcessAccessSignallingRequest extends MobilityMessage {

	public AccessNetworkSignalInfo getAn_APDU();

	public SelectedUMTSAlgorithms getSelectedUMTSAlgorithms();

	public SelectedGSMAlgorithm getSelectedGSMAlgorithm();

	public ChosenRadioResourceInformation getChosenRadioResourceInformation();

	public Integer getSelectedRabId();

	public MAPExtensionContainer getExtensionContainer();

	public Codec getIUSelectedCodec();

	public CodecList getIuAvailableCodecsList();

	public AoIPCodec getAoipSelectedCodecTarget();

	public AoIPCodecsList getAoipAvailableCodecsListMap();

	public ExternalSignalInfo getBssAPDU();

}
