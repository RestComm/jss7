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
 * Start time:14:14:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:14:47 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TransmissionMediumRequirement extends ISUPParameter {
    int _PARAMETER_CODE = 0x02;

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
    /**
     * 0 0 0 1 0 0 0 0 - 3 64 kbit/s unrestricted
     */
    int _MEDIUM_3x64_KBIT_UNRESTRICTED = 16;
    /**
     * 0 0 0 1 0 0 0 1 - 4 64 kbit/s unrestricted
     */
    int _MEDIUM_4x64_KBIT_UNRESTRICTED = 17;
    /**
     * 0 0 0 1 0 0 1 0 - 5 64 kbit/s unrestricted
     */
    int _MEDIUM_5x64_KBIT_UNRESTRICTED = 18;
    /**
     * 0 0 0 1 0 1 0 0 - 7 64 kbit/s unrestricted
     */
    int _MEDIUM_7x64_KBIT_UNRESTRICTED = 20;
    /**
     * 0 0 0 1 0 1 0 1 - 8 64 kbit/s unrestricted
     */
    int _MEDIUM_8x64_KBIT_UNRESTRICTED = 21;
    /**
     * 0 0 0 1 0 1 1 0 - 9 64 kbit/s unrestricted
     */
    int _MEDIUM_9x64_KBIT_UNRESTRICTED = 22;
    /**
     * 0 0 0 1 0 1 1 1 - 10 64 kbit/s unrestricted
     */
    int _MEDIUM_10x64_KBIT_UNRESTRICTED = 23;
    /**
     * 0 0 0 1 1 0 0 0 - 11 64 kbit/s unrestricted
     */
    int _MEDIUM_11x64_KBIT_UNRESTRICTED = 24;
    /**
     * 0 0 0 1 1 0 0 1 - 12 64 kbit/s unrestricted
     */
    int _MEDIUM_12x64_KBIT_UNRESTRICTED = 25;
    /**
     * 0 0 0 1 1 0 1 0 - 13 64 kbit/s unrestricted
     */
    int _MEDIUM_13x64_KBIT_UNRESTRICTED = 26;
    /**
     * 0 0 0 1 1 0 1 1 - 14 64 kbit/s unrestricted
     */
    int _MEDIUM_14x64_KBIT_UNRESTRICTED = 27;
    /**
     * 0 0 0 1 1 1 0 0 - 15 64 kbit/s unrestricted
     */
    int _MEDIUM_15x64_KBIT_UNRESTRICTED = 28;
    /**
     * 0 0 0 1 1 1 0 1 - 16 64 kbit/s unrestricted
     */
    int _MEDIUM_16x64_KBIT_UNRESTRICTED = 29;
    /**
     * 0 0 0 1 1 1 1 0 - 17 64 kbit/s unrestricted
     */
    int _MEDIUM_17x64_KBIT_UNRESTRICTED = 30;
    /**
     * 0 0 0 1 1 1 1 1 - 18 64 kbit/s unrestricted
     */
    int _MEDIUM_18x64_KBIT_UNRESTRICTED = 31;
    /**
     * 0 0 1 0 0 0 0 0 - 19 64 kbit/s unrestricted
     */
    int _MEDIUM_19x64_KBIT_UNRESTRICTED = 32;
    /**
     * 0 0 1 0 0 0 0 1 - 20 64 kbit/s unrestricted
     */
    int _MEDIUM_20x64_KBIT_UNRESTRICTED = 33;
    /**
     * 0 0 1 0 0 0 1 0 - 21 64 kbit/s unrestricted
     */
    int _MEDIUM_21x64_KBIT_UNRESTRICTED = 34;
    /**
     * 0 0 1 0 0 0 1 1 - 22 64 kbit/s unrestricted
     */
    int _MEDIUM_22x64_KBIT_UNRESTRICTED = 35;
    /**
     * 0 0 1 0 0 1 0 0 - 23 64 kbit/s unrestricted
     */
    int _MEDIUM_23x64_KBIT_UNRESTRICTED = 36;
    /**
     * 0 0 1 0 0 1 1 0 - 25 64 kbit/s unrestricted
     */
    int _MEDIUM_25x64_KBIT_UNRESTRICTED = 38;
    /**
     * 0 0 1 0 0 1 1 1 - 26 64 kbit/s unrestricted
     */
    int _MEDIUM_26x64_KBIT_UNRESTRICTED = 39;
    /**
     * 0 0 1 0 1 0 0 0 - 27 64 kbit/s unrestricted
     */
    int _MEDIUM_27x64_KBIT_UNRESTRICTED = 40;
    /**
     * 0 0 1 0 1 0 0 1 - 28 64 kbit/s unrestricted
     */
    int _MEDIUM_28x64_KBIT_UNRESTRICTED = 41;
    /**
     * 0 0 1 0 1 0 1 0 - 29 64 kbit/s unrestricted
     */
    int _MEDIUM_29x64_KBIT_UNRESTRICTED = 42;

    int getTransimissionMediumRequirement();

    void setTransimissionMediumRequirement(int transimissionMediumRequirement);
}
