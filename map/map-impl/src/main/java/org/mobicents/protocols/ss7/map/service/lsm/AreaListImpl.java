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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaList;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class AreaListImpl extends MAPPrimitiveBase implements AreaList {

	private Area[] areas = null;

	/**
	 * 
	 */
	public AreaListImpl() {
		super();
	}

	/**
	 * @param areas
	 */
	public AreaListImpl(Area[] areas) {
		super();
		this.areas = areas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaList#getAreas()
	 */
	@Override
	public Area[] getAreas() {
		return this.areas;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding AreaList: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}
		areas = new Area[parameters.length];
		for (int count = 0; count < parameters.length; count++) {
			Parameter p = parameters[count];

			if (p.getTagClass() != Tag.CLASS_UNIVERSAL || p.isPrimitive() || p.getTag() != Tag.SEQUENCE) {
				throw new MAPParsingComponentException(
						"Error while decoding LCSPrivacyCheck: Parameter 0 [callSessionUnrelated [0] PrivacyCheckRelatedAction] bad tag class, tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}

			areas[count] = new AreaImpl();
			areas[count].decode(p);
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.areas == null || this.areas.length == 0) {
			throw new MAPException("Error while encoding AreaList the mandatory parameter[Area[]] is not defined");
		}

		for (int count = 0; count < areas.length; count++) {
			Parameter p = areas[count].encode();
			p.setPrimitive(false);
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setTag(Tag.SEQUENCE);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding AreaList. Encdoing of Area failed", e);
			}
		}

	}

}
