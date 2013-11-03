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

import java.io.OutputStream;
import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 * The TP-Service-Centre-Time-Stamp field is given in semi-octet representation, and represents the local time in the following
 * way:
 *
 * Year: Month: Day: Hour: Minute: Second: Time Zone Digits: 2 2 2 2 2 2 2 (Semi-octets)
 *
 * The Time Zone indicates the difference, expressed in quarters of an hour, between the local time and GMT. In the first of the
 * two semi-octets, the first bit (bit 3 of the seventh octet of the TP-Service-Centre-Time-Stamp field) represents the
 * algebraic sign of this difference (0: positive, 1: negative).
 *
 * @author sergey vetyutnev
 *
 */
public interface AbsoluteTimeStamp extends Serializable {

    int getYear();

    int getMonth();

    int getDay();

    int getHour();

    int getMinute();

    int getSecond();

    /**
     * @return the timeZone in in quarters of an hour
     */
    int getTimeZone();

    void encodeData(OutputStream stm) throws MAPException;

}