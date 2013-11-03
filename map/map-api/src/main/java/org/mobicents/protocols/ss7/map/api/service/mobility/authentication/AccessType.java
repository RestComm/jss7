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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

/**
 *
 AccessType ::= ENUMERATED { call (0), emergencyCall (1), locationUpdating (2), supplementaryService (3), shortMessage (4),
 * gprsAttach (5), routingAreaUpdating (6), serviceRequest (7), pdpContextActivation (8), pdpContextDeactivation (9), ...,
 * gprsDetach (10)} -- exception handling: -- received values greater than 10 shall be ignored.
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum AccessType {
    call(0), emergencyCall(1), locationUpdating(2), supplementaryService(3), shortMessage(4), gprsAttach(5), routingAreaUpdating(
            6), serviceRequest(7), pdpContextActivation(8), pdpContextDeactivation(9), gprsDetach(10);

    private int code;

    private AccessType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static AccessType getInstance(int code) {
        switch (code) {
            case 0:
                return AccessType.call;
            case 1:
                return AccessType.emergencyCall;
            case 2:
                return AccessType.locationUpdating;
            case 3:
                return AccessType.supplementaryService;
            case 4:
                return AccessType.shortMessage;
            case 5:
                return AccessType.gprsAttach;
            case 6:
                return AccessType.routingAreaUpdating;
            case 7:
                return AccessType.serviceRequest;
            case 8:
                return AccessType.pdpContextActivation;
            case 9:
                return AccessType.pdpContextDeactivation;
            case 10:
                return AccessType.gprsDetach;
            default:
                return null;
        }
    }
}
