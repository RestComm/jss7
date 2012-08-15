/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author normandes
 *
 */
public class UESBIIuImpl implements UESBIIu, MAPAsnPrimitive {

	public static final String _PrimitiveName = "UESBIIu";
	
	private static final int _TAG_UESBI_IuA = 0;
	private static final int _TAG_UESBI_IuB = 1;
	
	private UESBIIuA uesbiIuA;
	private UESBIIuB uesbiIuB;
	
	public UESBIIuImpl() {
		
	}
	
	public UESBIIuImpl(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB) {
		this.uesbiIuA = uesbiIuA;
		this.uesbiIuB = uesbiIuB;
	}
	
	@Override
	public UESBIIuA getUESBI_IuA() {
		return this.uesbiIuA;
	}

	@Override
	public UESBIIuB getUESBI_IuB() {
		return this.uesbiIuB;
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
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.uesbiIuA = null;
		this.uesbiIuB = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _TAG_UESBI_IuA:
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter uesbiIuA is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.uesbiIuA = new UESBIIuAImpl();
					((UESBIIuAImpl) this.uesbiIuA).decodeAll(ais);
					break;
					
				case _TAG_UESBI_IuB:
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter uesbiIuB is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.uesbiIuB = new UESBIIuBImpl();
					((UESBIIuBImpl) this.uesbiIuB).decodeAll(ais);
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.uesbiIuA != null) {
			((UESBIIuAImpl) this.uesbiIuA).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UESBI_IuA);
		}
		if (this.uesbiIuB != null) {
			((UESBIIuBImpl) this.uesbiIuB).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UESBI_IuB);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");
		if (this.uesbiIuA != null) {
			sb.append("uesbiIuA=");
			sb.append(this.uesbiIuA);
			sb.append(", ");
		}
		
		if (this.uesbiIuB != null) {
			sb.append("uesbiIuB=");
			sb.append(this.uesbiIuB);
		}
		sb.append("]");

		return sb.toString();
	}

	

}
