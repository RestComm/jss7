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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class LCSClientIDImpl implements LCSClientID, MAPAsnPrimitive {
	private static final int _TAG_LCS_CLIENT_TYPE = 0;
	private static final int _TAG_LCS_CLIENT_EXTERNAL_ID = 1;
	private static final int _TAG_LCS_CLIENT_DIALED_BY_MS = 2;
	private static final int _TAG_LCS_CLIENT_INTERNAL_ID = 3;
	private static final int _TAG_LCS_CLIENT_NAME = 4;
	private static final int _TAG_LCS_APN = 5;
	private static final int _TAG_LCS_REQUESTOR_ID = 6;

	private LCSClientType lcsClientType = null;
	private LCSClientExternalID lcsClientExternalID = null;
	private LCSClientInternalID lcsClientInternalID = null;
	private LCSClientName lcsClientName = null;
	private AddressString lcsClientDialedByMS = null;
	private byte[] lcsAPN = null;
	private LCSRequestorID lcsRequestorID = null;

	/**
	 * @param lcsClientType
	 * @param lcsClientExternalID
	 * @param lcsClientInternalID
	 * @param lcsClientName
	 * @param lcsClientDialedByMS
	 * @param lcsAPN
	 * @param lcsRequestorID
	 */
	public LCSClientIDImpl(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID, LCSClientInternalID lcsClientInternalID,
			LCSClientName lcsClientName, AddressString lcsClientDialedByMS, byte[] lcsAPN, LCSRequestorID lcsRequestorID) {
		super();
		this.lcsClientType = lcsClientType;
		this.lcsClientExternalID = lcsClientExternalID;
		this.lcsClientInternalID = lcsClientInternalID;
		this.lcsClientName = lcsClientName;
		this.lcsClientDialedByMS = lcsClientDialedByMS;
		this.lcsAPN = lcsAPN;
		this.lcsRequestorID = lcsRequestorID;
	}

	/**
	 * 
	 */
	public LCSClientIDImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientType
	 * ()
	 */
	@Override
	public LCSClientType getLCSClientType() {
		return this.lcsClientType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientExternalID()
	 */
	@Override
	public LCSClientExternalID getLCSClientExternalID() {
		return this.lcsClientExternalID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientDialedByMS()
	 */
	@Override
	public AddressString getLCSClientDialedByMS() {
		return this.lcsClientDialedByMS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientInternalID()
	 */
	@Override
	public LCSClientInternalID getLCSClientInternalID() {
		return this.lcsClientInternalID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientName
	 * ()
	 */
	@Override
	public LCSClientName getLCSClientName() {
		return this.lcsClientName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSAPN()
	 */
	@Override
	public byte[] getLCSAPN() {
		return this.lcsAPN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSRequestorID
	 * ()
	 */
	@Override
	public LCSRequestorID getLCSRequestorID() {
		return this.lcsRequestorID;
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
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
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
			throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		AsnInputStream ais = asnIS.readSequenceStreamData(length);

		int tag = ais.readTag();
		// Decode mandatory lcsClientType [0] LCSClientType,
		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_LCS_CLIENT_TYPE) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSClientID: Parameter 0[lcsClientType [0] LCSClientType] bad tag class, tag or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		int length1 = ais.readLength();
		int lcsCltType = (int) ais.readIntegerData(length1);

		this.lcsClientType = LCSClientType.getLCSClientType(lcsCltType);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();
			switch (tag) {
			case _TAG_LCS_CLIENT_EXTERNAL_ID:
				// Optional lcsClientExternalID [1] LCSClientExternalID
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding LCSClientExternalID: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsClientExternalID = new LCSClientExternalIDImpl();
				((LCSClientExternalIDImpl)this.lcsClientExternalID).decodeAll(ais);
				break;
			case _TAG_LCS_CLIENT_DIALED_BY_MS:
				// lcsClientDialedByMS [2] AddressString OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding lcsClientDialedByMS: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsClientDialedByMS = new AddressStringImpl();
				((AddressStringImpl)this.lcsClientDialedByMS).decodeAll(ais);

				break;
			case _TAG_LCS_CLIENT_INTERNAL_ID:
				// lcsClientInternalID [3] LCSClientInternalID OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding lcsClientInternalID: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				int i1 = (int) ais.readIntegerData(length1);
				this.lcsClientInternalID = LCSClientInternalID.getLCSClientInternalID(i1);
				break;
			case _TAG_LCS_CLIENT_NAME:
				// lcsClientName [4] LCSClientName OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding lcsClientName: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsClientName = new LCSClientNameImpl();
				((LCSClientNameImpl)this.lcsClientName).decodeAll(ais);
				break;
			case _TAG_LCS_APN:
				// lcsAPN [5] APN OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding lcsAPN: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.lcsAPN = ais.readOctetStringData(length1);
				break;
			case _TAG_LCS_REQUESTOR_ID:
				// lcsRequestorID [6] LCSRequestorID OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException("Error while decoding lcsRequestorID: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsRequestorID = new LCSRequestorIDImpl();
				((LCSRequestorIDImpl)this.lcsRequestorID).decodeAll(ais);
				break;
			default:
				// Do we care?
			}
		}// while
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
		if (this.lcsClientType == null)
			throw new MAPException("lcsClientType must not be null");

		try {
			asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_TYPE, this.lcsClientType.getType());
		} catch (IOException e) {
			throw new MAPException("IOException when encoding parameter lcsClientType: ", e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding parameter lcsClientType: ", e);
		}

		if (this.lcsClientExternalID != null) {
			// Encode lcsClientExternalID [1] LCSClientExternalID OPTIONAL,
			((LCSClientExternalIDImpl)this.lcsClientExternalID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_EXTERNAL_ID);
		}

		if (this.lcsClientDialedByMS != null) {
			// lcsClientDialedByMS [2] AddressString OPTIONAL,
			((AddressStringImpl)this.lcsClientDialedByMS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_DIALED_BY_MS);
		}

		if (this.lcsClientInternalID != null) {
			// lcsClientInternalID [3] LCSClientInternalID OPTIONAL,
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_INTERNAL_ID, this.lcsClientInternalID.getId());
			} catch (IOException e) {
				throw new MAPException("IOException when encoding parameter lcsClientInternalID: ", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding parameter lcsClientInternalID: ", e);
			}
		}

		if (this.lcsClientName != null) {
			// lcsClientName [4] LCSClientName
			((LCSClientNameImpl)this.lcsClientName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_NAME);
		}

		if (this.lcsAPN != null) {
			// lcsAPN [5] APN OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_APN, this.lcsAPN);
			} catch (IOException e) {
				throw new MAPException("IOException when encoding parameter lcsAPN: ", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding parameter lcsAPN: ", e);
			}
		}

		if (this.lcsRequestorID != null) {
			// lcsRequestorID [6] LCSRequestorID
			((LCSRequestorIDImpl)this.lcsRequestorID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_REQUESTOR_ID);
		}
	}

}
