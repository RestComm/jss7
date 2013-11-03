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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBearerServiceCodeImpl extends OctetStringBase implements ExtBearerServiceCode {

    private static final String BEARER_SERVICE_CODE_VALUE = "bearerServiceCodeValue";
    private static final String DEFAULT_STRING_VALUE = null;

    public ExtBearerServiceCodeImpl() {
        super(1, 5, "ExtBearerServiceCode");
    }

    public ExtBearerServiceCodeImpl(byte[] data) {
        super(1, 5, "ExtBearerServiceCode", data);
    }

    public ExtBearerServiceCodeImpl(BearerServiceCodeValue value) {
        super(1, 5, "ExtBearerServiceCode");
        setBearerServiceCode(value);
    }

    public void setBearerServiceCode(BearerServiceCodeValue value) {
        if (value != null)
            this.data = new byte[] { (byte) (value.getBearerServiceCode()) };
    }

    public byte[] getData() {
        return data;
    }

    public BearerServiceCodeValue getBearerServiceCodeValue() {
        if (data == null || data.length < 1)
            return null;
        else
            return BearerServiceCodeValue.getInstance(this.data[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getBearerServiceCodeValue());

        sb.append(", Data=[");
        if (data != null) {
            for (int i1 : data) {
                sb.append(i1);
                sb.append(", ");
            }
        }
        sb.append("]");

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ExtBearerServiceCodeImpl> EXT_BEARER_SERVICE_CODE_XML = new XMLFormat<ExtBearerServiceCodeImpl>(
            ExtBearerServiceCodeImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ExtBearerServiceCodeImpl extBearerServiceCode)
                throws XMLStreamException {
            String val = xml.getAttribute(BEARER_SERVICE_CODE_VALUE, DEFAULT_STRING_VALUE);
            if (val != null) {
                extBearerServiceCode.setBearerServiceCode(Enum.valueOf(BearerServiceCodeValue.class, val));
            }

            // Byte integ = xml.get(BEARER_SERVICE_CODE_VALUE, Byte.class);
            // extBearerServiceCode.data = new byte[]{integ};

            // if (integ != null) {
            // BearerServiceCodeValue bearerServiceCodeValue = BearerServiceCodeValue.getInstance(integ);
            // extBearerServiceCode.data = new byte[]{(byte)bearerServiceCodeValue.getCode()};
            //
            //
            // }
        }

        @Override
        public void write(ExtBearerServiceCodeImpl extBearerServiceCode, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            BearerServiceCodeValue val = extBearerServiceCode.getBearerServiceCodeValue();
            if (val != null)
                xml.setAttribute(BEARER_SERVICE_CODE_VALUE, val.toString());

            // BearerServiceCodeValue bearerServiceCodeValue = extBearerServiceCode.getBearerServiceCodeValue();
            // if (bearerServiceCodeValue != null) {
            // xml.add((byte) bearerServiceCodeValue.getBearerServiceCode(), BEARER_SERVICE_CODE_VALUE, Byte.class);
            // }
        }
    };
}
