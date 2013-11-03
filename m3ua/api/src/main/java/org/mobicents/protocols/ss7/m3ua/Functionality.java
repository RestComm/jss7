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
package org.mobicents.protocols.ss7.m3ua;

/**
 *
 * @author amit bhayani
 *
 */
public enum Functionality {
    AS("AS"), IPSP("IPSP"), SGW("SGW");

    private static final String TYPE_AS = "AS";
    private static final String TYPE_IPSP = "IPSP";
    private static final String TYPE_SGW = "SGW";

    private String type = null;

    private Functionality(String type) {
        this.type = type;
    }

    public static Functionality getFunctionality(String type) {
        if (TYPE_AS.equals(type)) {
            return AS;
        } else if (TYPE_IPSP.equals(type)) {
            return IPSP;
        } else if (TYPE_SGW.equals(type)) {
            return SGW;
        }
        return null;
    }

    public String getType() {
        return this.type;
    }

}
