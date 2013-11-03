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

package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 *
 Bit 1 Bit 0 Message Class 0 0 Class 0 0 1 Class 1 Default meaning: ME-specific. 1 0 Class 2 (U)SIM specific message 1 1 Class
 * 3 Default meaning: TE specific (see 3GPP TS 27.005 [8])
 *
 * @author sergey vetyutnev
 *
 */
public enum DataCodingSchemaMessageClass {
    Class0(0), Class1(1), Class2(2), Class3(3);

    private int code;

    private DataCodingSchemaMessageClass(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static DataCodingSchemaMessageClass getInstance(int code) {
        switch (code) {
            case 0:
                return Class0;
            case 1:
                return Class1;
            case 2:
                return Class2;
            default:
                return Class3;
        }
    }
}