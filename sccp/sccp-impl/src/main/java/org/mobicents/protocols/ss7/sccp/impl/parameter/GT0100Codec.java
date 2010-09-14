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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.indicator.EncodingScheme;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author Oleg Kulikov
 */
public class GT0100Codec extends GTCodec {


    private GT0100 gt;
    
    /** Creates a new instance of GT0100Codec */
    public GT0100Codec() {
    }

    /** Creates a new instance of GT0100Codec */
    public GT0100Codec(GT0100 gt) {
        this.gt = gt;
    }
    
    @Override
    public GlobalTitle decode(InputStream in) throws IOException {
        int b1 = in.read() & 0xff;
        int b2 = in.read() & 0xff;
        int b3 = in.read() & 0xff;

        int tt = b1;

        EncodingScheme es = EncodingScheme.valueOf(b2 & 0x0f);
        NumberingPlan np = NumberingPlan.valueOf((b2 & 0xf0) >> 4);
        NatureOfAddress na = NatureOfAddress.valueOf(b3 & 0x7f);

        String digits = es.decodeDigits(in);
        return new GT0100(tt, np, na, digits);
    }

    @Override
    public void encode(OutputStream out) throws IOException {
        String digits = gt.getDigits();
        EncodingScheme es = digits.length() % 2 == 0 ? EncodingScheme.BCD_EVEN : EncodingScheme.BCD_ODD;
        
        out.write((byte) gt.getTranslationType());

        byte b = (byte) ((gt.getNumberingPlan().getValue() << 4) | (es.getValue()));
        out.write(b);

        b = (byte) (gt.getNatureOfAddress().getValue() & 0x7f);
        out.write(b);

        es.encodeDigits(digits, out);
    }

}
