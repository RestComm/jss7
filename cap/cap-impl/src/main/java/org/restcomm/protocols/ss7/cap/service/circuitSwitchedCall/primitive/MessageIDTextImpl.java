/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDText;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ByteArrayContainer;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageIDTextImpl extends SequenceBase implements MessageIDText {

    public static final int _ID_messageContent = 0;
    public static final int _ID_attributes = 1;

    private static final String MESSAGE_CONTENT = "messageContent";
    private static final String ATTRIBUTES = "attributes";

    private String messageContent;
    private byte[] attributes;

    public MessageIDTextImpl() {
        super("MessageIDText");
    }

    public MessageIDTextImpl(String messageContent, byte[] attributes) {
        super("MessageIDText");
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

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {
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

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MessageIDTextImpl> MESSAGE_ID_TEXT_XML = new XMLFormat<MessageIDTextImpl>(
            MessageIDTextImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MessageIDTextImpl messageIDText) throws XMLStreamException {
            messageIDText.messageContent = xml.getAttribute(MESSAGE_CONTENT, "");

            ByteArrayContainer bc = xml.get(ATTRIBUTES, ByteArrayContainer.class);
            if (bc != null) {
                messageIDText.attributes = bc.getData();
            }
        }

        @Override
        public void write(MessageIDTextImpl messageIDText, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (messageIDText.messageContent != null)
                xml.setAttribute(MESSAGE_CONTENT, messageIDText.messageContent);

            if (messageIDText.attributes != null) {
                ByteArrayContainer bac = new ByteArrayContainer(messageIDText.attributes);
                xml.add(bac, ATTRIBUTES, ByteArrayContainer.class);
            }
        }
    };
}
