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
