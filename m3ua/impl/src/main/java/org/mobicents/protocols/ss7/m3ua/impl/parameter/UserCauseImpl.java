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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

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

        this.user = 0;
        this.user |= value[0] & 0xFF;
        this.user <<= 8;
        this.user |= value[1] & 0xFF;

        this.cause = 0;
        this.cause |= value[2] & 0xFF;
        this.cause <<= 8;
        this.cause |= value[3] & 0xFF;

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
        value[0] = (byte) (this.user >> 8);
        value[1] = (byte) (this.user);

        value[2] = (byte) (this.cause >> 8);
        value[3] = (byte) (this.cause);

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
