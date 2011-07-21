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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class DeferredmtlrDataImpl extends MAPPrimitiveBase implements DeferredmtlrData {

	private BitSet deferredLocationEventType = null;
	private TerminationCause terminationCause = null;
	private LCSLocationInfo lcsLocationInfo = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData#
	 * getDeferredLocationEventType()
	 */
	@Override
	public BitSet getDeferredLocationEventType() {
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

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Decoding of DeferredmtlrData failed. Manadatory parameter DeferredLocationEventType should be present",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];

		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive()) {
			throw new MAPParsingComponentException(
					"Decoding of DeferredmtlrData failed. Decoding of parameter[deferredLocationEventType DeferredLocationEventType] has Invalid Tag Class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		AsnInputStream asnInputStream = new AsnInputStream(new ByteArrayInputStream(p.getData()));
		this.deferredLocationEventType = new BitSet();
		try {
			asnInputStream.readBitStringData(this.deferredLocationEventType, p.getData().length, true);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("Decode DeferredmtlrData failed. Failed to decode deferredLocationEventType DeferredLocationEventType", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (IOException e) {
			throw new MAPParsingComponentException("Decode DeferredmtlrData failed. Failed to decode deferredLocationEventType DeferredLocationEventType", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		for (int count = 1; count < parameters.length; count++) {
			p = parameters[1];
			switch (p.getTag()) {
			case 0:
				// terminationCause [0] TerminationCause OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding of DeferredmtlrData failed. Decoding of parameter[terminationCause [0] TerminationCause] has Invalid Tag Class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.terminationCause = TerminationCause.getTerminationCause(p.getData()[0]);
				break;
			case 1:
				// lcsLocationInfo [1] LCSLocationInfo
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding of DeferredmtlrData failed. Decoding of parameter[lcsLocationInfo [1] LCSLocationInfo] has Invalid Tag Class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsLocationInfo = new LCSLocationInfoImpl(); 
				this.lcsLocationInfo.decode(p); 
				break;
			default:
//				throw new MAPParsingComponentException(
//						"Error while decoding DeferredmtlrData: Expected terminationCause [0] TerminationCause or lcsLocationInfo [1] LCSLocationInfo but received "
//								+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
				break;
			}
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {
		if (this.deferredLocationEventType == null) {
			throw new MAPException("Encdoing of DeferredmtlrData failed. Missing mandatory parameter deferredLocationEventType DeferredLocationEventType");
		}

		// deferredLocationEventType DeferredLocationEventType,
		try {
			asnOs.writeStringBinary(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, this.deferredLocationEventType);
		} catch (AsnException e) {
			throw new MAPException(
					"Encdoing of DeferredmtlrData failed. Error while encoding mandatory parameter deferredLocationEventType DeferredLocationEventType", e);
		} catch (IOException e) {
			throw new MAPException(
					"Encdoing of DeferredmtlrData failed. Error while encoding mandatory parameter deferredLocationEventType DeferredLocationEventType", e);
		}

		if (this.terminationCause != null) {
			// terminationCause [0] TerminationCause OPTIONAL,
			asnOs.write(0x80);
			asnOs.write(0x01);
			asnOs.write(this.terminationCause.getCause());
		}

		if (this.lcsLocationInfo != null) {
			Parameter p = this.lcsLocationInfo.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(0x01);
			p.setPrimitive(false);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encdoing of DeferredmtlrData failed. Error while encoding parameter lcsLocationInfo [1] LCSLocationInfo", e);
			}
		}
	}

}
