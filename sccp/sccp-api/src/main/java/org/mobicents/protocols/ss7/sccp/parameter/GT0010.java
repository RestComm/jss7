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
 *
 * @author kulikov
 */
public class GT0010 extends GlobalTitle {
    private static final GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
    /** Translation type */
    private int tt;
    /** address digits */
    private String digits;

    public GT0010() {
        digits = "";
    }

    public GT0010(int tt, String digits) {
        this.tt = tt;
        this.digits = digits;
    }

    public int getTranslationType() {
        return tt;
    }

    public String getDigits() {
        return digits;
    }

    public GlobalTitleIndicator getIndicator() {
        return gti;
    }

    public boolean equals(Object other) {
        if (!(other instanceof GlobalTitle)) {
            return false;
        }

        GlobalTitle gt = (GlobalTitle) other;
        if (gt.getIndicator() != gti) {
            return false;
        }

        GT0010 gt1 = (GT0010) gt;
        return gt1.digits.equals(digits);
    }

    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return "GT0010{tt=" + tt + ", digits=" + digits + "}";
    }

    // default XML representation.
    protected static final XMLFormat<GT0010> XML = new XMLFormat<GT0010>(GT0010.class) {

        public void write(GT0010 ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(TRANSLATION_TYPE, ai.tt);
            xml.setAttribute(DIGITS, ai.digits);
        }

        public void read(InputElement xml, GT0010 ai) throws XMLStreamException {
            ai.tt = xml.getAttribute(TRANSLATION_TYPE).toInt();
            ai.digits = xml.getAttribute(DIGITS).toString();
        }
    };

}
