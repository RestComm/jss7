/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ErrorCodeImpl extends ParameterImpl implements ErrorCode {

    private int code;

    public ErrorCodeImpl(int code) {
        this.code = code;
        this.tag = Parameter.Error_Code;
    }

    public ErrorCodeImpl(byte[] data) {
        this.code = 0;
        this.code |= data[0] & 0xFF;
        this.code <<= 8;
        this.code |= data[1] & 0xFF;
        this.code <<= 8;
        this.code |= data[2] & 0xFF;
        this.code <<= 8;
        this.code |= data[3] & 0xFF;
        this.tag = Parameter.Error_Code;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (code >>> 24);
        data[1] = (byte) (code >>> 16);
        data[2] = (byte) (code >>> 8);
        data[3] = (byte) (code);

        return data;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode code=%d", code);
    }

}
