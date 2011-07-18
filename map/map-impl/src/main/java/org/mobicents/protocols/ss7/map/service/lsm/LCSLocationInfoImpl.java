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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class LCSLocationInfoImpl extends MAPPrimitiveBase implements LCSLocationInfo {

	private ISDNAddressString networkNodeNumber = null;
	private LMSI lmsi = null;
	private MAPExtensionContainer extensionContainer = null;
	private Boolean gprsNodeIndicator = null;
	private AdditionalNumber additionalNumber = null;
	private BitSet supportedLCSCapabilitySets = null;
	private BitSet additionalLCSCapabilitySets = null;

	/**
	 * 
	 */
	public LCSLocationInfoImpl() {
		super();
	}
	
	/**
	 * @param networkNodeNumber
	 * @param lmsi
	 * @param extensionContainer
	 * @param gprsNodeIndicator
	 * @param additionalNumber
	 * @param supportedLCSCapabilitySets
	 * @param additionalLCSCapabilitySets
	 */
	public LCSLocationInfoImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, Boolean gprsNodeIndicator,
			AdditionalNumber additionalNumber, BitSet supportedLCSCapabilitySets, BitSet additionalLCSCapabilitySets) {
		super();
		this.networkNodeNumber = networkNodeNumber;
		this.lmsi = lmsi;
		this.extensionContainer = extensionContainer;
		this.gprsNodeIndicator = gprsNodeIndicator;
		this.additionalNumber = additionalNumber;
		this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
		this.additionalLCSCapabilitySets = additionalLCSCapabilitySets;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getNetworkNodeNumber()
	 */
	@Override
	public ISDNAddressString getNetworkNodeNumber() {
		return this.networkNodeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#getLMSI()
	 */
	@Override
	public LMSI getLMSI() {
		return this.lmsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getGprsNodeIndicator()
	 */
	@Override
	public Boolean getGprsNodeIndicator() {
		return this.gprsNodeIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getAdditionalNumber()
	 */
	@Override
	public AdditionalNumber getAdditionalNumber() {
		return this.additionalNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getSupportedLCSCapabilitySets()
	 */
	@Override
	public BitSet getSupportedLCSCapabilitySets() {
		return this.supportedLCSCapabilitySets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo#
	 * getadditionalLCSCapabilitySets()
	 */
	@Override
	public BitSet getAdditionalLCSCapabilitySets() {
		return this.additionalLCSCapabilitySets;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 1) {
			throw new MAPParsingComponentException("Error while decoding LCSLocationInfo: Needs at least 1 mandatory parameters, found"
					+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory dataCodingScheme [0] USSD-DataCodingScheme,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.STRING_OCTET) {
			throw new MAPParsingComponentException(
					"Error while decoding LCSLocationInfo: Parameter [networkNode-Number ISDN-AddressString] bad tag class, tag or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.networkNodeNumber = new ISDNAddressStringImpl();
		this.networkNodeNumber.decode(p);

		for (int count = 1; count < parameters.length; count++) {
			p = parameters[0];
			switch (p.getTag()) {
			case 0:
				// lmsi [0] LMSI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [lmsi [0] LMSI ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lmsi = new LMSIImpl();
				this.lmsi.decode(p);

				break;
			case 1:
				// extensionContainer [1] ExtensionContainer
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [lmsi [0] LMSI ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer = new MAPExtensionContainerImpl();
				this.extensionContainer.decode(p);

				break;
			case 2:
				// gprsNodeIndicator [2] NULL
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding LCSLocationInfo: Parameter [lmsi [0] LMSI ] bad tag class, tag or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.gprsNodeIndicator = true;
				break;
			case 3:
				// additional-Number [3] Additional-Number OPTIONAL
				this.additionalNumber = new AdditionalNumberImpl();
				this.additionalNumber.decode(p);
				break;
			case 4:
				// supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets
				AsnInputStream asnInputStream = new AsnInputStream(new ByteArrayInputStream(p.getData()));
				// TODO what should be length of BitSet?
				this.supportedLCSCapabilitySets = new BitSet();
				try {
					asnInputStream.readBitStringData(this.supportedLCSCapabilitySets, p.getData().length, true);
				} catch (AsnException e) {
					throw new MAPParsingComponentException(
							"Decode LCSLocationInfo failed. Failed to decode supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				} catch (IOException e) {
					throw new MAPParsingComponentException(
							"Decode LCSLocationInfo failed. Failed to decode supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				break;
			case 5:
				// additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets
				AsnInputStream asnInputStream1 = new AsnInputStream(new ByteArrayInputStream(p.getData()));
				// TODO what should be length of BitSet?
				this.additionalLCSCapabilitySets = new BitSet();
				try {
					asnInputStream1.readBitStringData(this.additionalLCSCapabilitySets, p.getData().length, true);
				} catch (AsnException e) {
					throw new MAPParsingComponentException(
							"Decode LCSLocationInfo failed. Failed to decode additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				} catch (IOException e) {
					throw new MAPParsingComponentException(
							"Decode LCSLocationInfo failed. Failed to decode additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				break;
			default:
				throw new MAPParsingComponentException("Error while decoding LCSLocationInfo: Expected tags 0 - 5 but found" + p.getTag(),
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}// end of for loop
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.networkNodeNumber == null) {
			throw new MAPException("Error while encoding LCSLocationInfo the mandatory parameter networkNode-Number ISDN-AddressString is not defined");
		}

		Parameter p = this.networkNodeNumber.encode();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.STRING_OCTET);
		p.setPrimitive(true);
		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Error while encoding LCSLocationInfo. Encdoing mandatory parameter networkNode-Number ISDN-AddressString failed", e);
		}

		// lmsi [0] LMSI OPTIONAL,

		if (this.lmsi != null) {
			// lmsi [0] LMSI OPTIONAL,
			p = this.lmsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(0);
			p.setPrimitive(true);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding LCSLocationInfo. Encdoing parameter 0 [lmsi [0] LMSI OPTIONAL] failed", e);
			}
		}

		if (this.extensionContainer != null) {
			// extensionContainer [1] ExtensionContainer OPTIONAL,
			p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(1);
			p.setPrimitive(true);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Error while encoding LCSLocationInfo. Encdoing parameter 1 [extensionContainer [1] ExtensionContainer OPTIONAL] failed", e);
			}
		}

		if (this.gprsNodeIndicator != null) {
			// gprsNodeIndicator [2] NULL OPTIONAL,
			asnOs.write(0x82);
			asnOs.write(0x00);
		}

		if (this.additionalNumber != null) {
			// additional-Number [3] Additional-Number OPTIONAL,
			p = this.additionalNumber.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(3);
			p.setPrimitive(false);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Error while encoding LCSLocationInfo. Encdoing parameter 3 [additional-Number [3] Additional-Number OPTIONAL] failed",
						e);
			}
		}

		if (this.supportedLCSCapabilitySets != null) {
			// supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets
			// OPTIONAL,
			try {
				asnOs.writeStringBinary(Tag.CLASS_CONTEXT_SPECIFIC, 4, this.supportedLCSCapabilitySets);
			} catch (AsnException e) {
				throw new MAPException(
						"Error while encoding LCSLocationInfo. Encdoing parameter 4 [supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets OPTIONAL] failed",
						e);
			} catch (IOException e) {
				throw new MAPException(
						"Error while encoding LCSLocationInfo. Encdoing parameter 4 [supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets OPTIONAL] failed",
						e);
			}
		}

		if (this.additionalLCSCapabilitySets != null) {
			// additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets
			// OPTIONAL
			try {
				asnOs.writeStringBinary(Tag.CLASS_CONTEXT_SPECIFIC, 5, this.additionalLCSCapabilitySets);
			} catch (AsnException e) {
				throw new MAPException(
						"Error while encoding LCSLocationInfo. Encdoing parameter 5 [additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL] failed",
						e);
			} catch (IOException e) {
				throw new MAPException(
						"Error while encoding LCSLocationInfo. Encdoing parameter 5 [additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL] failed",
						e);
			}
		}

	}

}
