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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class TAIdImpl extends OctetStringBase implements TAId {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public TAIdImpl() {
        super(5, 5, "TAId");
    }

    public TAIdImpl(byte[] data) {
        super(5, 5, "TAId", data);
    }

    public byte[] getData() {
        return data;
    }

    // TODO: add implementing of internal structure

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TAIdImpl> TA_ID_XML = new XMLFormat<TAIdImpl>(TAIdImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TAIdImpl taId) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                taId.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(TAIdImpl taId, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (taId.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(taId.data));
            }
        }
    };

}
