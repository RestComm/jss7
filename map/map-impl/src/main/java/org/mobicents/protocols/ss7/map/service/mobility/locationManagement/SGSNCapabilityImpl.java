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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author Lasith Waruna Perera
 * @author sergey vetyutnev
 *
 */
public class SGSNCapabilityImpl extends SequenceBase implements SGSNCapability {

    private static final int TAG_extensionContainer = 1;
    private static final int TAG_superChargerSupportedInServingNetworkEntity = 2;
    private static final int TAG_gprsEnhancementsSupportIndicator = 3;
    private static final int TAG_supportedCamelPhases = 4;
    private static final int TAG_supportedLCSCapabilitySets = 5;
    private static final int TAG_offeredCamel4CSIs = 6;
    private static final int TAG_smsCallBarringSupportIndicator = 7;
    private static final int TAG_supportedRATTypesIndicator = 8;
    private static final int TAG_supportedFeatures = 9;
    private static final int TAG_tAdsDataRetrieval = 10;
    private static final int TAG_homogeneousSupportOfIMSVoiceOverPSSessions = 11;

    private boolean solsaSupportIndicator;
    private MAPExtensionContainer extensionContainer;
    private SuperChargerInfo superChargerSupportedInServingNetworkEntity;
    private boolean gprsEnhancementsSupportIndicator;
    private SupportedCamelPhases supportedCamelPhases;
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    private OfferedCamel4CSIs offeredCamel4CSIs;
    private boolean smsCallBarringSupportIndicator;
    private SupportedRATTypes supportedRATTypesIndicator;
    private SupportedFeatures supportedFeatures;
    private boolean tAdsDataRetrieval;
    private Boolean homogeneousSupportOfIMSVoiceOverPSSessions;

    public SGSNCapabilityImpl() {
        super("SGSNCapability");
    }

    public SGSNCapabilityImpl(boolean solsaSupportIndicator, MAPExtensionContainer extensionContainer,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean gprsEnhancementsSupportIndicator,
            SupportedCamelPhases supportedCamelPhases, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
            OfferedCamel4CSIs offeredCamel4CSIs, boolean smsCallBarringSupportIndicator,
            SupportedRATTypes supportedRATTypesIndicator, SupportedFeatures supportedFeatures, boolean tAdsDataRetrieval,
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions) {
        super("SGSNCapability");
        this.solsaSupportIndicator = solsaSupportIndicator;
        this.extensionContainer = extensionContainer;
        this.superChargerSupportedInServingNetworkEntity = superChargerSupportedInServingNetworkEntity;
        this.gprsEnhancementsSupportIndicator = gprsEnhancementsSupportIndicator;
        this.supportedCamelPhases = supportedCamelPhases;
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        this.smsCallBarringSupportIndicator = smsCallBarringSupportIndicator;
        this.supportedRATTypesIndicator = supportedRATTypesIndicator;
        this.supportedFeatures = supportedFeatures;
        this.tAdsDataRetrieval = tAdsDataRetrieval;
        this.homogeneousSupportOfIMSVoiceOverPSSessions = homogeneousSupportOfIMSVoiceOverPSSessions;
    }

    @Override
    public boolean getSolsaSupportIndicator() {
        return this.solsaSupportIndicator;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity() {
        return this.superChargerSupportedInServingNetworkEntity;
    }

    @Override
    public boolean getGprsEnhancementsSupportIndicator() {
        return this.gprsEnhancementsSupportIndicator;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return this.supportedCamelPhases;
    }

    @Override
    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return this.supportedLCSCapabilitySets;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public boolean getSmsCallBarringSupportIndicator() {
        return this.smsCallBarringSupportIndicator;
    }

    @Override
    public SupportedRATTypes getSupportedRATTypesIndicator() {
        return this.supportedRATTypesIndicator;
    }

    @Override
    public SupportedFeatures getSupportedFeatures() {
        return this.supportedFeatures;
    }

    @Override
    public boolean getTAdsDataRetrieval() {
        return this.tAdsDataRetrieval;
    }

    @Override
    public Boolean getHomogeneousSupportOfIMSVoiceOverPSSessions() {
        return this.homogeneousSupportOfIMSVoiceOverPSSessions;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.solsaSupportIndicator = false;
        this.extensionContainer = null;
        this.superChargerSupportedInServingNetworkEntity = null;
        this.gprsEnhancementsSupportIndicator = false;
        this.supportedCamelPhases = null;
        this.supportedLCSCapabilitySets = null;
        this.offeredCamel4CSIs = null;
        this.smsCallBarringSupportIndicator = false;
        this.supportedRATTypesIndicator = null;
        this.supportedFeatures = null;
        this.tAdsDataRetrieval = false;
        this.homogeneousSupportOfIMSVoiceOverPSSessions = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".solsaSupportIndicator: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.solsaSupportIndicator = true;
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case TAG_superChargerSupportedInServingNetworkEntity: // superChargerSupportedInServingNetworkEntity
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".superChargerSupportedInServingNetworkEntity: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            ais2.readTag();
                            this.superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl();
                            ((SuperChargerInfoImpl) this.superChargerSupportedInServingNetworkEntity).decodeAll(ais2);
                            break;
                        case TAG_gprsEnhancementsSupportIndicator:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gprsEnhancementsSupportIndicator: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.gprsEnhancementsSupportIndicator = true;
                            break;
                        case TAG_supportedCamelPhases:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCamelPhases: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                            ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                            break;
                        case TAG_supportedLCSCapabilitySets:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedLCSCapabilitySets: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl();
                            ((SupportedLCSCapabilitySetsImpl) this.supportedLCSCapabilitySets).decodeAll(ais);
                            break;
                        case TAG_offeredCamel4CSIs:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".offeredCamel4CSIs: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
                            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
                            break;
                        case TAG_smsCallBarringSupportIndicator:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".smsCallBarringSupportIndicator: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.smsCallBarringSupportIndicator = true;
                            break;
                        case TAG_supportedRATTypesIndicator:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedRATTypesIndicator: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedRATTypesIndicator = new SupportedRATTypesImpl();
                            ((SupportedRATTypesImpl) this.supportedRATTypesIndicator).decodeAll(ais);
                            break;
                        case TAG_supportedFeatures:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedFeatures: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedFeatures = new SupportedFeaturesImpl();
                            ((SupportedFeaturesImpl) this.supportedFeatures).decodeAll(ais);
                            break;
                        case TAG_tAdsDataRetrieval:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tAdsDataRetrieval: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.tAdsDataRetrieval = true;
                            break;
                        case TAG_homogeneousSupportOfIMSVoiceOverPSSessions:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".homogeneousSupportOfIMSVoiceOverPSSessions: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.homogeneousSupportOfIMSVoiceOverPSSessions = ais.readBoolean();
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;
                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.solsaSupportIndicator)
                asnOs.writeNull();

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_extensionContainer);

            if (superChargerSupportedInServingNetworkEntity != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_superChargerSupportedInServingNetworkEntity);
                int pos = asnOs.StartContentDefiniteLength();
                ((SuperChargerInfoImpl) this.superChargerSupportedInServingNetworkEntity).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }

            if (this.gprsEnhancementsSupportIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_gprsEnhancementsSupportIndicator);

            if (this.supportedCamelPhases != null)
                ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_supportedCamelPhases);

            if (this.supportedLCSCapabilitySets != null)
                ((SupportedLCSCapabilitySetsImpl) this.supportedLCSCapabilitySets).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_supportedLCSCapabilitySets);

            if (this.offeredCamel4CSIs != null)
                ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_offeredCamel4CSIs);

            if (this.smsCallBarringSupportIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_smsCallBarringSupportIndicator);

            if (this.supportedRATTypesIndicator != null)
                ((SupportedRATTypesImpl) this.supportedRATTypesIndicator).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_supportedRATTypesIndicator);

            if (this.supportedFeatures != null)
                ((SupportedFeaturesImpl) this.supportedFeatures).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_supportedFeatures);

            if (this.smsCallBarringSupportIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_smsCallBarringSupportIndicator);

            if (this.tAdsDataRetrieval)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_tAdsDataRetrieval);

            if (this.homogeneousSupportOfIMSVoiceOverPSSessions != null) {
                asnOs.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, TAG_homogeneousSupportOfIMSVoiceOverPSSessions,
                        this.homogeneousSupportOfIMSVoiceOverPSSessions);
            }

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.solsaSupportIndicator) {
            sb.append("solsaSupportIndicator, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.superChargerSupportedInServingNetworkEntity != null) {
            sb.append("superChargerSupportedInServingNetworkEntity=");
            sb.append(this.superChargerSupportedInServingNetworkEntity.toString());
            sb.append(", ");
        }

        if (this.gprsEnhancementsSupportIndicator) {
            sb.append("gprsEnhancementsSupportIndicator, ");
        }

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases.toString());
            sb.append(", ");
        }

        if (this.supportedLCSCapabilitySets != null) {
            sb.append("supportedLCSCapabilitySets=");
            sb.append(this.supportedLCSCapabilitySets.toString());
            sb.append(", ");
        }

        if (this.offeredCamel4CSIs != null) {
            sb.append("offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs.toString());
            sb.append(", ");
        }

        if (this.smsCallBarringSupportIndicator) {
            sb.append("smsCallBarringSupportIndicator, ");
        }

        if (this.supportedRATTypesIndicator != null) {
            sb.append("supportedRATTypesIndicator=");
            sb.append(this.supportedRATTypesIndicator.toString());
            sb.append(", ");
        }

        if (this.supportedFeatures != null) {
            sb.append("supportedFeatures=");
            sb.append(this.supportedFeatures.toString());
            sb.append(", ");
        }

        if (this.tAdsDataRetrieval) {
            sb.append("tAdsDataRetrieval, ");
        }

        if (this.homogeneousSupportOfIMSVoiceOverPSSessions != null) {
            sb.append("homogeneousSupportOfIMSVoiceOverPSSessions=");
            sb.append(this.homogeneousSupportOfIMSVoiceOverPSSessions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
