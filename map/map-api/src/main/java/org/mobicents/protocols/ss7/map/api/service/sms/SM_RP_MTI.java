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

package org.mobicents.protocols.ss7.map.api.service.sms;

/**
 *
 * SM-RP-MTI ::= INTEGER (0..10) -- 0 SMS Deliver -- 1 SMS Status Report -- other values are reserved for future use and shall
 * be discarded if -- received
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum SM_RP_MTI {

    SMS_Deliver(0), SMS_Status_Report(1);

    private int code;

    private SM_RP_MTI(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static SM_RP_MTI getInstance(int code) {
        switch (code) {
            case 0:
                return SMS_Deliver;
            case 1:
                return SMS_Status_Report;
            default:
                return null;
        }
    }

}
