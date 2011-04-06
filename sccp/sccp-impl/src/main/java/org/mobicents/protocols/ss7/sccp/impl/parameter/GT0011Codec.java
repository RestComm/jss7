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
import org.mobicents.protocols.ss7.indicator.EncodingScheme;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GT0011Codec extends GTCodec {
    private GT0011 gt;
    
    public GT0011Codec() {
    }
    
    public GT0011Codec(GT0011 gt) {
        this.gt = gt;
    }
    
    
    public GlobalTitle decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;        
        int tt = b;
        
        b = in.read() & 0xff;
        
        EncodingScheme es = EncodingScheme.valueOf(b & 0x0f);
        NumberingPlan np = NumberingPlan.valueOf((b & 0xf0)>>4);
        
        String digits = es.decodeDigits(in);
        return new GT0011(tt, np, digits);
    }

    
    public void encode(OutputStream out) throws IOException {
        String digits = gt.getDigits();
        EncodingScheme es = digits.length() % 2 == 0 ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
        out.write(gt.getTranslationType());
        out.write((gt.getNp().getValue() << 4) | es.getValue());
        es.encodeDigits(digits, out);
    }

}
