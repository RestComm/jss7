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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ByteArrayContainer {

    private static final String VALUE = "value";

    private static final String DEFAULT_VALUE = null;

    private byte[] data;

    public ByteArrayContainer() {
    }

    public ByteArrayContainer(byte[] val) {
        this.data = val;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] val) {
        data = val;
    }

    /**
     * XML Serialization/Deserialization
     */
    public static final XMLFormat<ByteArrayContainer> ISUP_BYTE_ARRAY_XML = new XMLFormat<ByteArrayContainer>(
            ByteArrayContainer.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ByteArrayContainer arr) throws XMLStreamException {

            String s = xml.getAttribute(VALUE, DEFAULT_VALUE);
            arr.setData(DatatypeConverter.parseHexBinary(s));
        }

        @Override
        public void write(ByteArrayContainer arr, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            byte[] data = arr.getData();
            String s;
            if (data != null) {
                s = DatatypeConverter.printHexBinary(data);
            } else {
                s = "";
            }

            xml.setAttribute(VALUE, s);
        }
    };

}
