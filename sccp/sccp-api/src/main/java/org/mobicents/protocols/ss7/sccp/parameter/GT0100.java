/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.indicator.EncodingScheme;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author Oleg Kulikov
 */
public class GT0100  extends GlobalTitle {

    private final static GlobalTitleIndicator gti = 
            GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS;
    private int tt;
    private NumberingPlan np;
    private EncodingScheme encodingScheme;
    private NatureOfAddress na;
    private String digits = "";

    /** Creates a new instance of GT0100 */
    public GT0100() {
    }

    public GT0100(int translationType, NumberingPlan numberingPlan,
            NatureOfAddress natureOfAddress, String digits) {
        this.tt = translationType;
        this.np = numberingPlan;
        this.na = natureOfAddress;
        this.digits = digits;
        this.encodingScheme = digits.length() % 2 == 0 ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
    }

    public void decode(InputStream in) throws IOException {
        int b1 = in.read() & 0xff;
        int b2 = in.read() & 0xff;
        int b3 = in.read() & 0xff;

        tt = b1;

        encodingScheme = EncodingScheme.valueOf(b2 & 0x0f);
        np = NumberingPlan.valueOf((b2 & 0xf0) >> 4);
        na = NatureOfAddress.valueOf(b3 & 0x7f);

        digits = encodingScheme.decodeDigits(in);
    }

    public void encode(OutputStream out) throws IOException {
        out.write((byte) tt);

        byte b = (byte) ((np.getValue() << 4) | (encodingScheme.getValue()));
        out.write(b);

        b = (byte) (na.getValue() & 0x7f);
        out.write(b);

        encodingScheme.encodeDigits(digits, out);
    }

    public int getTranslationType() {
        return tt;
    }

    public NumberingPlan getNumberingPlan() {
        return np;
    }

    public NatureOfAddress getNatureOfAddress() {
        return na;
    }

    public String getDigits() {
        return digits;
    }

    public GlobalTitleIndicator getIndicator() {
        return gti;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GlobalTitle)) {
            return false;
        }
        
        GlobalTitle gt = (GlobalTitle) other;
        if (gt.getIndicator() != gti) {
            return false;
        }
        
        GT0100 gt1 = (GT0100)gt;
        return gt1.tt == tt && gt1.np == np &&
                gt1.na == na && gt1.digits.equals(digits);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = hash + this.tt;
        hash = hash + (this.np != null ? 1 : 0);
        hash = hash + (this.na != null ? 1 : 0);
        hash = hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return "{tt=" + tt + ", np=" + np + ", na=" + na + ", digist=" + digits + "}";
    }
}
