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
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;

/**
 * MAP-RefuseInfo ::= SEQUENCE {
 *   reason Reason,
 *   ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPRefuseInfoImpl {

	public static final int MAP_REFUSE_INFO_TAG = 0x03;

	protected static final int REFUSE_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean REFUSE_TAG_PC_CONSTRUCTED = false;
	protected static final boolean REFUSE_TAG_PC_PRIMITIVE = true;

	private Reason reason;
	private MAPExtensionContainer extensionContainer;
	private ApplicationContextName alternativeAcn;

	public Reason getReason() {
		return this.reason;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public ApplicationContextName getAlternativeAcn() {
		return this.alternativeAcn;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	public void setAlternativeAcn( ApplicationContextName alternativeAcn ) {
		this.alternativeAcn = alternativeAcn;
	}

	public void decode(AsnInputStream ais) throws MAPParsingComponentException {
		// MAP-RefuseInfo ::= SEQUENCE {
		//   reason                          ENUMERATED {
		//	      noReasonGiven                  ( 0 ), 
		//	      invalidDestinationReference    ( 1 ), 
		//	      invalidOriginatingReference    ( 2 ) }, 
		//	   ... , 
		//	   extensionContainer              SEQUENCE {
		//	      privateExtensionList   [0] IMPLICIT SEQUENCE  ( SIZE( 1 .. 10 ) ) OF
		//	         SEQUENCE {
		//	            extId      MAP-EXTENSION .&extensionId  ( {
		//	               , 
		//	               ...} ) , 
		//	            extType    MAP-EXTENSION .&ExtensionType  ( {
		//	               , 
		//	               ...} { @extId   }  )  OPTIONAL} OPTIONAL, 
		//	      pcs-Extensions         [1] IMPLICIT SEQUENCE {
		//	         ... } OPTIONAL, 
		//	      ... } OPTIONAL, 
		//	   alternativeApplicationContext   OBJECT IDENTIFIER OPTIONAL}

		this.reason = null;
		this.alternativeAcn = null;
		this.extensionContainer = null;

		try {
			AsnInputStream localAis = ais.readSequenceStream();

			int tag = localAis.readTag();
			if (tag != Tag.ENUMERATED || localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
				throw new MAPParsingComponentException("Error decoding MAP-RefuseInfo.Reason: bad tag or tagClass",
						MAPParsingComponentExceptionReason.MistypedParameter);				
			int reasonCode = (int) localAis.readInteger();
			this.reason = Reason.getReason(reasonCode);			

			while (localAis.available() > 0) {
				tag = localAis.readTag();

				switch( localAis.getTagClass() ) {
				case Tag.CLASS_UNIVERSAL:
					switch (tag) {
					case Tag.SEQUENCE:
						this.extensionContainer = new MAPExtensionContainerImpl();
						this.extensionContainer.decodeAll(localAis);
						break;

					case Tag.OBJECT_IDENTIFIER:
						this.alternativeAcn = new ApplicationContextNameImpl();
						long[] oid = localAis.readObjectIdentifier();
						this.alternativeAcn.setOid(oid);
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
			throw new MAPParsingComponentException("IOException when decoding MAPRefuseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPRefuseInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOS) throws MAPException {

		if (this.reason == null)
			throw new MAPException("Error decoding MAP-RefuseInfo: Reason field must not be empty");

		try {
			asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAP_REFUSE_INFO_TAG);
			int pos = asnOS.StartContentDefiniteLength();
			
			asnOS.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.reason.getCode());

			if (this.extensionContainer != null)
				this.extensionContainer.encodeAll(asnOS);
			if (this.alternativeAcn != null)
				asnOS.writeObjectIdentifier(this.alternativeAcn.getOid());
			
			asnOS.FinalizeContent(pos);

		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPRefuseInfo: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPRefuseInfo: " + e.getMessage(), e);
		}
	}

}
