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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;

/**
 * @author sergey vetyutnev
 * 
 */
public class MAPPrivateExtensionImpl implements MAPPrivateExtension {

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

	public void decode(AsnInputStream ais) throws AsnException, IOException, MAPException {

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

		byte[] seqData = ais.readSequence();

		ByteArrayInputStream localIS = new ByteArrayInputStream(seqData);
		AsnInputStream localAis = new AsnInputStream(localIS);

		int tag;
		int seqx = 0;

		while (localAis.available() > 0) {
			switch (seqx) {
			case 0:
				tag = localAis.readTag();
				if (tag == Tag.OBJECT_IDENTIFIER)
					this.oId = localAis.readObjectIdentifier();
				else
					throw new MAPException("OBJECT_IDENTIFIER tag is expected when parsing PrivateExtension OId");
				break;

			default:
				this.data = new byte[localIS.available()];
				localAis.read(this.data);
				break;
			}

			seqx++;
		}

	}

	public void encode(AsnOutputStream asnOS) throws IOException, MAPException {
		if (this.oId == null || this.oId.length < 2)
			throw new MAPException("OId value must not be empty when coding PrivateExtension");

		AsnOutputStream localAos = new AsnOutputStream();

		localAos.writeObjectIdentifier(this.oId);

		if (this.data != null)
			localAos.write(this.data);

		byte[] data = localAos.toByteArray();

		asnOS.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
		asnOS.writeLength(data.length);
		asnOS.write(data);
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

}
