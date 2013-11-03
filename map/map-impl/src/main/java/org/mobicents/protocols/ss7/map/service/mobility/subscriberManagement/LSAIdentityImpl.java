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

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LSAIdentityImpl extends OctetStringBase implements LSAIdentity {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public LSAIdentityImpl() {
        super(3, 3, "LSAIdentity");
    }

    public LSAIdentityImpl(byte[] data) {
        super(3, 3, "LSAIdentity", data);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean isPlmnSignificantLSA() {
        return ((this.data[2] & 0x01) == 0x01);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LSAIdentityImpl> LSA_IDENTITY_XML = new XMLFormat<LSAIdentityImpl>(LSAIdentityImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LSAIdentityImpl lsaIdentity) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                lsaIdentity.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(LSAIdentityImpl lsaIdentity, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (lsaIdentity.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(lsaIdentity.data));
            }
        }
    };
}
