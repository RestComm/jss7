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

package org.mobicents.protocols.ss7.inap.api.primitives;

/**
*
TerminalType ::= ENUMERATED {
unknown (0),
dialPulse (1),
dtmf (2),
isdn (3),
isdnNoDtmf (4),
spare (16)
}
-- Identifies the terminal type so that the SCF can specify, to the SRF,
-- the appropriate type of capability
-- (voice recognition, DTMF, display capability, etc.).
*
*
* @author sergey vetyutnev
*
*/
public enum TerminalType {

    unknown(0), dialPulse(1), dtmf(2), isdn(3), isdnNoDtmf(4), spare(16);

    private int code;

    private TerminalType(int code) {
        this.code = code;
    }

    public static TerminalType getInstance(int code) {
        switch (code) {
        case 0:
            return TerminalType.unknown;
        case 1:
            return TerminalType.dialPulse;
        case 2:
            return TerminalType.dtmf;
        case 3:
            return TerminalType.isdn;
        case 4:
            return TerminalType.isdnNoDtmf;
        case 16:
            return TerminalType.spare;
        }

        return null;
    }

    public int getCode() {
        return code;
    }

}
