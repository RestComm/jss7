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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * AreaType ::= ENUMERATED { countryCode (0), plmnId (1), locationAreaId (2), routingAreaId (3), cellGlobalId (4), ... ,
 * utranCellId (5) }
 *
 * @author amit bhayani
 *
 */
public enum AreaType {

    countryCode(0), plmnId(1), locationAreaId(2), routingAreaId(3), cellGlobalId(4), utranCellId(5);

    private final int type;

    private AreaType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static AreaType getAreaType(int type) {
        switch (type) {
            case 0:
                return countryCode;
            case 1:
                return plmnId;
            case 2:
                return locationAreaId;
            case 3:
                return routingAreaId;
            case 4:
                return cellGlobalId;
            case 5:
                return utranCellId;
            default:
                return null;
        }
    }
}
