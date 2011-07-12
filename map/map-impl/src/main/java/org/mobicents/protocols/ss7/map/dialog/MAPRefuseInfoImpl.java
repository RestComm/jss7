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
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;

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

		this.setReason(null);
		this.setExtensionContainer(null);
		this.setAlternativeAcn(null);

		try {
			byte[] seqData = ais.readSequence();

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(seqData));

			int tag;

			int seqz = 0;
			while (localAis.available() > 0) {
				tag = localAis.readTag();

				if (seqz == 0) {
					if (tag == Tag.ENUMERATED) {
						int length = localAis.readLength();
						if (length != 1) {
							throw new MAPParsingComponentException("Expected length of MAP-RefuseInfo.Reason to be 1 but found " + length,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}

						int reasonCode = localAis.read();

						this.reason = Reason.getReason(reasonCode);
					} else {
						throw new MAPParsingComponentException("The first element of MAP-RefuseInfo must be the Reason when decoding MAP-RefuseInfo",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
				} else {

					Boolean badTag = false;
					switch (tag) {
					case Tag.SEQUENCE:
						this.extensionContainer = new MAPExtensionContainerImpl();
						byte[] buf = localAis.readSequence();
						AsnInputStream lis = new AsnInputStream(new ByteArrayInputStream(buf));
						((MAPExtensionContainerImpl) this.extensionContainer).decode(lis, localAis.getTagClass(), localAis.isTagPrimitive(), tag, buf.length);
						break;

					case Tag.OBJECT_IDENTIFIER:
						this.alternativeAcn = new ApplicationContextNameImpl();
						this.alternativeAcn.setOid(localAis.readObjectIdentifier());
						break;

					default:
						badTag = true;
						break;
					}

					if (badTag)
						break;
				}

				seqz++;
			}

			if (this.getReason() == null)
				throw new MAPParsingComponentException("The first element of MAP-RefuseInfo must be the Reason when decoding MAP-RefuseInfo",
						MAPParsingComponentExceptionReason.MistypedParameter);
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
			throw new MAPException("Reason field must not be empty");

		try {
			AsnOutputStream localAos = new AsnOutputStream();

			byte[] reason = null;
			byte[] extContData = null;
			byte[] altAcnData = null;

			reason = new byte[1];
			reason[0] = (byte) this.reason.getCode();

			if (this.extensionContainer != null) {
				localAos.reset();
				((MAPExtensionContainerImpl) this.extensionContainer).encode(localAos);
				extContData = localAos.toByteArray();
			}

			if (this.alternativeAcn != null) {
				localAos.reset();
				localAos.writeObjectIdentifier(this.alternativeAcn.getOid());
				altAcnData = localAos.toByteArray();
			}

			localAos.reset();

			localAos.writeTag(Tag.CLASS_UNIVERSAL, REFUSE_TAG_PC_PRIMITIVE, Tag.ENUMERATED);
			localAos.writeLength(reason.length);
			localAos.write(reason);

			if (extContData != null) {
				localAos.writeTag(Tag.CLASS_UNIVERSAL, REFUSE_TAG_PC_CONSTRUCTED, Tag.SEQUENCE);
				localAos.writeLength(extContData.length);
				localAos.write(extContData);
			}

			if (altAcnData != null) {
				// localAos.writeTag(Tag.CLASS_UNIVERSAL,
				// REFUSE_TAG_PC_CONSTRUCTED, Tag.OBJECT_IDENTIFIER);
				// localAos.writeLength(altAcnData.length);
				localAos.write(altAcnData);
			}

			byte[] data = localAos.toByteArray();

			asnOS.writeTag(REFUSE_TAG_CLASS, REFUSE_TAG_PC_CONSTRUCTED, MAP_REFUSE_INFO_TAG);
			asnOS.writeLength(data.length);
			asnOS.write(data);

		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPRefuseInfo: " + e.getMessage(), e);
		}
	}

}
