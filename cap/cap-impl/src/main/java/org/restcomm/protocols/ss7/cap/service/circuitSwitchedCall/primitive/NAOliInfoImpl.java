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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.restcomm.protocols.ss7.cap.primitives.OctetStringLength1Base;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class NAOliInfoImpl extends OctetStringLength1Base implements NAOliInfo {

    private static final String VALUE = "value";

    public NAOliInfoImpl() {
        super("NAOliInfo");
    }

    public NAOliInfoImpl(int data) {
        super("NAOliInfo", data);
    }

    @Override
    public int getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<NAOliInfoImpl> NA_OLI_INFO_XML = new XMLFormat<NAOliInfoImpl>(NAOliInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, NAOliInfoImpl naOliInfo) throws XMLStreamException {
            naOliInfo.data = xml.getAttribute(VALUE, 0);
        }

        @Override
        public void write(NAOliInfoImpl naOliInfo, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(VALUE, naOliInfo.data);
        }
    };
}
