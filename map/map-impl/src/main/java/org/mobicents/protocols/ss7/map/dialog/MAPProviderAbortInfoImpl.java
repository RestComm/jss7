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
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPProviderAbortInfoImpl {

	public static final int MAP_PROVIDER_ABORT_INFO_TAG = 0x05;

	protected static final int PROVIDER_ABORT_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean PROVIDER_ABORT_TAG_PC_CONSTRUCTED = false;

	private MAPProviderAbortReason mapProviderAbortReason = null;
	private MAPExtensionContainer extensionContainer;

	public MAPProviderAbortInfoImpl() {
	}

	public MAPProviderAbortReason getMAPProviderAbortReason() {
		return this.mapProviderAbortReason;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
		this.mapProviderAbortReason = mapProvAbrtReas;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	public void decode(AsnInputStream ais) throws MAPParsingComponentException {
		// MAP-ProviderAbortInfo ::= SEQUENCE {
		// map-ProviderAbortReason ENUMERATED {
		// abnormalDialogue ( 0 ),
		// invalidPDU ( 1 ) },
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
		// ... } OPTIONAL}

		this.setMAPProviderAbortReason(null);
		this.setExtensionContainer(null);
		
		try {
			byte[] seqData = ais.readSequence();

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(seqData));

			int tag;

			int seqz = 0;
			while (localAis.available() > 0) {
				tag = localAis.readTag();
				if (seqz == 0) {
					// first element must be map-ProviderAbortReason
					if (tag != Tag.ENUMERATED)
						throw new MAPParsingComponentException(
								"The first element of MAP-ProviderAbortInfo must be map-ProviderAbortReason when decoding MAP-ProviderAbortInfo",
								MAPParsingComponentExceptionReason.MistypedParameter);
					int length = localAis.readLength();
					if (length != 1) {
						throw new MAPParsingComponentException("Expected length of MAPProviderAbortInfoImpl.MAPProviderAbortReason to be 1 but found " + length
								+ "  when decoding MAP-ProviderAbortInfo", MAPParsingComponentExceptionReason.MistypedParameter);
					}

					int code = localAis.read();
					this.mapProviderAbortReason = MAPProviderAbortReason.getInstance(code);
					if (this.mapProviderAbortReason == null)
						throw new MAPParsingComponentException("Bad map-ProviderAbortReason code received when decoding MAP-ProviderAbortInfo" + code,
								MAPParsingComponentExceptionReason.MistypedParameter);
				} else {
					if (tag == Tag.SEQUENCE) {
						this.extensionContainer = new MAPExtensionContainerImpl();
						byte[] buf = localAis.readSequence();
						AsnInputStream lis = new AsnInputStream(new ByteArrayInputStream(buf));
						((MAPExtensionContainerImpl) this.extensionContainer).decode(lis, localAis.getTagClass(), localAis.isTagPrimitive(), tag, buf.length);
					} else
						break;
				}

				seqz++;
			}
			if (this.getMAPProviderAbortReason() == null)
				throw new MAPParsingComponentException(
						"The first element of MAP-ProviderAbortInfo must be map-ProviderAbortReason when decoding MAP-ProviderAbortInfo",
						MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOS) throws MAPException {
		
		if (this.mapProviderAbortReason == null)
			throw new MAPException("MapProviderAbortReason parameter has not set - when encoding MAP-ProviderAbortInfo");

		byte[] reason = null;
		byte[] extContData = null;

		reason = new byte[3];
		reason[0] = Tag.ENUMERATED;
		reason[1] = 0x01;
		reason[2] = (byte) this.mapProviderAbortReason.getCode();

		AsnOutputStream localAos = new AsnOutputStream();
		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl) this.extensionContainer).encode(localAos);
			extContData = localAos.toByteArray();
		}
		
		try {
			localAos.reset();
			localAos.write(reason);

			if (extContData != null) {
				localAos.writeTag(Tag.CLASS_UNIVERSAL, PROVIDER_ABORT_TAG_PC_CONSTRUCTED, Tag.SEQUENCE);
				localAos.writeLength(extContData.length);
				localAos.write(extContData);
			}
			byte[] data = localAos.toByteArray();

			asnOS.writeTag(PROVIDER_ABORT_TAG_CLASS, PROVIDER_ABORT_TAG_PC_CONSTRUCTED, MAP_PROVIDER_ABORT_INFO_TAG);
			asnOS.writeLength(data.length);
			asnOS.write(data);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPProviderAbortInfo: " + e.getMessage(), e);
		}
	}

}
