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

package org.mobicents.protocols.ss7.cap.api;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum CAPServerRole {
    // GSM Service Switching Function
    gsmSSF(0),
    // GSM Assisting Service Switching Function
    assistingGsmSSF(1),
    // GSM Specialized Resource Function
    gsmSRF(2),
    // Short Message Service Service Switching Function
    smsSSF(3),
    // GPRS Service Switching Function
    gprsSSF(4),
    // GSM Service Control Function
    gsmSCF(10);

    private int code;

    private CAPServerRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
