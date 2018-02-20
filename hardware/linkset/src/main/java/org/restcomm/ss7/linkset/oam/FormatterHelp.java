/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.ss7.linkset.oam;

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
