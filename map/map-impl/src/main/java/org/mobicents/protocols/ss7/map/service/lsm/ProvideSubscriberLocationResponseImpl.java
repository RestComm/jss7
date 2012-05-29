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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * TODO Add unit test
 * 
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationResponseImpl extends LsmMessageImpl implements ProvideSubscriberLocationResponse {

	private static final int _TAG_AGE_OF_LOCATION_ESTIMATE = 0;
	private static final int _TAG_EXTENSIONCONTAINER = 1;
	private static final int _TAG_ADD_LOCATION_ESTIMATE = 2;
	private static final int _TAG_DEFERRED_MT_LR_RESPONSE_IND = 3;
	private static final int _TAG_GERAN_POSITIONING_DATA = 4;
	private static final int _TAG_UTRAN_POSITIONING_DATA = 5;
	private static final int _TAG_CELL_ID_OR_SAI = 6;
	private static final int _TAG_SAI_PRESENT = 7;
	private static final int _TAG_ACCURACY_FULFILMENT_INDICATOR = 8;

	private byte[] locationEstimate = null;
	private byte[] geranPositioningData = null;
	private byte[] utranPositioningData = null;
	private Integer ageOfLocationEstimate = null;
	private byte[] additionalLocationEstimate = null;
	private MAPExtensionContainer extensionContainer = null;
	private Boolean deferredMTLRResponseIndicator = null;
	private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = null;
	private Boolean saiPresent = null;
	private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;

	/**
	 * 
	 */
	public ProvideSubscriberLocationResponseImpl() {
		super();
	}

	/**
	 * @param locationEstimate
	 * @param geranPositioningData
	 * @param utranPositioningData
	 * @param ageOfLocationEstimate
	 * @param additionalLocationEstimate
	 * @param extensionContainer
	 * @param deferredMTLRResponseIndicator
	 * @param cellGlobalIdOrServiceAreaIdOrLAI
	 * @param saiPresent
	 * @param accuracyFulfilmentIndicator
	 */
	public ProvideSubscriberLocationResponseImpl(byte[] locationEstimate, byte[] geranPositioningData, byte[] utranPositioningData,
			Integer ageOfLocationEstimate, byte[] additionalLocationEstimate, MAPExtensionContainer extensionContainer, Boolean deferredMTLRResponseIndicator,
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, Boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator)
			throws MAPException {
		super();

		if (locationEstimate == null || locationEstimate.length < 1 || locationEstimate.length > 20) {
			throw new MAPException("Mandatory parameter locationEstimate cannot be null or its length should be between 1 and 20 ");
		}

		if (ageOfLocationEstimate != null && (ageOfLocationEstimate < 0 || ageOfLocationEstimate > 32767)) {
			throw new MAPException("ageOfLocationEstimate cannot be less than 0 or greater than 32767");
		}

		this.locationEstimate = locationEstimate;
		this.geranPositioningData = geranPositioningData;
		this.utranPositioningData = utranPositioningData;
		this.ageOfLocationEstimate = ageOfLocationEstimate;
		this.additionalLocationEstimate = additionalLocationEstimate;
		this.extensionContainer = extensionContainer;
		this.deferredMTLRResponseIndicator = deferredMTLRResponseIndicator;
		this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
		this.saiPresent = saiPresent;
		this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
	}

	public MAPMessageType getMessageType() {
		return MAPMessageType.provideSubscriberLocation_Response;
	}

	public int getOperationCode() {
		return MAPOperationCode.provideSubscriberLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getLocationEstimate()
	 */
	public byte[] getLocationEstimate() {
		return this.locationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getGeranPositioningData()
	 */
	public byte[] getGeranPositioningData() {
		return this.geranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getUtranPositioningData()
	 */
	public byte[] getUtranPositioningData() {
		return this.utranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getAgeOfLocationEstimate()
	 */
	public Integer getAgeOfLocationEstimate() {
		return this.ageOfLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication
	 * #getAdditionalLocationEstimate()
	 */
	public byte[] getAdditionalLocationEstimate() {
		return this.additionalLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getExtensionContainer()
	 */
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication
	 * #getDeferredMTLRResponseIndicator()
	 */
	public Boolean getDeferredMTLRResponseIndicator() {
		return this.deferredMTLRResponseIndicator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication
	 * #getCellGlobalIdOrServiceAreaIdOrLAI()
	 */
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return this.cellGlobalIdOrServiceAreaIdOrLAI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getSaiPresent()
	 */
	public Boolean getSaiPresent() {
		return this.saiPresent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication
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

		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [locationEstimate Ext-GeographicalInformation] bad tag class or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();
			int length1;
			switch (tag) {
			case _TAG_AGE_OF_LOCATION_ESTIMATE:
				// // ageOfLocationEstimate [0] AgeOfLocationInformation
				// OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [ageOfLocationEstimate [0] AgeOfLocationInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.ageOfLocationEstimate = (int) ais.readIntegerData(length1);
				break;
			case _TAG_EXTENSIONCONTAINER:
				// extensionContainer [1] ExtensionContainer OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [extensionContainer [1] ExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			case _TAG_ADD_LOCATION_ESTIMATE:
				// add-LocationEstimate [2] Add-GeographicalInformation
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [add-LocationEstimate [2] Add-GeographicalInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.additionalLocationEstimate = ais.readOctetStringData(length1);
				break;
			case _TAG_DEFERRED_MT_LR_RESPONSE_IND:
				// deferredmt-lrResponseIndicator [3] NULL OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [deferredmt-lrResponseIndicator [3] NULL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.deferredMTLRResponseIndicator = true;
				break;
			case _TAG_GERAN_POSITIONING_DATA:
				// geranPositioningData [4] PositioningDataInformation OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [geranPositioningData [4] PositioningDataInformation] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.geranPositioningData = ais.readOctetStringData(length1);
				break;
			case _TAG_UTRAN_POSITIONING_DATA:
				// utranPositioningData [5] UtranPositioningDataInfo OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [utranPositioningData [5] UtranPositioningDataInfo] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.utranPositioningData = ais.readOctetStringData(length1);
				break;
			case _TAG_CELL_ID_OR_SAI:
				// cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
				AsnInputStream ais2 = ais.readSequenceStream();
				ais2.readTag();
				((CellGlobalIdOrServiceAreaIdOrLAIImpl)this.cellGlobalIdOrServiceAreaIdOrLAI).decodeAll(ais2);
				break;
			case _TAG_SAI_PRESENT:
				// sai-Present [7] NULL OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [sai-Present [7] NULL] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.saiPresent = true;
				break;
			case _TAG_ACCURACY_FULFILMENT_INDICATOR:
				// accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				int indicator = (int) ais.readIntegerData(length1);
				this.accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(indicator);
				break;
			default:
				// Do we care?
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
		if (this.locationEstimate == null) {
			throw new MAPException("Error while encoding ProvideSubscriberLocation-Res the mandatory parameter locationEstimate is not defined");
		}

		try {
			asnOs.writeOctetString(this.locationEstimate);
		} catch (IOException e) {
			throw new MAPException("IOException while encoding parameter locationEstimate", e);
		} catch (AsnException e) {
			throw new MAPException("AsnException while encoding parameter locationEstimate", e);
		}

		if (this.ageOfLocationEstimate != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AGE_OF_LOCATION_ESTIMATE, this.ageOfLocationEstimate);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter ageOfLocationEstimate", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter ageOfLocationEstimate", e);
			}
		}

		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSIONCONTAINER);
		}

		if (this.additionalLocationEstimate != null) {
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ADD_LOCATION_ESTIMATE, this.additionalLocationEstimate);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter additionalLocationEstimate", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter additionalLocationEstimate", e);
			}
		}

		if (this.deferredMTLRResponseIndicator != null) {
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_DEFERRED_MT_LR_RESPONSE_IND);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter deferredMTLRResponseIndicator", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter deferredMTLRResponseIndicator", e);
			}
		}

		if (this.geranPositioningData != null) {
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GERAN_POSITIONING_DATA, this.geranPositioningData);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter geranPositioningData", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter geranPositioningData", e);
			}
		}

		if (this.utranPositioningData != null) {
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UTRAN_POSITIONING_DATA, this.utranPositioningData);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter utranPositioningData", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter utranPositioningData", e);
			}
		}

		if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
			try {
				asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CELL_ID_OR_SAI);
				int pos = asnOs.StartContentDefiniteLength();
				((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).encodeAll(asnOs);
				asnOs.FinalizeContent(pos);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter cellGlobalIdOrServiceAreaIdOrLAI", e);
			}
		}

		if (this.saiPresent != null) {
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SAI_PRESENT);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter saiPresent", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter saiPresent", e);
			}
		}
	}

}
