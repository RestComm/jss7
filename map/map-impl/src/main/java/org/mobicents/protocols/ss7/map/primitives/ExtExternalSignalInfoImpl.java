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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExtProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SignalInfo;

/*
 *
 * @author cristian veliscu
 *
 */
public class ExtExternalSignalInfoImpl implements ExtExternalSignalInfo, MAPAsnPrimitive {
    private SignalInfo signalInfo = null;
    private ExtProtocolId extProtocolId = null;
    private MAPExtensionContainer extensionContainer = null;

    private static final String _PrimitiveName = "ExtExternalSignalInfo";

    public ExtExternalSignalInfoImpl() {
    }

    public ExtExternalSignalInfoImpl(SignalInfo signalInfo, ExtProtocolId extProtocolId,
            MAPExtensionContainer extensionContainer) {
        this.signalInfo = signalInfo;
        this.extProtocolId = extProtocolId;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public SignalInfo getSignalInfo() {
        return this.signalInfo;
    }

    @Override
    public ExtProtocolId getExtProtocolId() {
        return this.extProtocolId;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
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

    @Override
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
        this.signalInfo = null;
        this.extProtocolId = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                    case Tag.ENUMERATED:
                        int code = (int) ais.readInteger();
                        this.extProtocolId = ExtProtocolId.getExtProtocolId(code);
                        break;
                    case Tag.STRING_OCTET:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".signalInfo: Parameter extensionContainer is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.signalInfo = new SignalInfoImpl();
                        ((SignalInfoImpl) this.signalInfo).decodeAll(ais);
                        break;
                    case Tag.SEQUENCE:
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
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.extProtocolId != null)
                asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.extProtocolId.getCode());
            if (this.signalInfo != null)
                ((SignalInfoImpl) this.signalInfo).encodeAll(asnOs);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding ExternalSignalInfo : " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding ExternalSignalInfo : " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.signalInfo != null) {
            sb.append("signalInfo=[");
            sb.append(this.signalInfo);
            sb.append("], ");
        }

        if (this.extProtocolId != null) {
            sb.append("extProtocolId=[");
            sb.append(this.extProtocolId);
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
}