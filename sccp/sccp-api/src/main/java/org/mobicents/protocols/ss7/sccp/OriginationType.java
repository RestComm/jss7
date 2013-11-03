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
 *
 * @author sergey vetyutnev
 *
 */
public enum OriginationType {
    LocalOriginated("LocalOriginated"), RemoteOriginated("RemoteOriginated"), All("All");

    private static final String LOCAL_ORIGINATED = "LocalOriginated";
    private static final String REMOTE_ORIGINATED = "RemoteOriginated";
    private static final String ALL = "All";

    private final String type;

    private OriginationType(String type) {
        this.type = type;
    }

    public static OriginationType getInstance(String type) {
        if (LOCAL_ORIGINATED.equalsIgnoreCase(type)) {
            return LocalOriginated;
        } else if (REMOTE_ORIGINATED.equalsIgnoreCase(type)) {
            return RemoteOriginated;
        } else if (ALL.equalsIgnoreCase(type)) {
            return All;
        }

        return null;
    }

    public String getType() {
        return this.type;
    }

}
