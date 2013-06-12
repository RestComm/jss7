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
