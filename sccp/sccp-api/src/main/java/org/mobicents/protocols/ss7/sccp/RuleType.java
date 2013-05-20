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
