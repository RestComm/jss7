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
package org.restcomm.protocols.ss7.m3ua;

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
