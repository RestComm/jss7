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
package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 --Bits 1 to 4 of octet (x+1) define the priority of the LSA identification. --Bit 4321 -- 0000 priority 1 = lowest priority
 * -- 0001 priority 2 = second lowest priority -- : : : : -- 1111 priority 16= highest priority
 *
 *
 * @author Lasith Waruna Perera
 *
 */
public enum LSAIdentificationPriorityValue {

    Priority_1(0), Priority_2(0x01), Priority_3(0x02), Priority_4(0x03), Priority_5(0x04), Priority_6(0x05), Priority_7(0x06), Priority_8(
            0x07), Priority_9(0x08), Priority_10(0x09), Priority_11(0x0A), Priority_12(0x0B), Priority_13(0x0C), Priority_14(
            0x0D), Priority_15(0x0D), Priority_16(0x0F);

    private int code;

    private LSAIdentificationPriorityValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static LSAIdentificationPriorityValue getInstance(int code) {
        switch (code) {
            case 0:
                return Priority_1;
            case 0x01:
                return Priority_2;
            case 0x02:
                return Priority_3;
            case 0x03:
                return Priority_4;
            case 0x04:
                return Priority_5;
            case 0x05:
                return Priority_6;
            case 0x06:
                return Priority_7;
            case 0x07:
                return Priority_8;
            case 0x08:
                return Priority_9;
            case 0x09:
                return Priority_10;
            case 0x0A:
                return Priority_11;
            case 0x0B:
                return Priority_12;
            case 0x0C:
                return Priority_13;
            case 0x0D:
                return Priority_14;
            case 0x0E:
                return Priority_15;
            case 0x0F:
                return Priority_16;
            default:
                return null;
        }
    }
}
