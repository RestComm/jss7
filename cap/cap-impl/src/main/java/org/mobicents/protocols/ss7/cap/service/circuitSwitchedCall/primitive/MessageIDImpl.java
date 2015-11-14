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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;
import java.util.ArrayList;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDText;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariableMessage;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageIDImpl implements MessageID, CAPAsnPrimitive {

    public static final int _ID_elementaryMessageID = 0;
    public static final int _ID_text = 1;
    public static final int _ID_elementaryMessageIDs = 29;
    public static final int _ID_variableMessage = 30;

    public static final String _PrimitiveName = "MessageID";

    private static final String ELEMENTARY_MESSAGE_ID = "elementaryMessageID";
    private static final String TEXT = "text";
    private static final String ELEMENTARY_MESSAGE_IDS = "elementaryMessageIDs";
    private static final String VARIABLE_MESSAGE = "variableMessage";

    private Integer elementaryMessageID;
    private MessageIDText text;
    private ArrayList<Integer> elementaryMessageIDs;
    private VariableMessage variableMessage;

    public MessageIDImpl() {
    }

    public MessageIDImpl(Integer elementaryMessageID) {
        this.elementaryMessageID = elementaryMessageID;
    }

    public MessageIDImpl(MessageIDText text) {
        this.text = text;
    }

    public MessageIDImpl(ArrayList<Integer> elementaryMessageIDs) {
        this.elementaryMessageIDs = elementaryMessageIDs;
    }

    public MessageIDImpl(VariableMessage variableMessage) {
        this.variableMessage = variableMessage;
    }

    @Override
    public Integer getElementaryMessageID() {
        return elementaryMessageID;
    }

    @Override
    public MessageIDText getText() {
        return text;
    }

    @Override
    public ArrayList<Integer> getElementaryMessageIDs() {
        return elementaryMessageIDs;
    }

    @Override
    public VariableMessage getVariableMessage() {
        return variableMessage;
    }

    @Override
    public int getTag() throws CAPException {

        if (this.elementaryMessageID != null) {
            return _ID_elementaryMessageID;
        } else if (this.text != null) {
            return _ID_text;
        } else if (this.elementaryMessageIDs != null) {
            return _ID_elementaryMessageIDs;
        } else if (this.variableMessage != null) {
            return _ID_variableMessage;
        } else {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": no of choices has been definite");
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (this.elementaryMessageID != null)
            return true;
        else
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.elementaryMessageID = null;
        this.text = null;
        this.elementaryMessageIDs = null;
        this.variableMessage = null;

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        switch (ais.getTag()) {
            case _ID_elementaryMessageID:
                this.elementaryMessageID = (int) ais.readIntegerData(length);
                break;
            case _ID_text:
                this.text = new MessageIDTextImpl();
                ((MessageIDTextImpl) this.text).decodeData(ais, length);
                break;
            case _ID_elementaryMessageIDs:
                this.elementaryMessageIDs = new ArrayList<Integer>();

                AsnInputStream ais2 = ais.readSequenceStreamData(length);
                while (true) {
                    if (ais2.available() == 0)
                        break;

                    int tag2 = ais2.readTag();
                    if (ais2.getTagClass() != Tag.CLASS_UNIVERSAL || tag2 != Tag.INTEGER || !ais2.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": bad tagClass or tag or is not primitive when decoding an elementaryMessageIDs SEQUENCE",
                                CAPParsingComponentExceptionReason.MistypedParameter);

                    Integer val = (int) ais2.readInteger();
                    this.elementaryMessageIDs.add(val);
                }
                break;
            case _ID_variableMessage:
                this.variableMessage = new VariableMessageImpl();
                ((VariableMessageImpl) this.variableMessage).decodeData(ais, length);
                break;
            default:
                throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag: " + ais.getTag(),
                        CAPParsingComponentExceptionReason.MistypedParameter);
        }
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        int choiceCnt = 0;
        if (this.elementaryMessageID != null)
            choiceCnt++;
        if (this.text != null)
            choiceCnt++;
        if (this.elementaryMessageIDs != null)
            choiceCnt++;
        if (this.variableMessage != null)
            choiceCnt++;

        if (choiceCnt != 1)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": only one choice must be definite, found: "
                    + choiceCnt);

        try {
            if (this.elementaryMessageID != null)
                asnOs.writeIntegerData(this.elementaryMessageID);
            if (this.text != null)
                ((MessageIDTextImpl) this.text).encodeData(asnOs);
            if (this.elementaryMessageIDs != null) {
                if (this.elementaryMessageIDs.size() < 1 || this.elementaryMessageIDs.size() > 16)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": elementaryMessageIDs count must be from 1 to 16, found: " + this.elementaryMessageIDs.size());

                for (Integer val : this.elementaryMessageIDs) {
                    if (val == null)
                        throw new CAPException("Error while encoding " + _PrimitiveName
                                + ": the elementaryMessageIDs array has null values");
                    asnOs.writeInteger(val);
                }
            }
            if (this.variableMessage != null)
                ((VariableMessageImpl) this.variableMessage).encodeData(asnOs);
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

        if (this.elementaryMessageID != null) {
            sb.append("elementaryMessageID=");
            sb.append(elementaryMessageID);
        }
        if (this.text != null) {
            sb.append(" text=");
            sb.append(text.toString());
        }
        if (this.elementaryMessageIDs != null) {
            sb.append(" elementaryMessageIDs=[");
            for (Integer val : this.elementaryMessageIDs) {
                if (val != null) {
                    sb.append(val);
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        if (this.variableMessage != null) {
            sb.append(" variableMessage=");
            sb.append(variableMessage.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MessageIDImpl> MESSAGE_ID_XML = new XMLFormat<MessageIDImpl>(MessageIDImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MessageIDImpl messageID) throws XMLStreamException {
            messageID.elementaryMessageID = xml.get(ELEMENTARY_MESSAGE_ID, Integer.class);
            messageID.text = xml.get(TEXT, MessageIDTextImpl.class);

            MessageID_ElementaryMessageIDs al = xml.get(ELEMENTARY_MESSAGE_IDS, MessageID_ElementaryMessageIDs.class);
            if (al != null) {
                messageID.elementaryMessageIDs = al.getData();
            }

            messageID.variableMessage = xml.get(VARIABLE_MESSAGE, VariableMessageImpl.class);

            int choiceCount = 0;
            if (messageID.elementaryMessageID != null)
                choiceCount++;
            if (messageID.text != null)
                choiceCount++;
            if (messageID.elementaryMessageIDs != null)
                choiceCount++;
            if (messageID.variableMessage != null)
                choiceCount++;

            if (choiceCount != 1)
                throw new XMLStreamException("MessageID decoding error: there must be one choice selected, found: "
                        + choiceCount);
        }

        @Override
        public void write(MessageIDImpl messageID, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (messageID.elementaryMessageID != null)
                xml.add(messageID.elementaryMessageID, ELEMENTARY_MESSAGE_ID, Integer.class);
            if (messageID.text != null)
                xml.add((MessageIDTextImpl) messageID.text, TEXT, MessageIDTextImpl.class);
            if (messageID.elementaryMessageIDs != null) {
                MessageID_ElementaryMessageIDs al = new MessageID_ElementaryMessageIDs(messageID.elementaryMessageIDs);
                xml.add(al, ELEMENTARY_MESSAGE_IDS, MessageID_ElementaryMessageIDs.class);
            }
            if (messageID.variableMessage != null)
                xml.add((VariableMessageImpl) messageID.variableMessage, VARIABLE_MESSAGE, VariableMessageImpl.class);
        }
    };

    public static class MessageID_ElementaryMessageIDs extends ArrayListSerializingBase<Integer> {

        public MessageID_ElementaryMessageIDs() {
            super(ELEMENTARY_MESSAGE_ID, Integer.class);
        }

        public MessageID_ElementaryMessageIDs(ArrayList<Integer> data) {
            super(ELEMENTARY_MESSAGE_ID, Integer.class, data);
        }

    }
}
