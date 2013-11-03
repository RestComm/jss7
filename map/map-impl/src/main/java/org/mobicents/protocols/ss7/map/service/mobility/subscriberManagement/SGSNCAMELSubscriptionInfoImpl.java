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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SGSNCAMELSubscriptionInfoImpl extends SequenceBase implements SGSNCAMELSubscriptionInfo {

    private static final int _TAG_gprsCsi = 0;
    private static final int _TAG_moSmsCsi = 1;
    private static final int _TAG_extensionContainer = 2;
    private static final int _TAG_mtSmsCsi = 3;
    private static final int _TAG_mtSmsCamelTdpCriteriaList = 4;
    private static final int _TAG_mgCsi = 5;

    private GPRSCSI gprsCsi;
    private SMSCSI moSmsCsi;
    private MAPExtensionContainer extensionContainer;
    private SMSCSI mtSmsCsi;
    private ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList;
    private MGCSI mgCsi;

    public SGSNCAMELSubscriptionInfoImpl() {
        super("SGSNCAMELSubscriptionInfo");
    }

    public SGSNCAMELSubscriptionInfoImpl(GPRSCSI gprsCsi, SMSCSI moSmsCsi, MAPExtensionContainer extensionContainer,
            SMSCSI mtSmsCsi, ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList, MGCSI mgCsi) {
        super("SGSNCAMELSubscriptionInfo");
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.extensionContainer = extensionContainer;
        this.mtSmsCsi = mtSmsCsi;
        this.mtSmsCamelTdpCriteriaList = mtSmsCamelTdpCriteriaList;
        this.mgCsi = mgCsi;
    }

    @Override
    public GPRSCSI getGprsCsi() {
        return this.gprsCsi;
    }

    @Override
    public SMSCSI getMoSmsCsi() {
        return this.moSmsCsi;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public SMSCSI getMtSmsCsi() {
        return this.mtSmsCsi;
    }

    @Override
    public ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
        return this.mtSmsCamelTdpCriteriaList;
    }

    @Override
    public MGCSI getMgCsi() {
        return this.mgCsi;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.gprsCsi = null;
        this.moSmsCsi = null;
        this.extensionContainer = null;
        this.mtSmsCsi = null;
        this.mtSmsCamelTdpCriteriaList = null;
        this.mgCsi = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_gprsCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gprsCsi: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.gprsCsi = new GPRSCSIImpl();
                            ((GPRSCSIImpl) this.gprsCsi).decodeAll(ais);
                            break;
                        case _TAG_moSmsCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".moSmsCsi: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.moSmsCsi = new SMSCSIImpl();
                            ((SMSCSIImpl) this.moSmsCsi).decodeAll(ais);
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_mtSmsCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mtSmsCsi: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.mtSmsCsi = new SMSCSIImpl();
                            ((SMSCSIImpl) this.mtSmsCsi).decodeAll(ais);
                            break;
                        case _TAG_mtSmsCamelTdpCriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mtSmsCamelTdpCriteriaList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = null;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException(
                                            "Error while decoding "
                                                    + _PrimitiveName
                                                    + ".mtSmsCamelTdpCriteriaList.mtsmsCAMELTDPCriteria: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                mtsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl();
                                ((MTsmsCAMELTDPCriteriaImpl) mtsmsCAMELTDPCriteria).decodeAll(ais2);
                                this.mtSmsCamelTdpCriteriaList.add(mtsmsCAMELTDPCriteria);
                            }

                            if (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter mtSmsCamelTdpCriteriaList size must be from 1 to 10, found: "
                                        + this.mtSmsCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_mgCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mgCsi: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.mgCsi = new MGCSIImpl();
                            ((MGCSIImpl) this.mgCsi).decodeAll(ais);
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

        if (this.mtSmsCamelTdpCriteriaList != null
                && (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter mtSmsCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.mtSmsCamelTdpCriteriaList.size());
        }

        try {

            if (this.gprsCsi != null)
                ((GPRSCSIImpl) this.gprsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gprsCsi);

            if (this.moSmsCsi != null)
                ((SMSCSIImpl) this.moSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_moSmsCsi);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (this.mtSmsCsi != null)
                ((SMSCSIImpl) this.mtSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mtSmsCsi);

            if (this.mtSmsCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_mtSmsCamelTdpCriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria : this.mtSmsCamelTdpCriteriaList) {
                    ((MTsmsCAMELTDPCriteriaImpl) mtsmsCAMELTDPCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.mgCsi != null)
                ((MGCSIImpl) this.mgCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mgCsi);

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.gprsCsi != null) {
            sb.append("gprsCsi=");
            sb.append(this.gprsCsi.toString());
            sb.append(", ");
        }

        if (this.moSmsCsi != null) {
            sb.append("moSmsCsi=");
            sb.append(this.moSmsCsi.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.mtSmsCsi != null) {
            sb.append("mtSmsCsi=");
            sb.append(this.mtSmsCsi.toString());
            sb.append(", ");
        }

        if (this.mtSmsCamelTdpCriteriaList != null) {
            sb.append("mtSmsCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (MTsmsCAMELTDPCriteria be : this.mtSmsCamelTdpCriteriaList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.mgCsi != null) {
            sb.append("mgCsi=");
            sb.append(this.mgCsi.toString());

        }

        sb.append("]");

        return sb.toString();
    }

}
