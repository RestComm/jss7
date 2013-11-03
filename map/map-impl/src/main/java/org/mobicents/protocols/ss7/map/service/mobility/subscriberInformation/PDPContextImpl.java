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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class PDPContextImpl extends SequenceBase implements PDPContext {

    private static final int _ID_pdp_Type = 16;
    private static final int _ID_pdp_Address = 17;
    private static final int _ID_QoS_Subscribed = 18;
    private static final int _ID_vplmnAddressAllowed = 19;
    private static final int _ID_apn = 20;
    private static final int _ID_extensionContainer = 21;
    private static final int _ID_ext_QoS_Subscribed = 0;
    private static final int _ID_pdp_ChargingCharacteristics = 1;
    private static final int _ID_ext2_QoS_Subscribed = 2;
    private static final int _ID_ext3_QoS_Subscribed = 3;
    private static final int _ID_ext4_QoS_Subscribed = 4;
    private static final int _ID_apn_oi_Replacement = 5;
    private static final int _ID_ext_pdp_Type = 6;
    private static final int _ID_ext_pdp_Address = 7;
    private static final int _ID_sipto_Permission = 8;
    private static final int _ID_lipa_Permission = 9;

    private int pdpContextId;
    private PDPType pdpType;
    private PDPAddress pdpAddress;
    private QoSSubscribed qosSubscribed;
    private boolean vplmnAddressAllowed;
    private APN apn;
    private MAPExtensionContainer extensionContainer;
    private ExtQoSSubscribed extQoSSubscribed;
    private ChargingCharacteristics chargingCharacteristics;
    private Ext2QoSSubscribed ext2QoSSubscribed;
    private Ext3QoSSubscribed ext3QoSSubscribed;
    private Ext4QoSSubscribed ext4QoSSubscribed;
    private APNOIReplacement apnoiReplacement;
    private ExtPDPType extpdpType;
    private PDPAddress extpdpAddress;
    private SIPTOPermission sipToPermission;
    private LIPAPermission lipaPermission;

    public PDPContextImpl(int pdpContextId, PDPType pdpType, PDPAddress pdpAddress, QoSSubscribed qosSubscribed,
            boolean vplmnAddressAllowed, APN apn, MAPExtensionContainer extensionContainer, ExtQoSSubscribed extQoSSubscribed,
            ChargingCharacteristics chargingCharacteristics, Ext2QoSSubscribed ext2QoSSubscribed,
            Ext3QoSSubscribed ext3QoSSubscribed, Ext4QoSSubscribed ext4QoSSubscribed, APNOIReplacement apnoiReplacement,
            ExtPDPType extpdpType, PDPAddress extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission) {
        super("PDPContext");

        this.pdpContextId = pdpContextId;
        this.pdpType = pdpType;
        this.pdpAddress = pdpAddress;
        this.qosSubscribed = qosSubscribed;
        this.vplmnAddressAllowed = vplmnAddressAllowed;
        this.apn = apn;
        this.extensionContainer = extensionContainer;
        this.extQoSSubscribed = extQoSSubscribed;
        this.chargingCharacteristics = chargingCharacteristics;
        this.ext2QoSSubscribed = ext2QoSSubscribed;
        this.ext3QoSSubscribed = ext3QoSSubscribed;
        this.ext4QoSSubscribed = ext4QoSSubscribed;
        this.apnoiReplacement = apnoiReplacement;
        this.extpdpType = extpdpType;
        this.extpdpAddress = extpdpAddress;
        this.sipToPermission = sipToPermission;
        this.lipaPermission = lipaPermission;
    }

    public PDPContextImpl(int pdpContextId) {
        super("PDPContext");

        this.pdpContextId = pdpContextId;
    }

    public PDPContextImpl() {
        super("PDPContext");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPContextId()
     */
    public int getPDPContextId() {
        return this.pdpContextId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPType()
     */
    public PDPType getPDPType() {
        return this.pdpType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getPDPAddress()
     */
    public PDPAddress getPDPAddress() {
        return this.pdpAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getQoSSubscribed()
     */
    public QoSSubscribed getQoSSubscribed() {
        return this.qosSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #isVPLMNAddressAllowed()
     */
    public boolean isVPLMNAddressAllowed() {
        return this.vplmnAddressAllowed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getAPN()
     */
    public APN getAPN() {
        return this.apn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtQoSSubscribed()
     */
    public ExtQoSSubscribed getExtQoSSubscribed() {
        return this.extQoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getChargingCharacteristics()
     */
    public ChargingCharacteristics getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt2QoSSubscribed()
     */
    public Ext2QoSSubscribed getExt2QoSSubscribed() {
        return this.ext2QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt3QoSSubscribed()
     */
    public Ext3QoSSubscribed getExt3QoSSubscribed() {
        return this.ext3QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExt4QoSSubscribed()
     */
    public Ext4QoSSubscribed getExt4QoSSubscribed() {
        return this.ext4QoSSubscribed;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getAPNOIReplacement()
     */
    public APNOIReplacement getAPNOIReplacement() {
        return this.apnoiReplacement;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtPDPType()
     */
    public ExtPDPType getExtPDPType() {
        return this.extpdpType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getExtPDPAddress()
     */
    public PDPAddress getExtPDPAddress() {
        return this.extpdpAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getSIPTOPermission()
     */
    public SIPTOPermission getSIPTOPermission() {
        return this.sipToPermission;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext #getLIPAPermission()
     */
    public LIPAPermission getLIPAPermission() {
        return this.lipaPermission;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.pdpContextId = -1;
        this.pdpType = null;
        this.pdpAddress = null;
        this.qosSubscribed = null;
        this.vplmnAddressAllowed = false;
        this.apn = null;
        this.extensionContainer = null;
        this.extQoSSubscribed = null;
        this.chargingCharacteristics = null;
        this.ext2QoSSubscribed = null;
        this.ext3QoSSubscribed = null;
        this.ext4QoSSubscribed = null;
        this.apnoiReplacement = null;
        this.extpdpType = null;
        this.extpdpAddress = null;
        this.sipToPermission = null;
        this.lipaPermission = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.INTEGER: // pdpContextId
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdpContextId: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdpContextId = (int) ais.readInteger();
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _ID_pdp_Type:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdpType: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdpType = new PDPTypeImpl();
                            ((PDPTypeImpl) this.pdpType).decodeAll(ais);
                            break;

                        case _ID_pdp_Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdpAddress: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdpAddress = new PDPAddressImpl();
                            ((PDPAddressImpl) this.pdpAddress).decodeAll(ais);
                            break;

                        case _ID_QoS_Subscribed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".qosSubscribed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.qosSubscribed = new QoSSubscribedImpl();
                            ((QoSSubscribedImpl) this.qosSubscribed).decodeAll(ais);
                            break;

                        case _ID_vplmnAddressAllowed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vplmnAddressAllowed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.vplmnAddressAllowed = true;
                            break;

                        case _ID_apn:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apn: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apn = new APNImpl();
                            ((APNImpl) this.apn).decodeAll(ais);
                            break;

                        case _ID_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        case _ID_ext_QoS_Subscribed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extQoSSubscribed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extQoSSubscribed = new ExtQoSSubscribedImpl();
                            ((ExtQoSSubscribedImpl) this.extQoSSubscribed).decodeAll(ais);
                            break;

                        case _ID_pdp_ChargingCharacteristics:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".chargingCharacteristics: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.chargingCharacteristics = new ChargingCharacteristicsImpl();
                            ((ChargingCharacteristicsImpl) this.chargingCharacteristics).decodeAll(ais);
                            break;

                        case _ID_ext2_QoS_Subscribed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ext2QoSSubscribed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ext2QoSSubscribed = new Ext2QoSSubscribedImpl();
                            ((Ext2QoSSubscribedImpl) this.ext2QoSSubscribed).decodeAll(ais);
                            break;

                        case _ID_ext3_QoS_Subscribed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ext3QoSSubscribed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ext3QoSSubscribed = new Ext3QoSSubscribedImpl();
                            ((Ext3QoSSubscribedImpl) this.ext3QoSSubscribed).decodeAll(ais);
                            break;

                        case _ID_ext4_QoS_Subscribed:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ext4QoSSubscribed: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ext4QoSSubscribed = new Ext4QoSSubscribedImpl();
                            ((Ext4QoSSubscribedImpl) this.ext4QoSSubscribed).decodeAll(ais);
                            break;

                        case _ID_apn_oi_Replacement:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apnoiReplacement: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apnoiReplacement = new APNOIReplacementImpl();
                            ((APNOIReplacementImpl) this.apnoiReplacement).decodeAll(ais);
                            break;

                        case _ID_ext_pdp_Type:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extpdpType: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extpdpType = new ExtPDPTypeImpl();
                            ((ExtPDPTypeImpl) this.extpdpType).decodeAll(ais);
                            break;

                        case _ID_ext_pdp_Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extpdpAddress: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extpdpAddress = new PDPAddressImpl();
                            ((PDPAddressImpl) this.extpdpAddress).decodeAll(ais);
                            break;

                        case _ID_sipto_Permission:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException(
                                        "Error while decoding sipToPermission: Parameter bad tag or tag class or not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i1 = (int) ais.readInteger();
                            this.sipToPermission = SIPTOPermission.getInstance(i1);
                            break;

                        case _ID_lipa_Permission:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException(
                                        "Error while decoding lipaPermission: Parameter bad tag or tag class or not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i2 = (int) ais.readInteger();
                            this.lipaPermission = LIPAPermission.getInstance(i2);
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

        if (this.pdpContextId < 1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": A mandatory parameter ContextId has not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.pdpType == null) {
            // System.out.println(" this.pdpType " + this.pdpType );
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": A mandatory parameter pdpType has not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.qosSubscribed == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": A mandatory parameter qosSubscribed has not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.apn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": A mandatory parameter apn has not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pdpType == null || this.qosSubscribed == null || this.apn == null) {
            throw new MAPException("pdpType, qosSubscribed and apn parameters must not be null");
        }

        try {
            asnOs.writeInteger((int) this.pdpContextId);

            ((PDPTypeImpl) this.pdpType).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdp_Type);

            if (this.pdpAddress != null)
                ((PDPAddressImpl) this.pdpAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdp_Address);

            ((QoSSubscribedImpl) this.qosSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_QoS_Subscribed);

            if (this.vplmnAddressAllowed)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_vplmnAddressAllowed);

            ((APNImpl) this.apn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_apn);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.extQoSSubscribed != null)
                ((ExtQoSSubscribedImpl) this.extQoSSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_ext_QoS_Subscribed);

            if (this.chargingCharacteristics != null)
                ((ChargingCharacteristicsImpl) this.chargingCharacteristics).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_pdp_ChargingCharacteristics);

            if (this.ext2QoSSubscribed != null)
                ((Ext2QoSSubscribedImpl) this.ext2QoSSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_ext2_QoS_Subscribed);

            if (this.ext3QoSSubscribed != null)
                ((Ext3QoSSubscribedImpl) this.ext3QoSSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_ext3_QoS_Subscribed);

            if (this.ext4QoSSubscribed != null)
                ((Ext4QoSSubscribedImpl) this.ext4QoSSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_ext4_QoS_Subscribed);

            if (this.apnoiReplacement != null)
                ((APNOIReplacementImpl) this.apnoiReplacement).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_apn_oi_Replacement);

            if (this.extpdpType != null)
                ((ExtPDPTypeImpl) this.extpdpType).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext_pdp_Type);

            if (this.extpdpAddress != null)
                ((PDPAddressImpl) this.extpdpAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext_pdp_Address);

            if (this.sipToPermission != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_sipto_Permission, this.sipToPermission.getCode());

            if (this.lipaPermission != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_lipa_Permission, this.lipaPermission.getCode());

        } catch (IOException e) {
            throw new MAPException("IOException when encoding PDPContext : " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding PDPContext : " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("pdpContextId=");
        sb.append(this.pdpContextId);
        if (this.pdpType != null) {
            sb.append(", pdpType=");
            sb.append(this.pdpType.toString());
        }
        if (this.qosSubscribed != null) {
            sb.append(", qosSubscribed=");
            sb.append(this.qosSubscribed.toString());
        }
        if (this.vplmnAddressAllowed) {
            sb.append(", vplmnAddressAllowed");
        }
        if (this.apn != null) {
            sb.append(", apn=");
            sb.append(this.apn.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.extQoSSubscribed != null) {
            sb.append(", extQoSSubscribed=");
            sb.append(this.extQoSSubscribed.toString());
        }
        if (this.chargingCharacteristics != null) {
            sb.append(", chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
        }
        if (this.ext2QoSSubscribed != null) {
            sb.append(", ext2QoSSubscribed=");
            sb.append(this.ext2QoSSubscribed.toString());
        }
        if (this.ext3QoSSubscribed != null) {
            sb.append(", ext3QoSSubscribed=");
            sb.append(this.ext3QoSSubscribed.toString());
        }
        if (this.ext4QoSSubscribed != null) {
            sb.append(", ext4QoSSubscribed=");
            sb.append(this.ext4QoSSubscribed.toString());
        }
        if (this.apnoiReplacement != null) {
            sb.append(", apnoiReplacement=");
            sb.append(this.apnoiReplacement.toString());
        }
        if (this.extpdpType != null) {
            sb.append(", extpdpType=");
            sb.append(this.extpdpType.toString());
        }
        if (this.extpdpAddress != null) {
            sb.append(", extpdpAddress=");
            sb.append(this.extpdpAddress.toString());
        }
        if (this.sipToPermission != null) {
            sb.append(", sipToPermission=");
            sb.append(this.sipToPermission.toString());
        }
        if (this.lipaPermission != null) {
            sb.append(", lipaPermission=");
            sb.append(this.lipaPermission.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
