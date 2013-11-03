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
 * @author amit bhayani
 *
 */
public enum AlertingLevel {
    Level0(0), Level1(1), Level2(2);

    private final int level;

    private AlertingLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public static AlertingLevel getInstance(int data) {
        switch (data) {
            case 0:
                return Level0;
            case 1:
                return Level1;
            case 2:
                return Level2;
        }

        return null;
    }
}
