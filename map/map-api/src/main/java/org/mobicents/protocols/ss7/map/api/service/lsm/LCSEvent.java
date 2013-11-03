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
 * LCS-Event ::= ENUMERATED { emergencyCallOrigination (0), emergencyCallRelease (1), mo-lr (2), ..., deferredmt-lrResponse (3)
 * } -- exception handling: -- a SubscriberLocationReport-Arg containing an unrecognized LCS-Event -- shall be rejected by a
 * receiver with a return error cause of unexpected data value
 *
 * @author amit bhayani
 *
 */
public enum LCSEvent {

    emergencyCallOrigination(0), emergencyCallRelease(1), molr(2), deferredmtlrResponse(3);

    private final int event;

    private LCSEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return this.event;
    }

    public static LCSEvent getLCSEvent(int event) {
        switch (event) {
            case 0:
                return emergencyCallOrigination;
            case 1:
                return emergencyCallRelease;
            case 2:
                return molr;
            case 3:
                return deferredmtlrResponse;
            default:
                return null;
        }
    }
}
