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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;

public class MAPExtensionContainerImpl extends MAPPrimitiveBase implements MAPExtensionContainer {

	protected static final int PRIVATEEXTENSIONLIST_REF_TAG = 0x00;
	protected static final int PSCEXTENSIONS_REF_TAG = 0x01;

	private ArrayList<MAPPrivateExtension> privateExtensionList;
	private byte[] pcsExtensions;

	public MAPExtensionContainerImpl() {
	}

	public MAPExtensionContainerImpl(ArrayList<MAPPrivateExtension> privateExtensionList, byte[] pcsExtensions) {
		this.privateExtensionList = privateExtensionList;
		this.pcsExtensions = pcsExtensions;
	}

	@Override
	public ArrayList<MAPPrivateExtension> getPrivateExtensionList() {
		return this.privateExtensionList;
	}

	@Override
	public void setPrivateExtensionList(ArrayList<MAPPrivateExtension> privateExtensionList) {
		this.privateExtensionList = privateExtensionList;
	}

	@Override
	public byte[] getPcsExtensions() {
		return this.pcsExtensions;
	}

	@Override
	public void setPcsExtensions(byte[] pcsExtensions) {
		this.pcsExtensions = pcsExtensions;
	}


	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	public void decode(AsnInputStream lis, int tagClass, boolean isPrimitive, int masterTag, int length) throws MAPParsingComponentException {

		// Definitioon from GSM 09.02 version 5.15.1 Page 690
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
		// ... } OPTIONAL,
		// ... }


		int tag;
		Boolean privateExtensionListHasDecoded = false;

		if (length == 0)
			return;
		
		try {
//			byte[] buf = new byte[length];
//			if (localAis.read(buf) != length)
//				throw new MAPParsingComponentException("Not enouph data for reading the ExtensionContainer",
//						MAPParsingComponentExceptionReason.MistypedParameter);
//			AsnInputStream lis = new AsnInputStream(new ByteArrayInputStream(buf));
			
			while (lis.available() > 0) {
				tag = lis.readTag();
				if (tag == MAPExtensionContainerImpl.PRIVATEEXTENSIONLIST_REF_TAG && lis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					if (privateExtensionListHasDecoded)
						throw new MAPParsingComponentException("More than one PrivateExtensionList has found when ExtensionContainer decoding",
								MAPParsingComponentExceptionReason.MistypedParameter);

					byte[] seqData2 = lis.readSequence();
					ByteArrayInputStream localIS2 = new ByteArrayInputStream(seqData2);
					AsnInputStream localAis2 = new AsnInputStream(localIS2);
					this.privateExtensionList = new ArrayList<MAPPrivateExtension>();

					tag = localAis2.readTag();
					if (tag != Tag.SEQUENCE || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL)
						throw new MAPParsingComponentException("Bad TAG when PrivateExtensionList decoding - 1",
								MAPParsingComponentExceptionReason.MistypedParameter);

					byte[] seqData3 = localAis2.readSequence();
					ByteArrayInputStream localIS3 = new ByteArrayInputStream(seqData3);
					AsnInputStream localAis3 = new AsnInputStream(localIS3);

					while (localAis3.available() > 0) {
						tag = localAis3.readTag();
						if (tag != Tag.SEQUENCE || localAis2.getTagClass() != Tag.CLASS_UNIVERSAL)
							throw new MAPParsingComponentException("Bad TAG when PrivateExtensionList decoding - 2",
									MAPParsingComponentExceptionReason.MistypedParameter);
						if (this.privateExtensionList.size() >= 10)
							throw new MAPParsingComponentException("More then 10 PrivateExtension found when PrivateExtensionList decoding",
									MAPParsingComponentExceptionReason.MistypedParameter);

						MAPPrivateExtensionImpl privateExtension = new MAPPrivateExtensionImpl();
						privateExtension.decode(localAis3);
						this.privateExtensionList.add(privateExtension);
					}

					privateExtensionListHasDecoded = true;
				} else if (tag == MAPExtensionContainerImpl.PSCEXTENSIONS_REF_TAG && lis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					// this.pcsExtensions = new byte[localIS.available()];
					this.pcsExtensions = lis.readSequence();

					// pcs-Extensions block has found - finish decoding
					break;
				} else {
					// other block has found - finish decoding
					break;
				}
			}
		} catch (MAPException e) {
			throw new MAPParsingComponentException("MAPException when decoding ExtensionContainer: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ExtensionContainer: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ExtensionContainer: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}
	
	public void encode(AsnOutputStream asnOS) throws MAPException {
		if (this.privateExtensionList == null && this.pcsExtensions == null)
			throw new MAPException(
					"Both PrivateExtensionList and PcsExtensions are empty when ExtensionContainer encoding");
		if (this.privateExtensionList != null
				&& (this.privateExtensionList.size() == 0 || this.privateExtensionList.size() > 10))
			throw new MAPException(
					"PrivateExtensionList must contains from 1 to 10 elements when ExtensionContainer encoding");

		AsnOutputStream localAos = new AsnOutputStream();

		try {
			byte[] data1 = null;
			if (this.privateExtensionList != null) {
				ArrayList<byte[]> lstPrivateExtention = new ArrayList<byte[]>();

				int wholeLen = 0;
				for (MAPPrivateExtension pe : this.privateExtensionList) {
					localAos.reset();
					((MAPPrivateExtensionImpl) pe).encode(localAos);
					byte[] byteBuf = localAos.toByteArray();
					wholeLen += byteBuf.length;
					lstPrivateExtention.add(byteBuf);
				}

				localAos.reset();
				localAos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
				localAos.writeLength(wholeLen);
				for (byte[] byteBuf : lstPrivateExtention) {
					localAos.write(byteBuf);
				}

				data1 = localAos.toByteArray();
			}

			if (data1 != null) {
				asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAPExtensionContainerImpl.PRIVATEEXTENSIONLIST_REF_TAG);
				asnOS.writeLength(data1.length);
				asnOS.write(data1);
			}
			if (this.pcsExtensions != null) {
				asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAPExtensionContainerImpl.PSCEXTENSIONS_REF_TAG);
				asnOS.writeLength(this.pcsExtensions.length);
				asnOS.write(this.pcsExtensions);
			}
		} catch (IOException e) {
			throw new MAPException("IOException when encoding ExtensionContainer: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding ExtensionContainer: " + e.getMessage(), e);
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ExtensionContainer [");
		
		if (this.privateExtensionList != null && this.privateExtensionList.size() > 0) {
			for(MAPPrivateExtension pe : this.privateExtensionList) {
				sb.append("\n");
				sb.append(pe.toString());
			}
		}
		
		if (this.pcsExtensions != null) {
			sb.append("\nPcsExtensions=");
			sb.append(this.ArrayToString(this.pcsExtensions));
		}

		sb.append("]");

		return sb.toString();
	}
	
	private String ArrayToString(byte[] array) {
		StringBuilder sb = new StringBuilder();
		int i1 = 0;
		for (byte b : array) {
			if (i1 == 0)
				i1 = 1;
			else
				sb.append(", ");
			sb.append(b);
		}
		return sb.toString();
	}
}
