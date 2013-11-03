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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class EUtranCgiImpl extends OctetStringBase implements EUtranCgi {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public EUtranCgiImpl() {
        super(7, 7, "EUtranCgi");
    }

    public EUtranCgiImpl(byte[] data) {
        super(7, 7, "EUtranCgi", data);
    }

    public byte[] getData() {
        return data;
    }

    // TODO: add implementing of internal structure

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<EUtranCgiImpl> E_UTRAN_CGI_XML = new XMLFormat<EUtranCgiImpl>(EUtranCgiImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, EUtranCgiImpl eUtranCgi) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                eUtranCgi.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(EUtranCgiImpl eUtranCgi, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (eUtranCgi.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(eUtranCgi.data));
            }
        }
    };
}
