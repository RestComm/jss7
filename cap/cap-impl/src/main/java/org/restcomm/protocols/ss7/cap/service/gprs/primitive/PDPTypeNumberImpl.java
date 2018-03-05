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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.primitives.OctetStringLength1Base;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PDPTypeNumberImpl extends OctetStringLength1Base implements PDPTypeNumber {

    public PDPTypeNumberImpl() {
        super("PDPTypeNumber");
    }

    public PDPTypeNumberImpl(int data) {
        super("PDPTypeNumber", data);
    }

    public PDPTypeNumberImpl(PDPTypeNumberValue value) {
        super("PDPTypeNumber", value != null ? value.getCode() : 0);
    }

    @Override
    public PDPTypeNumberValue getPDPTypeNumberValue() {
        return PDPTypeNumberValue.getInstance(data);
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.getPDPTypeNumberValue() != null) {
            sb.append("PDPTypeNumberValue=");
            sb.append(this.getPDPTypeNumberValue());
        }

        sb.append("]");

        return sb.toString();
    }

}
