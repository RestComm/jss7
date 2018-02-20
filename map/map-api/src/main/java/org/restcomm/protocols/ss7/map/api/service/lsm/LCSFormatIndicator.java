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

package org.restcomm.protocols.ss7.map.api.service.lsm;

/**
 * LCS-FormatIndicator ::= ENUMERATED { logicalName (0), e-mailAddress (1), msisdn (2), url (3), sipUrl (4), ... }
 *
 * @author amit bhayani
 *
 */
public enum LCSFormatIndicator {

    logicalName(0), emailAddress(1), msisdn(2), url(3), sipUrl(4);

    private final int indicator;

    private LCSFormatIndicator(int indicator) {
        this.indicator = indicator;
    }

    public int getIndicator() {
        return this.indicator;
    }

    public static LCSFormatIndicator getLCSFormatIndicator(int indicator) {
        switch (indicator) {
            case 0:
                return logicalName;
            case 1:
                return emailAddress;
            case 2:
                return msisdn;
            case 3:
                return url;
            case 4:
                return sipUrl;
            default:
                return null;
        }
    }
}
