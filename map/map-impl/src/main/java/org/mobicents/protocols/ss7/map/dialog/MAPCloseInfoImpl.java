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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPCloseInfoImpl {
	
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

	
	public void decode(AsnInputStream ais) throws MAPParsingComponentException {
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

		try {
			byte[] seqData = ais.readSequence();

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(seqData));

			int tag;

			while (localAis.available() > 0) {
				tag = localAis.readTag();
				if (tag == Tag.SEQUENCE) {
					this.extensionContainer = new MAPExtensionContainerImpl();
					byte[] buf = localAis.readSequence();
					AsnInputStream lis = new AsnInputStream(new ByteArrayInputStream(buf));
					((MAPExtensionContainerImpl) this.extensionContainer).decode(lis, localAis.getTagClass(), localAis.isTagPrimitive(), tag, buf.length);

				} else
					break;
			}
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPCloseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	public void encode(AsnOutputStream asnOS) throws MAPException {

		try {
			AsnOutputStream localAos = new AsnOutputStream();

			byte[] extContData = null;

			if (this.extensionContainer != null) {
				localAos.reset();
				((MAPExtensionContainerImpl) this.extensionContainer).encode(localAos);
				extContData = localAos.toByteArray();
			}

			localAos.reset();

			if (extContData != null) {
				localAos.writeTag(Tag.CLASS_UNIVERSAL, CLOSE_INFO_TAG_PC_CONSTRUCTED, Tag.SEQUENCE);
				localAos.writeLength(extContData.length);
				localAos.write(extContData);
			}

			byte[] data = localAos.toByteArray();

			// Now let us write the MAP OPEN-INFO Tags
			asnOS.writeTag(CLOSE_INFO_TAG_CLASS, CLOSE_INFO_TAG_PC_CONSTRUCTED, MAP_CLOSE_INFO_TAG);
			asnOS.writeLength(data.length);
			asnOS.write(data);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPCloseInfo: " + e.getMessage(), e);
		}
	}
}


