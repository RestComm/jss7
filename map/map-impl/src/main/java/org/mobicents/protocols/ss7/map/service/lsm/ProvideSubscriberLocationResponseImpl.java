/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

/**
 *
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
    private static final int _TAG_velocityEstimate = 9;
    private static final int _TAG_mo_lrShortCircuitIndicator = 10;
    private static final int _TAG_geranGANSSpositioningData = 11;
    private static final int _TAG_utranGANSSpositioningData = 12;
    private static final int _TAG_targetServingNodeForHandover = 13;

    public static final String _PrimitiveName = "ProvideSubscriberLocationResponse";

    private ExtGeographicalInformation locationEstimate;
    private PositioningDataInformation geranPositioningData;
    private UtranPositioningDataInfo utranPositioningData;
    private Integer ageOfLocationEstimate;
    private AddGeographicalInformation additionalLocationEstimate;
    private MAPExtensionContainer extensionContainer;
    private boolean deferredMTLRResponseIndicator;
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private boolean saiPresent;
    private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
    private VelocityEstimate velocityEstimate;
    private boolean moLrShortCircuitIndicator;
    private GeranGANSSpositioningData geranGANSSpositioningData;
    private UtranGANSSpositioningData utranGANSSpositioningData;
    private ServingNodeAddress targetServingNodeForHandover;

    /**
     *
     */
    public ProvideSubscriberLocationResponseImpl() {
        super();
    }

    public ProvideSubscriberLocationResponseImpl(ExtGeographicalInformation locationEstimate,
            PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover) {
        super();

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
        this.velocityEstimate = velocityEstimate;
        this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
        this.geranGANSSpositioningData = geranGANSSpositioningData;
        this.utranGANSSpositioningData = utranGANSSpositioningData;
        this.targetServingNodeForHandover = targetServingNodeForHandover;
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
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getLocationEstimate()
     */
    public ExtGeographicalInformation getLocationEstimate() {
        return this.locationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getGeranPositioningData()
     */
    public PositioningDataInformation getGeranPositioningData() {
        return this.geranPositioningData;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.
     * ProvideSubscriberLocationResponseIndication#getUtranPositioningData()
     */
    public UtranPositioningDataInfo getUtranPositioningData() {
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
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getAdditionalLocationEstimate()
     */
    public AddGeographicalInformation getAdditionalLocationEstimate() {
        return this.additionalLocationEstimate;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getDeferredMTLRResponseIndicator()
     */
    public boolean getDeferredMTLRResponseIndicator() {
        return this.deferredMTLRResponseIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellIdOrSai() {
        return this.cellGlobalIdOrServiceAreaIdOrLAI;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication#getSaiPresent()
     */
    public boolean getSaiPresent() {
        return this.saiPresent;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationResponseIndication
     * #getAccuracyFulfilmentIndicator()
     */
    public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
        return this.accuracyFulfilmentIndicator;
    }

    public VelocityEstimate getVelocityEstimate() {
        return velocityEstimate;
    }

    public boolean getMoLrShortCircuitIndicator() {
        return moLrShortCircuitIndicator;
    }

    public GeranGANSSpositioningData getGeranGANSSpositioningData() {
        return geranGANSSpositioningData;
    }

    public UtranGANSSpositioningData getUtranGANSSpositioningData() {
        return utranGANSSpositioningData;
    }

    public ServingNodeAddress getTargetServingNodeForHandover() {
        return targetServingNodeForHandover;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
     * (org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
     * (org.mobicents.protocols.asn.AsnInputStream, int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.locationEstimate = null;
        this.geranPositioningData = null;
        this.utranPositioningData = null;
        this.ageOfLocationEstimate = null;
        this.additionalLocationEstimate = null;
        this.extensionContainer = null;
        this.deferredMTLRResponseIndicator = false;
        this.cellGlobalIdOrServiceAreaIdOrLAI = null;
        this.saiPresent = false;
        this.accuracyFulfilmentIndicator = null;
        this.velocityEstimate = null;
        this.moLrShortCircuitIndicator = false;
        this.geranGANSSpositioningData = null;
        this.utranGANSSpositioningData = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter [locationEstimate Ext-GeographicalInformation] bad tag, tag class or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.locationEstimate = new ExtGeographicalInformationImpl();
        ((ExtGeographicalInformationImpl) this.locationEstimate).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_AGE_OF_LOCATION_ESTIMATE:
                        // // ageOfLocationEstimate [0] AgeOfLocationInformation
                        // OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [ageOfLocationEstimate [0] AgeOfLocationInformation] is not Sequence",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.ageOfLocationEstimate = (int) ais.readInteger();
                        break;
                    case _TAG_EXTENSIONCONTAINER:
                        // extensionContainer [1] ExtensionContainer OPTIONAL,
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [extensionContainer [1] ExtensionContainer] is not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _TAG_ADD_LOCATION_ESTIMATE:
                        // add-LocationEstimate [2] Add-GeographicalInformation
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [add-LocationEstimate [2] Add-GeographicalInformation] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.additionalLocationEstimate = new AddGeographicalInformationImpl();
                        ((AddGeographicalInformationImpl) this.additionalLocationEstimate).decodeAll(ais);
                        break;
                    case _TAG_DEFERRED_MT_LR_RESPONSE_IND:
                        // deferredmt-lrResponseIndicator [3] NULL OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [deferredmt-lrResponseIndicator [3] NULL] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        ais.readNull();
                        this.deferredMTLRResponseIndicator = true;
                        break;
                    case _TAG_GERAN_POSITIONING_DATA:
                        // geranPositioningData [4] PositioningDataInformation
                        // OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [geranPositioningData [4] PositioningDataInformation] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.geranPositioningData = new PositioningDataInformationImpl();
                        ((PositioningDataInformationImpl) this.geranPositioningData).decodeAll(ais);
                        break;
                    case _TAG_UTRAN_POSITIONING_DATA:
                        // utranPositioningData [5] UtranPositioningDataInfo
                        // OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [utranPositioningData [5] UtranPositioningDataInfo] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.utranPositioningData = new UtranPositioningDataInfoImpl();
                        ((UtranPositioningDataInfoImpl) this.utranPositioningData).decodeAll(ais);
                        break;
                    case _TAG_CELL_ID_OR_SAI:
                        // cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [cellIdOrSai [6] CellGlobalIdOrServiceAreaIdOrLAI] is not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.cellGlobalIdOrServiceAreaIdOrLAI = LocationInformationGPRSImpl
                                .decodeCellGlobalIdOrServiceAreaIdOrLAI(ais, _PrimitiveName);
                        // this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
                        // AsnInputStream ais2 = ais.readSequenceStream();
                        // ais2.readTag();
                        // ((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).decodeAll(ais2);
                        break;
                    case _TAG_SAI_PRESENT:
                        // sai-Present [7] NULL OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [sai-Present [7] NULL] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        ais.readNull();
                        this.saiPresent = true;
                        break;
                    case _TAG_ACCURACY_FULFILMENT_INDICATOR:
                        // accuracyFulfilmentIndicator [8]
                        // AccuracyFulfilmentIndicator
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException(
                                    "Error while decoding "
                                            + _PrimitiveName
                                            + ": Parameter [accuracyFulfilmentIndicator [8] AccuracyFulfilmentIndicator] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        int indicator = (int) ais.readInteger();
                        this.accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator
                                .getAccuracyFulfilmentIndicator(indicator);
                        break;

                    case _TAG_velocityEstimate:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter velocityEstimate is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.velocityEstimate = new VelocityEstimateImpl();
                        ((VelocityEstimateImpl) this.velocityEstimate).decodeAll(ais);
                        break;
                    case _TAG_mo_lrShortCircuitIndicator:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter moLrShortCircuitIndicator is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        ais.readNull();
                        this.moLrShortCircuitIndicator = true;
                        break;
                    case _TAG_geranGANSSpositioningData:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter geranGANSSpositioningData is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.geranGANSSpositioningData = new GeranGANSSpositioningDataImpl();
                        ((GeranGANSSpositioningDataImpl) this.geranGANSSpositioningData).decodeAll(ais);
                        break;
                    case _TAG_utranGANSSpositioningData:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter utranGANSSpositioningData is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.utranGANSSpositioningData = new UtranGANSSpositioningDataImpl();
                        ((UtranGANSSpositioningDataImpl) this.utranGANSSpositioningData).decodeAll(ais);
                        break;
                    case _TAG_targetServingNodeForHandover:
                        // targetServingNodeForHandover
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter targetServingNodeForHandover is not constructed",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.targetServingNodeForHandover = new ServingNodeAddressImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((ServingNodeAddressImpl) this.targetServingNodeForHandover).decodeAll(ais2);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.locationEstimate == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter locationEstimate is not defined");
        }

        ((ExtGeographicalInformationImpl) this.locationEstimate).encodeAll(asnOs);

        if (this.ageOfLocationEstimate != null) {
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AGE_OF_LOCATION_ESTIMATE, this.ageOfLocationEstimate);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding parameter " + _PrimitiveName + " .ageOfLocationEstimate", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName + " .ageOfLocationEstimate", e);
            }
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_EXTENSIONCONTAINER);
        }

        if (this.additionalLocationEstimate != null) {
            ((AddGeographicalInformationImpl) this.additionalLocationEstimate).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_ADD_LOCATION_ESTIMATE);
        }

        if (this.deferredMTLRResponseIndicator) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_DEFERRED_MT_LR_RESPONSE_IND);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding parameter " + _PrimitiveName
                        + " .deferredMTLRResponseIndicator", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName
                        + " .deferredMTLRResponseIndicator", e);
            }
        }

        if (this.geranPositioningData != null) {
            ((PositioningDataInformationImpl) this.geranPositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_GERAN_POSITIONING_DATA);
        }

        if (this.utranPositioningData != null) {
            ((UtranPositioningDataInfoImpl) this.utranPositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_UTRAN_POSITIONING_DATA);
        }

        if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CELL_ID_OR_SAI);
                int pos = asnOs.StartContentDefiniteLength();
                ((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName
                        + " .cellGlobalIdOrServiceAreaIdOrLAI", e);
            }
        }

        if (this.saiPresent) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SAI_PRESENT);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding parameter " + _PrimitiveName + " .saiPresent", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName + " saiPresent", e);
            }
        }

        if (this.accuracyFulfilmentIndicator != null) {
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ACCURACY_FULFILMENT_INDICATOR,
                        this.accuracyFulfilmentIndicator.getIndicator());
            } catch (IOException e) {
                throw new MAPException("IOException while encoding parameter " + _PrimitiveName
                        + " .accuracyFulfilmentIndicator", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName
                        + " .accuracyFulfilmentIndicator", e);
            }
        }

        if (this.velocityEstimate != null) {
            ((VelocityEstimateImpl) this.velocityEstimate).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_velocityEstimate);
        }

        if (this.moLrShortCircuitIndicator) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mo_lrShortCircuitIndicator);
            } catch (IOException e) {
                throw new MAPException(
                        "IOException while encoding parameter " + _PrimitiveName + " .moLrShortCircuitIndicator", e);
            } catch (AsnException e) {
                throw new MAPException(
                        "AsnException while encoding parameter " + _PrimitiveName + " moLrShortCircuitIndicator", e);
            }
        }

        if (this.geranGANSSpositioningData != null) {
            ((GeranGANSSpositioningDataImpl) this.geranGANSSpositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_geranGANSSpositioningData);
        }

        if (this.utranGANSSpositioningData != null) {
            ((UtranGANSSpositioningDataImpl) this.utranGANSSpositioningData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_utranGANSSpositioningData);
        }

        if (this.targetServingNodeForHandover != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_targetServingNodeForHandover);
                int pos = asnOs.StartContentDefiniteLength();
                ((ServingNodeAddressImpl) this.targetServingNodeForHandover).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding parameter " + _PrimitiveName
                        + " .targetServingNodeForHandover", e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.locationEstimate != null) {
            sb.append("locationEstimate");
            sb.append(this.locationEstimate);
        }
        if (this.geranPositioningData != null) {
            sb.append(", geranPositioningData=");
            sb.append(this.geranPositioningData);
        }
        if (this.utranPositioningData != null) {
            sb.append(", utranPositioningData=");
            sb.append(this.utranPositioningData);
        }
        if (this.ageOfLocationEstimate != null) {
            sb.append(", ageOfLocationEstimate=");
            sb.append(this.ageOfLocationEstimate);
        }
        if (this.additionalLocationEstimate != null) {
            sb.append(", additionalLocationEstimate=");
            sb.append(this.additionalLocationEstimate);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.deferredMTLRResponseIndicator) {
            sb.append(", deferredMTLRResponseIndicator");
        }
        if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
            sb.append(", cellGlobalIdOrServiceAreaIdOrLAI=");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAI);
        }
        if (this.saiPresent) {
            sb.append(", saiPresent");
        }
        if (this.accuracyFulfilmentIndicator != null) {
            sb.append(", accuracyFulfilmentIndicator=");
            sb.append(this.accuracyFulfilmentIndicator);
        }
        if (this.velocityEstimate != null) {
            sb.append(", velocityEstimate=");
            sb.append(this.velocityEstimate);
        }
        if (this.moLrShortCircuitIndicator) {
            sb.append(", moLrShortCircuitIndicator");
        }
        if (this.geranGANSSpositioningData != null) {
            sb.append(", geranGANSSpositioningData=");
            sb.append(this.geranGANSSpositioningData);
        }
        if (this.utranGANSSpositioningData != null) {
            sb.append(", utranGANSSpositioningData=");
            sb.append(this.utranGANSSpositioningData);
        }
        if (this.targetServingNodeForHandover != null) {
            sb.append(", targetServingNodeForHandover=");
            sb.append(this.targetServingNodeForHandover);
        }

        sb.append("]");

        return sb.toString();
    }
}
