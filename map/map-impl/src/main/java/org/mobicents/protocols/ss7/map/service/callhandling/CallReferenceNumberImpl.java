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

package org.mobicents.protocols.ss7.map.service.callhandling;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.impl.message.parameter.ByteArrayContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallReferenceNumberImpl extends OctetStringBase implements CallReferenceNumber {

    private static final String DATA_XML = "data";

    public CallReferenceNumberImpl() {
        super(1, 8, "CallReferenceNumber");
    }

    public CallReferenceNumberImpl(byte[] data) {
        super(1, 8, "CallReferenceNumber", data);
    }

    public byte[] getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallReferenceNumberImpl> CALL_REFERENCE_NUMBER_XML = new XMLFormat<CallReferenceNumberImpl>(
            CallReferenceNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallReferenceNumberImpl crn) throws XMLStreamException {
            ByteArrayContainer bc = xml.get(DATA_XML, ByteArrayContainer.class);
            if (bc != null) {
                crn.data = bc.getData();
            }
        }

        @Override
        public void write(CallReferenceNumberImpl crn, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (crn.data != null && crn.data.length > 0) {
                ByteArrayContainer bac = new ByteArrayContainer(crn.data);
                xml.add(bac, DATA_XML, ByteArrayContainer.class);
            }
        }
    };
}
