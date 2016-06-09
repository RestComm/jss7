/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.mobicents.protocols.ss7.cap.service.sms.primitive;

import javolution.text.CharArray;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringBase;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 */
public class FreeFormatDataSMSImpl extends OctetStringBase implements FreeFormatDataSMS {

    public FreeFormatDataSMSImpl() {
        super(1, 160, "FreeFormatDataSMS");
    }

    public FreeFormatDataSMSImpl(byte[] data) {
        super(1, 160, "FreeFormatDataSMS", data);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<FreeFormatDataSMSImpl> FREE_FORMAT_DATA_SMS_XML = new XMLFormat<FreeFormatDataSMSImpl>(
            FreeFormatDataSMSImpl.class) {

        // serialize as simple text content

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, FreeFormatDataSMSImpl obj) throws XMLStreamException {

            CharArray arr = xml.getText();
            obj.data = hexToBytes(arr.array(), arr.offset(), arr.length());
        }

        @Override
        public void write(FreeFormatDataSMSImpl obj, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            xml.addText(bytesToHex(obj.data));
        }
    };
}
