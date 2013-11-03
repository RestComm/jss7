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

import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

public class ASPIdentifierImpl extends ParameterImpl implements ASPIdentifier {

    private long aspID = 0;
    private byte[] value;

    protected ASPIdentifierImpl(byte[] value) {
        this.tag = Parameter.ASP_Identifier;

        this.aspID = 0;
        this.aspID |= value[0] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[1] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[2] & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value[3] & 0xFF;

        this.value = value;
    }

    protected ASPIdentifierImpl(long id) {
        this.tag = Parameter.ASP_Identifier;
        aspID = id;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];

        // encode asp identifier
        value[0] = (byte) (aspID >> 24);
        value[1] = (byte) (aspID >> 16);
        value[2] = (byte) (aspID >> 8);
        value[3] = (byte) (aspID);
    }

    public long getAspId() {
        return aspID;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("ASPIdentifier id=%d", aspID);
    }
}
