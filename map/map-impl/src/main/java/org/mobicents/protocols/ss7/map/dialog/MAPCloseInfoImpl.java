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
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPCloseInfoImpl implements MAPAsnPrimitive {
	
	public static final int MAP_CLOSE_INFO_TAG = 0x02;

	protected static final int CLOSE_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean CLOSE_INFO_TAG_PC_PRIMITIVE = true;
	protected static final boolean CLOSE_INFO_TAG_PC_CONSTRUCTED = false;

	private MAPExtensionContainer extensionContainer;


	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}


	@Override
	public int getTag() throws MAPException {
		return MAP_CLOSE_INFO_TAG;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
		// MAP-CloseInfo ::= SEQUENCE { 
		//   ... ,  
		//   extensionContainer SEQUENCE { 
		//      privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 .. 10 ) OF 
		//         SEQUENCE { 
		//            extId      MAP-EXTENSION .&extensionId  ( { 
		//               ,  
		//               ...} ) ,  
		//            extType    MAP-EXTENSION .&ExtensionType  ( { 
		//               ,  
		//               ...} { @extId   }  )  OPTIONAL} OPTIONAL,  
		//      pcs-Extensions [1] IMPLICIT SEQUENCE { 
		//         ... } OPTIONAL,  
		//      ... } OPTIONAL} 
		
		this.setExtensionContainer(null);

		AsnInputStream localAis = ais.readSequenceStreamData(length);

		while (localAis.available() > 0) {
			int tag = localAis.readTag();

			switch (localAis.getTagClass()) {
			case Tag.CLASS_UNIVERSAL:
				switch (tag) {
				case Tag.SEQUENCE:
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(localAis);
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
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_CLOSE_INFO_TAG);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPCloseInfo: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOS) throws MAPException {

		if (this.extensionContainer != null)
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOS);
	}
}


