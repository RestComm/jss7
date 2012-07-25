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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.ASCICallReference;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;

/**
 * 

MAP V3:

prepareGroupCall  OPERATION ::= {				--Timer m
	ARGUMENT
		PrepareGroupCallArg
	RESULT
		PrepareGroupCallRes
	ERRORS {
		systemFailure |
		noGroupCallNumberAvailable |
		unexpectedDataValue}
	CODE	local:39 }

PrepareGroupCallArg ::= SEQUENCE {
	teleservice	Ext-TeleserviceCode,
	asciCallReference	ASCI-CallReference,
	codec-Info	CODEC-Info,
	cipheringAlgorithm	CipheringAlgorithm,
	groupKeyNumber-Vk-Id	[0] GroupKeyNumber	OPTIONAL,
	groupKey		[1] Kc		OPTIONAL, 
	-- this parameter shall not be sent and shall be discarded if received
	priority		[2] EMLPP-Priority	OPTIONAL,
	uplinkFree	[3] NULL		OPTIONAL,
	extensionContainer	[4] ExtensionContainer	OPTIONAL,
	...,
	vstk			[5] VSTK		OPTIONAL,
	vstk-rand		[6] VSTK-RAND	OPTIONAL,
	talkerChannelParameter	[7] NULL		OPTIONAL,
	uplinkReplyIndicator	[8] NULL		OPTIONAL}

GroupKeyNumber ::= INTEGER (0..15)

Kc ::= OCTET STRING (SIZE (8))

VSTK ::= OCTET STRING (SIZE (16))

VSTK-RAND ::= OCTET STRING (SIZE (5))
	-- The 36 bit value is carried in bit 7 of octet 1 to bit 4 of octet 5
	-- bits 3, 2, 1, and 0 of octet 5 are padded with zeros.

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PrepareGroupCallRequest extends CallHandlingMessage {

	public ExtTeleserviceCode getTeleservice();

	public ASCICallReference getAsciCallReference();

	public CODECInfo getCodecInfo();

	public CipheringAlgorithm getCipheringAlgorithm();

	public Integer getGroupKeyNumberVkId();

	public byte[] getGroupKey();

	public EMLPPPriority getPriority();

	public boolean getUplinkFree();

	public MAPExtensionContainer getExtensionContainer();

	public byte[] getVstk();

	public byte[] getVstkRand();

	public boolean getTalkerChannelParameter();

	public boolean getUplinkReplyIndicator();

}
