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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 *
 * AdditionalNetworkResource ::= ENUMERATED { sgsn (0), ggsn (1), gmlc (2), gsmSCF (3), nplr (4), auc (5), ...} -- if unknown
 * value is received in AdditionalNetworkResource -- it shall be ignored.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum AdditionalNetworkResource {
    sgsn(0), ggsn(1), gmlc(2), gsmSCF(3), nplr(4), auc(5);

    private int code;

    private AdditionalNetworkResource(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AdditionalNetworkResource getInstance(int code) {
        switch (code) {
            case 0:
                return sgsn;
            case 1:
                return ggsn;
            case 2:
                return gmlc;
            case 3:
                return gsmSCF;
            case 4:
                return nplr;
            case 5:
                return auc;
            default:
                return null;
        }
    }

}
