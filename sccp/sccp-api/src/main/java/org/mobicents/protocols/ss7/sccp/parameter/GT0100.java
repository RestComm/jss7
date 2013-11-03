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

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.EncodingScheme;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author Oleg Kulikov
 */
public class GT0100 extends GlobalTitle {

    private static final GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS;
    private int tt;
    private NumberingPlan np;
    private EncodingScheme encodingScheme;
    private NatureOfAddress nai;
    private String digits = "";

    /** Creates a new instance of GT0100 */
    public GT0100() {
    }

    public GT0100(int translationType, NumberingPlan numberingPlan, NatureOfAddress natureOfAddress, String digits) {
        this.tt = translationType;
        this.np = numberingPlan;
        this.nai = natureOfAddress;
        this.digits = digits;
        this.encodingScheme = digits.length() % 2 == 0 ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
    }

    public int getTranslationType() {
        return tt;
    }

    public NumberingPlan getNumberingPlan() {
        return np;
    }

    public NatureOfAddress getNatureOfAddress() {
        return nai;
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

        GT0100 gt1 = (GT0100) gt;
        return gt1.tt == tt && gt1.np == np && gt1.nai == nai && gt1.digits.equals(digits);
    }

    public int hashCode() {
        int hash = 3;
        hash = hash + this.tt;
        hash = hash + (this.np != null ? 1 : 0);
        hash = hash + (this.nai != null ? 1 : 0);
        hash = hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return "GT0100{tt=" + tt + ", np=" + np + ", na=" + nai + ", digits=" + digits + "}";
    }

    // default XML representation.
    protected static final XMLFormat<GT0100> XML = new XMLFormat<GT0100>(GT0100.class) {

        public void write(GT0100 ai, OutputElement xml) throws XMLStreamException {
            xml.setAttribute(TRANSLATION_TYPE, ai.tt);
            xml.setAttribute(ENCODING_SCHEME, ai.encodingScheme.getValue());
            xml.setAttribute(NUMBERING_PLAN, ai.np.getValue());
            xml.setAttribute(NATURE_OF_ADDRESS_INDICATOR, ai.nai.getValue());
            xml.setAttribute(DIGITS, ai.digits);
        }

        public void read(InputElement xml, GT0100 ai) throws XMLStreamException {
            ai.tt = xml.getAttribute(TRANSLATION_TYPE).toInt();
            ai.encodingScheme = EncodingScheme.valueOf(xml.getAttribute(ENCODING_SCHEME).toInt());
            try {
                ai.np = NumberingPlan.valueOf(xml.getAttribute(NUMBERING_PLAN).toInt());
                ai.nai = NatureOfAddress.valueOf(xml.getAttribute(NATURE_OF_ADDRESS_INDICATOR).toInt());
            } catch (IOException e) {
                throw new XMLStreamException(e);
            }
            ai.digits = xml.getAttribute(DIGITS).toString();
        }
    };
}
