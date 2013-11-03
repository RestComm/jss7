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
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VLRCapabilityImpl implements VLRCapability, MAPAsnPrimitive {

    public static final int _TAG_supportedCamelPhases = 0;
    public static final int _TAG_solsaSupportIndicator = 2;
    public static final int _TAG_istSupportIndicator = 1;
    public static final int _TAG_superChargerSupportedInServingNetworkEntity = 3;
    public static final int _TAG_longFTNSupported = 4;
    public static final int _TAG_supportedLCSCapabilitySets = 5;
    public static final int _TAG_offeredCamel4CSIs = 6;
    public static final int _TAG_supportedRATTypesIndicator = 7;
    public static final int _TAG_longGroupIDSupported = 8;
    public static final int _TAG_mtRoamingForwardingSupported = 9;

    public static final String _PrimitiveName = "VlrCapability";

    private SupportedCamelPhases supportedCamelPhases;
    private MAPExtensionContainer extensionContainer;
    private boolean solsaSupportIndicator;
    private ISTSupportIndicator istSupportIndicator;
    private SuperChargerInfo superChargerSupportedInServingNetworkEntity;
    private boolean longFtnSupported;
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    private OfferedCamel4CSIs offeredCamel4CSIs;
    private SupportedRATTypes supportedRATTypesIndicator;
    private boolean longGroupIDSupported;
    private boolean mtRoamingForwardingSupported;

    public VLRCapabilityImpl() {
    }

    public VLRCapabilityImpl(SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer,
            boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
            SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
            SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported) {
        this.supportedCamelPhases = supportedCamelPhases;
        this.extensionContainer = extensionContainer;
        this.solsaSupportIndicator = solsaSupportIndicator;
        this.istSupportIndicator = istSupportIndicator;
        this.superChargerSupportedInServingNetworkEntity = superChargerSupportedInServingNetworkEntity;
        this.longFtnSupported = longFtnSupported;
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        this.supportedRATTypesIndicator = supportedRATTypesIndicator;
        this.longGroupIDSupported = longGroupIDSupported;
        this.mtRoamingForwardingSupported = mtRoamingForwardingSupported;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getSolsaSupportIndicator() {
        return solsaSupportIndicator;
    }

    public ISTSupportIndicator getIstSupportIndicator() {
        return istSupportIndicator;
    }

    public SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity() {
        return superChargerSupportedInServingNetworkEntity;
    }

    public boolean getLongFtnSupported() {
        return longFtnSupported;
    }

    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return supportedLCSCapabilitySets;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    public SupportedRATTypes getSupportedRATTypesIndicator() {
        return supportedRATTypesIndicator;
    }

    public boolean getLongGroupIDSupported() {
        return longGroupIDSupported;
    }

    public boolean getMtRoamingForwardingSupported() {
        return mtRoamingForwardingSupported;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        supportedCamelPhases = null;
        extensionContainer = null;
        solsaSupportIndicator = false;
        istSupportIndicator = null;
        superChargerSupportedInServingNetworkEntity = null;
        longFtnSupported = false;
        supportedLCSCapabilitySets = null;
        offeredCamel4CSIs = null;
        supportedRATTypesIndicator = null;
        longGroupIDSupported = false;
        mtRoamingForwardingSupported = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_supportedCamelPhases: // supportedCamelPhases
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".supportedCamelPhases: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                        ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                        break;
                    case _TAG_solsaSupportIndicator:
                        // solsaSupportIndicator
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".solsaSupportIndicator: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.solsaSupportIndicator = true;
                        break;
                    case _TAG_istSupportIndicator:
                        // istSupportIndicator
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".istSupportIndicator: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.istSupportIndicator = ISTSupportIndicator.getInstance(i1);
                        break;
                    case _TAG_superChargerSupportedInServingNetworkEntity: // superChargerSupportedInServingNetworkEntity
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".superChargerSupportedInServingNetworkEntity: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl();
                        ((SuperChargerInfoImpl) this.superChargerSupportedInServingNetworkEntity).decodeAll(ais2);
                        break;
                    case _TAG_longFTNSupported:
                        // longFtnSupported
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".longFTNSupported: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.longFtnSupported = true;
                        break;
                    case _TAG_supportedLCSCapabilitySets: // supportedLCSCapabilitySets
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".supportedLCSCapabilitySets: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl();
                        ((SupportedLCSCapabilitySetsImpl) this.supportedLCSCapabilitySets).decodeAll(ais);
                        break;
                    case _TAG_offeredCamel4CSIs: // offeredCamel4CSIs
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".offeredCamel4CSIs: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
                        ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
                        break;
                    case _TAG_supportedRATTypesIndicator: // supportedRATTypesIndicator
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".supportedRATTypesIndicator: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.supportedRATTypesIndicator = new SupportedRATTypesImpl();
                        ((SupportedRATTypesImpl) this.supportedRATTypesIndicator).decodeAll(ais);
                        break;
                    case _TAG_longGroupIDSupported:
                        // longGroupIDSupported
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".longGroupIDSupported: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.longGroupIDSupported = true;
                        break;
                    case _TAG_mtRoamingForwardingSupported:
                        // mtRoamingForwardingSupported
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".longFTNSupported: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.mtRoamingForwardingSupported = true;
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                switch (tag) {
                    case Tag.SEQUENCE:
                        // extensionContainer
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                }
            } else {

                ais.advanceElement();
            }
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (supportedCamelPhases != null)
                ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_supportedCamelPhases);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            if (solsaSupportIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_solsaSupportIndicator);
            if (istSupportIndicator != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_istSupportIndicator, istSupportIndicator.getCode());
            if (superChargerSupportedInServingNetworkEntity != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_superChargerSupportedInServingNetworkEntity);
                int pos = asnOs.StartContentDefiniteLength();
                ((SuperChargerInfoImpl) this.superChargerSupportedInServingNetworkEntity).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
            if (longFtnSupported)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_longFTNSupported);
            if (supportedLCSCapabilitySets != null)
                ((SupportedLCSCapabilitySetsImpl) this.supportedLCSCapabilitySets).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_supportedLCSCapabilitySets);
            if (offeredCamel4CSIs != null)
                ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_offeredCamel4CSIs);
            if (supportedRATTypesIndicator != null)
                ((SupportedRATTypesImpl) this.supportedRATTypesIndicator).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_supportedRATTypesIndicator);
            if (longGroupIDSupported)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_longGroupIDSupported);
            if (mtRoamingForwardingSupported)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mtRoamingForwardingSupported);

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

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }
        if (this.solsaSupportIndicator) {
            sb.append("solsaSupportIndicator, ");
        }
        if (this.istSupportIndicator != null) {
            sb.append("istSupportIndicator=");
            sb.append(this.istSupportIndicator.toString());
            sb.append(", ");
        }
        if (this.superChargerSupportedInServingNetworkEntity != null) {
            sb.append("superChargerSupportedInServingNetworkEntity=");
            sb.append(this.superChargerSupportedInServingNetworkEntity.toString());
            sb.append(", ");
        }
        if (this.longFtnSupported) {
            sb.append("longFtnSupported, ");
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
        if (this.supportedRATTypesIndicator != null) {
            sb.append("supportedRATTypesIndicator=");
            sb.append(this.supportedRATTypesIndicator.toString());
            sb.append(", ");
        }
        if (this.longGroupIDSupported) {
            sb.append("longGroupIDSupported, ");
        }
        if (this.mtRoamingForwardingSupported) {
            sb.append("mtRoamingForwardingSupported, ");
        }

        sb.append("]");

        return sb.toString();
    }
}
