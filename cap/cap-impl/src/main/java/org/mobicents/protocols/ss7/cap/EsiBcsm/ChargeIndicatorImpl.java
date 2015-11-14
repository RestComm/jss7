/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringLength1Base;

/**
*
* @author sergey vetyutnev
*
*/
public class ChargeIndicatorImpl extends OctetStringLength1Base implements ChargeIndicator {

    private static final String VALUE = "value";

    private static final String DEFAULT_VALUE = "";

    public ChargeIndicatorImpl() {
        super("ChargeIndicator");
    }

    public ChargeIndicatorImpl(int data) {
        super("ChargeIndicator", data);
    }

    public ChargeIndicatorImpl(ChargeIndicatorValue value) {
        super("ChargeIndicator");

        if (value != null)
            this.data = value.getCode();
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public ChargeIndicatorValue getChargeIndicatorValue() {
        return ChargeIndicatorValue.getInstance(data);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ChargeIndicatorImpl> CHARGE_INDICATOR_XML = new XMLFormat<ChargeIndicatorImpl>(ChargeIndicatorImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ChargeIndicatorImpl chargeIndicator) throws XMLStreamException {
            String val = xml.getAttribute(VALUE, DEFAULT_VALUE);
            if (val != null) {
                ChargeIndicatorValue value = Enum.valueOf(ChargeIndicatorValue.class, val);
                if (value != null) {
                    chargeIndicator.data = value.getCode();
                }
            }
        }

        @Override
        public void write(ChargeIndicatorImpl chargeIndicator, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            ChargeIndicatorValue value = chargeIndicator.getChargeIndicatorValue();
            if (value != null)
                xml.setAttribute(VALUE, value.toString());
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        ChargeIndicatorValue value = this.getChargeIndicatorValue();
        if (value != null) {
            sb.append("chargeIndicatorValue=");
            sb.append(value);
        }

        sb.append("]");

        return sb.toString();
    }

}
