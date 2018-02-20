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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ZoneCodeImpl extends OctetStringBase implements ZoneCode {

    public ZoneCodeImpl() {
        super(2, 2, "ZoneCode");
    }

    public ZoneCodeImpl(byte[] data) {
        super(2, 2, "ZoneCode", data);
    }

    public ZoneCodeImpl(int value) {
        super(2, 2, "ZoneCode");

        this.data = new byte[2];
        this.data[0] = (byte) ((value & 0xFF00) >> 8);
        this.data[1] = (byte) (value & 0xFF);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public int getValue() {
        if (this.data == null || this.data.length != 2)
            return 0;

        int res = ((this.data[0] & 0xFF) << 8) | (this.data[1] & 0xFF);
        return res;
    }

}
