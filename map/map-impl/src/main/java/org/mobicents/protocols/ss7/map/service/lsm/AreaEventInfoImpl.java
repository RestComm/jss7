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
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class AreaEventInfoImpl extends MAPPrimitiveBase implements AreaEventInfo {

	private AreaDefinition areaDefinition = null;
	private OccurrenceInfo occurrenceInfo = null;
	private Integer intervalTime = null;

	/**
	 * 
	 */
	public AreaEventInfoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param areaDefinition
	 * @param occurrenceInfo
	 * @param intervalTime
	 */
	public AreaEventInfoImpl(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime) {
		super();
		this.areaDefinition = areaDefinition;
		this.occurrenceInfo = occurrenceInfo;
		this.intervalTime = intervalTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo#
	 * getAreaDefinition()
	 */
	@Override
	public AreaDefinition getAreaDefinition() {
		return this.areaDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo#
	 * getOccurrenceInfo()
	 */
	@Override
	public OccurrenceInfo getOccurrenceInfo() {
		return this.occurrenceInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo#getIntervalTime
	 * ()
	 */
	@Override
	public Integer getIntervalTime() {
		return this.intervalTime;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding AreaEventInfo: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];

		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException(
					"Error while decoding AreaEventInfo: Parameter 0 [areaDefinition [0] AreaDefinition] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.areaDefinition = new AreaDefinitionImpl();
		this.areaDefinition.decode(p);

		for (int count = 1; count < parameters.length; count++) {
			p = parameters[count];
			switch (p.getTag()) {
			case 1:
				// occurrenceInfo [1] OccurrenceInfo OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding AreaEventInfo: Parameter 1 [occurrenceInfo [1] OccurrenceInfo] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.occurrenceInfo = OccurrenceInfo.getOccurrenceInfo(p.getData()[0]);
				break;
			case 2:
				// intervalTime [2] IntervalTime OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding AreaEventInfo: Parameter 2 [intervalTime [2] IntervalTime] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				byte[] data = p.getData();

				byte temp;
				this.intervalTime = 0;

				for (int i = 0; i < data.length; i++) {
					temp = data[i];
					this.intervalTime = (this.intervalTime << 8) | (0x00FF & temp);
				}
				break;
			default:
//				throw new MAPParsingComponentException(
//						"Error while decoding AreaEventInfo: Expected occurrenceInfo [1] OccurrenceInfo or intervalTime [2] IntervalTime, but found"
//								+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
				break;
			}
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.areaDefinition == null) {
			throw new MAPException("Error while encoding AreaEventInfo the mandatory parameter[areaDefinition [0] AreaDefinition] is not defined");
		}

		Parameter p = this.areaDefinition.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setPrimitive(false);
		p.setTag(0);

		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Error while encoding AreaEventInfo the mandatory parameter[areaDefinition [0] AreaDefinition] is not defined", e);
		}

		if (this.occurrenceInfo != null) {
			asnOs.write(0x81);
			asnOs.write(0x01);
			asnOs.write(this.occurrenceInfo.getInfo());
		}

		if (this.intervalTime != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 2, this.intervalTime);
			} catch (IOException e) {
				throw new MAPException("Error while encoding AreaEventInfo. Encdoing of the parameter[intervalTime [2] IntervalTime] failed", e);
			} catch (AsnException e) {
				throw new MAPException("Error while encoding AreaEventInfo. Encdoing of the parameter[intervalTime [2] IntervalTime] failed", e);
			}
		}

	}

}
