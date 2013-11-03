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

/**
 * Start time:14:15:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:15:48 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TransmissionMediumUsed extends ISUPParameter {
    int _PARAMETER_CODE = 0x35;

    // FIXME: add C defs

    /**
     * 0 0 0 0 0 0 0 0 speech
     */
    int _MEDIUM_SPEECH = 0;

    /**
     * 0 0 0 0 0 0 1 0 - 64 kbit/s unrestricted
     */
    int _MEDIUM_64_KBIT_UNRESTRICTED = 2;
    /**
     * 0 0 0 0 0 0 1 1 - 3.1 kHz audio
     */
    int _MEDIUM_3_1_KHz_AUDIO = 3;
    /**
     * 0 0 0 0 0 1 0 0 - reserved for alternate speech (service 2)/64 kbit/s unrestricted (service 1)
     */
    int _MEDIUM_RESERVED_ASS2 = 4;
    /**
     * 0 0 0 0 0 1 0 1 - reserved for alternate 64 kbit/s unrestricted (service 1)/speech (service 2)
     */
    int _MEDIUM_RESERVED_ASS1 = 5;
    /**
     * 0 0 0 0 0 1 1 0 - 64 kbit/s preferred
     */
    int _MEDIUM_64_KBIT_PREFERED = 6;
    /**
     * 0 0 0 0 0 1 1 1 - 2 64 kbit/s unrestricted
     */
    int _MEDIUM_2x64_KBIT_UNRESTRICTED = 7;
    /**
     * 0 0 0 0 1 0 0 0 - 384 kbit/s unrestricted
     */
    int _MEDIUM_384_KBIT_UNRESTRICTED = 8;
    /**
     * 0 0 0 0 1 0 0 1 - 1536 kbit/s unrestricted
     */
    int _MEDIUM_1536_KBIT_UNRESTRICTED = 9;
    /**
     * 0 0 0 0 1 0 1 0 - 1920 kbit/s unrestricted
     */
    int _MEDIUM_1920_KBIT_UNRESTRICTED = 10;

    int getTransimissionMediumUsed();

    void setTransimissionMediumUsed(int transimissionMediumUsed);

}
