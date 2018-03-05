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

package org.restcomm.protocols.ss7.map.api.primitives;

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
