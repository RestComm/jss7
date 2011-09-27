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
