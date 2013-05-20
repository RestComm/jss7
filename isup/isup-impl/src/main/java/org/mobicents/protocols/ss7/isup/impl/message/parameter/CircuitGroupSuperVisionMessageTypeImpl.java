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
 * Start time:16:49:41 2009-03-30<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;

/**
 * Start time:16:49:41 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CircuitGroupSuperVisionMessageTypeImpl extends AbstractISUPParameter implements CircuitGroupSuperVisionMessageType {

    private int CircuitGroupSuperVisionMessageTypeIndicator = 0;

    public CircuitGroupSuperVisionMessageTypeImpl(byte[] b) throws ParameterException {
        super();
        decode(b);
    }

    public CircuitGroupSuperVisionMessageTypeImpl(int CircuitGroupSuperVisionMessageTypeIndicator) {
        super();
        this.CircuitGroupSuperVisionMessageTypeIndicator = CircuitGroupSuperVisionMessageTypeIndicator;
    }

    public CircuitGroupSuperVisionMessageTypeImpl() {
        super();

    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 1) {
            throw new ParameterException("byte[] must not be null or has size different than 1.");
        }
        this.CircuitGroupSuperVisionMessageTypeIndicator = b[0] & 0x03;
        return 1;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = new byte[] { (byte) (this.CircuitGroupSuperVisionMessageTypeIndicator & 0x03) };

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

    public int getCircuitGroupSuperVisionMessageTypeIndicator() {
        return CircuitGroupSuperVisionMessageTypeIndicator;
    }

    public void setCircuitGroupSuperVisionMessageTypeIndicator(int CircuitGroupSuperVisionMessageTypeIndicator) {
        this.CircuitGroupSuperVisionMessageTypeIndicator = CircuitGroupSuperVisionMessageTypeIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
