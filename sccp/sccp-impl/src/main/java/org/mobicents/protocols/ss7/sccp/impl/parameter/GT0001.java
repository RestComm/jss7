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
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mobicents.protocols.ss7.indicator.GTIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GT0001 implements GlobalTitle {
    private GTIndicator gti = GTIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
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

    public GTIndicator getIndicator() {
        return this.gti;
    }

}
