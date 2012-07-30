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
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 *
 * 
 * @author amit bhayani
 * 
 */
public class SubscriberLocationReportRequestImpl extends LsmMessageImpl implements SubscriberLocationReportRequest {

	private static final int _TAG_MSISDN = 0;
	private static final int _TAG_IMSI = 1;
	private static final int _TAG_IMEI = 2;
	private static final int _TAG_NA_ESRD = 3;
	private static final int _TAG_NA_ESRK = 4;
	private static final int _TAG_LOCATION_ESTIMATE = 5;
	private static final int _TAG_AGE_OF_LOCATION_ESTIMATE = 6;
	private static final int _TAG_SLR_ARG_EXTENSION_CONTAINER = 7;
	private static final int _TAG_ADD_LOCATION_ESTIMATE = 8;
	private static final int _TAG_DEFERRED_MT_LR_DATA = 9;
	private static final int _TAG_LCS_REFERENCE_NUMBER = 10;
	private static final int _TAG_GERAN_POSITIONING_DATA = 11;
	private static final int _TAG_UTRAN_POSITIONING_DATA = 12;
	private static final int _TAG_CELL_ID_OR_SAI = 13;
	private static final int _TAG_H_GMLC_ADDRESS = 14;
	private static final int _TAG_LCS_SERVICE_TYPE_ID = 15;
	private static final int _TAG_SAI_PRESENT = 17;
	private static final int _TAG_PSEUDONYM_INDICATOR = 18;
	private static final int _TAG_ACCURACY_FULFILMENT_INDICATOR = 19;
	private static final int _TAG_velocityEstimate = 20;
	private static final int _TAG_sequenceNumber = 21;
	private static final int _TAG_periodicLDRInfo = 22;
	private static final int _TAG_mo_lrShortCircuitIndicator = 23;
	private static final int _TAG_geranGANSSpositioningData = 24;
	private static final int _TAG_utranGANSSpositioningData = 25;
	private static final int _TAG_targetServingNodeForHandover = 26;

	private LCSEvent lcsEvent;
	private LCSClientID lcsClientID;
	private LCSLocationInfo lcsLocationInfo;
	private ISDNAddressString msisdn;
	private IMSI imsi;
	private IMEI imei;
	private ISDNAddressString naEsrd;
	private ISDNAddressString naEsrk;
	private ExtGeographicalInformation locationEstimate;
	private Integer ageOfLocationEstimate;
	private SLRArgExtensionContainer slrArgExtensionContainer;
	private AddGeographicalInformation addLocationEstimate;
	private DeferredmtlrData deferredmtlrData;
	private Integer lcsReferenceNumber;
	private PositioningDataInformation geranPositioningData;
	private UtranPositioningDataInfo utranPositioningData;
	private CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai;
	private GSNAddress hgmlcAddress;
	private Integer lcsServiceTypeID;
	private boolean saiPresent;
	private boolean pseudonymIndicator;
	private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
	private VelocityEstimate velocityEstimate;
	private Integer sequenceNumber;
	private PeriodicLDRInfo periodicLDRInfo;
	private boolean moLrShortCircuitIndicator;
	private GeranGANSSpositioningData geranGANSSpositioningData;
	private UtranGANSSpositioningData utranGANSSpositioningData;
	private ServingNodeAddress targetServingNodeForHandover;

	/**
	 * 
	 */
	public SubscriberLocationReportRequestImpl() {
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
	public SubscriberLocationReportRequestImpl(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn,
			IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate,
			Integer ageOfLocationEstimate, SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
			DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
			UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress, Integer lcsServiceTypeID,
			boolean saiPresent, boolean pseudonymIndicator, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
			Integer sequenceNumber, PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
			UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover) {
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
		this.velocityEstimate = velocityEstimate;
		this.sequenceNumber = sequenceNumber;
		this.periodicLDRInfo = periodicLDRInfo;
		this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
		this.geranGANSSpositioningData = geranGANSSpositioningData;
		this.utranGANSSpositioningData = utranGANSSpositioningData;
		this.targetServingNodeForHandover = targetServingNodeForHandover;
	}

	public MAPMessageType getMessageType() {
		return MAPMessageType.subscriberLocationReport_Request;
	}

	public int getOperationCode() {
		return MAPOperationCode.subscriberLocationReport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSEvent()
	 */
	public LCSEvent getLCSEvent() {
		return this.lcsEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSClientID()
	 */
	public LCSClientID getLCSClientID() {
		return this.lcsClientID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSLocationInfo()
	 */
	public LCSLocationInfo getLCSLocationInfo() {
		return this.lcsLocationInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getMSISDN()
	 */
	public ISDNAddressString getMSISDN() {
		return this.msisdn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getIMSI()
	 */
	public IMSI getIMSI() {
		return this.imsi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getIMEI()
	 */
	public IMEI getIMEI() {
		return this.imei;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getNaESRD()
	 */
	public ISDNAddressString getNaESRD() {
		return this.naEsrd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getNaESRK()
	 */
	public ISDNAddressString getNaESRK() {
		return this.naEsrk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLocationEstimate()
	 */
	public ExtGeographicalInformation getLocationEstimate() {
		return this.locationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getAgeOfLocationEstimate()
	 */
	public Integer getAgeOfLocationEstimate() {
		return this.ageOfLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getSLRArgExtensionContainer()
	 */
	public SLRArgExtensionContainer getSLRArgExtensionContainer() {
		return this.slrArgExtensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getAdditionalLocationEstimate()
	 */
	public AddGeographicalInformation getAdditionalLocationEstimate() {
		return this.addLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getDeferredmtlrData()
	 */
	public DeferredmtlrData getDeferredmtlrData() {
		return this.deferredmtlrData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSReferenceNumber()
	 */
	public Integer getLCSReferenceNumber() {
		return this.lcsReferenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getGeranPositioningData()
	 */
	public PositioningDataInformation getGeranPositioningData() {
		return this.geranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getUtranPositioningData()
	 */
	public UtranPositioningDataInfo getUtranPositioningData() {
		return this.utranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication
	 * #getCellGlobalIdOrServiceAreaIdOrLAI()
	 */
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return this.cellIdOrSai;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getSaiPresent()
	 */
	public boolean getSaiPresent() {
		return this.saiPresent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getHGMLCAddress()
	 */
	public GSNAddress getHGMLCAddress() {
		return this.hgmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getLCSServiceTypeID()
	 */
	public Integer getLCSServiceTypeID() {
		return this.lcsServiceTypeID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication#getPseudonymIndicator()
	 */
	public boolean getPseudonymIndicator() {
		return this.pseudonymIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportRequestIndication
	 * #getAccuracyFulfilmentIndicator()
	 */
	public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
		return this.accuracyFulfilmentIndicator;
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

	@Override
	public VelocityEstimate getVelocityEstimate() {
		return velocityEstimate;
	}

	@Override
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	@Override
	public PeriodicLDRInfo getPeriodicLDRInfo() {
		return periodicLDRInfo;
	}

	@Override
	public boolean getMoLrShortCircuitIndicator() {
		return moLrShortCircuitIndicator;
	}

	@Override
	public GeranGANSSpositioningData getGeranGANSSpositioningData() {
		return geranGANSSpositioningData;
	}

	@Override
	public UtranGANSSpositioningData getUtranGANSSpositioningData() {
		return utranGANSSpositioningData;
	}

	@Override
	public ServingNodeAddress getTargetServingNodeForHandover() {
		return targetServingNodeForHandover;
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
		// Decode mandatory lcs-Event LCS-Event,
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.ENUMERATED) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-Event LCS-Event] bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		int length1 = ais.readLength();
		int event = (int) ais.readIntegerData(length1);
		this.lcsEvent = LCSEvent.getLCSEvent(event);

		tag = ais.readTag();
		// Decode mandatory lcs-ClientID LCS-ClientID
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || ais.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-ClientID LCS-ClientID] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsClientID = new LCSClientIDImpl();
		((LCSClientIDImpl)this.lcsClientID).decodeAll(ais);

		tag = ais.readTag();
		// Decode mandatory lcsLocationInfo LCSLocationInfo
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || ais.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcsLocationInfo LCSLocationInfo] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsLocationInfo = new LCSLocationInfoImpl();
		((LCSLocationInfoImpl)this.lcsLocationInfo).decodeAll(ais);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();
			switch (tag) {
			case _TAG_MSISDN:
				// msisdn [0] ISDN-AddressString OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [msisdn [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.msisdn = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl)this.msisdn).decodeAll(ais);
				break;
			case _TAG_IMSI:
				// imsi [1] IMSI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [imsi [2] IMSI ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsi = new IMSIImpl();
				((IMSIImpl)this.imsi).decodeAll(ais);
				break;
			case _TAG_IMEI:
				// imei [2] IMEI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [imei [2] IMEI OPTIONAL, ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imei = new IMEIImpl();
				((IMEIImpl)this.imei).decodeAll(ais);
				break;
			case _TAG_NA_ESRD:
				// na-ESRD [3] ISDN-AddressString OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [na-ESRD [3] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.naEsrd = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl)this.naEsrd).decodeAll(ais);
				break;
			case _TAG_NA_ESRK:
				// na-ESRK [4] ISDN-AddressString OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [na-ESRK [4] ISDN-AddressString] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.naEsrk = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl)this.naEsrk).decodeAll(ais);
				break;
			case _TAG_LOCATION_ESTIMATE:
				// locationEstimate [5] Ext-GeographicalInformation OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [locationEstimate [5] Ext-GeographicalInformation OPTIONAL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.locationEstimate = new ExtGeographicalInformationImpl();
				((ExtGeographicalInformationImpl)this.locationEstimate).decodeAll(ais);
				break;
			case _TAG_AGE_OF_LOCATION_ESTIMATE:
				// ageOfLocationEstimate [6] AgeOfLocationInformation OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [ageOfLocationEstimate [6] AgeOfLocationInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.ageOfLocationEstimate = (int) ais.readIntegerData(length1);
				break;
			case _TAG_SLR_ARG_EXTENSION_CONTAINER:
				// slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.slrArgExtensionContainer = new SLRArgExtensionContainerImpl();
				((SLRArgExtensionContainerImpl)this.slrArgExtensionContainer).decodeAll(ais);
				break;
			case _TAG_ADD_LOCATION_ESTIMATE:
				// add-LocationEstimate [8] Add-GeographicalInformation
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [add-LocationEstimate [8] Add-GeographicalInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.addLocationEstimate = new AddGeographicalInformationImpl();
				((AddGeographicalInformationImpl)this.addLocationEstimate).decodeAll(ais);
				break;
			case _TAG_DEFERRED_MT_LR_DATA:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [deferredmt-lrData [9] Deferredmt-lrData ] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.deferredmtlrData = new DeferredmtlrDataImpl();
				((DeferredmtlrDataImpl)this.deferredmtlrData).decodeAll(ais);
				break;
			case _TAG_LCS_REFERENCE_NUMBER:
				// lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcs-ReferenceNumber [10] LCS-ReferenceNumber] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.lcsReferenceNumber = (int) ais.readOctetString()[0];
				break;
			case _TAG_GERAN_POSITIONING_DATA:
				// geranPositioningData [11] PositioningDataInformation
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [geranPositioningData [11] PositioningDataInformation OPTIONAL,] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.geranPositioningData = new PositioningDataInformationImpl();
				((PositioningDataInformationImpl)this.geranPositioningData).decodeAll(ais);
				break;
			case _TAG_UTRAN_POSITIONING_DATA:
				// utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.utranPositioningData = new UtranPositioningDataInfoImpl();
				((UtranPositioningDataInfoImpl)this.utranPositioningData).decodeAll(ais);
				break;
			case _TAG_CELL_ID_OR_SAI:
				// cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
				AsnInputStream ais2 = ais.readSequenceStream();
				ais2.readTag();
				((CellGlobalIdOrServiceAreaIdOrLAIImpl)this.cellIdOrSai).decodeAll(ais2);
				break;
			case _TAG_H_GMLC_ADDRESS:
				// h-gmlc-Address [14] GSN-Address
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [h-gmlc-Address [14] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.hgmlcAddress = new GSNAddressImpl();
				((GSNAddressImpl)this.hgmlcAddress).decodeAll(ais);
				break;
			case _TAG_LCS_SERVICE_TYPE_ID:
				// lcsServiceTypeID [15] LCSServiceTypeID OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [lcsServiceTypeID [15] LCSServiceTypeID] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.lcsServiceTypeID = (int) ais.readIntegerData(length1);
				break;
			case _TAG_SAI_PRESENT:
				// sai-Present [17] NULL OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [sai-Present [17]] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.saiPresent = true;
				break;
			case _TAG_PSEUDONYM_INDICATOR:
				// pseudonymIndicator [18] NULL OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [pseudonymIndicator [18] NULL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.pseudonymIndicator = true;
				break;
			case _TAG_ACCURACY_FULFILMENT_INDICATOR:
				// accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SubscriberLocationReportRequestIndication: Parameter [accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				int indicator = (int) ais.readIntegerData(length1);
				this.accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(indicator);
				break;
			default:
				ais.advanceElement();
				break;
			}
		}

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
		if (this.lcsEvent == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsEvent is not defined");
		}

		if (this.lcsClientID == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsClientID is not defined");
		}

		if (this.lcsLocationInfo == null) {
			throw new MAPException("Error while encoding SubscriberLocationReportRequestIndication the mandatory parameter lcsLocationInfo is not defined");
		}

		try {
			asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.lcsEvent.getEvent());
		} catch (IOException e) {
			throw new MAPException("IOException while encoding parameter lcsEvent", e);
		} catch (AsnException e) {
			throw new MAPException("AsnException while encoding parameter lcsEvent", e);
		}

		((LCSClientIDImpl)this.lcsClientID).encodeAll(asnOs);

		((LCSLocationInfoImpl)this.lcsLocationInfo).encodeAll(asnOs);

		if (this.msisdn != null) {
			// msisdn [0] ISDN-AddressString OPTIONAL,
			((ISDNAddressStringImpl)this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MSISDN);
		}

		if (this.imsi != null) {
			// imsi [1] IMSI OPTIONAL,
			((IMSIImpl)this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMSI);
		}

		if (this.imei != null) {
			// imei [2] IMEI OPTIONAL,
			((IMEIImpl)this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMEI);
		}

		if (this.naEsrd != null) {
			// na-ESRD [3] ISDN-AddressString OPTIONAL,
			((ISDNAddressStringImpl)this.naEsrd).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NA_ESRD);
		}

		if (this.naEsrk != null) {
			// na-ESRK [4] ISDN-AddressString OPTIONAL
			((ISDNAddressStringImpl)this.naEsrk).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NA_ESRK);
		}

		if (this.locationEstimate != null) {
			// locationEstimate [5] Ext-GeographicalInformation OPTIONAL,
			((ExtGeographicalInformationImpl)this.locationEstimate).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LOCATION_ESTIMATE);
		}

		// ageOfLocationEstimate [6] AgeOfLocationInformation OPTIONAL,
		if (this.ageOfLocationEstimate != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AGE_OF_LOCATION_ESTIMATE, this.ageOfLocationEstimate);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter ageOfLocationEstimate", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter ageOfLocationEstimate", e);
			}
		}

		if (this.slrArgExtensionContainer != null) {
			// slr-ArgExtensionContainer [7] SLR-ArgExtensionContainer
			((SLRArgExtensionContainerImpl)this.slrArgExtensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SLR_ARG_EXTENSION_CONTAINER);
		}

		if (this.addLocationEstimate != null) {
			// add-LocationEstimate [8] Add-GeographicalInformation OPTIONAL,
			((AddGeographicalInformationImpl)this.addLocationEstimate).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ADD_LOCATION_ESTIMATE);
		}

		if (this.deferredmtlrData != null) {
			// deferredmt-lrData [9] Deferredmt-lrData
			((DeferredmtlrDataImpl)this.deferredmtlrData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_DEFERRED_MT_LR_DATA);
		}

		if (this.lcsReferenceNumber != null) {
			// lcs-ReferenceNumber [10] LCS-ReferenceNumber
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_REFERENCE_NUMBER, new byte[] { (byte)(int)this.lcsReferenceNumber });
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter lcs-ReferenceNumber", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter lcs-ReferenceNumber", e);
			}
		}

		if (this.geranPositioningData != null) {
			// geranPositioningData [11] PositioningDataInformation OPTIONAL,
			((PositioningDataInformationImpl)this.geranPositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GERAN_POSITIONING_DATA);
		}

		if (this.utranPositioningData != null) {
			// utranPositioningData [12] UtranPositioningDataInfo OPTIONAL,
			((UtranPositioningDataInfoImpl)this.utranPositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UTRAN_POSITIONING_DATA);
		}

		if (this.cellIdOrSai != null) {
			// cellIdOrSai [13] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CELL_ID_OR_SAI);
				int pos = asnOs.StartContentDefiniteLength();
				((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellIdOrSai).encodeAll(asnOs);
				asnOs.FinalizeContent(pos);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter cellIdOrSai", e);
			}

			((CellGlobalIdOrServiceAreaIdOrLAIImpl)this.cellIdOrSai).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CELL_ID_OR_SAI);
		}

		if (this.hgmlcAddress != null) {
			// h-gmlc-Address [14] GSN-Address OPTIONAL,
			((GSNAddressImpl)this.hgmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_H_GMLC_ADDRESS);
		}

		if (this.lcsServiceTypeID != null) {
			// lcsServiceTypeID [15] LCSServiceTypeID OPTIONAL,
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_SERVICE_TYPE_ID, this.lcsServiceTypeID);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter lcsServiceTypeID", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter lcsServiceTypeID", e);
			}
		}

		if (this.saiPresent) {
			// sai-Present [17] NULL OPTIONAL,
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SAI_PRESENT);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter sai-Present", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter sai-Present", e);
			}
		}

		if (this.pseudonymIndicator) {
			// pseudonymIndicator [18] NULL OPTIONAL,
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PSEUDONYM_INDICATOR);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter sai-Present", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter sai-Present", e);
			}
		}

		if (this.accuracyFulfilmentIndicator != null) {
			// accuracyFulfilmentIndicator [19] AccuracyFulfilmentIndicator
			// OPTIONAL
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ACCURACY_FULFILMENT_INDICATOR, this.accuracyFulfilmentIndicator.getIndicator());
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter accuracyFulfilmentIndicator", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter accuracyFulfilmentIndicator", e);
			}
		}

	}

}
