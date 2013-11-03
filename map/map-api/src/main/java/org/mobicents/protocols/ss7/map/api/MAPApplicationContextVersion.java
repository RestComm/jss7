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

package org.mobicents.protocols.ss7.map.api;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum MAPApplicationContextVersion {
    version1(1), version2(2), version3(3), version4(4);

    private int version;

    private MAPApplicationContextVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
    }

    public static MAPApplicationContextVersion getInstance(long version) {
        switch ((int) version) {
            case 1:
                return MAPApplicationContextVersion.version1;
            case 2:
                return MAPApplicationContextVersion.version2;
            case 3:
                return MAPApplicationContextVersion.version3;
            case 4:
                return MAPApplicationContextVersion.version4;
        }

        return null;
    }

}
