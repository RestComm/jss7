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
public enum AlertingCategory {
    Category1(4), Category2(5), Category3(6), Category4(7), Category5(8);

    private final int category;

    private AlertingCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return this.category;
    }

    public static AlertingCategory getInstance(int data) {
        switch (data) {
            case 4:
                return Category1;
            case 5:
                return Category2;
            case 6:
                return Category3;
            case 7:
                return Category4;
            case 8:
                return Category5;
        }

        return null;
    }
}
