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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class LCSClientIDImpl extends MAPPrimitiveBase implements LCSClientID {

	private LCSClientType lcsClientType = null;
	private LCSClientExternalID lcsClientExternalID = null;
	private LCSClientInternalID lcsClientInternalID = null;
	private LCSClientName lcsClientName = null;
	private AddressString lcsClientDialedByMS = null;
	private byte[] lcsAPN = null;
	private LCSRequestorID lcsRequestorID = null;

	/**
	 * @param lcsClientType
	 * @param lcsClientExternalID
	 * @param lcsClientInternalID
	 * @param lcsClientName
	 * @param lcsClientDialedByMS
	 * @param lcsAPN
	 * @param lcsRequestorID
	 */
	public LCSClientIDImpl(LCSClientType lcsClientType, LCSClientExternalID lcsClientExternalID, LCSClientInternalID lcsClientInternalID,
			LCSClientName lcsClientName, AddressString lcsClientDialedByMS, byte[] lcsAPN, LCSRequestorID lcsRequestorID) {
		super();
		this.lcsClientType = lcsClientType;
		this.lcsClientExternalID = lcsClientExternalID;
		this.lcsClientInternalID = lcsClientInternalID;
		this.lcsClientName = lcsClientName;
		this.lcsClientDialedByMS = lcsClientDialedByMS;
		this.lcsAPN = lcsAPN;
		this.lcsRequestorID = lcsRequestorID;
	}

	/**
	 * 
	 */
	public LCSClientIDImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientType
	 * ()
	 */
	@Override
	public LCSClientType getLCSClientType() {
		return this.lcsClientType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientExternalID()
	 */
	@Override
	public LCSClientExternalID getLCSClientExternalID() {
		return this.lcsClientExternalID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientDialedByMS()
	 */
	@Override
	public AddressString getLCSClientDialedByMS() {
		return this.lcsClientDialedByMS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#
	 * getLCSClientInternalID()
	 */
	@Override
	public LCSClientInternalID getLCSClientInternalID() {
		return this.lcsClientInternalID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSClientName
	 * ()
	 */
	@Override
	public LCSClientName getLCSClientName() {
		return this.lcsClientName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSAPN()
	 */
	@Override
	public byte[] getLCSAPN() {
		return this.lcsAPN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID#getLCSRequestorID
	 * ()
	 */
	@Override
	public LCSRequestorID getLCSRequestorID() {
		return this.lcsRequestorID;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding LCSClientID: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory lcsClientType [0] LCSClientType,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != 0) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSClientID: Parameter 0[lcsClientType [0] LCSClientType] bad tag class, tag or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.lcsClientType = LCSClientType.getLCSClientType(p.getData()[0]);

		for (int count = 1; count < parameters.length; count++) {
			p = parameters[count];

			switch (p.getTag()) {

			case 1:
				// Optional lcsClientExternalID [1] LCSClientExternalID
				// OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive() || p.getTag() != 1) {
					throw new MAPParsingComponentException("Error while decoding LCSClientExternalID: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.lcsClientExternalID = new LCSClientExternalIDImpl();
				this.lcsClientExternalID.decode(p);
				break;
			case 2:
				// lcsClientDialedByMS [2] AddressString OPTIONAL,
				this.lcsClientDialedByMS = new AddressStringImpl();
				this.lcsClientDialedByMS.decode(p);
				break;
			case 3:
				// lcsClientInternalID [3] LCSClientInternalID OPTIONAL
				this.lcsClientInternalID = LCSClientInternalID.getLCSClientInternalID(p.getData()[0]);
				break;
			case 4:
				// lcsClientName [4] LCSClientName OPTIONAL,

				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive() || p.getTag() != 4) {
					throw new MAPParsingComponentException("Error while decoding LCSClientName: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.lcsClientName = new LCSClientNameImpl();
				this.lcsClientName.decode(p);

				break;
			case 5:
				// lcsAPN [5] APN OPTIONAL,
				// TODO OctetString is screwed
				break;
			case 6:
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive() || p.getTag() != 6) {
					throw new MAPParsingComponentException("Error while decoding LCSRequestorID: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				// lcsRequestorID [6] LCSRequestorID OPTIONAL
				this.lcsRequestorID = new LCSRequestorIDImpl();
				this.lcsRequestorID.decode(p);
				break;
			default:
				throw new MAPParsingComponentException(
						"Decoding LCSClientID failed. Expected lcsClientExternalID [1], lcsClientDialedByMS [2], lcsClientInternalID [3], lcsClientName [4], lcsAPN [5], lcsRequestorID [6]  but found "
								+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {
		if (this.lcsClientType == null) {
			throw new MAPException("Error while encoding LCSClientID the mandatory parameter lcsClientType is not defined");
		}

		// Encode locationEstimateType [0] LocationEstimateType
		asnOs.write(0x80);// TAG
		asnOs.write(0x01);
		asnOs.write(this.lcsClientType.getType());

		if (this.lcsClientExternalID != null) {
			// Encode lcsClientExternalID [1] LCSClientExternalID OPTIONAL,
			Parameter p = this.lcsClientExternalID.encode();
			p.setPrimitive(true);
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(1);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientID failed. Failed to Encode lcsClientExternalID [1] LCSClientExternalID", e);
			}

		}

		if (this.lcsClientDialedByMS != null) {
			Parameter p = this.lcsClientDialedByMS.encode();
			p.setPrimitive(true);
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(2);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientID failed. Failed to Encode lcsClientExternalID [1] LCSClientExternalID", e);
			}
		}

		if (this.lcsClientInternalID != null) {
			asnOs.write(0x83);
			asnOs.write(0x01);
			asnOs.write(this.lcsClientInternalID.getId());
		}

		if (this.lcsClientName != null) {
			Parameter p = this.lcsClientName.encode();
			p.setPrimitive(false);
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(4);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientID failed. Failed to Encode lcsClientExternalID [1] LCSClientExternalID", e);
			}
		}
		
		if(this.lcsAPN != null){
			asnOs.write(0x85);
			//TODO : Is this correct?
			asnOs.write(this.lcsAPN.length);
			try {
				asnOs.write(this.lcsAPN);
			} catch (IOException e) {
				throw new MAPException("Encoding of LCSClientID failed. Failed to Encode lcsAPN [5] APN", e);
			}
		}
		
		if (this.lcsRequestorID != null) {
			Parameter p = this.lcsRequestorID.encode();
			p.setPrimitive(true);
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(6);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientID failed. Failed to Encode lcsClientExternalID [1] LCSClientExternalID", e);
			}
		}

	}
}
