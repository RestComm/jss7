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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

/**
 *
 GuidanceInfo ::= ENUMERATED { enterPW (0), enterNewPW (1), enterNewPW-Again (2)} -- How this information is really delivered
 * to the subscriber -- (display, announcement, ...) is not part of this -- specification.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum GuidanceInfo {
    enterPW(0), enterNewPW(1), enterNewPWAgain(2);

    private int code;

    private GuidanceInfo(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static GuidanceInfo getInstance(int code) {
        switch (code) {
            case 0:
                return GuidanceInfo.enterPW;
            case 1:
                return GuidanceInfo.enterNewPW;
            case 2:
                return GuidanceInfo.enterNewPWAgain;
            default:
                return null;
        }
    }
}
