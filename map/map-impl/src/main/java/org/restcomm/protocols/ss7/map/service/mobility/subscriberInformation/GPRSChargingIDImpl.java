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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GPRSChargingIDImpl extends OctetStringBase implements GPRSChargingID {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public GPRSChargingIDImpl() {
        super(4, 4, "GPRSChargingID");
    }

    public GPRSChargingIDImpl(byte[] data) {
        super(4, 4, "GPRSChargingID", data);
    }

    public byte[] getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GPRSChargingIDImpl> GPRS_CHARGING_ID_XML = new XMLFormat<GPRSChargingIDImpl>(GPRSChargingIDImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GPRSChargingIDImpl charingId) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                charingId.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(GPRSChargingIDImpl charingId, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (charingId.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(charingId.data));
            }
        }
    };
}
