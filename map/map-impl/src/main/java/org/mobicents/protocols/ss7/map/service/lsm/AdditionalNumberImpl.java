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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * TODO Self generated trace. Please test from real trace
 * 
 * @author amit bhayani
 * 
 */
public class AdditionalNumberImpl extends MAPPrimitiveBase implements AdditionalNumber {
	private ISDNAddressString mSCNumber = null;
	private ISDNAddressString sGSNNumber = null;

	/**
	 * 
	 */
	public AdditionalNumberImpl() {
		super();
	}

	/**
	 * @param mSCNumber
	 * @param sGSNNumber
	 */
	public AdditionalNumberImpl(ISDNAddressString mSCNumber, ISDNAddressString sGSNNumber) {
		super();
		this.mSCNumber = mSCNumber;
		this.sGSNNumber = sGSNNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber#getMSCNumber
	 * ()
	 */
	@Override
	public ISDNAddressString getMSCNumber() {
		return this.mSCNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber#
	 * getSGSNNumber()
	 */
	@Override
	public ISDNAddressString getSGSNNumber() {
		return this.sGSNNumber;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length != 1) {
			throw new MAPParsingComponentException("Error while decoding AdditionalNumber: Needs 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		Parameter p = parameters[0];

		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
			throw new MAPParsingComponentException("Error while decoding AdditionalNumber: Parameter bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		if (p.getTag() == 0) {
			this.mSCNumber = new ISDNAddressStringImpl();
			this.mSCNumber.decode(p);
		} else if (p.getTag() == 1) {
			this.sGSNNumber = new ISDNAddressStringImpl();
			this.sGSNNumber.decode(p);
		} else {
			throw new MAPParsingComponentException(
					"Error while decoding AdditionalNumber: Expexted msc-Number [0] ISDN-AddressString or sgsn-Number [1] ISDN-AddressString, but found "
							+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.mSCNumber != null) {
			Parameter p = this.mSCNumber.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding AdditionalNumber. Encoding of Parameter 0 [msc-Number [0] ISDN-AddressString] failed", e);
			}

		} else if (this.sGSNNumber != null) {
			Parameter p = this.sGSNNumber.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(1);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding AdditionalNumber. Encoding of Parameter 1 [sgsn-Number [1] ISDN-AddressString] failed", e);
			}
		} else {
			throw new MAPException("Error while encoding AdditionalNumber either msc-Number or sgsn-Number should be set");
		}
	}

}
