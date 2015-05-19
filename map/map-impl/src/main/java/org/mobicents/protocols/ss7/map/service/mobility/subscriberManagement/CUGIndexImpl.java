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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author alerant appngin
 */
@SuppressWarnings("serial")
public class CUGIndexImpl implements CUGIndex, MAPAsnPrimitive {

    private static final String _PrimitiveName = "CUG-Index";
    private static final String DATA = "data";

    private int data;

    public CUGIndexImpl() {
    }

    public CUGIndexImpl(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public int getTag() throws MAPException {
        return Tag.INTEGER;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    public void decodeAll(AsnInputStream ansIS)
            throws MAPParsingComponentException {
        int length;
        try {
            length = ansIS.readLength();
        } catch (IOException e) {
            throw new MAPParsingComponentException("Failed to parse "
                    + _PrimitiveName, e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        decodeData(ansIS, length);

    }

    public void decodeData(AsnInputStream ansIS, int length)
            throws MAPParsingComponentException {
        long data;
        try {
            data = ansIS.readIntegerData(length);
        } catch (Exception e) {
            throw new MAPParsingComponentException(_PrimitiveName
                    + " invalid data", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (data < 0 || data > 32767)
            throw new MAPParsingComponentException(_PrimitiveName
                    + " value out of range: " + data,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        this.data = (int) data;

    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        encodeAll(asnOs, getTagClass(), getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
            throws MAPException {
        try {
            asnOs.writeTag(tagClass, getIsPrimitive(), tag);
        } catch (Exception e) {
            throw new MAPException("Failed to encode " + _PrimitiveName, e);
        }
        encodeData(asnOs);
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (data < 0 || data > 32767)
            throw new MAPException("Cannot encode invalid " + _PrimitiveName
                    + " data value: " + data);
        try {
            asnOs.writeIntegerData(getData());
        } catch (Exception e) {
            throw new MAPException("Failed to encode " + _PrimitiveName, e);
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CUGIndexImpl> CUG_INDEX_XML = new XMLFormat<CUGIndexImpl>(
            CUGIndexImpl.class) {

        @Override
        public void write(CUGIndexImpl obj,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.add(obj.data, DATA, Integer.class);
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                CUGIndexImpl obj) throws XMLStreamException {
            obj.data = xml.get(DATA, Integer.class);
        }

    };
}
