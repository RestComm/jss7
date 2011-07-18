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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class ResponseTimeImpl extends MAPPrimitiveBase implements ResponseTime {

	private ResponseTimeCategory responseTimeCategory = null;

	/**
	 * 
	 */
	public ResponseTimeImpl() {
		super();
	}

	/**
	 * @param responseTimeCategory
	 */
	public ResponseTimeImpl(ResponseTimeCategory responseTimeCategory) {
		super();
		this.responseTimeCategory = responseTimeCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime#
	 * getResponseTimeCategory()
	 */
	@Override
	public ResponseTimeCategory getResponseTimeCategory() {
		return this.responseTimeCategory;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding ResponseTime: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory dataCodingScheme [0] USSD-DataCodingScheme,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.ENUMERATED) {
			throw new MAPParsingComponentException(
					"Error while decoding ResponseTime: Parameter 0[ResponseTimeCategory ::= ENUMERATED] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.responseTimeCategory = ResponseTimeCategory.getResponseTimeCategory(p.getData()[0]);
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.responseTimeCategory == null) {
			throw new MAPException("Error while encoding ResponseTime the mandatory parameter responseTimeCategory is not defined");
		}

		// Encode mandatory param dataCodingScheme
		asnOs.write(0x0a);
		asnOs.write(0x01);
		asnOs.write(this.responseTimeCategory.getCategory());
	}

}
