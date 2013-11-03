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

package org.mobicents.protocols.ss7.cap.api.primitives;

import java.io.Serializable;

/**
 *
 DateAndTime ::= OCTET STRING (SIZE(7)) -- DateAndTime is BCD encoded. The year digit indicating millenium occupies bits --
 * 0-3 of the first octet, and the year digit indicating century occupies bits -- 4-7 of the first octet. -- The year digit
 * indicating decade occupies bits 0-3 of the second octet, -- whilst the digit indicating the year within the decade occupies
 * bits 4-7 of -- the second octet. -- The most significant month digit occupies bits 0-3 of the third octet, -- and the least
 * significant month digit occupies bits 4-7 of the third octet. -- The most significant day digit occupies bits 0-3 of the
 * fourth octet, -- and the least significant day digit occupies bits 4-7 of the fourth octet. -- The most significant hours
 * digit occupies bits 0-3 of the fifth octet, -- and the least significant digit occupies bits 4-7 of the fifth octet. -- The
 * most significant minutes digit occupies bits 0-3 of the sixth octet, -- and the least significant digit occupies bits 4-7 of
 * the sixth octet. -- The most significant seconds digit occupies bits 0-3 of the seventh octet, -- and the least seconds
 * significant digit occupies bits 4-7 of the seventh octet. -- For the encoding of digits in an octet, refer to the
 * timeAndtimezone parameter.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DateAndTime extends Serializable {

    byte[] getData();

    int getYear();

    int getMonth();

    int getDay();

    int getHour();

    int getMinute();

    int getSecond();

    void setYear(int year);

    void setMonth(int month);

    void setDay(int day);

    void setHour(int hour);

    void setMinute(int minute);

    void setSecond(int second);

}