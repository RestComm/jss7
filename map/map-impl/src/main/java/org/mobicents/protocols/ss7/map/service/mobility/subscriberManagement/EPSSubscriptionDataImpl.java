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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EPSSubscriptionDataImpl extends SequenceBase implements EPSSubscriptionData {

    private static final int _TAG_apnOiReplacement = 0;
    private static final int _TAG_rfspId = 2;
    private static final int _TAG_ambr = 3;
    private static final int _TAG_apnConfigurationProfile = 4;
    private static final int _TAG_stnSr = 6;
    private static final int _TAG_extensionContainer = 5;
    private static final int _TAG_mpsCSPriority = 7;
    private static final int _TAG_mpsEPSPriority = 8;

    private APNOIReplacement apnOiReplacement;
    private Integer rfspId;
    private AMBR ambr;
    private APNConfigurationProfile apnConfigurationProfile;
    private ISDNAddressString stnSr;
    private MAPExtensionContainer extensionContainer;
    private boolean mpsCSPriority;
    private boolean mpsEPSPriority;

    public EPSSubscriptionDataImpl() {
        super("EPSSubscriptionData");
    }

    public EPSSubscriptionDataImpl(APNOIReplacement apnOiReplacement, Integer rfspId, AMBR ambr,
            APNConfigurationProfile apnConfigurationProfile, ISDNAddressString stnSr, MAPExtensionContainer extensionContainer,
            boolean mpsCSPriority, boolean mpsEPSPriority) {
        super("EPSSubscriptionData");
        this.apnOiReplacement = apnOiReplacement;
        this.rfspId = rfspId;
        this.ambr = ambr;
        this.apnConfigurationProfile = apnConfigurationProfile;
        this.stnSr = stnSr;
        this.extensionContainer = extensionContainer;
        this.mpsCSPriority = mpsCSPriority;
        this.mpsEPSPriority = mpsEPSPriority;
    }

    @Override
    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    @Override
    public Integer getRfspId() {
        return this.rfspId;
    }

    @Override
    public AMBR getAmbr() {
        return this.ambr;
    }

    @Override
    public APNConfigurationProfile getAPNConfigurationProfile() {
        return this.apnConfigurationProfile;
    }

    @Override
    public ISDNAddressString getStnSr() {
        return this.stnSr;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getMpsCSPriority() {
        return this.mpsCSPriority;
    }

    @Override
    public boolean getMpsEPSPriority() {
        return this.mpsEPSPriority;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.apnOiReplacement = null;
        this.rfspId = null;
        this.ambr = null;
        this.apnConfigurationProfile = null;
        this.stnSr = null;
        this.extensionContainer = null;
        this.mpsCSPriority = false;
        this.mpsEPSPriority = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {

                    switch (tag) {

                        case _TAG_apnOiReplacement:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apnOiReplacement: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apnOiReplacement = new APNOIReplacementImpl();
                            ((APNOIReplacementImpl) this.apnOiReplacement).decodeAll(ais);
                            break;
                        case _TAG_rfspId:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".rfspId: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.rfspId = new Integer((int) ais.readInteger());
                            break;
                        case _TAG_ambr:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ambr: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ambr = new AMBRImpl();
                            ((AMBRImpl) this.ambr).decodeAll(ais);
                            break;
                        case _TAG_apnConfigurationProfile:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apnConfigurationProfile: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apnConfigurationProfile = new APNConfigurationProfileImpl();
                            ((APNConfigurationProfileImpl) this.apnConfigurationProfile).decodeAll(ais);
                            break;
                        case _TAG_stnSr:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".stnSr: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.stnSr = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.stnSr).decodeAll(ais);
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_mpsCSPriority:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mpsCSPriority: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.mpsCSPriority = true;
                            break;
                        case _TAG_mpsEPSPriority:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mpsEPSPriority: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.mpsEPSPriority = true;
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

            if (this.apnOiReplacement != null)
                ((APNOIReplacementImpl) this.apnOiReplacement).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_apnOiReplacement);

            if (this.rfspId != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_rfspId, this.rfspId);

            if (this.ambr != null)
                ((AMBRImpl) this.ambr).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ambr);

            if (this.apnConfigurationProfile != null)
                ((APNConfigurationProfileImpl) this.apnConfigurationProfile).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_apnConfigurationProfile);

            if (this.stnSr != null)
                ((ISDNAddressStringImpl) this.stnSr).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_stnSr);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (this.mpsCSPriority)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mpsCSPriority);

            if (this.mpsEPSPriority)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mpsEPSPriority);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        if (this.rfspId != null) {
            sb.append("rfspId=");
            sb.append(this.rfspId.toString());
            sb.append(", ");
        }
        if (this.ambr != null) {
            sb.append("ambr=");
            sb.append(this.ambr.toString());
            sb.append(", ");
        }

        if (this.apnConfigurationProfile != null) {
            sb.append("apnConfigurationProfile=");
            sb.append(this.apnConfigurationProfile.toString());
            sb.append(", ");
        }

        if (this.stnSr != null) {
            sb.append("stnSr=");
            sb.append(this.stnSr.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.mpsCSPriority) {
            sb.append("mpsCSPriority, ");
        }

        if (this.mpsEPSPriority) {
            sb.append("mpsEPSPriority ");
        }

        sb.append("]");

        return sb.toString();
    }
}
