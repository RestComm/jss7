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
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;

/**
 * @author amit bhayani
 *
 */
public class SendRoutingInfoForLCSResponseImpl extends LsmMessageImpl implements SendRoutingInfoForLCSResponse {

    private static final int _TAG_TARGET_MS = 0;
    private static final int _TAG_LCS_LOCATION_INFO = 1;
    private static final int _TAG_EXTENSION_CONTAINER = 2;
    private static final int _TAG_V_GMLC_ADDRESS = 3;
    private static final int _TAG_H_GMLC_ADDRESS = 4;
    private static final int _TAG_PPR_ADDRESS = 5;
    private static final int _TAG_ADDITIONAL_V_GMLC_ADDRESS = 6;

    public static final String _PrimitiveName = "SendRoutingInfoForLCSResponse";

    private SubscriberIdentity targetMS;
    private LCSLocationInfo lcsLocationInfo;
    private MAPExtensionContainer extensionContainer;
    private GSNAddress vgmlcAddress;
    private GSNAddress hGmlcAddress;
    private GSNAddress pprAddress;
    private GSNAddress additionalVGmlcAddress;

    /**
     *
     */
    public SendRoutingInfoForLCSResponseImpl() {
        super();
    }

    /**
     * @param targetMS
     * @param lcsLocationInfo
     * @param extensionContainer
     * @param vgmlcAddress
     * @param hGmlcAddress
     * @param pprAddress
     * @param additionalVGmlcAddress
     */
    public SendRoutingInfoForLCSResponseImpl(SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
            MAPExtensionContainer extensionContainer, GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress,
            GSNAddress additionalVGmlcAddress) {
        super();
        this.targetMS = targetMS;
        this.lcsLocationInfo = lcsLocationInfo;
        this.extensionContainer = extensionContainer;
        this.vgmlcAddress = vgmlcAddress;
        this.hGmlcAddress = hGmlcAddress;
        this.pprAddress = pprAddress;
        this.additionalVGmlcAddress = additionalVGmlcAddress;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForLCS_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForLCS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getTargetMS()
     */
    public SubscriberIdentity getTargetMS() {
        return this.targetMS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getLCSLocationInfo()
     */
    public LCSLocationInfo getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getVgmlcAddress()
     */
    public GSNAddress getVgmlcAddress() {
        return this.vgmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getHGmlcAddress()
     */
    public GSNAddress getHGmlcAddress() {
        return this.hGmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getPprAddress()
     */
    public GSNAddress getPprAddress() {
        return this.pprAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.
     * SendRoutingInforForLCSResponseIndication#getAdditionalVGmlcAddress()
     */
    public GSNAddress getAdditionalVGmlcAddress() {
        return this.additionalVGmlcAddress;
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

        this.targetMS = null;
        this.lcsLocationInfo = null;
        this.extensionContainer = null;
        this.vgmlcAddress = null;
        this.hGmlcAddress = null;
        this.pprAddress = null;
        this.additionalVGmlcAddress = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        // targetMS [0] SubscriberIdentity
        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_TARGET_MS) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter [targetMS [0] SubscriberIdentity] bad tag, tag class or primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.targetMS = new SubscriberIdentityImpl();
        AsnInputStream ais2 = ais.readSequenceStream();
        ais2.readTag();
        ((SubscriberIdentityImpl) this.targetMS).decodeAll(ais2);

        // lcsLocationInfo [1] LCSLocationInfo,
        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_LCS_LOCATION_INFO) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter [lcsLocationInfo [1] LCSLocationInfo] bad tag, tag class or primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.lcsLocationInfo = new LCSLocationInfoImpl();
        ((LCSLocationInfoImpl) this.lcsLocationInfo).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_EXTENSION_CONTAINER:
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [extensionContainer [2] ExtensionContainer] is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _TAG_V_GMLC_ADDRESS:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [v-gmlc-Address [3] GSN-Address] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.vgmlcAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.vgmlcAddress).decodeAll(ais);
                        break;
                    case _TAG_H_GMLC_ADDRESS:
                        // h-gmlc-Address [4] GSN-Address OPTIONAL,
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [h-gmlc-Address [4] GSN-Address] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.hGmlcAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.hGmlcAddress).decodeAll(ais);
                        break;
                    case _TAG_PPR_ADDRESS:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [ppr-Address [5] GSN-Address] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.pprAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.pprAddress).decodeAll(ais);
                        break;
                    case _TAG_ADDITIONAL_V_GMLC_ADDRESS:
                        // additional-v-gmlc-Address [6] GSN-Address OPTIONAL
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [additional-v-gmlc-Address [6] GSN-Address] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.additionalVGmlcAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.additionalVGmlcAddress).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }

        }// while

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
            asnOs.writeTag(tagClass, false, tag);
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
        if (this.targetMS == null) {
            throw new MAPException("Encoding of " + _PrimitiveName
                    + " failed. Manadatory parameter targetMS [0] SubscriberIdentity is not set");
        }

        if (this.lcsLocationInfo == null) {
            throw new MAPException("Encoding of " + _PrimitiveName
                    + " failed. Manadatory parameter lcsLocationInfo [1] LCSLocationInfo is not set");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_TARGET_MS);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.targetMS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    ((SubscriberIdentityImpl) this.targetMS).getTag());
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding parameter " + _PrimitiveName
                    + ".targetMS [0] SubscriberIdentity");
        }

        ((LCSLocationInfoImpl) this.lcsLocationInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_LOCATION_INFO);

        if (this.extensionContainer != null) {
            // extensionContainer [2] ExtensionContainer OPTIONAL,
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_EXTENSION_CONTAINER);
        }

        if (this.vgmlcAddress != null) {
            // v-gmlc-Address [3] GSN-Address OPTIONAL,
            ((GSNAddressImpl) this.vgmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_V_GMLC_ADDRESS);
        }

        if (this.hGmlcAddress != null) {
            // h-gmlc-Address [4] GSN-Address OPTIONAL,
            ((GSNAddressImpl) this.hGmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_H_GMLC_ADDRESS);
        }

        if (this.pprAddress != null) {
            // ppr-Address [5] GSN-Address OPTIONAL,
            ((GSNAddressImpl) this.pprAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PPR_ADDRESS);
        }

        if (this.additionalVGmlcAddress != null) {
            // additional-v-gmlc-Address [6] GSN-Address OPTIONAL,
            ((GSNAddressImpl) this.additionalVGmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_ADDITIONAL_V_GMLC_ADDRESS);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.targetMS != null) {
            sb.append("targetMS");
            sb.append(this.targetMS);
        }
        if (this.lcsLocationInfo != null) {
            sb.append(", lcsLocationInfo=");
            sb.append(this.lcsLocationInfo);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.vgmlcAddress != null) {
            sb.append(", vgmlcAddress=");
            sb.append(this.vgmlcAddress);
        }
        if (this.hGmlcAddress != null) {
            sb.append(", hGmlcAddress=");
            sb.append(this.hGmlcAddress);
        }
        if (this.pprAddress != null) {
            sb.append(", pprAddress=");
            sb.append(this.pprAddress);
        }
        if (this.additionalVGmlcAddress != null) {
            sb.append(", additionalVGmlcAddress=");
            sb.append(this.additionalVGmlcAddress);
        }

        sb.append("]");

        return sb.toString();
    }
}
