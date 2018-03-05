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
import java.util.ArrayList;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariableMessage;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.primitives.ArrayListSerializingBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariableMessageImpl extends SequenceBase implements VariableMessage {

    public static final int _ID_elementaryMessageID = 0;
    public static final int _ID_variableParts = 1;

    private static final String ELEMENTARY_MESSAGE_ID = "elementaryMessageID";
    private static final String VARIABLE_PARTS = "variableParts";
    private static final String VARIABLE_PART = "variablePart";

    private int elementaryMessageID;
    private ArrayList<VariablePart> variableParts;

    public VariableMessageImpl() {
        super("VariableMessage");
    }

    public VariableMessageImpl(int elementaryMessageID, ArrayList<VariablePart> variableParts) {
        super("VariableMessage");
        this.elementaryMessageID = elementaryMessageID;
        this.variableParts = variableParts;
    }

    @Override
    public int getElementaryMessageID() {
        return elementaryMessageID;
    }

    @Override
    public ArrayList<VariablePart> getVariableParts() {
        return variableParts;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {
        this.elementaryMessageID = 0;
        this.variableParts = null;
        boolean elementaryMessageIDFound = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_elementaryMessageID:
                        this.elementaryMessageID = (int) ais.readInteger();
                        elementaryMessageIDFound = true;
                        break;
                    case _ID_variableParts:
                        this.variableParts = new ArrayList<VariablePart>();

                        AsnInputStream ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            ais2.readTag();
                            VariablePartImpl val = new VariablePartImpl();
                            val.decodeAll(ais2);
                            this.variableParts.add(val);
                        }
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.variableParts == null || !elementaryMessageIDFound)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": elementaryMessageID and variableParts are mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.variableParts == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": variableParts must not be null");
        if (this.variableParts.size() < 1 || this.variableParts.size() > 5)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": variableParts size must not be from 1 to 5, found: " + this.variableParts.size());

        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_elementaryMessageID, this.elementaryMessageID);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_variableParts);
            int pos = aos.StartContentDefiniteLength();
            for (VariablePart val : this.variableParts) {
                if (val == null)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": the variableParts array has null values");
                ((VariablePartImpl) val).encodeAll(aos);
            }
            aos.FinalizeContent(pos);

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

        sb.append("elementaryMessageID=");
        sb.append(elementaryMessageID);
        if (this.variableParts != null) {
            sb.append(", variableParts=[");
            for (VariablePart val : this.variableParts) {
                if (val != null) {
                    sb.append("variablePart=[");
                    sb.append(val.toString());
                    sb.append("], ");
                }
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<VariableMessageImpl> VARIABLE_MESSAGE_XML = new XMLFormat<VariableMessageImpl>(
            VariableMessageImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, VariableMessageImpl variableMessage)
                throws XMLStreamException {
            variableMessage.elementaryMessageID = xml.getAttribute(ELEMENTARY_MESSAGE_ID, 0);

            VariableMessage_VariableParts al = xml.get(VARIABLE_PARTS, VariableMessage_VariableParts.class);
            if (al != null) {
                variableMessage.variableParts = al.getData();
            }
        }

        @Override
        public void write(VariableMessageImpl variableMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(ELEMENTARY_MESSAGE_ID, variableMessage.elementaryMessageID);

            if (variableMessage.variableParts != null) {
                VariableMessage_VariableParts al = new VariableMessage_VariableParts(variableMessage.variableParts);
                xml.add(al, VARIABLE_PARTS, VariableMessage_VariableParts.class);
            }
        }
    };

    public static class VariableMessage_VariableParts extends ArrayListSerializingBase<VariablePart> {

        public VariableMessage_VariableParts() {
            super(VARIABLE_PART, VariablePartImpl.class);
        }

        public VariableMessage_VariableParts(ArrayList<VariablePart> data) {
            super(VARIABLE_PART, VariablePartImpl.class, data);
        }

    }

}
