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
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class ProvideSubscriberLocationResponseIndicationImpl extends LsmMessageImpl implements ProvideSubscriberLocationResponseIndication {

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
	public ProvideSubscriberLocationResponseIndicationImpl() {
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
	public ProvideSubscriberLocationResponseIndicationImpl(byte[] locationEstimate, byte[] geranPositioningData, byte[] utranPositioningData,
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getLocationEstimate()
	 */
	@Override
	public byte[] getLocationEstimate() {
		return this.locationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getGeranPositioningData()
	 */
	@Override
	public byte[] getGeranPositioningData() {
		return this.geranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getUtranPositioningData()
	 */
	@Override
	public byte[] getUtranPositioningData() {
		return this.utranPositioningData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getAgeOfLocationEstimate()
	 */
	@Override
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
	@Override
	public byte[] getAdditionalLocationEstimate() {
		return this.additionalLocationEstimate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getExtensionContainer()
	 */
	@Override
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
	@Override
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
	@Override
	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
		return this.cellGlobalIdOrServiceAreaIdOrLAI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * ProvideSubscriberLocationResponseIndication#getSaiPresent()
	 */
	@Override
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
	@Override
	public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
		return this.accuracyFulfilmentIndicator;
	}

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters.length < 1) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationResponseIndication: Needs at least 1 mandatory parameters, found"
							+ +(parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// locationEstimate Ext-GeographicalInformation,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive() || p.getTag() != Tag.SEQUENCE) {
			throw new MAPParsingComponentException(
					"Error while decoding ProvideSubscriberLocationResponseIndication: Parameter [locationEstimate Ext-GeographicalInformation] bad tag class or not constructed",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		this.locationEstimate = p.getData();

		for (int count = 0; count < parameters.length; count++) {
			p = parameters[count];
			switch (p.getTag()) {
			case 0:
				// ageOfLocationEstimate [0] AgeOfLocationInformation OPTIONAL,
				byte[] data = p.getData();

				byte temp;
				this.ageOfLocationEstimate = 0;

				for (int i = 0; i < data.length; i++) {
					temp = data[i];
					this.ageOfLocationEstimate = (this.ageOfLocationEstimate << 8) | (0x00FF & temp);
				}
				break;
			case 1:
				// extensionContainer [1] ExtensionContainer OPTIONAL,
				this.extensionContainer = new MAPExtensionContainerImpl();
				this.extensionContainer.decode(p);
				break;
			case 2:
				// add-LocationEstimate [2] Add-GeographicalInformation
				// OPTIONAL,
				this.additionalLocationEstimate = p.getData();
				break;
			case 3:
				// deferredmt-lrResponseIndicator [3] NULL OPTIONAL,
				this.deferredMTLRResponseIndicator = true;
				break;
			case 4:
				// geranPositioningData [4] PositioningDataInformation
				this.geranPositioningData = p.getData();
				break;
			case 5:
				// utranPositioningData [5] UtranPositioningDataInfo OPTIONAL,
				this.utranPositioningData = p.getData();
				break;
			case 6:
				// cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
				this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
				this.cellGlobalIdOrServiceAreaIdOrLAI.decode(p);
				break;
			case 7:
				// sai-Present [7] NULL OPTIONAL,
				this.saiPresent = true;
				break;
			case 8:
				// accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator
				// OPTIONAL
				this.accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.getAccuracyFulfilmentIndicator(p.getData()[0]);
				break;
			default:
//				throw new MAPParsingComponentException(
//						"Error while decoding ProvideSubscriberLocationResponseIndication: Expected ageOfLocationEstimate [0] AgeOfLocationInformation or extensionContainer "
//								+ "[1] ExtensionContainer or add-LocationEstimate [2] Add-GeographicalInformation or deferredmt-lrResponseIndicator [3] NULL or geranPositioningData [4] "
//								+ "PositioningDataInformation or utranPositioningData [5] UtranPositioningDataInfo or cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI or sai-Present [7] "
//								+ "NULL or accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator but received " + p.getTag(),
//						MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}

	}

	public void encode(AsnOutputStream asnOs) throws MAPException {
		// locationEstimate Ext-GeographicalInformation
		asnOs.write(0x10);
		asnOs.write(this.locationEstimate.length);
		asnOs.write(this.locationEstimate);

		// ageOfLocationEstimate [0] AgeOfLocationInformation
		if (this.ageOfLocationEstimate != null) {
			try {
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, this.ageOfLocationEstimate);
			} catch (IOException e) {
				throw new MAPException(
						"Error while encoding ProvideSubscriberLocationResponseIndication. Encdoing of the parameter[ageOfLocationEstimate [0] AgeOfLocationInformation] failed",
						e);
			} catch (AsnException e) {
				throw new MAPException(
						"Error while encoding ProvideSubscriberLocationResponseIndication. Encdoing of the parameter[ageOfLocationEstimate [0] AgeOfLocationInformation] failed",
						e);
			}
		}

		// extensionContainer [1] ExtensionContainer
		if (this.extensionContainer != null) {
			Parameter p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true); // TODO Primitive?
			p.setTag(0x01);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of ProvideSubscriberLocationResponseIndication failed. Failed to parse extensionContainer [1] ExtensionContainer", e);
			}
		}

		// add-LocationEstimate [2] Add-GeographicalInformation OPTIONAL,
		if (additionalLocationEstimate != null) {
			asnOs.write(0x10);
			asnOs.write(this.locationEstimate.length);
			asnOs.write(this.locationEstimate);

		}

		if (this.deferredMTLRResponseIndicator != null) {
			// deferredmt-lrResponseIndicator [3] NULL
			asnOs.write(0x83);
			asnOs.write(0x00);
		}

		if (this.geranPositioningData != null) {
			// geranPositioningData [4] PositioningDataInformation
			asnOs.write(0x84);
			asnOs.write(this.geranPositioningData.length);
			asnOs.write(this.geranPositioningData);
		}

		if (this.utranPositioningData != null) {
			// utranPositioningData [5] UtranPositioningDataInfo
			asnOs.write(0x85);
			asnOs.write(this.utranPositioningData.length);
			asnOs.write(this.utranPositioningData);
		}

		if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
			Parameter p = this.cellGlobalIdOrServiceAreaIdOrLAI.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true); // TODO Primitive?
			p.setTag(0x06);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of ProvideSubscriberLocationResponseIndication failed. Failed to parse cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI", e);
			}
		}

		if (this.saiPresent != null) {
			// sai-Present [7] NULL
			asnOs.write(0x87);
			asnOs.write(0x00);
		}

		if (this.accuracyFulfilmentIndicator != null) {
			// accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator
			asnOs.write(0x88);
			asnOs.write(0x01);
			asnOs.write(this.accuracyFulfilmentIndicator.getIndicator());
		}
	}

}
