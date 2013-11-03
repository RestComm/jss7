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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtTeleserviceCodeImpl extends OctetStringBase implements ExtTeleserviceCode {

    private static final String TELE_SERVICE_CODE_VALUE = "teleserviceCodeValue";
    private static final String DEFAULT_STRING_VALUE = null;

    public ExtTeleserviceCodeImpl() {
        super(1, 5, "ExtTeleserviceCode");
    }

    public ExtTeleserviceCodeImpl(byte[] data) {
        super(1, 5, "ExtTeleserviceCode", data);
    }

    public ExtTeleserviceCodeImpl(TeleserviceCodeValue value) {
        super(1, 5, "TeleserviceCode");
        setTeleserviceCode(value);
    }

    public void setTeleserviceCode(TeleserviceCodeValue value) {
        if (value != null)
            this.data = new byte[] { (byte) (value.getCode()) };
    }

    public byte[] getData() {
        return data;
    }

    public TeleserviceCodeValue getTeleserviceCodeValue() {
        if (data == null || data.length < 1)
            return null;
        else
            return TeleserviceCodeValue.getInstance(this.data[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getTeleserviceCodeValue());

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
    protected static final XMLFormat<ExtTeleserviceCodeImpl> EXT_BEARER_SERVICE_CODE_XML = new XMLFormat<ExtTeleserviceCodeImpl>(
            ExtTeleserviceCodeImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ExtTeleserviceCodeImpl extTeleserviceCode)
                throws XMLStreamException {
            String val = xml.getAttribute(TELE_SERVICE_CODE_VALUE, DEFAULT_STRING_VALUE);
            if (val != null) {
                extTeleserviceCode.setTeleserviceCode(Enum.valueOf(TeleserviceCodeValue.class, val));
            }

            // Byte integ = xml.get(TELE_SERVICE_CODE_VALUE, Byte.class);
            // if (integ != null) {
            // extTeleserviceCode.data = new byte[] { integ };
            // }
        }

        @Override
        public void write(ExtTeleserviceCodeImpl extTeleserviceCode, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            TeleserviceCodeValue val = extTeleserviceCode.getTeleserviceCodeValue();
            if (val != null)
                xml.setAttribute(TELE_SERVICE_CODE_VALUE, val.toString());

            // TeleserviceCodeValue bearerServiceCodeValue = extTeleserviceCode.getTeleserviceCodeValue();
            // if (bearerServiceCodeValue != null) {
            // xml.add((byte) bearerServiceCodeValue.getCode(), TELE_SERVICE_CODE_VALUE, Byte.class);
            // }
        }
    };
}
