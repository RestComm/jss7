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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfiguration;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWAllocationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificAPNInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class APNConfigurationImpl extends SequenceBase implements APNConfiguration {

    private static final int _TAG_contextId = 0;
    private static final int _TAG_pDNType = 1;
    private static final int _TAG_servedPartyIPIPv4Address = 2;
    private static final int _TAG_apn = 3;
    private static final int _TAG_ePSQoSSubscribed = 4;
    private static final int _TAG_pdnGwIdentity = 5;
    private static final int _TAG_pdnGwAllocationType = 6;
    private static final int _TAG_vplmnAddressAllowed = 7;
    private static final int _TAG_chargingCharacteristics = 8;
    private static final int _TAG_ambr = 9;
    private static final int _TAG_specificAPNInfoList = 10;
    private static final int _TAG_extensionContainer = 11;
    private static final int _TAG_servedPartyIPIPv6Address = 12;
    private static final int _TAG_apnOiReplacement = 13;
    private static final int _TAG_siptoPermission = 14;
    private static final int _TAG_lipaPermission = 15;

    private int contextId;
    private PDNType pDNType;
    private PDPAddress servedPartyIPIPv4Address;
    private APN apn;
    private EPSQoSSubscribed ePSQoSSubscribed;
    private PDNGWIdentity pdnGwIdentity;
    private PDNGWAllocationType pdnGwAllocationType;
    private boolean vplmnAddressAllowed;
    private ChargingCharacteristics chargingCharacteristics;
    private AMBR ambr;
    private ArrayList<SpecificAPNInfo> specificAPNInfoList;
    private MAPExtensionContainer extensionContainer;
    private PDPAddress servedPartyIPIPv6Address;
    private APNOIReplacement apnOiReplacement;
    private SIPTOPermission siptoPermission;
    private LIPAPermission lipaPermission;

    public APNConfigurationImpl() {
        super("APNConfiguration");
    }

    public APNConfigurationImpl(int contextId, PDNType pDNType, PDPAddress servedPartyIPIPv4Address, APN apn,
            EPSQoSSubscribed ePSQoSSubscribed, PDNGWIdentity pdnGwIdentity, PDNGWAllocationType pdnGwAllocationType,
            boolean vplmnAddressAllowed, ChargingCharacteristics chargingCharacteristics, AMBR ambr,
            ArrayList<SpecificAPNInfo> specificAPNInfoList, MAPExtensionContainer extensionContainer,
            PDPAddress servedPartyIPIPv6Address, APNOIReplacement apnOiReplacement, SIPTOPermission siptoPermission,
            LIPAPermission lipaPermission) {
        super("APNConfiguration");
        this.contextId = contextId;
        this.pDNType = pDNType;
        this.servedPartyIPIPv4Address = servedPartyIPIPv4Address;
        this.apn = apn;
        this.ePSQoSSubscribed = ePSQoSSubscribed;
        this.pdnGwIdentity = pdnGwIdentity;
        this.pdnGwAllocationType = pdnGwAllocationType;
        this.vplmnAddressAllowed = vplmnAddressAllowed;
        this.chargingCharacteristics = chargingCharacteristics;
        this.ambr = ambr;
        this.specificAPNInfoList = specificAPNInfoList;
        this.extensionContainer = extensionContainer;
        this.servedPartyIPIPv6Address = servedPartyIPIPv6Address;
        this.apnOiReplacement = apnOiReplacement;
        this.siptoPermission = siptoPermission;
        this.lipaPermission = lipaPermission;
    }

    @Override
    public int getContextId() {
        return this.contextId;
    }

    @Override
    public PDNType getPDNType() {
        return this.pDNType;
    }

    @Override
    public PDPAddress getServedPartyIPIPv4Address() {
        return this.servedPartyIPIPv4Address;
    }

    @Override
    public APN getApn() {
        return this.apn;
    }

    @Override
    public EPSQoSSubscribed getEPSQoSSubscribed() {
        return this.ePSQoSSubscribed;
    }

    @Override
    public PDNGWIdentity getPdnGwIdentity() {
        return this.pdnGwIdentity;
    }

    @Override
    public PDNGWAllocationType getPdnGwAllocationType() {
        return this.pdnGwAllocationType;
    }

    @Override
    public boolean getVplmnAddressAllowed() {
        return this.vplmnAddressAllowed;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    @Override
    public AMBR getAmbr() {
        return this.ambr;
    }

    @Override
    public ArrayList<SpecificAPNInfo> getSpecificAPNInfoList() {
        return this.specificAPNInfoList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public PDPAddress getServedPartyIPIPv6Address() {
        return this.servedPartyIPIPv6Address;
    }

    @Override
    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    @Override
    public SIPTOPermission getSiptoPermission() {
        return this.siptoPermission;
    }

    @Override
    public LIPAPermission getLipaPermission() {
        return lipaPermission;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.contextId = -1;
        this.pDNType = null;
        this.servedPartyIPIPv4Address = null;
        this.apn = null;
        this.ePSQoSSubscribed = null;
        this.pdnGwIdentity = null;
        this.pdnGwAllocationType = null;
        this.vplmnAddressAllowed = false;
        this.chargingCharacteristics = null;
        this.ambr = null;
        this.specificAPNInfoList = null;
        this.extensionContainer = null;
        this.servedPartyIPIPv6Address = null;
        this.apnOiReplacement = null;
        this.siptoPermission = null;
        this.lipaPermission = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_contextId:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".contextId: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.contextId = (int) ais.readInteger();
                            break;
                        case _TAG_pDNType:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pDNType: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pDNType = new PDNTypeImpl();
                            ((PDNTypeImpl) this.pDNType).decodeAll(ais);
                            break;
                        case _TAG_servedPartyIPIPv4Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".servedPartyIPIPv4Address: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.servedPartyIPIPv4Address = new PDPAddressImpl();
                            ((PDPAddressImpl) this.servedPartyIPIPv4Address).decodeAll(ais);
                            break;
                        case _TAG_apn:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apn: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apn = new APNImpl();
                            ((APNImpl) this.apn).decodeAll(ais);
                            break;
                        case _TAG_ePSQoSSubscribed:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ePSQoSSubscribed: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ePSQoSSubscribed = new EPSQoSSubscribedImpl();
                            ((EPSQoSSubscribedImpl) this.ePSQoSSubscribed).decodeAll(ais);
                            break;
                        case _TAG_pdnGwIdentity:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwIdentity: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdnGwIdentity = new PDNGWIdentityImpl();
                            ((PDNGWIdentityImpl) this.pdnGwIdentity).decodeAll(ais);
                            break;
                        case _TAG_pdnGwAllocationType:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwAllocationType: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i1 = (int) ais.readInteger();
                            this.pdnGwAllocationType = PDNGWAllocationType.getInstance(i1);
                            break;
                        case _TAG_vplmnAddressAllowed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vplmnAddressAllowed: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.vplmnAddressAllowed = true;
                            break;
                        case _TAG_chargingCharacteristics:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".chargingCharacteristics: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.chargingCharacteristics = new ChargingCharacteristicsImpl();
                            ((ChargingCharacteristicsImpl) this.chargingCharacteristics).decodeAll(ais);
                            break;
                        case _TAG_ambr:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ambr: Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ambr = new AMBRImpl();
                            ((AMBRImpl) this.ambr).decodeAll(ais);
                            break;
                        case _TAG_specificAPNInfoList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".specificAPNInfoList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            SpecificAPNInfo specificAPNInfo = null;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.specificAPNInfoList = new ArrayList<SpecificAPNInfo>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + "SpecificAPNInfo: bad tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                specificAPNInfo = new SpecificAPNInfoImpl();
                                ((SpecificAPNInfoImpl) specificAPNInfo).decodeAll(ais2);
                                this.specificAPNInfoList.add(specificAPNInfo);
                            }

                            if (this.specificAPNInfoList.size() < 1 || this.specificAPNInfoList.size() > 50) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter specificAPNInfoList size must be from 1 to 50, found: "
                                        + this.specificAPNInfoList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_servedPartyIPIPv6Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".servedPartyIPIPv6Address: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.servedPartyIPIPv6Address = new PDPAddressImpl();
                            ((PDPAddressImpl) this.servedPartyIPIPv6Address).decodeAll(ais);
                            break;
                        case _TAG_apnOiReplacement:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apnOiReplacement: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apnOiReplacement = new APNOIReplacementImpl();
                            ((APNOIReplacementImpl) this.apnOiReplacement).decodeAll(ais);
                            break;
                        case _TAG_siptoPermission:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".siptoPermission: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i2 = (int) ais.readInteger();
                            this.siptoPermission = SIPTOPermission.getInstance(i2);
                            break;
                        case _TAG_lipaPermission:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".lipaPermission: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i3 = (int) ais.readInteger();
                            this.lipaPermission = LIPAPermission.getInstance(i3);
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

        if (this.pDNType == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament pDNType is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.apn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament apn is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.ePSQoSSubscribed == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ePSQoSSubscribed is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.contextId == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament contextId is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pDNType == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter pDNType is not defined");
        }

        if (this.apn == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter apn is not defined");
        }

        if (this.ePSQoSSubscribed == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter ePSQoSSubscribed is not defined");
        }

        if (this.specificAPNInfoList != null && (this.specificAPNInfoList.size() < 1 || this.specificAPNInfoList.size() > 50)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter specificAPNInfoList size must be from 1 to 50, found: " + this.specificAPNInfoList.size());
        }

        try {

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_contextId, this.contextId);

            ((PDNTypeImpl) this.pDNType).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pDNType);

            if (this.servedPartyIPIPv4Address != null)
                ((PDPAddressImpl) this.servedPartyIPIPv4Address).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_servedPartyIPIPv4Address);

            ((APNImpl) this.apn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_apn);

            ((EPSQoSSubscribedImpl) this.ePSQoSSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ePSQoSSubscribed);

            if (this.pdnGwIdentity != null)
                ((PDNGWIdentityImpl) this.pdnGwIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwIdentity);

            if (this.pdnGwAllocationType != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwAllocationType, this.pdnGwAllocationType.getCode());

            if (vplmnAddressAllowed)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vplmnAddressAllowed);

            if (this.chargingCharacteristics != null)
                ((ChargingCharacteristicsImpl) this.chargingCharacteristics).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_chargingCharacteristics);

            if (this.ambr != null)
                ((AMBRImpl) this.ambr).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ambr);

            if (specificAPNInfoList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_specificAPNInfoList);
                int pos = asnOs.StartContentDefiniteLength();
                for (SpecificAPNInfo specificAPNInfo : this.specificAPNInfoList) {
                    ((SpecificAPNInfoImpl) specificAPNInfo).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (this.servedPartyIPIPv6Address != null)
                ((PDPAddressImpl) this.servedPartyIPIPv6Address).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_servedPartyIPIPv6Address);

            if (this.apnOiReplacement != null)
                ((APNOIReplacementImpl) this.apnOiReplacement).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_apnOiReplacement);

            if (this.siptoPermission != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_siptoPermission, this.siptoPermission.getCode());

            if (this.lipaPermission != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_lipaPermission, this.lipaPermission.getCode());

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

        sb.append("contextId=");
        sb.append(this.contextId);
        sb.append(", ");

        if (this.pDNType != null) {
            sb.append("pDNType=");
            sb.append(this.pDNType.toString());
            sb.append(", ");
        }

        if (this.servedPartyIPIPv4Address != null) {
            sb.append("servedPartyIPIPv4Address=");
            sb.append(this.servedPartyIPIPv4Address.toString());
            sb.append(", ");
        }

        if (this.apn != null) {
            sb.append("apn=");
            sb.append(this.apn.toString());
            sb.append(", ");
        }

        if (this.ePSQoSSubscribed != null) {
            sb.append("ePSQoSSubscribed=");
            sb.append(this.ePSQoSSubscribed.toString());
            sb.append(", ");
        }

        if (this.pdnGwIdentity != null) {
            sb.append("pdnGwIdentity=");
            sb.append(this.pdnGwIdentity.toString());
            sb.append(", ");
        }

        if (this.pdnGwAllocationType != null) {
            sb.append("pdnGwAllocationType=");
            sb.append(this.pdnGwAllocationType.toString());
            sb.append(", ");
        }

        if (this.vplmnAddressAllowed) {
            sb.append("vplmnAddressAllowed, ");
        }

        if (this.chargingCharacteristics != null) {
            sb.append("chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
            sb.append(", ");
        }

        if (this.ambr != null) {
            sb.append("ambr=");
            sb.append(this.ambr.toString());
            sb.append(", ");
        }

        if (this.specificAPNInfoList != null) {
            sb.append("specificAPNInfoList=[");
            boolean firstItem = true;
            for (SpecificAPNInfo be : this.specificAPNInfoList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.servedPartyIPIPv6Address != null) {
            sb.append("servedPartyIPIPv6Address=");
            sb.append(this.servedPartyIPIPv6Address.toString());
            sb.append(", ");
        }

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        if (this.siptoPermission != null) {
            sb.append("siptoPermission=");
            sb.append(this.siptoPermission.toString());
            sb.append(", ");
        }

        if (this.lipaPermission != null) {
            sb.append("lipaPermission=");
            sb.append(this.lipaPermission.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
}
