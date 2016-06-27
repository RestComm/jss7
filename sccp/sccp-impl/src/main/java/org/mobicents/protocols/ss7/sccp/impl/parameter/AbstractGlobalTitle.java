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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLSerializable;

import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 */
public abstract class AbstractGlobalTitle extends AbstractParameter implements GlobalTitle, XMLSerializable {

    protected String digits;

    //not codable, just used to encode/decode digits in a common way.
    protected EncodingScheme encodingScheme;

    protected static final String GLOBALTITLE_INDICATOR = "gti";
    protected static final String DIGITS = "digits";
    protected static final String TRANSLATION_TYPE = "tt";
    protected static final String NUMBERING_PLAN = "np";
    protected static final String NATURE_OF_ADDRESS_INDICATOR = "nai";
    protected static final String ENCODING_SCHEME = "es";

    public AbstractGlobalTitle() {
    }

    @Override
    public String getDigits() {
        return this.digits;
    }

    @Override
    public void decode(byte[] b, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        this.decode(new ByteArrayInputStream(b), factory, sccpProtocolVersion);
    }

    @Override
    public byte[] encode(final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encodingScheme.encode(digits, baos);
        return baos.toByteArray();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((digits == null) ? 0 : digits.hashCode());
        result = prime * result + ((encodingScheme == null) ? 0 : encodingScheme.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractGlobalTitle other = (AbstractGlobalTitle) obj;
        if (digits == null) {
            if (other.digits != null)
                return false;
        } else if (!digits.equals(other.digits))
            return false;
        if (encodingScheme == null) {
            if (other.encodingScheme != null)
                return false;
        } else if (!encodingScheme.equals(other.encodingScheme))
            return false;
        return true;
    }

}
