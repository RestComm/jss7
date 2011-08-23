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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPOpenInfoImpl {

	public static final int MAP_OPEN_INFO_TAG = 0x00;

	protected static final int DESTINATION_REF_TAG = 0x00;
	protected static final int ORIGINATION_REF_TAG = 0x01;

	protected static final int OPEN_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean OPEN_INFO_TAG_PC_PRIMITIVE = true;
	protected static final boolean OPEN_INFO_TAG_PC_CONSTRUCTED = false;

	private AddressString destReference;
	private AddressString origReference;
	private MAPExtensionContainer extensionContainer;

	public AddressString getDestReference() {
		return this.destReference;
	}

	public AddressString getOrigReference() {
		return this.origReference;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public void setDestReference(AddressString destReference) {
		this.destReference = destReference;
	}

	public void setOrigReference(AddressString origReference) {
		this.origReference = origReference;

	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	public void decode(AsnInputStream ais) throws MAPParsingComponentException {

		// Definitioon from GSM 09.02 version 5.15.1 Page 690
		// map-open [0] IMPLICIT SEQUENCE {
		// destinationReference [0] IMPLICIT OCTET STRING ( SIZE( 1 .. 20 ) )
		// OPTIONAL,
		// originationReference [1] IMPLICIT OCTET STRING ( SIZE( 1 .. 20 ) )
		// OPTIONAL,
		// ... ,
		// extensionContainer SEQUENCE {
		// privateExtensionList [0] IMPLICIT SEQUENCE ( SIZE( 1 .. 10 ) ) OF
		// SEQUENCE {
		// extId MAP-EXTENSION .&extensionId ( {
		// ,
		// ...} ) ,
		// extType MAP-EXTENSION .&ExtensionType ( {
		// ,
		// ...} { @extId } ) OPTIONAL} OPTIONAL,
		// pcs-Extensions [1] IMPLICIT SEQUENCE {
		// ... } OPTIONAL,
		// ... } OPTIONAL},

		this.setDestReference(null);
		this.setOrigReference(null);
		this.setExtensionContainer(null);

		try {
			AsnInputStream localAis = ais.readSequenceStream();

			while (localAis.available() > 0) {
				int tag = localAis.readTag();

				switch (localAis.getTagClass()) {
				case Tag.CLASS_CONTEXT_SPECIFIC:
					switch (tag) {
					case DESTINATION_REF_TAG:
						this.destReference = new AddressStringImpl();
						this.destReference.decodeAll(localAis);
						break;

					case ORIGINATION_REF_TAG:
						this.origReference = new AddressStringImpl();
						this.origReference.decodeAll(localAis);
						break;

					default:
						localAis.advanceElement();
						break;
					}
					break;
					
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
			throw new MAPParsingComponentException("IOException when decoding MAPOpenInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPOpenInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOS) throws MAPException {

		try {
			asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAP_OPEN_INFO_TAG);
			int pos = asnOS.StartContentDefiniteLength();
			
			if (this.destReference != null)
				this.destReference.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, DESTINATION_REF_TAG);

			if (this.origReference != null)
				this.origReference.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, ORIGINATION_REF_TAG);

			if (this.extensionContainer != null)
				this.extensionContainer.encodeAll(asnOS);
			
			asnOS.FinalizeContent(pos);
			
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPOpenInfo: " + e.getMessage(), e);
		}

	}
}
