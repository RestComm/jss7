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
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationRequestImpl extends LsmMessageImpl implements ProvideSubscriberLocationRequest {

	private static final int _TAG_LCS_CLIENT_ID = 0;
	private static final int _TAG_PRIVACY_OVERRIDE = 1;
	private static final int _TAG_IMSI = 2;
	private static final int _TAG_MSISDN = 3;
	private static final int _TAG_LMSI = 4;
	private static final int _TAG_IMEI = 5;
	private static final int _TAG_LCS_PRIORITY = 6;
	private static final int _TAG_LCS_QOS = 7;
	private static final int _TAG_EXTENSION_CONTAINER = 8;
	private static final int _TAG_SUPPORTED_GAD_SHAPES = 9;
	private static final int _TAG_LCS_REFERENCE_NUMBER = 10;
	private static final int _TAG_LCS_SERVICE_TYPE_ID = 11;
	private static final int _TAG_LCS_CODEWORD = 12;
	private static final int _TAG_LCS_PRIVACY_CHECK = 13;
	private static final int _TAG_AREA_EVENT_INFO = 14;
	private static final int _TAG_H_GMLC_ADDRESS = 15;

	private LocationType locationType = null;
	private ISDNAddressString mlcNumber = null;
	private LCSClientID lcsClientID = null;
	private Boolean privacyOverride = false;
	private IMSI imsi = null;
	private ISDNAddressString msisdn;
	private LMSI lmsi = null;
	private IMEI imei = null;
	private LCSPriority lcsPriority;
	private LCSQoS lcsQoS = null;
	private MAPExtensionContainer extensionContainer = null;
	private SupportedGADShapes supportedGADShapes = null;
	private Byte lcsReferenceNumber = null;
	private Integer lcsServiceTypeID = -1;
	private LCSCodeword lcsCodeword = null;
	private LCSPrivacyCheck lcsPrivacyCheck = null;
	private AreaEventInfo areaEventInfo = null;
	private GSNAddress hgmlcAddress = null;

	/**
	 * 
	 */
	public ProvideSubscriberLocationRequestImpl() {
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
	public ProvideSubscriberLocationRequestImpl(LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID,
			Boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei, LCSPriority lcsPriority, LCSQoS lcsQoS,
			MAPExtensionContainer extensionContainer, SupportedGADShapes supportedGADShapes, Byte lcsReferenceNumber, Integer lcsServiceTypeID,
			LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress) {
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

	public MAPMessageType getMessageType() {
		return MAPMessageType.provideSubscriberLocation_Request;
	}

	public int getOperationCode() {
		return MAPOperationCode.provideSubscriberLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLocationType()
	 */
	public LocationType getLocationType() {
		return this.locationType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getMlcNumber()
	 */
	public ISDNAddressString getMlcNumber() {
		return this.mlcNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSClientID()
	 */
	public LCSClientID getLCSClientID() {
		return this.lcsClientID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getPrivacyOverride()
	 */
	public Boolean getPrivacyOverride() {
		return this.privacyOverride;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getIMSI()
	 */
	public IMSI getIMSI() {
		return this.imsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getMSISDN()
	 */
	public ISDNAddressString getMSISDN() {
		return this.msisdn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLMSI()
	 */
	public LMSI getLMSI() {
		return this.lmsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSPriority()
	 */
	public LCSPriority getLCSPriority() {
		return this.lcsPriority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSQoS()
	 */
	public LCSQoS getLCSQoS() {
		return this.lcsQoS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getIMEI()
	 */
	public IMEI getIMEI() {
		return this.imei;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getExtensionContainer()
	 */
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getSupportedGADShapes()
	 */
	public SupportedGADShapes getSupportedGADShapes() {
		return this.supportedGADShapes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSReferenceNumber()
	 */
	public Byte getLCSReferenceNumber() {
		return this.lcsReferenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSCodeword()
	 */
	public LCSCodeword getLCSCodeword() {
		return this.lcsCodeword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSServiceTypeID()
	 */
	public Integer getLCSServiceTypeID() {
		return this.lcsServiceTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getLCSPrivacyCheck()
	 */
	public LCSPrivacyCheck getLCSPrivacyCheck() {
		return this.lcsPrivacyCheck;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getAreaEventInfo()
	 */
	public AreaEventInfo getAreaEventInfo() {
		return this.areaEventInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationRequestIndication#getHGMLCAddress()
	 */
	public GSNAddress getHGMLCAddress() {
		return this.hgmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int tag = ais.readTag();

		// Decode mandatory locationType LocationType,
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || tag != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationRequestIndication: [locationType LocationType] bad tag class or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.locationType = new LocationTypeImpl();
		((LocationTypeImpl)this.locationType).decodeAll(ais);

		// Decode mandatory mlc-Number ISDN-AddressString,
		tag = ais.readTag();
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationRequestIndication: [mlc-Number ISDN-AddressString] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.mlcNumber = new ISDNAddressStringImpl();
		((ISDNAddressStringImpl)this.mlcNumber).decodeAll(ais);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();

			switch (tag) {
			case _TAG_LCS_CLIENT_ID:
				// lcs-ClientID [0] LCS-ClientID OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-ClientID [0] LCS-ClientID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsClientID = new LCSClientIDImpl();
				((LCSClientIDImpl)this.lcsClientID).decodeAll(ais);
				break;
			case _TAG_PRIVACY_OVERRIDE:
				// privacyOverride [1] NULL OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [privacyOverride [1] NULL ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				ais.readLength();
				this.privacyOverride = true;
				break;
			case _TAG_IMSI:
				// imsi [2] IMSI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [imsi [2] IMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsi = new IMSIImpl();
				((IMSIImpl)this.imsi).decodeAll(ais);
				break;
			case _TAG_MSISDN:
				// msisdn [3] ISDN-AddressString OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [msisdn [3] ISDN-AddressString ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.msisdn = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl)this.msisdn).decodeAll(ais);
				break;
			case _TAG_LMSI:
				// lmsi [4] LMSI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lmsi [4] LMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lmsi = new LMSIImpl();
				((LMSIImpl)this.lmsi).decodeAll(ais);
				break;
			case _TAG_IMEI:
				// imei [5] IMEI OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [imei [5] IMEI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imei = new IMEIImpl();
				((IMEIImpl)this.imei).decodeAll(ais);
				break;
			case _TAG_LCS_PRIORITY:
				// lcs-Priority [6] LCS-Priority OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-Priority [6] LCS-Priority ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				int length1 = ais.readLength();
				this.lcsPriority = LCSPriority.getInstance((int) ais.readOctetStringData(length1)[0]);

				break;
			case _TAG_LCS_QOS:
				// lcs-QoS [7] LCS-QoS OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-QoS [7] LCS-QoS ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsQoS = new LCSQoSImpl();
				((LCSQoSImpl)this.lcsQoS).decodeAll(ais);
				break;
			case _TAG_EXTENSION_CONTAINER:
				// extensionContainer [8] ExtensionContainer OPTIONAL,
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			case _TAG_SUPPORTED_GAD_SHAPES:
				// supportedGADShapes [9] SupportedGADShapes OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [supportedGADShapes [9] SupportedGADShapes] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.supportedGADShapes = new SupportedGADShapesImpl();
				((SupportedGADShapesImpl)this.supportedGADShapes).decodeAll(ais);
				break;
			case _TAG_LCS_REFERENCE_NUMBER:
				// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-ReferenceNumber [10] LCS-ReferenceNumber] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.lcsReferenceNumber = new Byte(ais.readOctetStringData(length1)[0]);
				break;
			case _TAG_LCS_SERVICE_TYPE_ID:
				// lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcsServiceTypeID [11] LCSServiceTypeID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.lcsServiceTypeID = (int) ais.readIntegerData(length1);
				break;
			case _TAG_LCS_CODEWORD:
				// lcsCodeword [12] LCSCodeword OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcsCodeword [12] LCSCodeword] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsCodeword = new LCSCodewordImpl();
				((LCSCodewordImpl)this.lcsCodeword).decodeAll(ais);
				break;
			case _TAG_LCS_PRIVACY_CHECK:
				// lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [lcs-PrivacyCheck [13] LCS-PrivacyCheck] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsPrivacyCheck = new LCSPrivacyCheckImpl();
				((LCSPrivacyCheckImpl)this.lcsPrivacyCheck).decodeAll(ais);
				break;
			case _TAG_AREA_EVENT_INFO:
				// areaEventInfo [14] AreaEventInfo OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [areaEventInfo [14] AreaEventInfo] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.areaEventInfo = new AreaEventInfoImpl();
				((AreaEventInfoImpl)this.areaEventInfo).decodeAll(ais);
				break;
			case _TAG_H_GMLC_ADDRESS:
				// h-gmlc-Address [15] GSN-Address OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationRequestIndication: Parameter [h-gmlc-Address [15] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.hgmlcAddress = new GSNAddressImpl();
				((GSNAddressImpl)this.hgmlcAddress).decodeAll(ais);
				break;
			default:
				// Do we care?
				ais.advanceElement();
				break;
			}
		}// end of while
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.locationType == null) {
			throw new MAPException("Error while encoding ProvideSubscriberLocation the mandatory parameter locationType is not defined");
		}

		if (this.mlcNumber == null) {
			throw new MAPException("Error while encoding ProvideSubscriberLocation the mandatory parameter mlc-Number is not defined");
		}

		((LocationTypeImpl)this.locationType).encodeAll(asnOs);

		((ISDNAddressStringImpl)this.mlcNumber).encodeAll(asnOs);

		if (this.lcsClientID != null) {
			// lcs-ClientID [0] LCS-ClientID OPTIONAL
			((LCSClientIDImpl)this.lcsClientID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_ID);
		}

		if (this.privacyOverride != null) {
			// privacyOverride [1] NULL OPTIONAL
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PRIVACY_OVERRIDE);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter privacyOverride", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter privacyOverride", e);
			}
		}

		if (this.imsi != null) {
			// imsi [2] IMSI OPTIONAL
			((IMSIImpl)this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMSI);
		}

		if (this.msisdn != null) {
			// msisdn [3] ISDN-AddressString OPTIONAL,
			((ISDNAddressStringImpl)this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MSISDN);
		}

		if (this.lmsi != null) {
			// lmsi [4] LMSI OPTIONAL,
			((LMSIImpl)this.lmsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LMSI);
		}

		if (this.imei != null) {
			// imei [5] IMEI OPTIONAL,
			((IMEIImpl)this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMEI);
		}

		if (this.lcsPriority != null) {
			// lcs-Priority [6] LCS-Priority OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_PRIORITY, new byte[] { (byte)this.lcsPriority.getCode() });
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter lcsPriority", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter lcsPriority", e);
			}
		}

		if (this.lcsQoS != null) {
			// lcs-QoS [7] LCS-QoS OPTIONAL
			((LCSQoSImpl)this.lcsQoS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_QOS);
		}

		if (this.extensionContainer != null) {
			// extensionContainer [8] ExtensionContainer OPTIONAL,
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}

		if (this.supportedGADShapes != null) {
			// supportedGADShapes [9] SupportedGADShapes OPTIONAL
			((SupportedGADShapesImpl)this.supportedGADShapes).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SUPPORTED_GAD_SHAPES);
		}

		if (this.lcsReferenceNumber != null) {
			// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_REFERENCE_NUMBER, new byte[] { this.lcsReferenceNumber.byteValue() });
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter lcsReferenceNumber", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter lcsReferenceNumber", e);
			}
		}

		if (this.lcsServiceTypeID != null) {
			// lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_SERVICE_TYPE_ID, this.lcsServiceTypeID);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter lcsReferenceNumber", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter lcsReferenceNumber", e);
			}
		}

		if (this.lcsCodeword != null) {
			// lcsCodeword [12] LCSCodeword OPTIONAL,
			((LCSCodewordImpl)this.lcsCodeword).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CODEWORD);
		}

		if (this.lcsPrivacyCheck != null) {
			// lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
			((LCSPrivacyCheckImpl)this.lcsPrivacyCheck).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_PRIVACY_CHECK);
		}

		if (this.areaEventInfo != null) {
			// areaEventInfo [14] AreaEventInfo OPTIONAL,
			((AreaEventInfoImpl)this.areaEventInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AREA_EVENT_INFO);
		}

		if (this.hgmlcAddress != null) {
			((GSNAddressImpl)this.hgmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_H_GMLC_ADDRESS);
		}
	}

}
