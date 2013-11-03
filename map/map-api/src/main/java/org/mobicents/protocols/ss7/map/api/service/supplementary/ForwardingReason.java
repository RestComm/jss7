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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

/**
 *
 ForwardingReason ::= ENUMERATED { notReachable (0), busy (1), noReply (2)}
 *
 * -- bits 43: forwarding reason -- 00 ms not reachable -- 01 ms busy -- 10 no reply -- 11 unconditional when used in a SRI
 * Result, -- or call deflection when used in a RCH Argument
 *
 *
 * @author cristian veliscu
 *
 */
public enum ForwardingReason {
    notReachable(0), busy(1), noReply(2), unconditionalOrCallDeflection(3);

    private int code;

    private ForwardingReason(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ForwardingReason getForwardingReason(int code) {
        switch (code) {
            case 0:
                return notReachable;
            case 1:
                return busy;
            case 2:
                return noReply;
            case 3:
                return unconditionalOrCallDeflection;
            default:
                return null;
        }
    }
}