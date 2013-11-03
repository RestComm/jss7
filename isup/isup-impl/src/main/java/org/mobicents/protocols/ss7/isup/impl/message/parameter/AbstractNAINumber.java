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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;

/**
 * Start time:14:02:37 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * This is number representation that has NAI field
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractNAINumber extends AbstractNumber implements NAINumber {

    private static final String NATURE_OF_ADDRESS_INDICATOR = "natureOfAddresIndicator";

    private static final int DEFAULT_NATURE_OF_ADDRESS_INDICATOR = 0;

    /**
     * Holds nature of address indicator bits - those are 7 first bits from ususaly top byte (first bit is even/odd flag.)
     */
    protected int natureOfAddresIndicator;

    public AbstractNAINumber(byte[] representation) throws ParameterException {
        super(representation);

    }

    public AbstractNAINumber(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public AbstractNAINumber(int natureOfAddresIndicator, String address) {
        super(address);
        this.natureOfAddresIndicator = natureOfAddresIndicator;
    }

    public AbstractNAINumber() {

    }

    public int decode(byte[] b) throws ParameterException {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);

        return this.decode(bis);
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddresIndicator;
    }

    public void setNatureOfAddresIndicator(int natureOfAddresIndicator) {
        this.natureOfAddresIndicator = natureOfAddresIndicator;
    }

    /**
     * This method is used in encode method. It encodes header part (1 or 2 bytes usually.)
     *
     * @param bis
     * @return - number of bytes encoded.
     */
    public int encodeHeader(ByteArrayOutputStream bos) {
        int b = this.natureOfAddresIndicator & 0x7f;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;

        if (isOdd)
            b |= 0x80;

        bos.write(b);

        return 1;
    }

    /**
     * This method is used in constructor that takes byte[] or ByteArrayInputStream as parameter. Decodes header part (its 1 or
     * 2 bytes usually.) Default implemetnation decodes header of one byte - where most significant bit is O/E indicator and
     * bits 7-1 are NAI. This method should be over
     *
     * @param bis
     * @return - number of bytes reads
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public int decodeHeader(ByteArrayInputStream bis) throws ParameterException {
        if (bis.available() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = bis.read() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.natureOfAddresIndicator = b & 0x7f;

        return 1;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AbstractNAINumber> ISUP_ABSTRACT_NAI_NUMBER_XML = new XMLFormat<AbstractNAINumber>(
            AbstractNAINumber.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AbstractNAINumber abstractNAINumber)
                throws XMLStreamException {
            ISUP_ABSTRACT_NUMBER_XML.read(xml, abstractNAINumber);

            abstractNAINumber.natureOfAddresIndicator = xml.getAttribute(NATURE_OF_ADDRESS_INDICATOR,
                    DEFAULT_NATURE_OF_ADDRESS_INDICATOR);
        }

        @Override
        public void write(AbstractNAINumber abstractNAINumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            ISUP_ABSTRACT_NUMBER_XML.write(abstractNAINumber, xml);

            xml.setAttribute(NATURE_OF_ADDRESS_INDICATOR, abstractNAINumber.natureOfAddresIndicator);
        }
    };

}
