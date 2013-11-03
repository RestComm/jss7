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

package org.mobicents.protocols.ss7.map.api.smstpdu;

/**
 * Mobile originated SMS TPDU message types
 *
 * @author sergey vetyutnev
 *
 */
public enum SmsTpduType {

    // mobile originated
    SMS_DELIVER_REPORT(0), SMS_SUBMIT(1), SMS_COMMAND(2), MoReserved(3),

    // mobile terminated
    SMS_DELIVER(10), SMS_SUBMIT_REPORT(11), SMS_STATUS_REPORT(12), MtReserved(13);

    private int code;

    private SmsTpduType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public int getEncodedValue() {
        if (this.code < 10)
            return this.code;
        else
            return this.code - 10;
    }

    public boolean isMobileOriginatedMessage() {
        if (this.code < 10)
            return true;
        else
            return false;
    }

    public static SmsTpduType getMobileOriginatedInstance(int code) {
        switch (code) {
            case 0:
                return SMS_DELIVER_REPORT;
            case 1:
                return SMS_SUBMIT;
            case 2:
                return SMS_COMMAND;
            default:
                return MoReserved;
        }
    }

    public static SmsTpduType getMobileTerminatedInstance(int code) {
        switch (code) {
            case 0:
                return SMS_DELIVER;
            case 1:
                return SMS_SUBMIT_REPORT;
            case 2:
                return SMS_STATUS_REPORT;
            default:
                return MtReserved;
        }
    }
}