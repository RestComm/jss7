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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class CarrierImpl extends OctetStringBase implements Carrier {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public CarrierImpl() {
        super(4, 4, "Carrier");
    }

    public CarrierImpl(byte[] data) {
        super(4, 4, "Carrier", data);
    }

    @Override
    public byte[] getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CarrierImpl> CARRIER_XML = new XMLFormat<CarrierImpl>(CarrierImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CarrierImpl carrier) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                carrier.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(CarrierImpl carrier, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (carrier.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(carrier.data));
            }
        }
    };

}
