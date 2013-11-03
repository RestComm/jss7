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

import java.io.ByteArrayOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallingPartyCategoryImpl extends AbstractISUPParameter implements CallingPartyCategory {

    private static final String CALLING_PARTY_CATEGORY = "callingPartyCategory";

    private static final int DEFAULT_CALLING_PARTY_CATEGORY = 0;

    private byte callingPartyCategory = 0;

    public CallingPartyCategoryImpl(byte callingPartyCategory) {
        super();
        this.callingPartyCategory = callingPartyCategory;
    }

    public CallingPartyCategoryImpl() {
        super();

    }

    public CallingPartyCategoryImpl(byte[] representation) throws ParameterException {
        super();
        this.decode(representation);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or have different size than 1");
        }
        this.callingPartyCategory = b[0];

        return 1;
    }

    public byte[] encode() throws ParameterException {

        return new byte[] { this.callingPartyCategory };
    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        bos.write(this.callingPartyCategory);
        return 1;
    }

    public byte getCallingPartyCategory() {
        return callingPartyCategory;
    }

    public void setCallingPartyCategory(byte callingPartyCategory) {
        this.callingPartyCategory = callingPartyCategory;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString() {
        return "CallingPartyCategory [callingPartyCategory=" + callingPartyCategory + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallingPartyCategoryImpl> ISUP_CALLING_PARTY_CATEGORY_XML = new XMLFormat<CallingPartyCategoryImpl>(
            CallingPartyCategoryImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallingPartyCategoryImpl callingPartyCategory)
                throws XMLStreamException {
            callingPartyCategory.callingPartyCategory = (byte) xml.getAttribute(CALLING_PARTY_CATEGORY,
                    DEFAULT_CALLING_PARTY_CATEGORY);
        }

        @Override
        public void write(CallingPartyCategoryImpl callingPartyCategory, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(CALLING_PARTY_CATEGORY, callingPartyCategory.callingPartyCategory & 0xFF);
        }
    };
}
