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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.parameter.GT0010;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class GT0010Codec extends GTCodec {

    private GT0010 gt;

    public GT0010Codec() {
    }

    public GT0010Codec(GT0010 gt) {
        this.gt = gt;
    }

    public GlobalTitle decode(InputStream in) throws IOException {
        int b = in.read() & 0xff;
        int tt = b;

        String digits = "";
        while (in.available() > 0) {
            b = in.read() & 0xff;
            digits += Integer.toHexString(b & 0x0f) + Integer.toHexString((b & 0xf0) >> 4);
        }

        return new GT0010(tt, digits);
    }

    public void encode(OutputStream out) throws IOException {
        if (gt == null) {
            throw new IOException("No GT to parse");
        }

        out.write(gt.getTranslationType());
        String digits = gt.getDigits();
        boolean odd = (digits.length() % 2) != 0;
        // writting digits
        int count = odd ? digits.length() - 1 : digits.length();
        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = digits.substring(i, i + 1);
            String ds2 = digits.substring(i + 1, i + 2);

            int d1 = Integer.parseInt(ds1, 16);
            int d2 = Integer.parseInt(ds2, 16);

            byte b = (byte) (d2 << 4 | d1);
            out.write(b);
        }

        // if number is odd append last digit with filler
        if (odd) {
            String ds1 = digits.substring(count, count + 1);
            int d = Integer.parseInt(ds1);

            byte b = (byte) (d & 0x0f);
            out.write(b);
        }
    }

}
