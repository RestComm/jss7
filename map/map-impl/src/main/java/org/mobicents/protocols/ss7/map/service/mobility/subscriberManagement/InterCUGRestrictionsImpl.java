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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictionsValue;
import org.mobicents.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InterCUGRestrictionsImpl extends OctetStringLength1Base implements InterCUGRestrictions {

    public InterCUGRestrictionsImpl() {
        super("InterCUGRestrictions");
    }

    public InterCUGRestrictionsImpl(int data) {
        super("InterCUGRestrictions", data);
    }

    public InterCUGRestrictionsImpl(InterCUGRestrictionsValue val) {
        super("InterCUGRestrictions", (val != null ? val.getCode() : 0));
    }

    @Override
    public int getData() {
        return this.data;
    }

    @Override
    public InterCUGRestrictionsValue getInterCUGRestrictionsValue() {
        return InterCUGRestrictionsValue.getInstance(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("InterCUGRestrictions=" + this.getInterCUGRestrictionsValue());

        sb.append("]");

        return sb.toString();
    }

}
