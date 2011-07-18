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
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequestIndication;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SubscriberLocationReportRequestIndicationImpl extends LsmMessageImpl implements SubscriberLocationReportRequestIndication {

	private LCSEvent lcsEvent = null;
	private LCSClientID lcsClientID = null;
	private LCSLocationInfo lcsLocationInfo = null;
	private ISDNAddressString msisdn = null;
	private IMSI imsi = null;
	private IMEI imei = null;
	private ISDNAddressString naEsrd = null;
	private ISDNAddressString naEsrk = null;
	private byte[] locationEstimate = null;
	private Integer ageOfLocationEstimate = null;
	private SLRArgExtensionContainer slrArgExtensionContainer = null;
	private byte[] addLocationEstimate = null;
	private DeferredmtlrData deferredmtlrData = null;
	private Byte lcsReferenceNumber = null;
	private byte[] geranPositioningData = null;
	private byte[] utranPositioningData = null;
	private CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = null;
	private byte[] hgmlcAddress = null;
	private Integer lcsServiceTypeID = null;
	private Boolean saiPresent = null;
	private Boolean pseudonymIndicator = null;
	private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;

	/**
	 * 
	 */
	public SubscriberLocationReportRequestIndicationImpl() {
		super();
	}

	/**
	 * @param lcsEvent
	 * @param lcsClientID
	 * @param lcsLocationInfo
	 * @param msisdn
	 * @param imsi
	 * @param imei
	 * @param naEsrd
	 * @param naEsrk
	 * @param locationEstimate
	 * @param ageOfLocationEstimate
	 * @param slrArgExtensionContainer
	 * @param addLocationEstimate
	 * @param deferredmtlrData
	 * @param lcsReferenceNumber
	 * @param geranPositioningData
	 * @param utranPositioningData
	 * @param cellIdOrSai
	 * @param hgmlcAddress
	 * @param lcsServiceTypeID
	 * @param saiPresent
	 * @param pseudonymIndicator
	 * @param accuracyFulfilmentIndicator
	 */
	public SubscriberLocationReportRequestIndicationImpl(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn,
			IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, byte[] locationEstimate, Integer ageOfLocationEstimate,
			SLRArgExtensionContainer slrArgExtensionContainer, byte[] addLocationEstimate, DeferredmtlrData deferredmtlrData, Byte lcsReferenceNumber,
			byte[] geranPositioningData, byte[] utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, byte[] hgmlcAddress,
			Integer lcsServiceTypeID, Boolean saiPresent, Boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) {
		super();
		this.lcsEvent = lcsEvent;
		this.lcsClientID = lcsClientID;
		this.lcsLocationInfo = lcsLocationInfo;
		this.msisdn = msisdn;
		this.imsi = imsi;
		this.imei = imei;
		this.naEsrd = naEsrd;
		this.naEsrk = naEsrk;
		this.locationEstimate = locationEstimate;
		this.ageOfLocationEstimate = ageOfLocationEstimate;
		this.slrArgExtensionContainer = slrArgExtensionContainer;
		this.addLocationEstimate = addLocationEstimate;
		this.deferredmtlrData = deferredmtlrData;
		this.lcsReferenceNumber = lcsReferenceNumber;
		this.geranPositioningData = geranPositioningData;
		this.utranPositioningData = utranPositioningData;
		this.cellIdOrSai = cellIdOrSai;
		this.hgmlcAddress = hgmlcAddress;
		this.lcsServiceTypeID = lcsServiceTypeID;
		this.saiPresent = saiPresent;
		this.pseudonymIndicator = pseudonymIndicator;
		this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSEvent()
	 */
	@Override
	public LCSEvent getLCSEvent() {
		return this.lcsEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSClientID()
	 */
	@Override
	public LCSClientID getLCSClientID() {
		return this.lcsClientID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSLocationInfo()
	 */
	@Override
	public LCSLocationInfo getLCSLocationInfo() {
		return this.lcsLocationInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getMSISDN()
	 */
	@Override
	public ISDNAddressString getMSISDN() {
		return this.msisdn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getIMSI()
	 */
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getIMEI()
	 */
	@Override
	public IMEI getIMEI() {
		return this.imei;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getNaESRD()
	 */
	@Override
	public ISDNAddressString getNaESRD() {
		return this.naEsrd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getNaESRK()
	 */
	@Override
	public ISDNAddressString getNaESRK() {
		return this.naEsrk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLocationEstimate()
	 */
	@Override
	public byte[] getLocationEstimate() {
		return this.locationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getAgeOfLocationEstimate()
	 */
	@Override
	public Integer getAgeOfLocationEstimate() {
		return this.ageOfLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getSLRArgExtensionContainer()
	 */
	@Override
	public SLRArgExtensionContainer getSLRArgExtensionContainer() {
		return this.slrArgExtensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getAdditionalLocationEstimate()
	 */
	@Override
	public byte[] getAdditionalLocationEstimate() {
		return this.addLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getDeferredmtlrData()
	 */
	@Override
	public DeferredmtlrData getDeferredmtlrData() {
		return this.deferredmtlrData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSReferenceNumber()
	 */
	@Override
	public Byte getLCSReferenceNumber() {
		return this.lcsReferenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getGeranPositioningData()
	 */
	@Override
	public byte[] getGeranPositioningData() {
		return this.geranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getUtranPositioningData()
	 */
	@Override
	public byte[] getUtranPositioningData() {
		return this.utranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication
	 * #getCellGlobalIdOrServiceAreaIdOrLAI()
	 */
	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return this.cellIdOrSai;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getSaiPresent()
	 */
	@Override
	public Boolean getSaiPresent() {
		return this.saiPresent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getHGMLCAddress()
	 */
	@Override
	public byte[] getHGMLCAddress() {
		return this.hgmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSServiceTypeID()
	 */
	@Override
	public Integer getLCSServiceTypeID() {
		return this.lcsServiceTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getPseudonymIndicator()
	 */
	@Override
	public Boolean getPseudonymIndicator() {
		return this.pseudonymIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication
	 * #getAccuracyFulfilmentIndicator()
	 */
	@Override
	public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
		return this.accuracyFulfilmentIndicator;
	}

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 3) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Needs at least 3 mandatory parameters, found"
							+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// Decode mandatory lcs-Event LCS-Event,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.ENUMERATED) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-Event LCS-Event] bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsEvent = LCSEvent.getLCSEvent(p.getData()[0]);

		// Decode mandatory lcs-ClientID LCS-ClientID
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || p.isPrimitive() || p.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-ClientID LCS-ClientID] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsClientID = new LCSClientIDImpl();
		this.lcsClientID.decode(p);

		// Decode mandatory lcsLocationInfo LCSLocationInfo
		p = parameters[2];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || p.isPrimitive() || p.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcsLocationInfo LCSLocationInfo] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsLocationInfo = new LCSLocationInfoImpl();
		this.lcsLocationInfo.decode(p);

		for (int count = 3; count < parameters.length; count++) {
			p = parameters[count];
			switch (p.getTag()) {
			case 0:
				// msisdn [0] ISDN-AddressString OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [msisdn [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.msisdn = new ISDNAddressStringImpl();
				this.msisdn.decode(p);

				break;
			case 1:
				// imsi [1] IMSI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [imsi [2] IMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsi = new IMSIImpl();
				this.imsi.decode(p);
				break;
			case 2:
				// imei [2] IMEI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [imei [2] IMEI OPTIONAL, ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imei = new IMEIImpl();
				this.imei.decode(p);
				break;

			case 3:
				// na-ESRD [3] ISDN-AddressString OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [na-ESRD [3] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.naEsrd = new ISDNAddressStringImpl();
				this.naEsrd.decode(p);
				break;
			case 4:
				// na-ESRK [4] ISDN-AddressString OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [na-ESRK [4] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.naEsrk = new ISDNAddressStringImpl();
				this.naEsrk.decode(p);
				break;
			case 5:
				// locationEstimate [5] Ext-GeographicalInformation OPTIONAL
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [locationEstimate [5] Ext-GeographicalInformation OPTIONAL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.locationEstimate = p.getData();
				break;
			case 6:
				// ageOfLocationEstimate [6] AgeOfLocationInformation OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [ageOfLocationEstimate [6] AgeOfLocationInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				byte[] data = p.getData();

				byte temp;
				this.ageOfLocationEstimate = 0;

				for (int i = 0; i < data.length; i++) {
					temp = data[i];
					this.ageOfLocationEstimate = (this.ageOfLocationEstimate << 8) | (0x00FF & temp);
				}
				break;
			case 7:
				// slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer
				// OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.slrArgExtensionContainer = new SLRArgExtensionContainerImpl();
				this.slrArgExtensionContainer.decode(p);
				break;
			case 8:
				// add-LocationEstimate [8] Add-GeographicalInformation
				// OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [add-LocationEstimate [8] Add-GeographicalInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.addLocationEstimate = p.getData();
				break;
			case 9:
				// deferredmt-lrData [9] Deferredmt-lrData,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [deferredmt-lrData [9] Deferredmt-lrData ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.deferredmtlrData = new DeferredmtlrDataImpl();
				this.deferredmtlrData.decode(p);
				break;
			case 10:
				// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
				// TODO LCS-ReferenceNumber is OCTET String of size 1. Just take
				// the byte from param?
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-ReferenceNumber [10] LCS-ReferenceNumber] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsReferenceNumber = p.getData()[0];
				break;
			case 11:
				// geranPositioningData [11] PositioningDataInformation
				// OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [geranPositioningData [11] PositioningDataInformation OPTIONAL,] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.geranPositioningData = p.getData();
				break;
			case 12:
				// utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,

				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.utranPositioningData = p.getData();
				break;
			case 13:
				// cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
				this.cellIdOrSai.decode(p);
				break;
			case 14:
				// h-gmlc-Address [14] GSN-Address
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [h-gmlc-Address [14] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.hgmlcAddress = p.getData();
				break;
			case 15:
				// lcsServiceTypeID [15] LCSServiceTypeID OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcsServiceTypeID [15] LCSServiceTypeID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.lcsServiceTypeID = new Integer(p.getData()[0]);
				break;
			case 17:
				// sai-Present [17] NULL OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [sai-Present [17]] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.saiPresent = true;
				break;
			case 18:
				// pseudonymIndicator [18] NULL OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [pseudonymIndicator [18] NULL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.pseudonymIndicator = true;
				break;
			case 19:
				// accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator
				// OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(p.getData()[0]);
				break;
			default:
//				throw new MAPParsingComponentException("Error while decoding SubscriberLocationReportRequestIndication: Expected tags 0 - 15 but found"
//						+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.lcsEvent == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsEvent is not defined");
		}

		if (this.lcsClientID == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsClientID is not defined");
		}

		if (this.lcsLocationInfo == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsLocationInfo is not defined");
		}

		// lcs-Event LCS-Event,
		asnOs.write(0x0a);
		asnOs.write(0x01);
		asnOs.write(this.lcsEvent.getEvent());

		// lcs-ClientID LCS-ClientID,
		Parameter p = this.lcsClientID.encode();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);

		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse lcs-ClientID LCS-ClientID", e);
		}

		// lcsLocationInfo LCSLocationInfo
		p = this.lcsLocationInfo.encode();
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setPrimitive(false);
		p.setTag(Tag.SEQUENCE);

		try {
			p.encode(asnOs);
		} catch (ParseException e) {
			throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse lcsLocationInfo LCSLocationInfo", e);
		}

		if (this.msisdn != null) {
			// msisdn [0] ISDN-AddressString OPTIONAL,
			p = this.msisdn.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x00);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse msisdn [0] ISDN-AddressString OPTIONAL",
						e);
			}
		}

		if (this.imsi != null) {
			// imsi [1] IMSI OPTIONAL,
			p = this.imsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x01);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse imsi [1] IMSI OPTIONAL", e);
			}
		}

		if (this.imei != null) {
			// imei [2] IMEI OPTIONAL,
			p = this.imsi.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x02);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse imei [2] IMEI OPTIONAL", e);
			}
		}

		if (this.naEsrd != null) {
			// na-ESRD [3] ISDN-AddressString OPTIONAL,
			p = this.naEsrd.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x03);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse na-ESRD [3] ISDN-AddressString OPTIONAL",
						e);
			}
		}

		if (this.naEsrk != null) {
			// na-ESRK [4] ISDN-AddressString OPTIONAL
			p = this.naEsrk.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x04);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse na-ESRK [4] ISDN-AddressString OPTIONAL",
						e);
			}
		}

		if (this.locationEstimate != null) {
			// locationEstimate [5] Ext-GeographicalInformation OPTIONAL,
			asnOs.write(0x85);
			asnOs.write(this.locationEstimate.length);
			try {
				asnOs.write(this.locationEstimate);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse locationEstimate [5] Ext-GeographicalInformation", e);
			}
		}

		// ageOfLocationEstimate [6] AgeOfLocationInformation OPTIONAL,
		if (this.ageOfLocationEstimate != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 6, this.ageOfLocationEstimate);
			} catch (IOException e) {
				throw new MAPException(
						"Error while encoding SubscriberLocationReportRequestIndication. Encdoing of the parameter[ageOfLocationEstimate [6] AgeOfLocationInformation] failed",
						e);
			}
		}

		if (this.slrArgExtensionContainer != null) {
			// slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer
			p = this.slrArgExtensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(7);
			p.setPrimitive(false); // TODO is it Primitive?
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer",
						e);
			}
		}

		if (this.addLocationEstimate != null) {
			// add-LocationEstimate [8] Add-GeographicalInformation OPTIONAL,
			asnOs.write(0x85);
			asnOs.write(this.addLocationEstimate.length);
			try {
				asnOs.write(this.addLocationEstimate);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse add-LocationEstimate [8] Add-GeographicalInformation", e);
			}
		}

		if (this.deferredmtlrData != null) {
			// deferredmt-lrData [9] Deferredmt-lrData
			p = this.deferredmtlrData.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(9);
			p.setPrimitive(false);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse deferredmt-lrData [9] Deferredmt-lrData",
						e);
			}
		}

		if (this.lcsReferenceNumber != null) {
			// lcs-ReferenceNumber [10] LCS-ReferenceNumber
			asnOs.write(0x8a);
			asnOs.write(0x01);
			asnOs.write(this.lcsReferenceNumber);
		}

		if (this.geranPositioningData != null) {
			// geranPositioningData [11] PositioningDataInformation OPTIONAL,
			asnOs.write(0x8b);
			asnOs.write(this.geranPositioningData.length);
			try {
				asnOs.write(this.geranPositioningData);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse geranPositioningData [11] PositioningDataInformation", e);
			}
		}

		if (this.utranPositioningData != null) {
			// utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,
			asnOs.write(0x8c);
			asnOs.write(this.utranPositioningData.length);
			try {
				asnOs.write(this.utranPositioningData);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse utranPositioningData [12] UtranPositioningDataInfo", e);
			}
		}

		if (this.cellIdOrSai != null) {
			// cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
			p = this.cellIdOrSai.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(0x8d);
			p.setPrimitive(false);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI", e);
			}
		}

		if (this.hgmlcAddress != null) {
			// h-gmlc-Address [14] GSN-Address OPTIONAL,
			asnOs.write(0x8e);
			asnOs.write(this.hgmlcAddress.length);
			try {
				asnOs.write(this.hgmlcAddress);
			} catch (IOException e) {
				throw new MAPException("Encoding of SubscriberLocationReportRequestIndication failed. Failed to parse h-gmlc-Address [14] GSN-Address", e);
			}
		}

		if (this.lcsServiceTypeID != null) {
			// lcsServiceTypeID [15] LCSServiceTypeID OPTIONAL,
			asnOs.write(0x8f);
			asnOs.write(0x01);
			asnOs.write(this.lcsServiceTypeID);
		}

		if (this.saiPresent != null) {
			// sai-Present [17] NULL OPTIONAL,
			asnOs.write(0x91);
			asnOs.write(0x00);
		}

		if (this.pseudonymIndicator != null) {
			// pseudonymIndicator [18] NULL OPTIONAL,
			asnOs.write(0x92);
			asnOs.write(0x00);
		}

		if (this.accuracyFulfilmentIndicator != null) {
			// accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator
			// OPTIONAL
			asnOs.write(0x93);
			asnOs.write(0x01);
			asnOs.write(this.accuracyFulfilmentIndicator.getIndicator());
		}
	}

}
