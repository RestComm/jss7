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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 *
 */
public class TeleserviceCodeImpl extends OctetStringLength1Base implements TeleserviceCode {
    private static final String TELE_SERVICE_CODE_VALUE = "teleserviceCodeValue";
    private static final String DATA = "data";

    public TeleserviceCodeImpl() {
        super("TeleserviceCode");
    }

    public TeleserviceCodeImpl(int data) {
        super("TeleserviceCode", data);
    }

    public TeleserviceCodeImpl(TeleserviceCodeValue value) {
        super("TeleserviceCode", value != null ? value.getCode() : 0);
    }

    public int getData() {
        return data;
    }

    public TeleserviceCodeValue getTeleserviceCodeValue() {
        return TeleserviceCodeValue.getInstance(this.data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getTeleserviceCodeValue());

        sb.append(", Data=");
        sb.append(this.data);

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TeleserviceCodeImpl> TELE_SERVICE_CODE_XML = new XMLFormat<TeleserviceCodeImpl>(
            TeleserviceCodeImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TeleserviceCodeImpl ssCode) throws XMLStreamException {
            ssCode.data = xml.get(DATA, Integer.class);

            String str = xml.get(TELE_SERVICE_CODE_VALUE, String.class);
        }

        @Override
        public void write(TeleserviceCodeImpl ssCode, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.add(ssCode.getData(), DATA, Integer.class);

            if (ssCode.getTeleserviceCodeValue() != null)
                xml.add((String) ssCode.getTeleserviceCodeValue().toString(), TELE_SERVICE_CODE_VALUE, String.class);
        }
    };

}
