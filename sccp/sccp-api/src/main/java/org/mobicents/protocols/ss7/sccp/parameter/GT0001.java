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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;

/**
 *
 * @author kulikov
 */
public class GT0001 extends GlobalTitle {
    private GlobalTitleIndicator gti = GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
    private NatureOfAddress nai;
    private String digits;
    private boolean odd = false;
    
    public GT0001() {
        digits = "";
    }
    
    public GT0001(NatureOfAddress nai, String digits) {
        this.nai = nai;
        this.digits = digits;
    }

    public void decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        
        nai = NatureOfAddress.valueOf(b & 0x7f);
        odd = (b & 0x80) == 0x80;
        
        while (in.available() > 0) {
            b = in.read() & 0xff;            
            digits += Integer.toHexString(b & 0x0f) +
                    Integer.toHexString((b & 0xf0) >> 4);
        }
        
        if (odd) {
            digits = digits.substring(1, digits.length() - 1);
        }
    }

    public void encode(OutputStream out) throws IOException {        
        //determine if number of digits is even or odd
        odd = (digits.length() % 2) != 0;
        
        //encoding first byte
        int b = 0x00;
        if (odd) {
            b = b | (byte)0x80;
        }
        
        //adding nature of address indicator
        b = b | (byte) nai.getValue();
        
        //write first byte
        out.write((byte)b);
        
        //writting digits
        int count = odd ? digits.length() - 1 : digits.length();
        for (int i = 0; i < count - 1; i+= 2) {
            String ds1 = digits.substring(i, i + 1);
            String ds2 = digits.substring(i + 1, i + 2);
            
            int d1 = Integer.parseInt(ds1,16);
            int d2 = Integer.parseInt(ds2, 16);
            
            b = (byte) (d2 << 4 | d1);
            out.write(b);
        }
        
        //if number is odd append last digit with filler
        if (odd) {
            String ds1 = digits.substring(count, count + 1);
            int d = Integer.parseInt(ds1);
            
            b = (byte)(d & 0x0f);
            out.write(b);
        }
    }

    public NatureOfAddress getNoA() {
        return this.nai;
    }
    
    public String getDigits() {
        return digits;
    }

    public GlobalTitleIndicator getIndicator() {
        return this.gti;
    }

    
    public boolean equals(Object other) {
        if (!(other instanceof GlobalTitle)) {
            return false;
        }
        
        GlobalTitle gt = (GlobalTitle) other;
        if (gt.getIndicator() != gti) {
            return false;
        }
        
        GT0001 gt1 = (GT0001)gt;
        return gt1.nai == nai && gt1.digits.equals(digits);
    }

    
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.gti != null ? this.gti.hashCode() : 0);
        hash = 53 * hash + (this.digits != null ? this.digits.hashCode() : 0);
        return hash;
    }
}
