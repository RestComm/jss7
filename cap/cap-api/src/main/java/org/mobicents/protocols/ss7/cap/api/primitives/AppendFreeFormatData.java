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

package org.mobicents.protocols.ss7.cap.api.primitives;

/**
 *
 AppendFreeFormatData ::= ENUMERATED { overwrite (0), append (1) }
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum AppendFreeFormatData {

    overwrite(0), append(1);

    private int code;

    private AppendFreeFormatData(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static AppendFreeFormatData getInstance(int code) {
        switch (code) {
            case 0:
                return AppendFreeFormatData.overwrite;
            case 1:
                return AppendFreeFormatData.append;
            default:
                return null;
        }
    }
}
