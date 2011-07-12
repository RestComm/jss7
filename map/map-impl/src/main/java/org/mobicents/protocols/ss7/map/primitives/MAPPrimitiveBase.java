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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * When extending this class one of decode() and one of encode() methods must be overridden !!!
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class MAPPrimitiveBase implements MAPPrimitive {

	@Override
	public void decode(Parameter p) throws MAPParsingComponentException {
		AsnInputStream ansIS = new AsnInputStream(new ByteArrayInputStream(p.getData()));
		this.decode(ansIS, p.getTagClass(), p.isPrimitive(), p.getTag(), p.getData().length);
	}

	@Override
	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int masterTag, int length) throws MAPParsingComponentException {
		
		if (length == 0)
			return;

		try {
			Parameter p = TcapFactory.createParameter();
			byte[] buf = new byte[length];
			if (ansIS.read(buf) != length)
				throw new MAPParsingComponentException("Error while decoding LocationInfoWithLMSI: bad tag class or tag or it is primitive: TagClass="
						+ masterTag + ", Tag=" + masterTag, MAPParsingComponentExceptionReason.MistypedParameter);
			p.setData(buf);
			p.setPrimitive(isPrimitive);
			p.setTagClass(tagClass);
			p.setTag(masterTag);

			this.decode(p);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding the primitive: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public Parameter encode() throws MAPException {
		AsnOutputStream asnOs = new AsnOutputStream(); 
		
		this.encode(asnOs);

		Parameter p = TcapFactory.createParameter();

		p.setTagClass(this.getTagClass());
		p.setPrimitive(this.getIsPrimitive());
		p.setTag(this.getTag());
		p.setData(asnOs.toByteArray());

		return p;
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		try {
			Parameter p = this.encode();

			// p.encode(asnOs);
			byte[] buf = this.encodeParameterToStream(p);
			asnOs.write(buf);
		} catch (IOException e) {
			throw new MAPException("ParseException when decoding the primitive: " + e.getMessage(), e);
		}
	}
	
	private byte[] encodeParameterToStream(Parameter p) throws IOException {
		if (p.getData() == null && p.getParameters() != null) {
			AsnOutputStream asnOs = new AsnOutputStream();
			for (Parameter px : p.getParameters()) {
				byte buf[] = encodeParameterToStream(px);
				asnOs.writeTag(px.getTagClass(), px.isPrimitive(), px.getTag());
				asnOs.writeLength(buf.length);
				asnOs.write(buf);
			}
			return asnOs.toByteArray();
		} else
			return p.getData();
	}

	@Override
	public int getTag() throws MAPException {
		return 0;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return true;
	}

}
