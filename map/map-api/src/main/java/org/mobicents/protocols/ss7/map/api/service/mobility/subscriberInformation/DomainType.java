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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

/**
 *
 * DomainType ::= ENUMERATED { cs-Domain (0), ps-Domain (1), ...} -- exception handling: -- reception of values > 1 shall be
 * mapped to 'cs-Domain'
 *
 * @author abhayani
 *
 */
public enum DomainType {
    csDomain(0), psDomain(1);

    private final int type;

    private DomainType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static DomainType getInstance(int type) {
        switch (type) {
            case 0:
                return csDomain;
            case 1:
                return psDomain;
            default:
                return null;
        }
    }

}
