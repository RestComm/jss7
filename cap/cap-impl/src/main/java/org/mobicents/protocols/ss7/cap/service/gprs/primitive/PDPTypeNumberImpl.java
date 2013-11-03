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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringLength1Base;

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
