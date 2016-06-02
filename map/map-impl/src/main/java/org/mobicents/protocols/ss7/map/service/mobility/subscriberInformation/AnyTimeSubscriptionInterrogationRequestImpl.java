/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import java.io.IOException;

/**
 * Created by vsubbotin on 24/05/16.
 */
public class AnyTimeSubscriptionInterrogationRequestImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationRequest, MAPAsnPrimitive {
    private static final int _TAG_SUBSCRIBER_IDENTITY = 0;
    private static final int _TAG_REQUESTED_SUBSCRIPTION_INFO = 1;
    private static final int _TAG_GSM_SCF_ADDRESS = 2;
    private static final int _TAG_EXTENSION_CONTAINER = 3;
    private static final int _TAG_LONG_FTN_SUPPORTED = 4;

    public static final String _PrimitiveName = "AnyTimeSubscriptionInterrogationRequest";

    private SubscriberIdentity subscriberIdentity;
    private RequestedSubscriptionInfo requestedSubscriptionInfo;
    private ISDNAddressString gsmSCFAddress;
    private MAPExtensionContainer mapExtensionContainer;
    private boolean isLongFTNSupported;

    public AnyTimeSubscriptionInterrogationRequestImpl() {

    }

    public AnyTimeSubscriptionInterrogationRequestImpl(SubscriberIdentity subscriberIdentity, RequestedSubscriptionInfo requestedSubscriptionInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer mapExtensionContainer, boolean isLongFTNSupported) {
        this.subscriberIdentity = subscriberIdentity;
        this.requestedSubscriptionInfo = requestedSubscriptionInfo;
        this.gsmSCFAddress = gsmSCFAddress;
        this.mapExtensionContainer = mapExtensionContainer;
        this.isLongFTNSupported = isLongFTNSupported;
    }

    public SubscriberIdentity getSubscriberIdentity() {
        return this.subscriberIdentity;
    }

    public RequestedSubscriptionInfo getRequestedSubscriptionInfo() {
        return this.requestedSubscriptionInfo;
    }

    public ISDNAddressString getGsmScfAddress() {
        return this.gsmSCFAddress;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.mapExtensionContainer;
    }

    public boolean getLongFTNSupported() {
        return this.isLongFTNSupported;
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
        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        this.subscriberIdentity = null;
        this.requestedSubscriptionInfo = null;
        this.gsmSCFAddress = null;
        this.mapExtensionContainer = null;
        this.isLongFTNSupported = false;

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_SUBSCRIBER_IDENTITY:
                        // decode SubscriberIdentity
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter subscriberIdentity is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.subscriberIdentity = new SubscriberIdentityImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((SubscriberIdentityImpl) this.subscriberIdentity).decodeAll(ais2);
                        break;
                    case _TAG_REQUESTED_SUBSCRIPTION_INFO:
                        // decode RequestedSubscriptionInfo
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter requestedInfo is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl();
                        ((RequestedSubscriptionInfoImpl)this.requestedSubscriptionInfo).decodeAll(ais);
                        break;
                    case _TAG_GSM_SCF_ADDRESS:
                        // decode gsmSCF-Address
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter gsmSCFAddress is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.gsmSCFAddress = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
                        break;
                    case _TAG_EXTENSION_CONTAINER:
                        // decode extensionContainer
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        mapExtensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) mapExtensionContainer).decodeAll(ais);
                        break;
                    case _TAG_LONG_FTN_SUPPORTED:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter longFTNSupported is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isLongFTNSupported = Boolean.TRUE;
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.subscriberIdentity == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": subscriberIdentity parameter is mandatory but is not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        if (this.requestedSubscriptionInfo == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": requestedSubscriptionInfo parameter is mandatory but is not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        if (this.gsmSCFAddress == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": gsmSCFAddress parameter is mandatory but is not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
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
        if (this.subscriberIdentity == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter subscriberIdentity is not defined");
        }
        if (this.requestedSubscriptionInfo == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter requestedSubscriptionInfo is not defined");
        }
        if (this.gsmSCFAddress == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gsmSCFAddress is not defined");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_SUBSCRIBER_IDENTITY);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.subscriberIdentity).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding " + _PrimitiveName
                    + " parameter subscriberIdentity [0] SubscriberIdentity");
        }

        ((RequestedSubscriptionInfoImpl)this.requestedSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_REQUESTED_SUBSCRIPTION_INFO);
        ((ISDNAddressStringImpl)this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GSM_SCF_ADDRESS);

        if (this.mapExtensionContainer != null) {
            ((MAPExtensionContainerImpl) this.mapExtensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
        }

        if (this.isLongFTNSupported) {
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LONG_FTN_SUPPORTED);
            } catch (IOException e) {
                throw new MAPException("IOException when encoding parameter longFTNSupported: ", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding parameter longFTNSupported: ", e);
            }
        }
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.subscriberIdentity != null) {
            sb.append("subscriberIdentity=");
            sb.append(this.subscriberIdentity);
        }
        if (this.requestedSubscriptionInfo != null) {
            sb.append(", requestedSubscriptionInfo=");
            sb.append(this.requestedSubscriptionInfo);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress);
        }
        if (this.mapExtensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.mapExtensionContainer);
        }

        if (this.isLongFTNSupported) {
            sb.append(", isLongFTNSupported");
        }

        sb.append("]");
        return sb.toString();
    }
}
