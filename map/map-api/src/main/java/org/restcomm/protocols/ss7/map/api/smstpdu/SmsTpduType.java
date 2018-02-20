/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.api.smstpdu;

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