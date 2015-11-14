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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartPrice;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartPriceImpl extends OctetStringBase implements VariablePartPrice {

    private static final String PRICE_INTEGER_PART = "priceIntegerPart";
    private static final String PRICE_HUNDREDTH_PART = "priceHundredthPart";

    public VariablePartPriceImpl() {
        super(4, 4, "VariablePartPrice");
    }

    public VariablePartPriceImpl(byte[] data) {
        super(4, 4, "VariablePartPrice");

        this.data = data;
    }

    public VariablePartPriceImpl(double price) {
        super(4, 4, "VariablePartPrice");

        setPrice(price);
    }

    protected void setPrice(double price) {
        this.data = new byte[4];

        long val = (long) (price * 100);
        if (val < 0)
            val = -val;
        this.data[0] = (byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100));
        this.data[1] = (byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100));
        this.data[2] = (byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100));
        this.data[3] = (byte) this.encodeByte((int) (val - (val / 100) * 100));
    }

    public VariablePartPriceImpl(int integerPart, int hundredthPart) {
        super(4, 4, "VariablePartPrice");

        setPriceIntegerHundredthPart(integerPart, hundredthPart);
    }

    protected void setPriceIntegerHundredthPart(int integerPart, int hundredthPart) {
        this.data = new byte[4];

        long val = ((long) integerPart * 100 + hundredthPart);
        if (val < 0)
            val = -val;
        this.data[0] = (byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100));
        this.data[1] = (byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100));
        this.data[2] = (byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100));
        this.data[3] = (byte) this.encodeByte((int) (val - (val / 100) * 100));
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public double getPrice() {
        if (this.data == null || this.data.length != 4)
            return Double.NaN;

        double res = this.decodeByte(data[0]) * 10000 + this.decodeByte(data[1]) * 100 + this.decodeByte(data[2])
                + (double) this.decodeByte(data[3]) / 100.0;
        return res;
    }

    @Override
    public int getPriceIntegerPart() {

        if (this.data == null || this.data.length != 4)
            return 0;

        int res = this.decodeByte(data[0]) * 10000 + this.decodeByte(data[1]) * 100 + this.decodeByte(data[2]);
        return res;
    }

    @Override
    public int getPriceHundredthPart() {

        if (this.data == null || this.data.length != 4)
            return 0;

        int res = this.decodeByte(data[3]);
        return res;
    }

    private int decodeByte(int bt) {
        return (bt & 0x0F) * 10 + ((bt & 0xF0) >> 4);
    }

    private int encodeByte(int val) {
        return (val / 10) | (val % 10) << 4;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        double val = this.getPrice();
        if (!Double.isNaN(val)) {
            sb.append("price=");
            sb.append(val);
            sb.append(", integerPart=");
            sb.append(this.getPriceIntegerPart());
            sb.append(", hundredthPart=");
            sb.append(this.getPriceHundredthPart());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<VariablePartPriceImpl> VARIABLE_PART_PRICE_XML = new XMLFormat<VariablePartPriceImpl>(
            VariablePartPriceImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, VariablePartPriceImpl variablePartPrice)
                throws XMLStreamException {
            variablePartPrice.setPriceIntegerHundredthPart(xml.getAttribute(PRICE_INTEGER_PART, 0),
                    xml.getAttribute(PRICE_HUNDREDTH_PART, 0));
        }

        @Override
        public void write(VariablePartPriceImpl variablePartPrice, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(PRICE_INTEGER_PART, variablePartPrice.getPriceIntegerPart());
            xml.setAttribute(PRICE_HUNDREDTH_PART, variablePartPrice.getPriceHundredthPart());
        }
    };
}
