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
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * @author amit bhayani
 * 
 */
public class LCSCodewordImpl implements LCSCodeword, MAPAsnPrimitive {

	private static final int _TAG_DATA_CODING_SCHEME = 0;
	private static final int _TAG_LCS_CODE_WORD_STRING = 1;

	private byte dataCodingScheme;
	private USSDString lcsCodewordString = null;

	/**
	 * 
	 */
	public LCSCodewordImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataCodingScheme
	 * @param lcsCodewordString
	 */
	public LCSCodewordImpl(byte dataCodingScheme, USSDString lcsCodewordString) {
		super();
		this.dataCodingScheme = dataCodingScheme;
		this.lcsCodewordString = lcsCodewordString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword#
	 * getDataCodingScheme()
	 */
	@Override
	public byte getDataCodingScheme() {
		return this.dataCodingScheme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword#
	 * getLCSCodewordString()
	 */
	@Override
	public USSDString getLCSCodewordString() {
		return this.lcsCodewordString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LCSClientName: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LCSClientName: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LCSCodeword: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LCSCodeword: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		AsnInputStream ais = asnIS.readSequenceStreamData(length);

		int tag = ais.readTag();

		// Decode mandatory dataCodingScheme [0] USSD-DataCodingScheme,
		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_DATA_CODING_SCHEME) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSCodeword: Parameter 0[dataCodingScheme [0] USSD-DataCodingScheme] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		int length1 = ais.readLength();
		this.dataCodingScheme = ais.readOctetStringData(length1)[0];

		tag = ais.readTag();

		// Decode mandatory lcsCodewordString [1] LCSCodewordString,
		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_LCS_CODE_WORD_STRING) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSCodeword: Parameter 1[lcsCodewordString [1] LCSCodewordString] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.lcsCodewordString = new USSDStringImpl();
		((USSDStringImpl)this.lcsCodewordString).decodeAll(ais);
		

		while (true) {
			if (ais.available() == 0)
				break;
			switch (ais.readTag()) {
			default:
				ais.advanceElement();
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding LCSClientName", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.lcsCodewordString == null)
			throw new MAPException("lcsCodewordString must not be null");

		try {
			asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_DATA_CODING_SCHEME, new byte[] { this.dataCodingScheme });

			((USSDStringImpl)this.lcsCodewordString).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CODE_WORD_STRING);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding LCSClientName", e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding LCSClientName", e);
		}
	}

}
