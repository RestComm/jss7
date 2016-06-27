/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.InputStream;
import java.io.OutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author amit bhayani
 *
 */
public class NoGlobalTitle extends AbstractGlobalTitle {


    public NoGlobalTitle() {

    }

    public NoGlobalTitle(String digits) {
        super.digits = digits;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
    }

    @Override
    public String getDigits() {
        return super.digits;
    }

    @Override
    public void decode(final InputStream in, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion)
            throws ParseException {
        this.digits = this.encodingScheme.decode(in);
    }

    @Override
    public void encode(OutputStream out, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion)
            throws ParseException {
        if (this.digits == null) {
            throw new IllegalStateException();
        }
        this.encodingScheme.encode(this.digits, out);
    }

    // default XML representation.
    protected static final XMLFormat<NoGlobalTitle> XML = new XMLFormat<NoGlobalTitle>(NoGlobalTitle.class) {

        public void write(NoGlobalTitle ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(DIGITS, ai.digits);
        }

        public void read(InputElement xml, NoGlobalTitle ai) throws XMLStreamException {
            ai.digits = xml.getAttribute(DIGITS).toString();
        }
    };
}
