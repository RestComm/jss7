/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.primitives;

/**
*
<code>
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
</code>
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
