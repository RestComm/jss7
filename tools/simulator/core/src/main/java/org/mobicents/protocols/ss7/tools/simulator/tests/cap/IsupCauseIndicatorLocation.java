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
 * IsupCauseIndicatorLocation ::= ENUMERATED { user(0),
 * privateNetworkServingLocalUser(1), publicNetworkServingLocalUser(2),
 * transitNetwork(3), publicNetworkServingRemoteUser(4),
 * privateNetworkServingRemoteUser(5), internationalNetwork(7) }
 *
 * @author mnowa
 *
 * @see Q.850
 */
public enum IsupCauseIndicatorLocation {
    user(0), privateNetworkServingLocalUser(1), publicNetworkServingLocalUser(2), transitNetwork(
            3), publicNetworkServingRemoteUser(4), privateNetworkServingRemoteUser(5), internationalNetwork(7),
    networkBeyondIp(10);

    private int code;

    private IsupCauseIndicatorLocation(int code) {
        this.code = code;
    }

    public static IsupCauseIndicatorLocation getInstance(int code) {
        switch (code) {
            case 0:
                return user;
            case 1:
                return privateNetworkServingLocalUser;
            case 2:
                return publicNetworkServingLocalUser;
            case 3:
                return transitNetwork;
            case 4:
                return publicNetworkServingRemoteUser;
            case 5:
                return privateNetworkServingRemoteUser;
            case 7:
                return internationalNetwork;
            case 10:
                return networkBeyondIp;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
