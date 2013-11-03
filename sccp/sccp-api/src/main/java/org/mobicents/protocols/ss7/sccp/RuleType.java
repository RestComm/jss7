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

package org.mobicents.protocols.ss7.sccp;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public enum RuleType {
    Solitary("Solitary"), Dominant("Dominant"), Loadshared("Loadshared"), Broadcast("Broadcast");

    private static final String SOLITARY = "Solitary";
    private static final String DOMINANT = "Dominant";
    private static final String LOADSHARED = "Loadshared";
    private static final String BROADCAST = "Broadcast";

    private final String type;

    private RuleType(String type) {
        this.type = type;
    }

    public static RuleType getInstance(String type) {
        if (SOLITARY.equalsIgnoreCase(type)) {
            return Solitary;
        } else if (DOMINANT.equalsIgnoreCase(type)) {
            return Dominant;
        } else if (LOADSHARED.equalsIgnoreCase(type)) {
            return Loadshared;
        } else if (BROADCAST.equalsIgnoreCase(type)) {
            return Broadcast;
        }

        return null;
    }

    public String getType() {
        return this.type;
    }
}
