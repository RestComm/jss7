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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;

/**
 * Start time:12:24:47 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class GenericDigitsImpl extends AbstractISUPParameter implements GenericDigits {

    private static final String ENCODING_SCHEME = "encodingScheme";
    private static final String TYPE_OF_DIGITS = "typeOfDigits";
    private static final String DIGITS = "digits";

    private static final int DEFAULT_VALUE = 0;

    private int encodingScheme;
    private int typeOfDigits;
    private byte[] digits;

    public GenericDigitsImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public GenericDigitsImpl(int encodingScheme, int typeOfDigits, byte[] digits) {
        super();
        this.encodingScheme = encodingScheme;
        this.typeOfDigits = typeOfDigits;
        this.setEncodedDigits(digits);
    }

    public GenericDigitsImpl() {
        super();

    }

    // TODO: add method: public String getDecodedDigits() ;
    // TODO: add method: public void setDecodedDigits(int encodingScheme, String digits) ;
    // TODO: add constructor: public GenericDigitsImpl(int encodingScheme, int typeOfDigits, String digits)

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 2) {
            throw new ParameterException("byte[] must not be null or has size less than 2");
        }
        this.typeOfDigits = b[0] & 0x1F;
        this.encodingScheme = (b[0] >> 5) & 0x07;
        this.digits = new byte[b.length - 1];

        for (int index = 1; index < b.length; index++) {
            this.digits[index - 1] = b[index];
        }
        return 1 + this.digits.length;
    }

    public byte[] encode() throws ParameterException {

        byte[] b = new byte[this.digits.length + 1];

        b[0] |= this.typeOfDigits & 0x1F;
        b[0] |= ((this.encodingScheme & 0x07) << 5);

        for (int index = 1; index < b.length; index++) {
            b[index] = (byte) this.digits[index - 1];
        }
        return b;

    }

    public int getEncodingScheme() {
        return encodingScheme;
    }

    public void setEncodingScheme(int encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public int getTypeOfDigits() {
        return typeOfDigits;
    }

    public void setTypeOfDigits(int typeOfDigits) {
        this.typeOfDigits = typeOfDigits;
    }

    public byte[] getEncodedDigits() {
        return digits;
    }

    public void setEncodedDigits(byte[] digits) {
        if (digits == null)
            throw new IllegalArgumentException("Digits must not be null");
        this.digits = digits;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GenericDigits [encodingScheme=");
        sb.append(encodingScheme);
        sb.append(", typeOfDigits=");
        sb.append(typeOfDigits);
        if (digits != null) {
            sb.append(", encodedDigits=[");
            sb.append(DatatypeConverter.printHexBinary(digits));
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GenericDigitsImpl> ISUP_GENERIC_DIGITS_XML = new XMLFormat<GenericDigitsImpl>(
            GenericDigitsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GenericDigitsImpl genericDigits) throws XMLStreamException {
            genericDigits.encodingScheme = xml.getAttribute(ENCODING_SCHEME, DEFAULT_VALUE);
            genericDigits.typeOfDigits = xml.getAttribute(TYPE_OF_DIGITS, DEFAULT_VALUE);

            ByteArrayContainer bc = xml.get(DIGITS, ByteArrayContainer.class);
            if (bc != null) {
                genericDigits.digits = bc.getData();
            }
        }

        @Override
        public void write(GenericDigitsImpl genericDigits, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(ENCODING_SCHEME, genericDigits.encodingScheme);
            xml.setAttribute(TYPE_OF_DIGITS, genericDigits.typeOfDigits);

            if (genericDigits.digits != null) {
                ByteArrayContainer bac = new ByteArrayContainer(genericDigits.digits);
                xml.add(bac, DIGITS, ByteArrayContainer.class);
            }
        }
    };
}
