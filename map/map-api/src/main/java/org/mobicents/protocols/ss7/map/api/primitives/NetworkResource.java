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
 *
 * NetworkResource ::= ENUMERATED { plmn (0), hlr (1), vlr (2), pvlr (3), controllingMSC (4), vmsc (5), eir (6), rss (7)}
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum NetworkResource {
    plmn(0), hlr(1), vlr(2), pvlr(3), controllingMSC(4), vmsc(5), eir(6), rss(7);

    private int code;

    private NetworkResource(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NetworkResource getInstance(int code) {
        switch (code) {
            case 0:
                return plmn;
            case 1:
                return hlr;
            case 2:
                return vlr;
            case 3:
                return pvlr;
            case 4:
                return controllingMSC;
            case 5:
                return vmsc;
            case 6:
                return eir;
            case 7:
                return rss;
            default:
                return null;
        }
    }

}
