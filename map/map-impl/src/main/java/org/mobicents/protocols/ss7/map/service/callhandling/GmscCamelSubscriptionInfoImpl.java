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

package org.mobicents.protocols.ss7.map.service.callhandling;

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
import org.mobicents.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GmscCamelSubscriptionInfoImpl extends SequenceBase implements GmscCamelSubscriptionInfo {

    private static final int _TAG_t_CSI = 0;
    private static final int _TAG_o_CSI = 1;
    private static final int _TAG_extensionContainer = 2;
    private static final int _TAG_o_BcsmCamelTDP_CriteriaList = 3;
    private static final int _TAG_t_BCSM_CAMEL_TDP_CriteriaList = 4;
    private static final int _TAG_d_csi = 5;

    private TCSI tCsi;
    private OCSI oCsi;
    private MAPExtensionContainer extensionContainer;
    private ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTdpCriteriaList;
    private ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList;
    private DCSI dCsi;

    public GmscCamelSubscriptionInfoImpl() {
        super("GmscCamelSubscriptionInfo");
    }

    public GmscCamelSubscriptionInfoImpl(TCSI tCsi, OCSI oCsi, MAPExtensionContainer extensionContainer,
            ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList,
            ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi) {
        super("GmscCamelSubscriptionInfo");

        this.tCsi = tCsi;
        this.oCsi = oCsi;
        this.extensionContainer = extensionContainer;
        this.oBcsmCamelTdpCriteriaList = oBcsmCamelTDPCriteriaList;
        this.tBcsmCamelTdpCriteriaList = tBcsmCamelTdpCriteriaList;
        this.dCsi = dCsi;
    }

    @Override
    public TCSI getTCsi() {
        return tCsi;
    }

    @Override
    public OCSI getOCsi() {
        return oCsi;
    }

    @Override
    public MAPExtensionContainer getMAPExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTdpCriteriaList() {
        return oBcsmCamelTdpCriteriaList;
    }

    @Override
    public ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
        return tBcsmCamelTdpCriteriaList;
    }

    @Override
    public DCSI getDCsi() {
        return dCsi;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.tCsi = null;
        this.oCsi = null;
        this.extensionContainer = null;
        this.oBcsmCamelTdpCriteriaList = null;
        this.tBcsmCamelTdpCriteriaList = null;
        this.dCsi = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {

                        case _TAG_t_CSI:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".t_CSI: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.tCsi = new TCSIImpl();
                            ((TCSIImpl) this.tCsi).decodeAll(ais);
                            break;
                        case _TAG_o_CSI:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".o_CSI: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.oCsi = new OCSIImpl();
                            ((OCSIImpl) this.oCsi).decodeAll(ais);
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_o_BcsmCamelTDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".o_BcsmCamelTDP_CriteriaList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            // TODO: implement it
                            ais.advanceElement();
                            break;
                        case _TAG_t_BCSM_CAMEL_TDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".o_BcsmCamelTDP_CriteriaList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            // TODO: implement it
                            ais.advanceElement();
                            break;
                        case _TAG_d_csi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".o_BcsmCamelTDP_CriteriaList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            // TODO: implement it
                            ais.advanceElement();
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
        if (this.tCsi != null)
            ((TCSIImpl) this.tCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_t_CSI);

        if (this.oCsi != null)
            ((OCSIImpl) this.oCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_o_CSI);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extensionContainer);

        if (this.oBcsmCamelTdpCriteriaList != null) {
            // TODO: implement it
            // _TAG_o_BcsmCamelTDP_CriteriaList
        }
        if (this.tBcsmCamelTdpCriteriaList != null) {
            // TODO: implement it
            // _TAG_t_BCSM_CAMEL_TDP_CriteriaList
        }
        if (this.dCsi != null) {
            // TODO: implement it
            // _TAG_d_csi
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.tCsi != null) {
            sb.append("tCsi=");
            sb.append(this.tCsi.toString());
        }
        if (this.oCsi != null) {
            sb.append(", oCsi=");
            sb.append(this.oCsi.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        if (this.oBcsmCamelTdpCriteriaList != null) {
            sb.append("oBcsmCamelTDPCriteriaList=[");
            for (OBcsmCamelTdpCriteria be : this.oBcsmCamelTdpCriteriaList) {
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.tBcsmCamelTdpCriteriaList != null) {
            sb.append("tBcsmCamelTdpCriteriaList=[");
            for (TBcsmCamelTdpCriteria be : this.tBcsmCamelTdpCriteriaList) {
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.dCsi != null) {
            sb.append(", dCsi=");
            sb.append(this.dCsi.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
