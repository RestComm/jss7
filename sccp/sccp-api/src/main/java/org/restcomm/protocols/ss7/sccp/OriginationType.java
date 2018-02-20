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

package org.restcomm.protocols.ss7.sccp;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum OriginationType {
    LOCAL("LocalOriginated"), REMOTE("RemoteOriginated"), ALL("All");

    private final String type;

    private OriginationType(String type) {
        this.type = type;
    }

    public static OriginationType getInstance(String type) {
        if (LOCAL.getValue().equalsIgnoreCase(type)) {
            return LOCAL;
        } else if (REMOTE.getValue().equalsIgnoreCase(type)) {
            return REMOTE;
        } else if (ALL.getValue().equalsIgnoreCase(type)) {
            return ALL;
        }

        throw new IllegalArgumentException("Wrong value: '"+type+"'");
    }

    public String getValue() {
        return this.type;
    }

}
