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
