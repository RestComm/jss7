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
 * OccurrenceInfo ::= ENUMERATED { oneTimeEvent (0), multipleTimeEvent (1), ...}
 *
 * @author abhayani
 *
 */
public enum OccurrenceInfo {

    oneTimeEvent(0), multipleTimeEvent(1);

    private int info;

    private OccurrenceInfo(int info) {
        this.info = info;
    }

    public int getInfo() {
        return this.info;
    }

    public static OccurrenceInfo getOccurrenceInfo(int info) {
        switch (info) {
            case 0:
                return oneTimeEvent;
            case 1:
                return multipleTimeEvent;
            default:
                return null;
        }
    }
}
