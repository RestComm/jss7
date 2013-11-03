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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class LocalRKIdentifierImpl extends ParameterImpl implements LocalRKIdentifier, XMLSerializable {

    private static final String ID = "id";

    private byte[] value;
    private long id;

    public LocalRKIdentifierImpl() {
        this.tag = Parameter.Local_Routing_Key_Identifier;
    }

    protected LocalRKIdentifierImpl(byte[] data) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        this.value = data;

        this.id = 0;
        this.id |= data[0] & 0xFF;
        this.id <<= 8;
        this.id |= data[1] & 0xFF;
        this.id <<= 8;
        this.id |= data[2] & 0xFF;
        this.id <<= 8;
        this.id |= data[3] & 0xFF;
    }

    protected LocalRKIdentifierImpl(long id) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        this.id = id;
        this.encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode routing context
        value[0] = (byte) (this.id >> 24);
        value[1] = (byte) (this.id >> 16);
        value[2] = (byte) (this.id >> 8);
        value[3] = (byte) (this.id);
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("LocalRKIdentifier id=%d", id);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LocalRKIdentifierImpl> RC_XML = new XMLFormat<LocalRKIdentifierImpl>(
            LocalRKIdentifierImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocalRKIdentifierImpl localRkId) throws XMLStreamException {
            localRkId.id = xml.getAttribute(ID).toLong();
            localRkId.encode();
        }

        @Override
        public void write(LocalRKIdentifierImpl localRkId, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(ID, localRkId.id);
        }
    };
}
