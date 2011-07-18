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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.map.service.supplementary.USSDStringImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class LCSRequestorIDImpl extends MAPPrimitiveBase implements LCSRequestorID {

	private byte dataCodingScheme;
	private USSDString requestorIDString;
	private LCSFormatIndicator lcsFormatIndicator;

	/**
	 * 
	 */
	public LCSRequestorIDImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dataCodingScheme
	 * @param requestorIDString
	 * @param lcsFormatIndicator
	 */
	public LCSRequestorIDImpl(byte dataCodingScheme, USSDString requestorIDString, LCSFormatIndicator lcsFormatIndicator) {
		super();
		this.dataCodingScheme = dataCodingScheme;
		this.requestorIDString = requestorIDString;
		this.lcsFormatIndicator = lcsFormatIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID#
	 * getDataCodingScheme()
	 */
	@Override
	public byte getDataCodingScheme() {
		return this.dataCodingScheme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID#
	 * getRequestorIDString()
	 */
	@Override
	public USSDString getRequestorIDString() {
		return this.requestorIDString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID#
	 * getLCSFormatIndicator()
	 */
	@Override
	public LCSFormatIndicator getLCSFormatIndicator() {
		return this.lcsFormatIndicator;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 2) {
			throw new MAPParsingComponentException("Error while decoding LCSRequestorID: Needs at least 2 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory dataCodingScheme [0] USSD-DataCodingScheme,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSRequestorID: Parameter 0[dataCodingScheme [0] USSD-DataCodingScheme] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		dataCodingScheme = p.getData()[0];

		// Decode mandatory nameString [2] NameString,
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 1) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSRequestorID: Parameter 1[requestorIDString [1] RequestorIDString] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.requestorIDString = new USSDStringImpl(p.getData(), null);

		if (parameters.length > 2) {
			// Decode lcs-FormatIndicator [3] LCS-FormatIndicator OPTIONAL
			p = parameters[2];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 3) {
				throw new MAPParsingComponentException(
						"Error while decoding LCSClientName: Parameter 2[lcs-FormatIndicator [3] LCS-FormatIndicator OPTIONAL] bad tag class, tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}

			this.lcsFormatIndicator = LCSFormatIndicator.getLCSFormatIndicator(p.getData()[0]);
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.requestorIDString == null) {
			throw new MAPException("Error while encoding LCSRequestorID the mandatory parameter RequestorIDString is not defined");
		}

		try {
			// Encode mandatory param dataCodingScheme
			asnOs.write(0x80);
			asnOs.write(0x01);
			asnOs.write(this.dataCodingScheme);

			// Encode mandatory param NameString
			asnOs.write(0x81);
			requestorIDString.encode();
			byte[] data = requestorIDString.getEncodedString();
			asnOs.write(data.length);
			asnOs.write(data);

			if (this.lcsFormatIndicator != null) {
				// Encode optional lcs-FormatIndicator [3] LCS-FormatIndicator
				asnOs.write(0x82);
				asnOs.write(0x01);
				asnOs.write(this.lcsFormatIndicator.getIndicator());
			}
		} catch (IOException e) {
			new MAPException("Encoding LCSClientName failed ", e);
		}
	}
}
