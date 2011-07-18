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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class CellGlobalIdOrServiceAreaIdOrLAIImpl extends MAPPrimitiveBase implements CellGlobalIdOrServiceAreaIdOrLAI {

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

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length != 1) {
			throw new MAPParsingComponentException(
					"Decoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Manadatory parameter cellGlobalIdOrServiceAreaIdFixedLength or laiFixedLength should be present",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];
		if (p.getTag() == 0) {
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
				throw new MAPParsingComponentException("Decoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Invalid Tag Class or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}

			this.cellGlobalIdOrServiceAreaIdFixedLength = p.getData();

		} else if (p.getTag() == 1) {
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
				throw new MAPParsingComponentException("Decoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Invalid Tag Class or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			this.laiFixedLength = p.getData();

		} else {
			throw new MAPParsingComponentException(
					"Decoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Expected manadatory parameter cellGlobalIdOrServiceAreaIdFixedLength[0] or laiFixedLength[1] should be present but found=",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.cellGlobalIdOrServiceAreaIdFixedLength != null) {
			// cellGlobalIdOrServiceAreaIdFixedLength [0]
			// CellGlobalIdOrServiceAreaIdFixedLength

			asnOs.write(0x80);
			asnOs.write(this.cellGlobalIdOrServiceAreaIdFixedLength.length);
			try {
				asnOs.write(this.cellGlobalIdOrServiceAreaIdFixedLength);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Failed to encode parameter[cellGlobalIdOrServiceAreaIdFixedLength [0] CellGlobalIdOrServiceAreaIdFixedLength]",
						e);
			}
		} else if (this.laiFixedLength != null) {
			if (this.cellGlobalIdOrServiceAreaIdFixedLength != null) {
				// laiFixedLength [1] LAIFixedLength

				asnOs.write(0x81);
				asnOs.write(this.laiFixedLength.length);
				try {
					asnOs.write(this.laiFixedLength);
				} catch (IOException e) {
					throw new MAPException(
							"Encoding of CellGlobalIdOrServiceAreaIdOrLAI failed. Failed to encode parameter[laiFixedLength [1] LAIFixedLength]", e);
				}
			}

		}
	}
}
