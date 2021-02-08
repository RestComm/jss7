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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.UserCause;

/**
 *
 * @author amit bhayani
 *
 */
public class UserCauseImpl extends ParameterImpl implements UserCause {

    private int user = 0;
    private int cause = 0;

    private byte[] value;

    protected UserCauseImpl(byte[] value) {
        this.tag = Parameter.User_Cause;

        this.cause = 0;
        this.cause |= value[0] & 0xFF;
        this.cause <<= 8;
        this.cause |= value[1] & 0xFF;

        this.user = 0;
        this.user |= value[2] & 0xFF;
        this.user <<= 8;
        this.user |= value[3] & 0xFF;

        this.value = value;
    }

    protected UserCauseImpl(int user, int cause) {
        this.tag = Parameter.User_Cause;
        this.user = user;
        this.cause = cause;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode routing context
        value[0] = (byte) (this.cause >> 8);
        value[1] = (byte) (this.cause);

        value[2] = (byte) (this.user >> 8);
        value[3] = (byte) (this.user);

    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    public int getCause() {
        return this.cause;
    }

    public int getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return String.format("UserCause cause = %d user = %d", this.cause, this.user);
    }

}
