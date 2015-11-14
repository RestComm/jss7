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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganization;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.mobicents.protocols.ss7.cap.primitives.OctetStringLength1Base;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PDPTypeOrganizationImpl extends OctetStringLength1Base implements PDPTypeOrganization {

    public PDPTypeOrganizationImpl() {
        super("PDPTypeOrganization");
    }

    public PDPTypeOrganizationImpl(int data) {
        super("PDPTypeOrganization", data);
    }

    public PDPTypeOrganizationImpl(PDPTypeOrganizationValue value) {
        super("PDPTypeOrganization", value != null ? (value.getCode() | 0xF0) : 0);
    }

    @Override
    public PDPTypeOrganizationValue getPDPTypeOrganizationValue() {
        return PDPTypeOrganizationValue.getInstance(data & 0x0F);
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.getPDPTypeOrganizationValue() != null) {
            sb.append("PDPTypeOrganizationValue=");
            sb.append(this.getPDPTypeOrganizationValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
