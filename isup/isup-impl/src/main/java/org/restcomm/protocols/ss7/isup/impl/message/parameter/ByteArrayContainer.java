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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

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
