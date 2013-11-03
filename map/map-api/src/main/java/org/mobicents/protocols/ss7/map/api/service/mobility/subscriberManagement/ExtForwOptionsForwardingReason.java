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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 -- bits 43: forwarding reason -- 00 ms not reachable -- 01 ms busy -- 10 no reply -- 11 unconditional
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum ExtForwOptionsForwardingReason {
    msNotReachable(0), msBusy(1), noReply(2), unconditional(3);

    private int code;

    private ExtForwOptionsForwardingReason(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ExtForwOptionsForwardingReason getInstance(int code) {
        switch (code) {
            case 0:
                return ExtForwOptionsForwardingReason.msNotReachable;
            case 1:
                return ExtForwOptionsForwardingReason.msBusy;
            case 2:
                return ExtForwOptionsForwardingReason.noReply;
            case 3:
                return ExtForwOptionsForwardingReason.unconditional;
            default:
                return null;
        }
    }
}
