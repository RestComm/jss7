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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtSSStatusImpl extends OctetStringBase implements ExtSSStatus {

    /**
     * SSStatus bits TS 3GPP TS 23.011
     */
    public static final byte sssBitQ = 8; // bit 4
    public static final byte sssBitP = 4; // bit 3
    public static final byte sssBitR = 2; // bit 2
    public static final byte sssBitA = 1; // bit 1

    public ExtSSStatusImpl() {
        super(1, 5, "ExtSSStatus");
    }

    public ExtSSStatusImpl(byte[] data) {
        super(1, 5, "ExtSSStatus", data);
    }

    public ExtSSStatusImpl(boolean bitQ, boolean bitP, boolean bitR, boolean bitA) {
        super(1, 5, "ExtSSStatus");

        this.data = new byte[1];

        if (bitQ)
            this.data[0] |= sssBitQ;
        if (bitP)
            this.data[0] |= sssBitP;
        if (bitR)
            this.data[0] |= sssBitR;
        if (bitA)
            this.data[0] |= sssBitA;
    }

    public byte[] getData() {
        return data;
    }

    public boolean getBitQ() {
        if (this.data == null || this.data.length < 1)
            return false;

        return ((this.data[0] & sssBitQ) > 0 ? true : false);
    }

    public boolean getBitP() {
        if (this.data == null || this.data.length < 1)
            return false;

        return ((this.data[0] & sssBitP) > 0 ? true : false);
    }

    public boolean getBitR() {
        if (this.data == null || this.data.length < 1)
            return false;

        return ((this.data[0] & sssBitR) > 0 ? true : false);
    }

    public boolean getBitA() {
        if (this.data == null || this.data.length < 1)
            return false;

        return ((this.data[0] & sssBitA) > 0 ? true : false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.getBitQ()) {
            sb.append("bitQ, ");
        }
        if (this.getBitP()) {
            sb.append("bitP, ");
        }
        if (this.getBitR()) {
            sb.append("bitR, ");
        }
        if (this.getBitA()) {
            sb.append("bitA, ");
        }

        sb.append("]");

        return sb.toString();
    }
}
