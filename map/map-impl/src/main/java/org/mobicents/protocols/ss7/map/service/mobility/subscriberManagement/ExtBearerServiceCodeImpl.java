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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBearerServiceCodeImpl extends OctetStringBase implements ExtBearerServiceCode {

    public ExtBearerServiceCodeImpl() {
        super(1, 5, "ExtBearerServiceCode");
    }

    public ExtBearerServiceCodeImpl(byte[] data) {
        super(1, 5, "ExtBearerServiceCode", data);
    }

    public ExtBearerServiceCodeImpl(BearerServiceCodeValue value) {
        super(1, 5, "ExtBearerServiceCode");

        if (value != null)
            this.data = new byte[] { (byte) (value.getBearerServiceCode()) };
    }

    public byte[] getData() {
        return data;
    }

    public BearerServiceCodeValue getBearerServiceCodeValue() {
        if (data == null || data.length < 1)
            return null;
        else
            return BearerServiceCodeValue.getInstance(this.data[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getBearerServiceCodeValue());

        sb.append(", Data=[");
        if (data != null) {
            for (int i1 : data) {
                sb.append(i1);
                sb.append(", ");
            }
        }
        sb.append("]");

        sb.append("]");

        return sb.toString();
    }
}
