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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDText;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageIDTextImpl implements MessageIDText, CAPAsnPrimitive {

    public static final int _ID_messageContent = 0;
    public static final int _ID_attributes = 1;

    public static final String _PrimitiveName = "MessageIDText";

    private String messageContent;
    private byte[] attributes;

    public MessageIDTextImpl() {
    }

    public MessageIDTextImpl(String messageContent, byte[] attributes) {
        this.messageContent = messageContent;
        this.attributes = attributes;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public byte[] getAttributes() {
        return attributes;
    }

    @Override
    public int getTag() throws CAPException {
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
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.messageContent = null;
        this.attributes = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_messageContent:
                        this.messageContent = ais.readIA5String();
                        if (this.messageContent.length() < 1 || this.messageContent.length() > 127)
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": messageContent length must be from 1 to 127, found " + this.messageContent.length(),
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        break;
                    case _ID_attributes:
                        this.attributes = ais.readOctetString();
                        if (this.attributes.length < 2 || this.attributes.length > 10)
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": attributes length must be from 2 to 10, found " + this.attributes.length,
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.messageContent == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": messageContent is mandatory but not found ", CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.messageContent == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": messageContent must not be null");
        if (this.messageContent.length() < 1 || this.messageContent.length() > 127)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": messageContent length must not be from 1 to 127, found: " + this.messageContent.length());

        try {
            aos.writeStringIA5(Tag.CLASS_CONTEXT_SPECIFIC, _ID_messageContent, this.messageContent);
            if (this.attributes != null) {
                if (this.attributes.length < 2 || this.attributes.length > 10)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": messageContent length must not be from 2 to 10, found: " + this.attributes.length);
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_attributes, this.attributes);
            }

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.messageContent != null) {
            sb.append("messageContent=[");
            sb.append(messageContent);
            sb.append("]");
        }
        if (this.attributes != null) {
            sb.append(", attributes=");
            sb.append(printDataArr(this.attributes));
        }

        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }
}
