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

package org.mobicents.protocols.ss7.sccp.parameter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;

/**
 * @author amit bhayani
 *
 */
public class NoGlobalTitle extends GlobalTitle {

    private String digits;

    public NoGlobalTitle() {

    }

    public NoGlobalTitle(String digits) {
        this.digits = digits;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle#getIndicator()
     */
    @Override
    public GlobalTitleIndicator getIndicator() {
        return GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle#getDigits()
     */
    @Override
    public String getDigits() {
        return this.digits;
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
