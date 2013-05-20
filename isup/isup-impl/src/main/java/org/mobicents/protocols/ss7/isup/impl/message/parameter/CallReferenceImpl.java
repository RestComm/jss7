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
 * Start time:14:48:06 2009-03-30<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;

/**
 * Start time:14:48:06 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallReferenceImpl extends AbstractISUPParameter implements CallReference {

    private int callIdentity = 0;
    // Should we use here SignalingPointCode class?
    private int signalingPointCode = 0;

    public CallReferenceImpl() {
        super();

    }

    public CallReferenceImpl(int callIdentity, int signalingPointCode) {
        super();
        this.callIdentity = callIdentity;
        this.signalingPointCode = signalingPointCode;
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 5) {
            throw new ParameterException("byte[] must not be null or have length of 5");
        }
        // for (int i = 0; i < 3; i++) {
        // this.callIdentity |= (b[i] << (i * 8));
        // }
        this.callIdentity = ((b[0] & 0xFF) << 16) | ((b[1] & 0xFF) << 8) | (b[2] & 0xFF);
        // this.signalingPointCode |= b[3];
        // this.signalingPointCode |= (b[4] << 8);
        this.signalingPointCode = ((b[3] & 0xFF) | ((b[4] & 0x3F) << 8));
        // This kills first two bits.
        // this.signalingPointCode &= 0x3FFF;
        return 5;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[5];

        // for (int i = 0; i < 3; i++) {
        // b[i] = (byte) (this.callIdentity >> (i * 8));
        //
        // }

        b[0] = (byte) (this.callIdentity >> 16);
        b[1] = (byte) (this.callIdentity >> 8);
        b[2] = (byte) this.callIdentity;

        b[3] = (byte) this.signalingPointCode;
        b[4] = (byte) ((this.signalingPointCode >> 8) & 0x3F);

        return b;

    }

    public int encode(ByteArrayOutputStream bos) throws ParameterException {
        byte[] b = this.encode();
        try {
            bos.write(b);
        } catch (IOException e) {
            throw new ParameterException(e);
        }
        return b.length;
    }

    public int getCallIdentity() {
        return callIdentity;
    }

    public void setCallIdentity(int callIdentity) {
        this.callIdentity = callIdentity & 0xFFFFFF;
    }

    public int getSignalingPointCode() {
        return signalingPointCode;
    }

    public void setSignalingPointCode(int signalingPointCode) {
        this.signalingPointCode = signalingPointCode;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
