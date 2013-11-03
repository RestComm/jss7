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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

/**
 *
 GPRSEventType ::= ENUMERATED { attach (1), attachChangeOfPosition (2), detached (3), pdp-ContextEstablishment (11),
 * pdp-ContextEstablishmentAcknowledgement (12), disonnect (13), pdp-ContextChangeOfPosition (14) }
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum GPRSEventType {
    attach(1), attachChangeOfPosition(2), detached(3), pdpContextEstablishment(11), pdpContextEstablishmentAcknowledgement(12), disonnect(
            13), pdpContextChangeOfPosition(14);

    private int code;

    private GPRSEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static GPRSEventType getInstance(int code) {
        switch (code) {
            case 1:
                return GPRSEventType.attach;
            case 2:
                return GPRSEventType.attachChangeOfPosition;
            case 3:
                return GPRSEventType.detached;
            case 11:
                return GPRSEventType.pdpContextEstablishment;
            case 12:
                return GPRSEventType.pdpContextEstablishmentAcknowledgement;
            case 13:
                return GPRSEventType.disonnect;
            case 14:
                return GPRSEventType.pdpContextChangeOfPosition;
            default:
                return null;
        }
    }

}
