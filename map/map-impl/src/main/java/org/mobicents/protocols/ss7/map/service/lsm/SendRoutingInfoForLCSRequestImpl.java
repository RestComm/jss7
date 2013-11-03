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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForLCSRequestImpl extends LsmMessageImpl implements SendRoutingInfoForLCSRequest {

    private static final int _TAG_MLC_NUMBER = 0;
    private static final int _TAG_TARGET_MS = 1;
    private static final int _TAG_EXTENSION_CONTAINER = 2;

    public static final String _PrimitiveName = "SendRoutingInfoForLCSRequest";

    private MAPExtensionContainer extensionContainer;
    private SubscriberIdentity targetMS;
    private ISDNAddressString mlcNumber;

    /**
     *
     */
    public SendRoutingInfoForLCSRequestImpl() {
        super();
    }

    /**
     * @param targetMS
     * @param mlcNumber
     */
    public SendRoutingInfoForLCSRequestImpl(ISDNAddressString mlcNumber, SubscriberIdentity targetMS) {
        super();
        this.targetMS = targetMS;
        this.mlcNumber = mlcNumber;
    }

    /**
     * @param extensionContainer
     * @param targetMS
     * @param mlcNumber
     */
    public SendRoutingInfoForLCSRequestImpl(ISDNAddressString mlcNumber, SubscriberIdentity targetMS,
            MAPExtensionContainer extensionContainer) {
        this(mlcNumber, targetMS);
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForLCS_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForLCS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getMLCNumber()
     */
    public ISDNAddressString getMLCNumber() {
        return this.mlcNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getTargetMS()
     */
    public SubscriberIdentity getTargetMS() {
        return this.targetMS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
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

        this.extensionContainer = null;
        this.targetMS = null;
        this.mlcNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        // mlcNumber [0] ISDN-AddressString,
        int tag = ais.readTag();

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_MLC_NUMBER) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter [mlcNumber [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.mlcNumber = new ISDNAddressStringImpl();
        ((ISDNAddressStringImpl) this.mlcNumber).decodeAll(ais);

        // targetMS [1] SubscriberIdentity
        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_TARGET_MS) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter [targetMS [1] SubscriberIdentity] bad tag class or not primitive or not Sequence",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        this.targetMS = new SubscriberIdentityImpl();
        AsnInputStream ais2 = ais.readSequenceStream();
        ais2.readTag();
        ((SubscriberIdentityImpl) this.targetMS).decodeAll(ais2);
        // this.targetMS = new SubscriberIdentityImpl();
        // int length1 = ais.readLength();
        // tag = ais.readTag();
        // ((SubscriberIdentityImpl)this.targetMS).decodeAll(ais);

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
        if (this.mlcNumber == null) {
            throw new MAPException("Encoding of " + _PrimitiveName
                    + " failed. Manadatory parameter mlcNumber [0] ISDN-AddressString is not set");
        }

        if (this.targetMS == null) {
            throw new MAPException("Encoding of " + _PrimitiveName
                    + " failed. Manadatory parameter targetMS [1] SubscriberIdentity is not set");
        }

        ((ISDNAddressStringImpl) this.mlcNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MLC_NUMBER);

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_TARGET_MS);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.targetMS).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding parameter targetMS [1] SubscriberIdentity");
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_EXTENSION_CONTAINER);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.mlcNumber != null) {
            sb.append("mlcNumber");
            sb.append(this.mlcNumber);
        }
        if (this.targetMS != null) {
            sb.append(", targetMS=");
            sb.append(this.targetMS);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");

        return sb.toString();
    }
}
