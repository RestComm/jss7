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
 TimeAndTimezone {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minTimeAndTimezoneLength ..
 * bound.&maxTimeAndTimezoneLength)) -- Indicates the time and timezone, relative to GMT. This parameter is BCD encoded. -- The
 * year digit indicating millenium occupies bits 0-3 of the first octet, and the year -- digit indicating century occupies bits
 * 4-7 of the first octet. -- The year digit indicating decade occupies bits 0-3 of the second octet, whilst the digit --
 * indicating the year within the decade occupies bits 4-7 of the second octet. -- The most significant month digit occupies
 * bits 0-3 of the third octet, and the least -- significant month digit occupies bits 4-7 of the third octet. -- The most
 * significant day digit occupies bits 0-3 of the fourth octet, and the least -- significant day digit occupies bits 4-7 of the
 * fourth octet. -- The most significant hours digit occupies bits 0-3 of the fifth octet, and the least -- significant hours
 * digit occupies bits 4-7 of the fifth octet. -- The most significant minutes digit occupies bits 0-3 of the sixth octet, and
 * the least -- significant minutes digit occupies bits 4-7 of the sixth octet. -- The most significant seconds digit occupies
 * bits 0-3 of the seventh octet, and the least -- significant seconds digit occupies bits 4-7 of the seventh octet. -- -- The
 * timezone information occupies the eighth octet. For the encoding of Timezone refer to -- 3GPP TS 23.040 [6]. -- -- The BCD
 * digits are packed and encoded as follows: -- -- Bit 7 6 5 4 | 3 2 1 0 -- 2nd digit | 1st digit Octet 1 -- 3rd digit | 4th
 * digit Octet 2 -- .. .. -- nth digit | n-1th digit Octet m -- -- 0000 digit 0 -- 0001 digit 1 -- 0010 digit 2 -- 0011 digit 3
 * -- 0100 digit 4 -- 0101 digit 5 -- 0110 digit 6 -- 0111 digit 7 -- 1000 digit 8 -- 1001 digit 9 -- 1010 spare -- 1011 spare
 * -- 1100 spare -- 1101 spare -- 1110 spare -- 1101 spare -- -- where the leftmost bit of the digit is either bit 7 or bit 3 of
 * the octet.
 *
 * TimeIfNoTariffSwitch ::= INTEGER(0..864000) -- TimeIfNoTariffSwitch is measured in 100 millisecond intervals
 *
 * @author sergey vetyutnev
 *
 */
public interface TimeAndTimezone extends Serializable {

    byte[] getData();

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

    void setYear(int year);

    void setMonth(int month);

    void setDay(int day);

    void setHour(int hour);

    void setMinute(int minute);

    void setSecond(int second);

    /**
     * @param timeZone the timeZone in in quarters of an hour
     */
    void setTimeZone(int timeZone);

}