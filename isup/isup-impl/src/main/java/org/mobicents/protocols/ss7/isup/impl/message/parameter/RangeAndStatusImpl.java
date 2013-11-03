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

/**
 * Start time:14:44:16 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * Start time:14:44:16 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RangeAndStatusImpl extends AbstractISUPParameter implements RangeAndStatus {

    private byte range;
    private byte[] status;

    // FIXME:
    // private Status[] status = null;

    public RangeAndStatusImpl(byte[] b) throws ParameterException {
        super();
        if (b.length < 1) {
            throw new ParameterException("RangeAndStatus requires atleast 1 byte.");
        }
        decode(b);

    }

    public RangeAndStatusImpl() {
        super();

    }

    public RangeAndStatusImpl(byte range, byte[] status) {
        super();
        this.range = range;
        setStatus(status);
    }

    public int decode(byte[] b) throws ParameterException {

        this.range = b[0];
        if (b.length == 1)
            return 1;
        this.status = new byte[b.length - 1];
        System.arraycopy(b, 1, this.status, 0, this.status.length);

        return b.length;
    }

    public byte[] encode() throws ParameterException {
        checkData(range, status);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(this.range);
        if (this.status != null) {
            try {
                bos.write(this.status);
            } catch (IOException e) {
                throw new ParameterException(e);
            }
        }
        return bos.toByteArray();
    }

    public byte getRange() {
        return range;
    }

    public void setRange(byte range) {
        this.setRange(range, false);
    }

    public void setRange(byte range, boolean addStatus) {
        this.range = range;
        // range tells how much cics are affected, or potentially affected.
        // statys field contains bits(1|0) to indicate
        if (addStatus) {
            // check len of byte, +1, for cic in message.
            int len = (range + 1) / 8;
            if ((range + 1) % 8 != 0) {
                len++;
            }
            this.status = new byte[len];
        }
    }

    public byte[] getStatus() {
        return status;
    }

    public void setStatus(byte[] status) {
        this.status = status;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus#isAffected (byte)
     */
    public boolean isAffected(byte b) throws IllegalArgumentException {
        if (this.status.length < (b / 8)) {
            throw new IllegalArgumentException("Argument exceeds status!");
        }
        int index_l = (b / 8);

        int index = b % 8; // number of bit to lit... ech
        int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks
        return (this.status[index_l] & n2Pattern) > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus#setAffected (byte, boolean)
     */
    public void setAffected(byte subrange, boolean v) throws IllegalArgumentException {
        if (this.status == null) {
            throw new IllegalArgumentException("Can not set affected if no status present!");
        }
        // ceck
        if (this.status.length < (subrange / 8)) {
            throw new IllegalArgumentException("Argument exceeds status!");
        }
        int index_l = (subrange / 8);
        int index = subrange % 8; // number of bit to lit... ech
        int n2Pattern = (int) Math.pow(2, index); // hmm no int pows... sucks

        if (v) {
            this.status[index_l] |= n2Pattern;
        } else {
            // not, we have to inverse pattern...
            n2Pattern = 0xFF ^ n2Pattern; // this will create bits with zeros in place of n2Pattern ones!
            this.status[index_l] &= n2Pattern; // do logical and, this will kill proper bit and leave rest unchanged
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    private static void checkData(byte range, byte[] status) throws ParameterException {
        // FIXME: add checks specific to messages~!
        if (status != null) {

            int len = (range + 1) / 8;
            if ((range + 1) % 8 != 0) {
                len++;
            }
            if (status.length != len) {
                throw new ParameterException("Wrong length of status part: " + status.length + ", range: " + range);
            }
        } else {
            // there are cases when this can be null;
        }
    }

}
