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

package org.mobicents.protocols.ss7.indicator;

import java.io.IOException;

/**
 * Numbering Plan constants.
 *
 * Look at ITU-T Q.713 Page 11
 *
 * @author kulikov
 * @author amit bhayani
 */
public enum NumberingPlan {

    UNKNOWN(0),
    /** Recommendations E.163 and E.164 */
    ISDN_TELEPHONY(1), GENERIC(2),
    /** Recommendation X.121 */
    DATA(3),
    /** Recommendation F.69 */
    TELEX(4),
    /** Recommendations E.210, 211 */
    MERITIME_MOBILE(5),
    /** Recommendation E.212 */
    LAND_MOBILE(6),
    /** Recommendation E.214 */
    ISDN_MOBILE(7), SPARE_8(8), SPARE_9(9), SPARE_10(10), SPARE_11(11), SPARE_12(12), SPARE_13(13), PRIVATE(14), RESERVED(15);

    private int value;

    private NumberingPlan(int v) {
        this.value = v;
    }

    public int getValue() {
        return value;
    }

    public static NumberingPlan valueOf(int v) throws IOException {
        switch (v) {
            case 0:
                return UNKNOWN;
            case 1:
                return ISDN_TELEPHONY;
            case 2:
                return GENERIC;
            case 3:
                return DATA;
            case 4:
                return TELEX;
            case 5:
                return MERITIME_MOBILE;
            case 6:
                return LAND_MOBILE;
            case 7:
                return ISDN_MOBILE;
            case 8:
                return SPARE_8;
            case 9:
                return SPARE_9;
            case 10:
                return SPARE_10;
            case 11:
                return SPARE_11;
            case 12:
                return SPARE_12;
            case 13:
                return SPARE_13;
            case 14:
                return PRIVATE;
            case 15:
                return RESERVED;
            default:
                throw new IOException("Unrecognized Numbering Plan. Must be between 0 to 15 and value is=" + v);
        }

    }
}
