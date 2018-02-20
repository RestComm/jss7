/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
 * Start time:13:29:12 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallTransferReference;

/**
 * Start time:13:29:12 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallTransferReferenceImpl extends AbstractISUPParameter implements CallTransferReference {

    private int callTransferReference = 0;

    public CallTransferReferenceImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public CallTransferReferenceImpl() {
        super();

    }

    public CallTransferReferenceImpl(int callTransferReference) {
        super();
        this.callTransferReference = callTransferReference;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must  not be null and length must  be 1");
        }
        this.callTransferReference = b[0] & 0xFF;
        return 1;
    }

    public byte[] encode() throws ParameterException {
        return new byte[] { (byte) this.callTransferReference };
    }

    public int getCallTransferReference() {
        return callTransferReference;
    }

    public void setCallTransferReference(int callTransferReference) {
        this.callTransferReference = callTransferReference  & 0xFF;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
