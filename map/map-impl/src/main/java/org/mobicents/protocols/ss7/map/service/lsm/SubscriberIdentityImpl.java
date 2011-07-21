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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SubscriberIdentityImpl extends MAPPrimitiveBase implements SubscriberIdentity {
	private IMSI imsi = null;
	private ISDNAddressString msisdn = null;
	
	/**
	 * 
	 */
	public SubscriberIdentityImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imsi
	 */
	public SubscriberIdentityImpl(IMSI imsi) {
		super();
		this.imsi = imsi;
		this.msisdn = null;
	}

	/**
	 * @param msisdn
	 */
	public SubscriberIdentityImpl(ISDNAddressString msisdn) {
		super();
		this.msisdn = msisdn;
		this.imsi = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getIMSI
	 * ()
	 */
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getMSISDN
	 * ()
	 */
	@Override
	public ISDNAddressString getMSISDN() {
		return this.msisdn;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();
		if (parameters == null || parameters.length != 1) {
			throw new MAPParsingComponentException("Error while decoding SubscriberIdentity: At least 1 mandatory parameters should be present but have"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
			throw new MAPParsingComponentException("Error while decoding SubscriberIdentity: Parameter bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		switch (p.getTag()) {
		case 0:
			// imsi [0] IMSI
			this.imsi = new IMSIImpl();
			this.imsi.decode(p);
			break;
		case 1:
			this.msisdn = new ISDNAddressStringImpl();
			this.msisdn.decode(p);
			break;
		default:
			throw new MAPParsingComponentException("Error while decoding SubscriberIdentity: Expected tags 0 or 1 but found" + p.getTag(),
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {
		if (this.imsi != null) {
			Parameter p = this.imsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(0);
			p.setPrimitive(true);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding SubscriberIdentity: Encoding of imsi [0] IMSI failed", e);
			}
		} else if (this.msisdn != null) {
			Parameter p = this.msisdn.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(1);
			p.setPrimitive(true);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding SubscriberIdentity: Encoding of msisdn [1] ISDN-AddressString failed", e);
			}
		} else {
			throw new MAPException("Error while encoding SubscriberIdentity: One of the IMSI or MSISDN should be prsent");
		}
	}

}
