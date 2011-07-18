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
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaList;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class AreaDefinitionImpl extends MAPPrimitiveBase implements AreaDefinition {

	private AreaList areaList = null;

	/**
	 * 
	 */
	public AreaDefinitionImpl() {
		super();
	}

	/**
	 * @param areaList
	 */
	public AreaDefinitionImpl(AreaList areaList) {
		super();
		this.areaList = areaList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition#getAreaList
	 * ()
	 */
	@Override
	public AreaList getAreaList() {
		return this.areaList;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length != 1) {
			throw new MAPParsingComponentException("Error while decoding AreaDefinition: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];

		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException(
					"Error while decoding AreaDefinition: Parameter 0 [areaList [0] AreaList] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.areaList = new AreaListImpl();
		this.areaList.decode(p);
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.areaList == null) {
			throw new MAPException("Error while encoding AreaDefinition the mandatory parameter[areaList [0] AreaList] is not defined");
		}

		Parameter p = this.areaList.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(0);
		p.setPrimitive(false);
		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Error while encoding AreaDefinition. Encdoing fialed for mandatory parameter[areaList [0] AreaList] is not defined", e);
		}
	}

}
