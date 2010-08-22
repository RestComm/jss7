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
package org.mobicents.protocols.ss7.indicator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author kulikov
 */
public enum EncodingScheme {
    UNKNOWN(0), 
    BCD_ODD(1), 
    BCD_EVEN(2);
    
    private int value;
    
    private EncodingScheme(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static EncodingScheme valueOf(int v) {
        switch (v) {
            case 0 : return UNKNOWN;
            case 1 : return BCD_ODD;
            case 2 : return BCD_EVEN;
            default : return null;
        }
    }
    
    public String decodeDigits(InputStream in) throws IOException {
        int b;
        String digits = "";
        
        while (in.available() > 0) {
            b = in.read() & 0xff;            
            digits += Integer.toHexString(b & 0x0f) +
                    Integer.toHexString((b & 0xf0) >> 4);
        }
        
        if (value == 1) {
            digits = digits.substring(1, digits.length() - 1);
        }
        
        return digits;
    }
    
    public void encodeDigits(String digits, OutputStream out) throws IOException {
        boolean odd = digits.length() % 2 != 0;
        int b = 0;
        
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
}
