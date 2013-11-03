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
 * Start time:13:15:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;

/**
 * Start time:13:15:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class BackwardGVNSImpl extends AbstractISUPParameter implements BackwardGVNS {

    private byte[] backwardGVNS = null;

    public BackwardGVNSImpl(byte[] backwardGVNS) throws ParameterException {
        super();
        decode(backwardGVNS);
    }

    public BackwardGVNSImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length == 0) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
        }
        this.backwardGVNS = b;
        return b.length;
    }

    public byte[] encode() throws ParameterException {

        for (int index = 0; index < this.backwardGVNS.length; index++) {
            this.backwardGVNS[index] = (byte) (this.backwardGVNS[index] & 0x7F);
        }

        this.backwardGVNS[this.backwardGVNS.length - 1] = (byte) (this.backwardGVNS[this.backwardGVNS.length - 1] & (1 << 7));
        return this.backwardGVNS;
    }

    public byte[] getBackwardGVNS() {
        return backwardGVNS;
    }

    public void setBackwardGVNS(byte[] backwardGVNS) {
        if (backwardGVNS == null || backwardGVNS.length == 0) {
            throw new IllegalArgumentException("byte[] must  not be null and length must  be greater than 0");
        }
        this.backwardGVNS = backwardGVNS;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
