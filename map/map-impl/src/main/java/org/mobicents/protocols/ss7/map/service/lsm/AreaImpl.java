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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class AreaImpl extends MAPPrimitiveBase implements Area {

	private AreaType areaType = null;
	private byte[] areaIdentification = null;

	/**
	 * 
	 */
	public AreaImpl() {
		super();
	}

	/**
	 * @param areaType
	 * @param areaIdentification
	 */
	public AreaImpl(AreaType areaType, byte[] areaIdentification) {
		super();
		this.areaType = areaType;
		this.areaIdentification = areaIdentification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.Area#getAreaType()
	 */
	@Override
	public AreaType getAreaType() {
		return this.areaType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.Area#getAreaIdentification
	 * ()
	 */
	@Override
	public byte[] getAreaIdentification() {
		return this.areaIdentification;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding Area: Needs at least 2 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException("Error while decoding Area: Parameter 0 [areaType [0] AreaType] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.areaType = AreaType.getAreaType(p.getData()[0]);

		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 1) {
			throw new MAPParsingComponentException(
					"Error while decoding Area: Parameter 0 [areaIdentification [1] AreaIdentification] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// TODO Should this be String?
		this.areaIdentification = p.getData();
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.areaType == null) {
			throw new MAPException("Error while encoding Area the mandatory parameter[areaType [0] AreaType] is not defined");
		}

		if (this.areaIdentification == null) {
			throw new MAPException("Error while encoding Area the mandatory parameter[areaIdentification [1] AreaIdentification] is not defined");
		}

		// areaType [0] AreaType,
		asnOs.write(0x80);
		asnOs.write(0x01);
		asnOs.write(this.areaType.getType());

		asnOs.write(0x81);
		asnOs.write(this.areaIdentification.length);
		asnOs.write(this.areaIdentification);

	}
}
