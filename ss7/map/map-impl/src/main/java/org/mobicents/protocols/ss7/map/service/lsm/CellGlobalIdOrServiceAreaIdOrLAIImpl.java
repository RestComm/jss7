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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class CellGlobalIdOrServiceAreaIdOrLAIImpl implements CellGlobalIdOrServiceAreaIdOrLAI, MAPAsnPrimitive {
	
	private static final int _TAG_CELL_GLOBAL_ID_OR_SERVICE_AREAR_ID= 0;
	private static final int _TAG_LAI = 1;

	private byte[] cellGlobalIdOrServiceAreaIdFixedLength = null;
	private byte[] laiFixedLength = null;

	/**
	 * 
	 */
	public CellGlobalIdOrServiceAreaIdOrLAIImpl() {
		super();
	}

	/**
	 * @param cellGlobalIdOrServiceAreaIdFixedLength
	 * @param laiFixedLength
	 */
	public CellGlobalIdOrServiceAreaIdOrLAIImpl(byte[] cellGlobalIdOrServiceAreaIdFixedLength, byte[] laiFixedLength) throws MAPException {

		if (cellGlobalIdOrServiceAreaIdFixedLength != null && laiFixedLength != null) {
			throw new MAPException("Either cellGlobalIdOrServiceAreaIdFixedLength or laiFixedLength can be set. Not both");
		}

		this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
		this.laiFixedLength = laiFixedLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * CellGlobalIdOrServiceAreaIdOrLAI
	 * #getCellGlobalIdOrServiceAreaIdFixedLength()
	 */
	@Override
	public byte[] getCellGlobalIdOrServiceAreaIdFixedLength() {
		return this.cellGlobalIdOrServiceAreaIdFixedLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * CellGlobalIdOrServiceAreaIdOrLAI#getLAIFixedLength()
	 */
	@Override
	public byte[] getLAIFixedLength() {
		return this.laiFixedLength;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		if(this.cellGlobalIdOrServiceAreaIdFixedLength != null ){
			return 0;
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive()
	 */
	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}
	
	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
			throw new MAPParsingComponentException("Error while decoding CellGlobalIdOrServiceAreaIdOrLAI: bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
					MAPParsingComponentExceptionReason.MistypedParameter);

		switch (asnIS.getTag()) {
		case _TAG_CELL_GLOBAL_ID_OR_SERVICE_AREAR_ID:
			this.cellGlobalIdOrServiceAreaIdFixedLength = asnIS.readOctetStringData(length);
			break;
		case _TAG_LAI:
			this.laiFixedLength = asnIS.readOctetStringData(length);
			break;
		default:
			throw new MAPParsingComponentException(
					"Error while decoding AdditionalNumber: Expexted msc-Number [0] ISDN-AddressString or sgsn-Number [1] ISDN-AddressString, but found "
							+ asnIS.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding AdditionalNumber: " + e.getMessage(), e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if(this.cellGlobalIdOrServiceAreaIdFixedLength != null){
			asnOs.write(this.cellGlobalIdOrServiceAreaIdFixedLength);
		} else {
			asnOs.write(this.laiFixedLength);
		}
	}


}
