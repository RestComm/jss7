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
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequestIndication;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationRequestIndicationImpl extends LsmMessageImpl implements ProvideSubscriberLocationRequestIndication {

	private LocationType locationType = null;
	private ISDNAddressString mlcNumber = null;
	private LCSClientID lcsClientID = null;
	private Boolean privacyOverride = false;
	private IMSI imsi = null;
	private ISDNAddressString msisdn;
	private LMSI lmsi = null;
	private IMEI imei = null;
	private Integer lcsPriority = 1;
	private LCSQoS lcsQoS = null;
	private MAPExtensionContainer extensionContainer = null;
	private BitSet supportedGADShapes = null;
	private Byte lcsReferenceNumber = null;
	private Integer lcsServiceTypeID = -1;
	private LCSCodeword lcsCodeword = null;
	private LCSPrivacyCheck lcsPrivacyCheck = null;
	private AreaEventInfo areaEventInfo = null;
	private byte[] hgmlcAddress = null;

	/**
	 * 
	 */
	public ProvideSubscriberLocationRequestIndicationImpl() {
		super();
	}

	/**
	 * @param locationType
	 * @param mlcNumber
	 * @param lcsClientID
	 * @param privacyOverride
	 * @param imsi
	 * @param msisdn
	 * @param lmsi
	 * @param imei
	 * @param lcsPriority
	 * @param lcsQoS
	 * @param extensionContainer
	 * @param supportedGADShapes
	 * @param lcsReferenceNumber
	 * @param lcsServiceTypeID
	 * @param lcsCodeword
	 * @param lcsPrivacyCheck
	 * @param areaEventInfo
	 * @param hgmlcAddress
	 */
	public ProvideSubscriberLocationRequestIndicationImpl(LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID,
			Boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei, Integer lcsPriority, LCSQoS lcsQoS,
			MAPExtensionContainer extensionContainer, BitSet supportedGADShapes, Byte lcsReferenceNumber, Integer lcsServiceTypeID, LCSCodeword lcsCodeword,
			LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, byte[] hgmlcAddress) {
		super();
		this.locationType = locationType;
		this.mlcNumber = mlcNumber;
		this.lcsClientID = lcsClientID;
		this.privacyOverride = privacyOverride;
		this.imsi = imsi;
		this.msisdn = msisdn;
		this.lmsi = lmsi;
		this.imei = imei;
		this.lcsPriority = lcsPriority;
		this.lcsQoS = lcsQoS;
		this.extensionContainer = extensionContainer;
		this.supportedGADShapes = supportedGADShapes;
		this.lcsReferenceNumber = lcsReferenceNumber;
		this.lcsServiceTypeID = lcsServiceTypeID;
		this.lcsCodeword = lcsCodeword;
		this.lcsPrivacyCheck = lcsPrivacyCheck;
		this.areaEventInfo = areaEventInfo;
		this.hgmlcAddress = hgmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLocationType()
	 */
	@Override
	public LocationType getLocationType() {
		return this.locationType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getMlcNumber()
	 */
	@Override
	public ISDNAddressString getMlcNumber() {
		return this.mlcNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSClientID()
	 */
	@Override
	public LCSClientID getLCSClientID() {
		return this.lcsClientID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getPrivacyOverride()
	 */
	@Override
	public Boolean getPrivacyOverride() {
		return this.privacyOverride;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getIMSI()
	 */
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getMSISDN()
	 */
	@Override
	public ISDNAddressString getMSISDN() {
		return this.msisdn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLMSI()
	 */
	@Override
	public LMSI getLMSI() {
		return this.lmsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSPriority()
	 */
	@Override
	public Integer getLCSPriority() {
		return this.lcsPriority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSQoS()
	 */
	@Override
	public LCSQoS getLCSQoS() {
		return this.lcsQoS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getIMEI()
	 */
	@Override
	public IMEI getIMEI() {
		return this.imei;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getSupportedGADShapes()
	 */
	@Override
	public BitSet getSupportedGADShapes() {
		return this.supportedGADShapes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSReferenceNumber()
	 */
	@Override
	public Byte getLCSReferenceNumber() {
		return this.lcsReferenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSCodeword()
	 */
	@Override
	public LCSCodeword getLCSCodeword() {
		return this.lcsCodeword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSServiceTypeID()
	 */
	@Override
	public Integer getLCSServiceTypeID() {
		return this.lcsServiceTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSPrivacyCheck()
	 */
	@Override
	public LCSPrivacyCheck getLCSPrivacyCheck() {
		return this.lcsPrivacyCheck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getAreaEventInfo()
	 */
	@Override
	public AreaEventInfo getAreaEventInfo() {
		return this.areaEventInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getHGMLCAddress()
	 */
	@Override
	public byte[] getHGMLCAddress() {
		return this.hgmlcAddress;
	}

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters.length < 2) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationRequestIndication: Needs at least 2 mandatory parameters, found"
							+ +(parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory locationType LocationType,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || p.isPrimitive() || p.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter 0[locationType LocationType] bad tag class or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.locationType = new LocationTypeImpl();
		this.locationType.decode(p);

		// Decode mandatory mlc-Number ISDN-AddressString,
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.STRING_OCTET) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter 1[mlc-Number ISDN-AddressString] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.mlcNumber = new ISDNAddressStringImpl();
		this.mlcNumber.decode(p);

		for (int count = 2; count < parameters.length; count++) {
			p = parameters[count];
			switch (p.getTag()) {
			case 0:
				// lcs-ClientID [0] LCS-ClientID OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-ClientID [0] LCS-ClientID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				lcsClientID = new LCSClientIDImpl();
				lcsClientID.decode(p);

				break;
			case 1:
				// privacyOverride [1] NULL OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [privacyOverride [1] NULL ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.privacyOverride = true;
				break;
			case 2:
				// imsi [2] IMSI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [imsi [2] IMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsi = new IMSIImpl();
				this.imsi.decode(p);
				break;
			case 3:
				// msisdn [3] ISDN-AddressString OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [msisdn [3] ISDN-AddressString ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.msisdn = new ISDNAddressStringImpl();
				this.msisdn.decode(p);
				break;
			case 4:
				// lmsi [4] LMSI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lmsi [4] LMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lmsi = new LMSIImpl();
				this.lmsi.decode(p);
				break;
			case 5:
				// imei [5] IMEI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [imei [5] IMEI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imei = new IMEIImpl();
				this.imei.decode(p);
				break;
			case 6:
				// lcs-Priority [6] LCS-Priority OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-Priority [6] LCS-Priority ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsPriority = new Integer(p.getData()[0]);
				break;
			case 7:
				// lcs-QoS [7] LCS-QoS OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-QoS [7] LCS-QoS ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsQoS = new LCSQoSImpl();
				this.lcsQoS.decode(p);
				break;
			case 8:
				// extensionContainer [8] ExtensionContainer OPTIONAL,
				// TODO add check here
				this.extensionContainer = new MAPExtensionContainerImpl();
				this.extensionContainer.decode(p);
				break;
			case 9:
				// supportedGADShapes [9] SupportedGADShapes OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [supportedGADShapes [9] SupportedGADShapes] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.supportedGADShapes = new BitSet(16);
				AsnInputStream asnInputStream = new AsnInputStream(new ByteArrayInputStream(p.getData()));
				try {
					asnInputStream.readBitStringData(this.supportedGADShapes, p.getData().length, true);
				} catch (AsnException e) {
					throw new MAPParsingComponentException("Decode ProvideSubscriberLocationRequestIndication failed. Failed to decode SupportedGADShapes", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				} catch (IOException e) {
					throw new MAPParsingComponentException("Decode ProvideSubscriberLocationRequestIndication failed. Failed to decode SupportedGADShapes", e,
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				break;
			case 10:
				// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
				// TODO LCS-ReferenceNumber is OCTET String of size 1. Just take
				// the byte from param?
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-ReferenceNumber [10] LCS-ReferenceNumber] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsReferenceNumber = p.getData()[0];
				break;
			case 11:
				// lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcsServiceTypeID [11] LCSServiceTypeID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsServiceTypeID = new Integer(p.getData()[0]);
				break;
			case 12:
				// lcsCodeword [12] LCSCodeword OPTIONAL,

				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcsCodeword [12] LCSCodeword] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.lcsCodeword = new LCSCodewordImpl();
				this.lcsCodeword.decode(p);
				break;
			case 13:
				// lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-PrivacyCheck [13] LCS-PrivacyCheck] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsPrivacyCheck = new LCSPrivacyCheckImpl();
				this.lcsPrivacyCheck.decode(p);
				break;
			case 14:
				// areaEventInfo [14] AreaEventInfo OPTIONAL
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [areaEventInfo [14] AreaEventInfo] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.areaEventInfo = new AreaEventInfoImpl();
				this.areaEventInfo.decode(p);
				break;
			case 15:
				// h-gmlc-Address [15] GSN-Address OPTIONAL
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [h-gmlc-Address [15] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.hgmlcAddress = p.getData();
				break;
			default:
//				throw new MAPParsingComponentException("Error while decoding ProvideSubscriberLocationRequestIndication: Expected tags 0 - 15 but found"
//						+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.locationType == null) {
			throw new MAPException("Error while encoding ProvideSubscriberLocation the mandatory parameter locationType is not defined");
		}

		if (this.mlcNumber == null) {
			throw new MAPException("Error while encoding ProvideSubscriberLocation the mandatory parameter mlc-Number is not defined");
		}

		// locationType LocationType
		Parameter p = this.locationType.encode();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);

		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse locationType LocationType", e);
		}

		// mlc-Number ISDN-AddressString,
		p = this.mlcNumber.encode();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(true);
		p.setTag(Tag.STRING_OCTET);

		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse mlc-Number ISDN-AddressString", e);
		}

		if (this.lcsClientID != null) {
			// lcs-ClientID [0] LCS-ClientID OPTIONAL,

			p = this.lcsClientID.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(0x00);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse lcs-ClientID [0] LCS-ClientID", e);
			}
		}

		if (this.privacyOverride != null) {
			// privacyOverride [1] NULL OPTIONAL,
			asnOs.write(0x81);
			asnOs.write(0x00);// NULL
		}

		if (this.imsi != null) {
			// imsi [2] IMSI OPTIONAL,
			p = this.imsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x02);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse imsi [2] IMSI", e);
			}
		}

		if (this.msisdn != null) {
			// msisdn [3] ISDN-AddressString
			p = this.msisdn.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x03);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse msisdn [3] ISDN-AddressString", e);
			}
		}

		if (this.lmsi != null) {
			// lmsi [4] LMSI
			p = this.lmsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x04);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse lmsi [4] LMSI", e);
			}
		}

		if (this.imei != null) {
			// imei [5] IMEI
			p = this.imei.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x05);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse imei [5] IMEI", e);
			}
		}

		if (this.lcsPriority != null) {
			// lcs-Priority [6] LCS-Priority
			asnOs.write(0x86);
			asnOs.write(0x01);
			asnOs.write(this.lcsPriority);
		}

		if (this.lcsQoS != null) {
			// lcs-QoS [7] LCS-QoS
			p = this.lcsQoS.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(0x07);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse lcs-QoS [7] LCS-QoS", e);
			}
		}

		if (this.extensionContainer != null) {
			// extensionContainer [8] ExtensionContainer
			p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true); // TODO Primitive?
			p.setTag(0x08);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse extensionContainer [8] ExtensionContainer", e);
			}
		}

		if (this.supportedGADShapes != null) {
			try {

				asnOs.writeStringBinary(Tag.CLASS_CONTEXT_SPECIFIC, 9, this.supportedGADShapes);
			} catch (AsnException e) {
				throw new MAPException("Error while encoding ProvideSubscriberLocation parameter9[supportedGADShapes [9] SupportedGADShapes]", e);
			} catch (IOException e) {
				throw new MAPException("Error while encoding ProvideSubscriberLocation parameter9[supportedGADShapes [9] SupportedGADShapes]", e);
			}
		}

		if (this.lcsReferenceNumber != null) {
			// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
			asnOs.write(0x8a);
			asnOs.write(0x01);
			asnOs.write(this.lcsReferenceNumber);
		}

		if (this.lcsServiceTypeID != null) {
			// lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
			asnOs.write(0x8b);
			asnOs.write(0x01);
			asnOs.write(this.lcsServiceTypeID);

		}

		if (this.lcsCodeword != null) {
			// lcsCodeword [12] LCSCodeword
			p = this.lcsCodeword.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(0x0c);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse lcsCodeword [12] LCSCodeword", e);
			}
		}

		if (this.lcsPrivacyCheck != null) {
			// lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL
			p = this.lcsCodeword.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(0x0d);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse lcs-PrivacyCheck [13] LCS-PrivacyCheck", e);
			}
		}

		if (this.areaEventInfo != null) {
			// areaEventInfo [14] AreaEventInfo OPTIONAL
			p = this.areaEventInfo.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(0x0e);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse areaEventInfo [14] AreaEventInfo", e);
			}
		}

		if (this.hgmlcAddress != null) {
			if (this.hgmlcAddress.length < 5 || this.hgmlcAddress.length > 17) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. h-gmlc-Address is either less than 5 or greater than 17");
			}
			// h-gmlc-Address [15] GSN-Address OPTIONAL
			asnOs.write(0x8f);
			asnOs.write(this.hgmlcAddress.length);
			try {
				asnOs.write(this.hgmlcAddress);
			} catch (IOException e) {
				throw new MAPException("Encoding of ProvideSubscriberLocation failed. Failed to parse h-gmlc-Address [15] GSN-Address LCS-QoS", e);
			}
		}

	}

}
