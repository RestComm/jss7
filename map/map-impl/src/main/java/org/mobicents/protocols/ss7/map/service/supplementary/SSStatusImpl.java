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

package org.mobicents.protocols.ss7.map.service.supplementary;

import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SSStatusImpl extends OctetStringLength1Base implements SSStatus {

    public static final int _mask_QBit = 0x08;
    public static final int _mask_PBit = 0x04;
    public static final int _mask_RBit = 0x02;
    public static final int _mask_ABit = 0x01;

    public SSStatusImpl() {
        super("SSStatus");
    }

    public SSStatusImpl(int data) {
        super("SSStatus", data);
    }

    public SSStatusImpl(boolean qBit, boolean pBit, boolean rBit, boolean aBit) {
        super("SSStatus", (qBit ? _mask_QBit : 0) + (pBit ? _mask_PBit : 0) + (rBit ? _mask_RBit : 0) + (aBit ? _mask_ABit : 0));
    }

    public int getData() {
        return data;
    }

    public boolean getQBit() {
        return (this.data & _mask_QBit) != 0;
    }

    public boolean getPBit() {
        return (this.data & _mask_PBit) != 0;
    }

    public boolean getRBit() {
        return (this.data & _mask_RBit) != 0;
    }

    public boolean getABit() {
        return (this.data & _mask_ABit) != 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getQBit()) {
            sb.append("QBit");
            sb.append(", ");
        }
        if (this.getPBit()) {
            sb.append("PBit");
            sb.append(", ");
        }
        if (this.getRBit()) {
            sb.append("RBit");
            sb.append(", ");
        }
        if (this.getABit()) {
            sb.append("ABit");
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

}
