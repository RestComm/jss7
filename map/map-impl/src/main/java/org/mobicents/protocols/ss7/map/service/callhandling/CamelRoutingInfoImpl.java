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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CamelRoutingInfoImpl extends SequenceBase implements CamelRoutingInfo {

    private static final int _TAG_gmscCamelSubscriptionInfo = 0;
    private static final int _TAG_extensionContainer = 1;

    private ForwardingData forwardingData;
    private GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo;
    private MAPExtensionContainer extensionContainer;

    public CamelRoutingInfoImpl() {
        super("CamelRoutingInfo");
    }

    public CamelRoutingInfoImpl(ForwardingData forwardingData, GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo,
            MAPExtensionContainer extensionContainer) {
        super("CamelRoutingInfo");
        this.forwardingData = forwardingData;
        this.gmscCamelSubscriptionInfo = gmscCamelSubscriptionInfo;
        this.extensionContainer = extensionContainer;
    }

    public ForwardingData getForwardingData() {
        return forwardingData;
    }

    public GmscCamelSubscriptionInfo getGmscCamelSubscriptionInfo() {
        return gmscCamelSubscriptionInfo;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.forwardingData = null;
        this.gmscCamelSubscriptionInfo = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.SEQUENCE: // forwardingData
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingData: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.forwardingData = new ForwardingDataImpl();
                            ((ForwardingDataImpl) this.forwardingData).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_gmscCamelSubscriptionInfo:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gmscCamelSubscriptionInfo: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.gmscCamelSubscriptionInfo = new GmscCamelSubscriptionInfoImpl();
                            ((GmscCamelSubscriptionInfoImpl) this.gmscCamelSubscriptionInfo).decodeAll(ais);
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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

        if (gmscCamelSubscriptionInfo == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament gmscCamelSubscriptionInfo is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.gmscCamelSubscriptionInfo == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": gmscCamelSubscriptionInfo must not be null");

        if (this.forwardingData != null)
            ((ForwardingDataImpl) this.forwardingData).encodeAll(asnOs);

        ((GmscCamelSubscriptionInfoImpl) this.gmscCamelSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                _TAG_gmscCamelSubscriptionInfo);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extensionContainer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.forwardingData != null) {
            sb.append("forwardingData=");
            sb.append(this.forwardingData.toString());
        }
        if (this.gmscCamelSubscriptionInfo != null) {
            sb.append(", gmscCamelSubscriptionInfo=");
            sb.append(this.gmscCamelSubscriptionInfo.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
