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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueCodeValue;
import org.mobicents.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CauseValueImpl extends OctetStringLength1Base implements CauseValue {

    public CauseValueImpl() {
        super("CauseValue");
    }

    public CauseValueImpl(int data) {
        super("CauseValue", data);
    }

    public CauseValueImpl(CauseValueCodeValue value) {
        super("CauseValue", value != null ? value.getCode() : 0);
    }

    @Override
    public CauseValueCodeValue getCauseValueCodeValue() {
        return CauseValueCodeValue.getInstance(this.data);
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("Value=");
        sb.append(this.getCauseValueCodeValue());

        sb.append(", Data=");
        sb.append(this.data);

        sb.append("]");

        return sb.toString();
    }

}
