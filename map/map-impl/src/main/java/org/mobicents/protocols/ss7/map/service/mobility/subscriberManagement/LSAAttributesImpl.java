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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentificationPriorityValue;
import org.mobicents.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSAAttributesImpl extends OctetStringLength1Base implements LSAAttributes {

    private static int preferentialAccess_mask = 0x10;
    private static int activeModeSupport_mask = 0x20;
    private static int lsaIdentificationPriority_mask = 0x0F;

    public LSAAttributesImpl() {
        super("LSAAttributes");
    }

    public LSAAttributesImpl(int data) {
        super("LSAAttributes", data);
    }

    public LSAAttributesImpl(LSAIdentificationPriorityValue value, boolean preferentialAccessAvailable,
            boolean activeModeSupportAvailable) {
        super("LSAAttributes", value.getCode() | (preferentialAccessAvailable ? preferentialAccess_mask : 0)
                | (activeModeSupportAvailable ? activeModeSupport_mask : 0));
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public LSAIdentificationPriorityValue getLSAIdentificationPriority() {
        return LSAIdentificationPriorityValue.getInstance(data & lsaIdentificationPriority_mask);
    }

    @Override
    public boolean isPreferentialAccessAvailable() {
        return ((data & preferentialAccess_mask) == preferentialAccess_mask);
    }

    @Override
    public boolean isActiveModeSupportAvailable() {
        return ((data & activeModeSupport_mask) == activeModeSupport_mask);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        sb.append("  LSAIdentificationPriorityValue=");
        sb.append(this.getLSAIdentificationPriority());

        if (this.isPreferentialAccessAvailable()) {
            sb.append(" , PreferentialAccessAvailable ");
        }

        if (this.isActiveModeSupportAvailable()) {
            sb.append(" , ActiveModeSupportAvailable ");
        }

        sb.append(", Data=");
        sb.append(this.data);

        sb.append("]");

        return sb.toString();
    }

}
