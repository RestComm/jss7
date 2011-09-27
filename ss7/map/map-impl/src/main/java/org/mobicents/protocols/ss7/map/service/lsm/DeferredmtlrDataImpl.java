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
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * TODO : Add unit test
 * 
 * @author amit bhayani
 * 
 */
public class DeferredmtlrDataImpl implements DeferredmtlrData, MAPAsnPrimitive {

	private static final int _TAG_TERMINATION_CAUSE = 0;
	private static final int _TAG_LCS_LOCATION_INFO = 1;

	private DeferredLocationEventType deferredLocationEventType = null;
	private TerminationCause terminationCause = null;
	private LCSLocationInfo lcsLocationInfo = null;

	/**
	 * 
	 */
	public DeferredmtlrDataImpl() {
		super();
	}

	/**
	 * @param deferredLocationEventType
	 * @param terminationCause
	 * @param lcsLocationInfo
	 */
	public DeferredmtlrDataImpl(DeferredLocationEventType deferredLocationEventType, TerminationCause terminationCause, LCSLocationInfo lcsLocationInfo) {
		super();
		this.deferredLocationEventType = deferredLocationEventType;
		this.terminationCause = terminationCause;
		this.lcsLocationInfo = lcsLocationInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData#
	 * getDeferredLocationEventType()
	 */
	@Override
	public DeferredLocationEventType getDeferredLocationEventType() {
		return this.deferredLocationEventType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData#
	 * getTerminationCause()
	 */
	@Override
	public TerminationCause getTerminationCause() {
		return this.terminationCause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData#
	 * getLCSLocationInfo()
	 */
	@Override
	public LCSLocationInfo getLCSLocationInfo() {
		return this.lcsLocationInfo;
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

		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_BIT) {
			throw new MAPParsingComponentException(
					"Error while decoding DeferredmtlrData: Parameter [deferredLocationEventType DeferredLocationEventType] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.deferredLocationEventType = new DeferredLocationEventTypeImpl();
		((DeferredLocationEventTypeImpl) this.deferredLocationEventType).decodeAll(ais);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();
			switch (tag) {
			case _TAG_TERMINATION_CAUSE:
				// terminationCause [0] TerminationCause OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding of DeferredmtlrData failed. Decoding of parameter[terminationCause [0] TerminationCause] has Invalid Tag Class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				int i1 = (int) ais.readInteger();

				this.terminationCause = TerminationCause.getTerminationCause(i1);
				break;
			case _TAG_LCS_LOCATION_INFO:
				// lcsLocationInfo [1] LCSLocationInfo
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding of DeferredmtlrData failed. Decoding of parameter[lcsLocationInfo [1] LCSLocationInfo] has Invalid Tag Class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsLocationInfo = new LCSLocationInfoImpl();
				((LCSLocationInfoImpl) this.lcsLocationInfo).decodeAll(ais);
				break;
			default:
//              throw new MAPParsingComponentException(
//              "Error while decoding DeferredmtlrData: Expected terminationCause [0] TerminationCause or lcsLocationInfo [1] LCSLocationInfo but received "
//                              + p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

				ais.advanceElement();
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
			throw new MAPException("AsnException when encoding reportSMDeliveryStatusRequest: " + e.getMessage(), e);
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
		if (this.deferredLocationEventType == null) {
			throw new MAPException("Encdoing of DeferredmtlrData failed. Missing mandatory parameter deferredLocationEventType DeferredLocationEventType");
		}

		((DeferredLocationEventTypeImpl) this.deferredLocationEventType).encodeAll(asnOs);

		try {
			if (this.terminationCause != null) {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_TERMINATION_CAUSE, this.terminationCause.getCause());
			}

		} catch (IOException e) {
			throw new MAPException("IOException when encoding Area: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding Area: " + e.getMessage(), e);
		}
		if (this.lcsLocationInfo != null) {
			((LCSLocationInfoImpl) this.lcsLocationInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_LOCATION_INFO);
		}
	}

}
