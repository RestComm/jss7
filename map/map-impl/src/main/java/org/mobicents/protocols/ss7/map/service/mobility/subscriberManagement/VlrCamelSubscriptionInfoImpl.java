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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class VlrCamelSubscriptionInfoImpl extends SequenceBase implements VlrCamelSubscriptionInfo {

    private static final int _TAG_oCsi = 0;
    private static final int _TAG_extensionContainer = 1;
    private static final int _TAG_ssCsi = 2;
    private static final int _TAG_oBcsmCamelTDPCriteriaList = 4;
    private static final int _TAG_tifCsi = 3;
    private static final int _TAG_mCsi = 5;
    private static final int _TAG_smsCsi = 6;
    private static final int _TAG_vtCsi = 7;
    private static final int _TAG_tBcsmCamelTdpCriteriaList = 8;
    private static final int _TAG_dCsi = 9;
    private static final int _TAG_mtSmsCSI = 10;
    private static final int _TAG_mtSmsCamelTdpCriteriaList = 11;

    private OCSI oCsi;
    private MAPExtensionContainer extensionContainer;
    private SSCSI ssCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
    private boolean tifCsi;
    private MCSI mCsi;
    private SMSCSI smsCsi;
    private TCSI vtCsi;
    private ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList;
    private DCSI dCsi;
    private SMSCSI mtSmsCSI;
    private ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList;

    public VlrCamelSubscriptionInfoImpl() {
        super("VlrCamelSubscriptionInfo");
    }

    public VlrCamelSubscriptionInfoImpl(OCSI oCsi, MAPExtensionContainer extensionContainer, SSCSI ssCsi,
            ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, boolean tifCsi, MCSI mCsi, SMSCSI smsCsi, TCSI vtCsi,
            ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, DCSI dCsi, SMSCSI mtSmsCSI,
            ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList) {
        super("VlrCamelSubscriptionInfo");
        this.oCsi = oCsi;
        this.extensionContainer = extensionContainer;
        this.ssCsi = ssCsi;
        this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
        this.tifCsi = tifCsi;
        this.mCsi = mCsi;
        this.smsCsi = smsCsi;
        this.vtCsi = vtCsi;
        this.tBcsmCamelTdpCriteriaList = tBcsmCamelTdpCriteriaList;
        this.dCsi = dCsi;
        this.mtSmsCSI = mtSmsCSI;
        this.mtSmsCamelTdpCriteriaList = mtSmsCamelTdpCriteriaList;
    }

    @Override
    public OCSI getOCsi() {
        return this.oCsi;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public SSCSI getSsCsi() {
        return this.ssCsi;
    }

    @Override
    public ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList() {
        return this.oBcsmCamelTDPCriteriaList;
    }

    @Override
    public boolean getTifCsi() {
        return this.tifCsi;
    }

    @Override
    public MCSI getMCsi() {
        return this.mCsi;
    }

    @Override
    public SMSCSI getSmsCsi() {
        return this.smsCsi;
    }

    @Override
    public TCSI getVtCsi() {
        return this.vtCsi;
    }

    @Override
    public ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
        return this.tBcsmCamelTdpCriteriaList;
    }

    @Override
    public DCSI getDCsi() {
        return this.dCsi;
    }

    @Override
    public SMSCSI getMtSmsCSI() {
        return this.mtSmsCSI;
    }

    @Override
    public ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
        return this.mtSmsCamelTdpCriteriaList;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.oCsi = null;
        this.extensionContainer = null;
        this.ssCsi = null;
        this.oBcsmCamelTDPCriteriaList = null;
        this.tifCsi = false;
        this.mCsi = null;
        this.smsCsi = null;
        this.vtCsi = null;
        this.tBcsmCamelTdpCriteriaList = null;
        this.dCsi = null;
        this.mtSmsCSI = null;
        this.mtSmsCamelTdpCriteriaList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:

                    switch (tag) {
                        case _TAG_oCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.oCsi = new OCSIImpl();
                            ((OCSIImpl) this.oCsi).decodeAll(ais);
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_ssCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ssCsi = new SSCSIImpl();
                            ((SSCSIImpl) this.ssCsi).decodeAll(ais);
                            break;
                        case _TAG_oBcsmCamelTDPCriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oBcsmCamelTDPCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad OBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                OBcsmCamelTdpCriteria elem = new OBcsmCamelTdpCriteriaImpl();
                                ((OBcsmCamelTdpCriteriaImpl) elem).decodeAll(ais2);
                                this.oBcsmCamelTDPCriteriaList.add(elem);
                            }

                            if (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 6) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 4, found: "
                                        + this.oBcsmCamelTDPCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_tifCsi:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tifCsi: Parameter is not primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.tifCsi = true;
                            break;
                        case _TAG_mCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.mCsi = new MCSIImpl();
                            ((MCSIImpl) this.mCsi).decodeAll(ais);
                            break;
                        case _TAG_smsCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".smsCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.smsCsi = new SMSCSIImpl();
                            ((SMSCSIImpl) this.smsCsi).decodeAll(ais);
                            break;
                        case _TAG_vtCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vtCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.vtCsi = new TCSIImpl();
                            ((TCSIImpl) this.vtCsi).decodeAll(ais);
                            break;
                        case _TAG_tBcsmCamelTdpCriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag2 = ais3.readTag();
                                if (tag2 != Tag.SEQUENCE || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad OBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                TBcsmCamelTdpCriteria elem = new TBcsmCamelTdpCriteriaImpl();
                                ((TBcsmCamelTdpCriteriaImpl) elem).decodeAll(ais3);
                                this.tBcsmCamelTdpCriteriaList.add(elem);
                            }

                            if (this.tBcsmCamelTdpCriteriaList.size() < 1 || this.tBcsmCamelTdpCriteriaList.size() > 6) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter tBcsmCamelTdpCriteriaList from 1 to 4, found: "
                                        + this.tBcsmCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_dCsi:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".dCsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.dCsi = new DCSIImpl();
                            ((DCSIImpl) this.dCsi).decodeAll(ais);
                            break;
                        case _TAG_mtSmsCSI:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mtSmsCSI: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.mtSmsCSI = new SMSCSIImpl();
                            ((SMSCSIImpl) this.mtSmsCSI).decodeAll(ais);
                            break;
                        case _TAG_mtSmsCamelTdpCriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mtSmsCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais4 = ais.readSequenceStream();
                            this.mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
                            while (true) {
                                if (ais4.available() == 0)
                                    break;

                                int tag2 = ais4.readTag();
                                if (tag2 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL || ais4.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad OBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                MTsmsCAMELTDPCriteria elem = new MTsmsCAMELTDPCriteriaImpl();
                                ((MTsmsCAMELTDPCriteriaImpl) elem).decodeAll(ais4);
                                this.mtSmsCamelTdpCriteriaList.add(elem);
                            }

                            if (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 6) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter mtSmsCamelTdpCriteriaList from 1 to 4, found: "
                                        + this.mtSmsCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        default:
                            ais.advanceElement();
                            break;
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

        if (this.oBcsmCamelTDPCriteriaList != null
                && (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 4, found: "
                    + this.oBcsmCamelTDPCriteriaList.size());
        }

        if (this.oBcsmCamelTDPCriteriaList != null
                && (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 4, found: "
                    + this.oBcsmCamelTDPCriteriaList.size());
        }

        if (this.oBcsmCamelTDPCriteriaList != null
                && (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 4)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 4, found: "
                    + this.oBcsmCamelTDPCriteriaList.size());
        }

        try {
            if (this.oCsi != null)
                ((OCSIImpl) this.oCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_oCsi);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);
            if (this.ssCsi != null)
                ((SSCSIImpl) this.ssCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ssCsi);
            if (this.oBcsmCamelTDPCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_oBcsmCamelTDPCriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria : this.oBcsmCamelTDPCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl) oBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.tifCsi)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_tifCsi);
            if (this.mCsi != null)
                ((MCSIImpl) this.mCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mCsi);
            if (this.smsCsi != null)
                ((SMSCSIImpl) this.smsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_smsCsi);
            if (this.vtCsi != null)
                ((TCSIImpl) this.vtCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vtCsi);
            if (this.tBcsmCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_tBcsmCamelTdpCriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria : this.tBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl) tbcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.dCsi != null)
                ((DCSIImpl) this.dCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_dCsi);
            if (this.mtSmsCSI != null)
                ((SMSCSIImpl) this.mtSmsCSI).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mtSmsCSI);
            if (this.mtSmsCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_mtSmsCamelTdpCriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (MTsmsCAMELTDPCriteria mtSMSCAMELTDPCriteria : this.mtSmsCamelTdpCriteriaList) {
                    ((MTsmsCAMELTDPCriteriaImpl) mtSMSCAMELTDPCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
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
        sb.append(_PrimitiveName + " [");

        if (this.oCsi != null) {
            sb.append("oCsi=");
            sb.append(this.oCsi.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.ssCsi != null) {
            sb.append("ssCsi=");
            sb.append(this.ssCsi.toString());
            sb.append(", ");
        }

        if (this.oBcsmCamelTDPCriteriaList != null) {
            sb.append("oBcsmCamelTDPCriteriaList=[");
            boolean firstItem = true;
            for (OBcsmCamelTdpCriteria be : this.oBcsmCamelTDPCriteriaList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.tifCsi) {
            sb.append("tifCsi ");
            sb.append(", ");
        }

        if (this.mCsi != null) {
            sb.append("mCsi=");
            sb.append(this.mCsi.toString());
            sb.append(", ");
        }

        if (this.smsCsi != null) {
            sb.append("smsCsi=");
            sb.append(this.smsCsi.toString());
            sb.append(", ");
        }

        if (this.vtCsi != null) {
            sb.append("vtCsi=");
            sb.append(this.vtCsi.toString());
            sb.append(", ");
        }

        if (this.tBcsmCamelTdpCriteriaList != null) {
            sb.append("tBcsmCamelTdpCriteriaList=[");
            boolean firstItem = true;
            for (TBcsmCamelTdpCriteria be : this.tBcsmCamelTdpCriteriaList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.dCsi != null) {
            sb.append("dCsi=");
            sb.append(this.dCsi.toString());
            sb.append(", ");
        }

        if (this.mtSmsCSI != null) {
            sb.append("mtSmsCSI=");
            sb.append(this.mtSmsCSI.toString());
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
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }

}
