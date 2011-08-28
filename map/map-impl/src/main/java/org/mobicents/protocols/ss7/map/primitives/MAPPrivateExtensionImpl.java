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

/*
 JBoss, Home of Professional Open Source
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

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;

/**
 * @author sergey vetyutnev
 * 
 */
public class MAPPrivateExtensionImpl implements MAPPrivateExtension, MAPAsnPrimitive {

	private long[] oId;
	private byte[] data;

	public MAPPrivateExtensionImpl() {
	}

	public MAPPrivateExtensionImpl(long[] oId, byte[] data) {
		this.oId = oId;
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension#getOId()
	 */
	@Override
	public long[] getOId() {
		return this.oId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension#setOId
	 * (long[])
	 */
	@Override
	public void setOId(long[] oId) {
		this.oId = oId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension#getData()
	 */
	@Override
	public byte[] getData() {
		return this.data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension#setData
	 * (byte[])
	 */
	@Override
	public void setData(byte[] data) {
		this.data = data;
	}


	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		// Definition from GSM 09.02 version 5.15.1 Page 690
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

		try {
			AsnInputStream ais = ansIS.readSequenceStream();
			this._decode(ais);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding PrivateExtension: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding PrivateExtension: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		
		try {
			AsnInputStream ais = ansIS.readSequenceStreamData(length);
			this._decode(ais);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding PrivateExtension: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding PrivateExtension: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS) throws MAPParsingComponentException, IOException, AsnException {

		// extId
		int tag = ansIS.readTag();
		if (tag != Tag.OBJECT_IDENTIFIER || ansIS.getTagClass() != Tag.CLASS_UNIVERSAL || !ansIS.isTagPrimitive())
			throw new MAPParsingComponentException("Error decoding PrivateExtension: bad tag, tagClass or primitiveFactor of ExtentionId",
					MAPParsingComponentExceptionReason.MistypedParameter);
		this.oId = ansIS.readObjectIdentifier();

		// extType
		if (ansIS.available() > 0) {
			this.data = new byte[ansIS.available()];
			ansIS.read(this.data);
		}
		else
			this.data = null;
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding PrivateExtension: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		
		if (this.oId == null || this.oId.length < 2)
			throw new MAPException("Error when encoding PrivateExtension: OId value must not be empty when coding PrivateExtension");
		
		try {
			asnOs.writeObjectIdentifier(this.oId);
			if (this.data != null)
				asnOs.write(this.data);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding PrivateExtension: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding PrivateExtension: " + e.getMessage(), e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PrivateExtension [");
		
		if (this.oId != null || this.oId.length > 0) {
			sb.append("Oid=");
			sb.append(this.ArrayToString(this.oId));
		}

		if (this.data != null ) {
			sb.append(", data=");
			sb.append(this.ArrayToString(this.data));
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
	
	private String ArrayToString(long[] array) {
		StringBuilder sb = new StringBuilder();
		int i1 = 0;
		for (long b : array) {
			if (i1 == 0)
				i1 = 1;
			else
				sb.append(", ");
			sb.append(b);
		}
		return sb.toString();
	}

	
	// ...............................
	


}
