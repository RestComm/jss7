/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

/**
 *
 * IsupNatureOfAddressIndicator ::= ENUMERATED { subscriberNumber(1),
 * unknown(2), nationalNumber(3), internationalNumber(4),
 * networkSpecificNumber(5), networkRoutingNumberInNationalNumberFormat(6),
 * networkRoutingNumberInNetworkSpecificNumberFormat(7),
 * networkRoutingNumberConcatenatedWithCalledDirectoryNumber(8) }
 *
 * @author mnowa
 *
 */
public enum IsupNatureOfAddressIndicator {
    subscriberNumber(1), unknown(2), nationalNumber(3), internationalNumber(4), networkSpecificNumber(5),
    networkRoutingNumberInNationalNumberFormat(6), networkRoutingNumberInNetworkSpecificNumberFormat(7),
    networkRoutingNumberConcatenatedWithCalledDirectoryNumber(8);

    private int code;

    private IsupNatureOfAddressIndicator(int code) {
        this.code = code;
    }

    public static IsupNatureOfAddressIndicator getInstance(int code) {
        switch (code) {
            case 1:
                return IsupNatureOfAddressIndicator.subscriberNumber;
            case 2:
                return IsupNatureOfAddressIndicator.unknown;
            case 3:
                return IsupNatureOfAddressIndicator.nationalNumber;
            case 4:
                return IsupNatureOfAddressIndicator.internationalNumber;
            case 5:
                return IsupNatureOfAddressIndicator.networkSpecificNumber;
            case 6:
                return IsupNatureOfAddressIndicator.networkRoutingNumberInNationalNumberFormat;
            case 7:
                return IsupNatureOfAddressIndicator.networkRoutingNumberInNetworkSpecificNumberFormat;
            case 8:
                return IsupNatureOfAddressIndicator.networkRoutingNumberConcatenatedWithCalledDirectoryNumber;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
