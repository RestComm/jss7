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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class LCSPrivacyCheckImpl extends MAPPrimitiveBase implements LCSPrivacyCheck {

	private PrivacyCheckRelatedAction callSessionUnrelated = null;
	private PrivacyCheckRelatedAction callSessionRelated = null;

	/**
	 * 
	 */
	public LCSPrivacyCheckImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param callSessionUnrelated
	 * @param callSessionRelated
	 */
	public LCSPrivacyCheckImpl(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated) {
		super();
		this.callSessionUnrelated = callSessionUnrelated;
		this.callSessionRelated = callSessionRelated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck#
	 * getCallSessionUnrelated()
	 */
	@Override
	public PrivacyCheckRelatedAction getCallSessionUnrelated() {
		return this.callSessionUnrelated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck#
	 * getCallSessionRelated()
	 */
	@Override
	public PrivacyCheckRelatedAction getCallSessionRelated() {
		return this.callSessionRelated;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding LCSPrivacyCheck: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode callSessionUnrelated [0] PrivacyCheckRelatedAction
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSPrivacyCheck: Parameter 0 [callSessionUnrelated [0] PrivacyCheckRelatedAction] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.callSessionUnrelated = PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(p.getData()[0]);

		if (parameters.length == 2) {
			p = parameters[1];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 1) {
				throw new MAPParsingComponentException(
						"Error while decoding LCSPrivacyCheck: Parameter 1 [callSessionRelated [1] PrivacyCheckRelatedAction] bad tag class, tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}

			this.callSessionRelated = PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(p.getData()[0]);
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.callSessionUnrelated == null) {
			throw new MAPException("Error while encoding LCSPrivacyCheck the mandatory parameter callSessionUnrelated is not defined");
		}

		// Encode mandatory callSessionUnrelated [0] PrivacyCheckRelatedAction
		asnOs.write(0x80);
		asnOs.write(0x01);
		asnOs.write(this.callSessionUnrelated.getAction());

		if (this.callSessionRelated != null) {
			asnOs.write(0x81);
			asnOs.write(0x01);
			asnOs.write(this.callSessionRelated.getAction());
		}

	}

}
