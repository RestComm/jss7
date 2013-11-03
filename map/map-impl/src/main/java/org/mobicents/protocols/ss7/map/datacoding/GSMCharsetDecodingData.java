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

package org.mobicents.protocols.ss7.map.datacoding;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GSMCharsetDecodingData {

    protected int totalSeptetCount;
    protected int leadingSeptetSkipCount;
    protected boolean ussdStyleEncoding;

    /**
     * SMS case constructor
     *
     * @param totalSeptetCount Length of a decoded message in characters (for SMS case)
     * @param leadingSeptetSkipCount Count of leading septets to skip
     */
    public GSMCharsetDecodingData(int totalSeptetCount, int leadingSeptetSkipCount) {
        this.totalSeptetCount = totalSeptetCount;
        this.leadingSeptetSkipCount = leadingSeptetSkipCount;
        this.ussdStyleEncoding = false;
    }

    /**
     * USSD case constructor
     */
    public GSMCharsetDecodingData() {
        this.totalSeptetCount = Integer.MAX_VALUE;
        this.leadingSeptetSkipCount = 0;
        this.ussdStyleEncoding = true;
    }
}
