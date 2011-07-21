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
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;

/**
 * @author amit bhayani
 * 
 */
public class LocationTypeImpl extends MAPPrimitiveBase implements LocationType {

	private LocationEstimateType locationEstimateType;
	private BitSet deferredLocationEventType;

	public LocationTypeImpl() {

	}

	/**
	 * 
	 */
	public LocationTypeImpl(final LocationEstimateType locationEstimateType, final BitSet deferredLocationEventType) {
		this.locationEstimateType = locationEstimateType;
		this.deferredLocationEventType = deferredLocationEventType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LocationType#
	 * getLocationEstimateType()
	 */
	@Override
	public LocationEstimateType getLocationEstimateType() {
		return this.locationEstimateType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LocationType#
	 * getDeferredLocationEventType()
	 */
	@Override
	public BitSet getDeferredLocationEventType() {
		return this.deferredLocationEventType;
	}

	public void decode(AsnInputStream ais, int tagClass, boolean isPrimitive, int masterTag, int length) throws MAPParsingComponentException {
		int tagTemp;
		try {

			// locationEstimateType [0] LocationEstimateType,
			tagTemp = ais.readTag();
			int len = ais.readLength();

			if (tagTemp != 0) {
				throw new MAPParsingComponentException("Decoding LocationType failed. Expected Parameter tag as LocationEstimateType[0] but received "
						+ tagTemp, MAPParsingComponentExceptionReason.MistypedParameter);
			}

			this.locationEstimateType = LocationEstimateType.getLocationEstimateType(ais.read());

			// If length greater than 3 decode
			// deferredLocationEventType [1] DeferredLocationEventType OPTIONAL
			if (length > 3) {
				tagTemp = ais.readTag();
				len = ais.readLength();
				this.deferredLocationEventType = new BitSet();
				ais.readBitStringData(this.deferredLocationEventType, len, true);
			}
		} catch (IOException e) {
			throw new MAPParsingComponentException("Decoding LocationType failed.", e, MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("Decoding LocationType failed.", e, MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}

	public void encode(AsnOutputStream asnOs) throws MAPException {
		if (this.locationEstimateType == null) {
			throw new MAPException("Error while encoding LocationType the mandatory parameter LocationEstimateType is not defined");
		}

		// Encode locationEstimateType [0] LocationEstimateType
		asnOs.write(0x80);// TAG
		asnOs.write(0x01);
		asnOs.write(this.locationEstimateType.getType());

		if (this.deferredLocationEventType != null) {
			// Encode deferredLocationEventType [1] DeferredLocationEventType
			// OPTIONAL
			// asnOs.write(0x81);// TAG
			// asnOs.write(this.deferredLocationEventType.length());
			try {

				asnOs.writeStringBinary(Tag.CLASS_CONTEXT_SPECIFIC, 1, this.deferredLocationEventType);
			} catch (AsnException e) {
				throw new MAPException("Error while encoding LocationType parameter1[deferredLocationEventType [1] DeferredLocationEventType]", e);
			} catch (IOException e) {
				throw new MAPException("Error while encoding LocationType parameter1[deferredLocationEventType [1] DeferredLocationEventType]", e);
			}
		}

	}
}
