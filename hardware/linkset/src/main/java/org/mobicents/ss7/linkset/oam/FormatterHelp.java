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

package org.mobicents.ss7.linkset.oam;

/**
 *
 */
public class FormatterHelp {

    public static final String SPACE = " ";
    public static final String EQUAL_SIGN = "=";
    public static final String NEW_LINE = "\n";

    public static void createPad(StringBuffer sb, int pad) {
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
    }

    public static String getLinkState(int state) {
        switch (state) {
            case LinkState.AVAILABLE:
                return "AVAILABLE";
            case LinkState.FAILED:
                return "FAILED";
            case LinkState.SHUTDOWN:
                return "SHUTDOWN";
            case LinkState.UNAVAILABLE:
                return "UNAVAILABLE";
            default:
                return "UNKNOWN";
        }
    }

    public static String getLinksetState(int state) {
        switch (state) {
            case LinksetState.AVAILABLE:
                return "AVAILABLE";
            case LinksetState.SHUTDOWN:
                return "SHUTDOWN";
            case LinksetState.UNAVAILABLE:
                return "UNAVAILABLE";
            default:
                return "UNKNOWN";
        }
    }

}
