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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;

/**
 *
 * @author kulikov
 */
public class GT0010  extends GlobalTitle {
    private final static GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY;
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

    public void decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;        
        tt = b;
    }

    public void encode(OutputStream in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        
        GT0010 gt1 = (GT0010)gt;
        return gt1.digits.equals(digits);
    }

    
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }
    
}
