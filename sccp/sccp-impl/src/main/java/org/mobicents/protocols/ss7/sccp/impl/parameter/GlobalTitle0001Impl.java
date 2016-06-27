/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0001;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 */
public class GlobalTitle0001Impl extends AbstractGlobalTitle implements GlobalTitle0001 {

    private NatureOfAddress natureOfAddress;

    public GlobalTitle0001Impl() {
    }

    /**
     * @param natureOfAddress
     */
    public GlobalTitle0001Impl(final String digits, final NatureOfAddress natureOfAddress) {
        super();
        if (natureOfAddress == null) {
            throw new IllegalArgumentException();
        }
        if (digits == null) {
            throw new IllegalArgumentException();
        }
        this.natureOfAddress = natureOfAddress;
        super.digits = digits;
        super.encodingScheme = super.digits.length() % 2 == 1 ? BCDOddEncodingScheme.INSTANCE : BCDEvenEncodingScheme.INSTANCE;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        return this.natureOfAddress;
    }

    @Override
    public void decode(final InputStream in, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            int b = in.read() & 0xff;
            this.natureOfAddress = NatureOfAddress.valueOf(b & 0x7f);
            if((b & 0x80) >0){
                super.encodingScheme = BCDOddEncodingScheme.INSTANCE;
            } else {
                super.encodingScheme = BCDEvenEncodingScheme.INSTANCE;
            }
            super.digits = this.encodingScheme.decode(in);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public void encode(final OutputStream out, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            if (this.natureOfAddress == null) {
                throw new IllegalStateException();
            }

            boolean odd = (super.digits.length() % 2) != 0;
            // encoding first byte
            int b = 0x00;
            if (odd) {
                b = b | (byte) 0x80;
            }
            // adding nature of address indicator
            b = b | (byte) this.natureOfAddress.getValue();
            // write first byte
            out.write((byte) b);

            // encode digits
            if(super.digits == null){
                throw new IllegalStateException();
            }
            this.encodingScheme.encode(digits, out);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((natureOfAddress == null) ? 0 : natureOfAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GlobalTitle0001Impl other = (GlobalTitle0001Impl) obj;
        if (natureOfAddress != other.natureOfAddress)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GlobalTitle0001Impl [digits=" + digits + ", natureOfAddress=" + natureOfAddress + ", encodingScheme="
                + encodingScheme + "]";
    }

 // default XML representation.
    protected static final XMLFormat<GlobalTitle0001Impl> XML = new XMLFormat<GlobalTitle0001Impl>(GlobalTitle0001Impl.class) {

        public void write(GlobalTitle0001Impl ai, OutputElement xml) throws XMLStreamException {
            // xml.setAttribute(GLOBALTITLE_INDICATOR, ai.gti.getValue());
            xml.setAttribute(NATURE_OF_ADDRESS_INDICATOR, ai.natureOfAddress.getValue());
            xml.setAttribute(DIGITS, ai.digits);
        }

        public void read(InputElement xml, GlobalTitle0001Impl ai) throws XMLStreamException {
            // ai.gti = GlobalTitleIndicator.valueOf(xml.getAttribute(GLOBALTITLE_INDICATOR).toInt());
            try {
                ai.natureOfAddress = NatureOfAddress.valueOf(xml.getAttribute(NATURE_OF_ADDRESS_INDICATOR).toInt());
            } catch (IllegalArgumentException e) {
                throw new XMLStreamException(e);
            }
            ai.digits = xml.getAttribute(DIGITS).toString();
        }
    };

}
