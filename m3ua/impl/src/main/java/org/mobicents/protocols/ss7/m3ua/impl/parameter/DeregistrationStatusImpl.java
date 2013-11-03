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

import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class DeregistrationStatusImpl extends ParameterImpl implements DeregistrationStatus {

    private int status;

    public DeregistrationStatusImpl(int status) {
        this.tag = Parameter.Deregistration_Status;
        this.status = status;
    }

    public DeregistrationStatusImpl(byte[] data) {
        this.tag = Parameter.Deregistration_Status;
        this.status = 0;
        this.status |= data[0] & 0xFF;
        this.status <<= 8;
        this.status |= data[1] & 0xFF;
        this.status <<= 8;
        this.status |= data[2] & 0xFF;
        this.status <<= 8;
        this.status |= data[3] & 0xFF;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = (byte) (status >>> 24);
        data[1] = (byte) (status >>> 16);
        data[2] = (byte) (status >>> 8);
        data[3] = (byte) (status);

        return data;
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return String.format("DeregistrationStatus = %d", status);
    }

}
