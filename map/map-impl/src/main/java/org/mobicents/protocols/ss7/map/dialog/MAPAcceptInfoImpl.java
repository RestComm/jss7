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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * map-accept [1] IMPLICIT SEQUENCE {
 *   ... ,
 *  extensionContainer SEQUENCE {
 *     privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 ..
 *        SEQUENCE {
 *           extId      MAP-EXTENSION .&extensionId ( {
 *              ,
 *              ...} ) ,
 *           extType    MAP-EXTENSION .&ExtensionType ( {
 *              ,
 *              ...} { @extId   } ) OPTIONAL} OPTIONAL,
 *     pcs-Extensions [1] IMPLICIT SEQUENCE {
 *        ... } OPTIONAL,
 *     ... } OPTIONAL},
 *
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPAcceptInfoImpl {
	
	public static final int MAP_ACCEPT_INFO_TAG = 0x01;

	protected static final int ACCEPT_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean ACCEPT_INFO_TAG_PC_PRIMITIVE = true;
	protected static final boolean ACCEPT_INFO_TAG_PC_CONSTRUCTED = false;

	private MAPExtensionContainer extensionContainer;


	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	
	public void decode(AsnInputStream ais) throws MAPParsingComponentException {
		// MAP-AcceptInfo ::= SEQUENCE {
		// ... ,  
		// extensionContainer SEQUENCE { 
		//    privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 .. 10 ) OF 
		//       SEQUENCE { 
		//          extId      MAP-EXTENSION .&extensionId  ( { 
		//             ,  
		//             ...} ) ,  
		//          extType    MAP-EXTENSION .&ExtensionType  ( { 
		// ,  
        //  ...} { @extId   }  )  OPTIONAL} OPTIONAL,  
        //  pcs-Extensions [1] IMPLICIT SEQUENCE { 
        //     ... } OPTIONAL,  
        //  ... } OPTIONAL}

		this.setExtensionContainer(null);
		
		try {
			AsnInputStream localAis = ais.readSequenceStream();

			while (localAis.available() > 0) {
				int tag = localAis.readTag();

				switch (localAis.getTagClass()) {
				case Tag.CLASS_UNIVERSAL:
					switch (tag) {
					case Tag.SEQUENCE:
						this.extensionContainer = new MAPExtensionContainerImpl();
						this.extensionContainer.decodeAll(localAis);
						break;

					default:
						localAis.advanceElement();
						break;
					}
					break;

				default:
					localAis.advanceElement();
					break;
				}
			}
			
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPAcceptInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPAcceptInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	public void encode(AsnOutputStream asnOS) throws MAPException {

		try {
			asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAP_ACCEPT_INFO_TAG);
			int pos = asnOS.StartContentDefiniteLength();

			if (this.extensionContainer != null)
				this.extensionContainer.encodeAll(asnOS);
			
			asnOS.FinalizeContent(pos);
			
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPAcceptInfo: " + e.getMessage(), e);
		}
	}
}

