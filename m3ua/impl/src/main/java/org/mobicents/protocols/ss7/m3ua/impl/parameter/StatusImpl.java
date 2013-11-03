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
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 *
 * @author amit bhayani
 *
 */
public class StatusImpl extends ParameterImpl implements Status {

    private int type;
    private int info;

    public StatusImpl(int type, int info) {
        this.type = type;
        this.info = info;
        this.tag = Parameter.Status;
    }

    public StatusImpl(byte[] data) {
        this.tag = Parameter.Status;

        this.type = 0;
        this.type |= data[0] & 0xFF;
        this.type <<= 8;
        this.type |= data[1] & 0xFF;

        this.info = 0;
        this.info |= data[2] & 0xFF;
        this.info <<= 8;
        this.info |= data[3] & 0xFF;

    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (type >>> 8);
        data[1] = (byte) (type);

        data[2] = (byte) (info >>> 8);
        data[3] = (byte) (info);

        return data;
    }

    public int getInfo() {
        return this.info;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.format("Status type=%d info=%d", type, info);
    }

}
