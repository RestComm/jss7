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

package org.mobicents.protocols.ss7.map.api.primitives;

/**
 * EMLPP-Priority ::= INTEGER (0..15) -- The mapping from the values A,B,0,1,2,3,4 to the integer-value is -- specified as
 * follows where A is the highest and 4 is the lowest -- priority level -- the integer values 7-15 are spare and shall be mapped
 * to value 4 priorityLevelA EMLPP-Priority ::= 6 priorityLevelB EMLPP-Priority ::= 5 priorityLevel0 EMLPP-Priority ::= 0
 * priorityLevel1 EMLPP-Priority ::= 1 priorityLevel2 EMLPP-Priority ::= 2 priorityLevel3 EMLPP-Priority ::= 3 priorityLevel4
 * EMLPP-Priority ::= 4
 *
 * @author cristian veliscu
 *
 */
public enum EMLPPPriority {
    priorityLevel0(0), priorityLevel1(1), priorityLevel2(2), priorityLevel3(3), priorityLevel4(4), priorityLevelB(5), priorityLevelA(
            6);

    private int code;

    private EMLPPPriority(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static EMLPPPriority getEMLPPPriority(int code) {
        switch (code) {
            case 0:
                return priorityLevel0;
            case 1:
                return priorityLevel1;
            case 2:
                return priorityLevel2;
            case 3:
                return priorityLevel3;
            case 4:
                return priorityLevel4;
            case 5:
                return priorityLevelB;
            case 6:
                return priorityLevelA;
            default:
                if ((code >= 7) && (code <= 15))
                    return priorityLevel4;
                return null;
        }
    }
}