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

package org.mobicents.protocols.ss7.sccp;

/**
 * LoadSharingAlgorithm defines bit in SLS to share message between Primary Address and Secondary Address
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public enum LoadSharingAlgorithm {
    Undefined("Undefined"), Bit0("Bit0"), Bit1("Bit1"), Bit2("Bit2"), Bit3("Bit3"), Bit4("Bit4");

    private static final String UNDEFINED = "Undefined";
    private static final String BIT0 = "Bit0";
    private static final String BIT1 = "Bit1";
    private static final String BIT2 = "Bit2";
    private static final String BIT3 = "Bit3";
    private static final String BIT4 = "Bit4";

    private final String algo;

    private LoadSharingAlgorithm(String type) {
        this.algo = type;
    }

    public static LoadSharingAlgorithm getInstance(String type) {
        if (UNDEFINED.equalsIgnoreCase(type)) {
            return Undefined;
        } else if (BIT0.equalsIgnoreCase(type)) {
            return Bit0;
        } else if (BIT1.equalsIgnoreCase(type)) {
            return Bit1;
        } else if (BIT2.equalsIgnoreCase(type)) {
            return Bit2;
        } else if (BIT3.equalsIgnoreCase(type)) {
            return Bit3;
        } else if (BIT4.equalsIgnoreCase(type)) {
            return Bit4;
        }

        return null;
    }

    public String getAlgo() {
        return this.algo;
    }
}
