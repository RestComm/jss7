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
 *
 * LocationEstimateType ::= ENUMERATED { currentLocation (0), currentOrLastKnownLocation (1), initialLocation (2), ...,
 * activateDeferredLocation (3), cancelDeferredLocation (4) } -- exception handling: -- a ProvideSubscriberLocation-Arg
 * containing an unrecognized LocationEstimateType -- shall be rejected by the receiver with a return error cause of unexpected
 * data value
 *
 * @author amit bhayani
 *
 */
public enum LocationEstimateType {

    currentLocation(0), currentOrLastKnownLocation(1), initialLocation(2), activateDeferredLocation(3), cancelDeferredLocation(
            4);

    private final int type;

    private LocationEstimateType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static LocationEstimateType getLocationEstimateType(int type) {
        switch (type) {
            case 0:
                return currentLocation;
            case 1:
                return currentOrLastKnownLocation;
            case 2:
                return initialLocation;
            case 3:
                return activateDeferredLocation;
            case 4:
                return cancelDeferredLocation;
            default:
                return null;
        }
    }
}
