/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;

/**
 * @author amit bhayani
 *
 */
public class DeferredLocationEventTypeImpl implements DeferredLocationEventType {
	
	private static final int _INDEX_MS_AVAILABLE = 0;
	private static final int _INDEX__ENTERING_INTO_AREA = 1;
	private static final int _INDEX_LEAVING_FROM_AREA = 2;
	private static final int _INDEX_BEING_INSIDE_AREA = 3;
	
	//TODO : Is this correct?
	private BitSetStrictLength bitString = new BitSetStrictLength(4);
	
	/**
	 * 
	 */
	public DeferredLocationEventTypeImpl() {
		super();
	}
	
	public DeferredLocationEventTypeImpl(boolean msAvailable, boolean enteringIntoArea, boolean leavingFromArea, boolean beingInsideArea) {
		if (msAvailable)
			this.bitString.set(_INDEX_MS_AVAILABLE);
		if (enteringIntoArea)
			this.bitString.set(_INDEX__ENTERING_INTO_AREA);
		if (leavingFromArea)
			this.bitString.set(_INDEX_LEAVING_FROM_AREA);
		if (beingInsideArea)
			this.bitString.set(_INDEX_BEING_INSIDE_AREA);	
		}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.STRING_BIT;
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
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
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
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		if (length == 0 || length > 4)
			throw new MAPParsingComponentException("Error decoding DeferredLocationEventType: the DeferredLocationEventType field must contain from 1 or 4 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);
		
		this.bitString = ansIS.readBitStringData(length);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
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
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			asnOs.writeBitStringData(this.bitString);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MWStatus: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getMsAvailable()
	 */
	@Override
	public boolean getMsAvailable() {
		return this.bitString.get(_INDEX_MS_AVAILABLE);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getEnteringIntoArea()
	 */
	@Override
	public boolean getEnteringIntoArea() {
		return this.bitString.get(_INDEX__ENTERING_INTO_AREA);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#getLeavingFromArea()
	 */
	@Override
	public boolean getLeavingFromArea() {
		return this.bitString.get(_INDEX_LEAVING_FROM_AREA);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType#beingInsideArea()
	 */
	@Override
	public boolean getBeingInsideArea() {
		return this.bitString.get(_INDEX_BEING_INSIDE_AREA);
	}

}
