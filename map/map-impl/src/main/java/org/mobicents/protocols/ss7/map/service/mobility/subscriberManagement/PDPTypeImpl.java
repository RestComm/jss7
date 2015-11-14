/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PDPTypeImpl extends OctetStringBase implements PDPType {

    public static final int _VALUE_ETSI = 0xF0 + 0; // PPP
    public static final int _VALUE_IETF = 0xF0 + 1; // IPv4, IPv6

    public static final int _VALUE_PPP = 1;
    public static final int _VALUE_IPv4 = 33;
    public static final int _VALUE_IPv6 = 87;

    public PDPTypeImpl() {
        super(2, 2, "PDPType");
    }

    public PDPTypeImpl(byte[] data) {
        super(2, 2, "PDPType", data);
    }

    public PDPTypeImpl(PDPTypeValue value) {
        super(2, 2, "PDPType");

        this.setPDPTypeValue(value);
    }

    protected void setPDPTypeValue(PDPTypeValue value) {
        this.data = new byte[2];

        switch (value) {
        case PPP:
            this.data[0] = (byte) _VALUE_ETSI;
            this.data[1] = (byte) _VALUE_PPP;
            break;
        case IPv4:
            this.data[0] = (byte) _VALUE_IETF;
            this.data[1] = (byte) _VALUE_IPv4;
            break;
        case IPv6:
            this.data[0] = (byte) _VALUE_IETF;
            this.data[1] = (byte) _VALUE_IPv6;
            break;
        }
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public PDPTypeValue getPDPTypeValue() {
        if (this.data != null && this.data.length == 2) {
            if ((this.data[0] & 0xFF) == _VALUE_ETSI) {
                if (this.data[1] == _VALUE_PPP)
                    return PDPTypeValue.PPP;
            } else if ((this.data[0] & 0xFF) == _VALUE_IETF) {
                if (this.data[1] == _VALUE_IPv4)
                    return PDPTypeValue.IPv4;
                if (this.data[1] == _VALUE_IPv6)
                    return PDPTypeValue.IPv6;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        PDPTypeValue value = this.getPDPTypeValue();
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(_PrimitiveName);
            sb.append(" [PDPTypeValue=");
            sb.append(value);
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
