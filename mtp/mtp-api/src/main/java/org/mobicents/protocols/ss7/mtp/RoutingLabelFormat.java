/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 *
 */
public enum RoutingLabelFormat {
    ANSI_Sls8Bit("ANSI_Sls8Bit", 8), ANSI_Sls5Bit("ANSI_Sls5Bit", 5), ITU("ITU", 4), Japan_TTC_DDI("Japan_TTC_DDI", 8), China(
            "China", 8), Japan_NTT("Japan_NTT", 8);

    private final String format;
    private final int slsLengthInBits;
    private final int maxSls;
    private final int slsMsbMask;

    private static final String FORMAT_ITU = "ITU";
    private static final String FORMAT_ANSI = "ANSI";
    private static final String FORMAT_ANSI_8BIT = "ANSI_Sls8Bit";
    private static final String FORMAT_ANSI_5BIT = "ANSI_Sls5Bit";
    private static final String FORMAT_Japan_TTC_DDI = "Japan_TTC_DDI";
    private static final String FROMAT_China = "China";
    private static final String FORMAT_Japan_NTT = "Japan_NTT";

    private static final int SLS_LSB_MASK = 0x01;

    private RoutingLabelFormat(String format, int slsLengthInBits) {
        this.format = format;
        this.slsLengthInBits = slsLengthInBits;

        switch (this.slsLengthInBits) {
            case 8:
                this.maxSls = 256;
                this.slsMsbMask = 0x80;
                break;
            case 5:
                this.maxSls = 32;
                this.slsMsbMask = 0x10;
                break;
            case 4:
                this.maxSls = 16;
                this.slsMsbMask = 0x08;
                break;
            default:
                this.maxSls = 16;
                this.slsMsbMask = 0x08;
                break;
        }
    }

    public String getFormat() {
        return this.format;
    }

    /**
     * @return the sls
     */
    public int getSlsLengthInBits() {
        return this.slsLengthInBits;
    }

    public int getMaxSls() {
        return maxSls;
    }

    public int getSlsMsbMask() {
        return slsMsbMask;
    }

    public int getSlsLsbMask() {
        return SLS_LSB_MASK;
    }

    public static RoutingLabelFormat getInstance(String format) {
        if (FORMAT_ITU.equals(format)) {
            return ITU;
        } else if (FORMAT_ANSI.equals(format)) {
            return ANSI_Sls8Bit;
        } else if (FORMAT_ANSI_8BIT.equals(format)) {
            return ANSI_Sls8Bit;
        } else if (FORMAT_ANSI_5BIT.equals(format)) {
            return ANSI_Sls5Bit;
        } else if (FORMAT_Japan_TTC_DDI.equals(format)) {
            return Japan_TTC_DDI;
        } else if (FROMAT_China.equals(format)) {
            return China;
        } else if (FORMAT_Japan_NTT.equals(format)) {
            return Japan_NTT;
        }

        return null;
    }

}
