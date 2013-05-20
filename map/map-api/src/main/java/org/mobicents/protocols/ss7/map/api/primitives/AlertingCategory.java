/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
