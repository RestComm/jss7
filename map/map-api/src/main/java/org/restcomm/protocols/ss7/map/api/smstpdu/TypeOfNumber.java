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
 * Bits 6 5 4 0 0 0 Unknown 1) 0 0 1 International number 2) 0 1 0 National number 3) 0 1 1 Network specific number 4) 1 0 0
 * Subscriber number 5) 1 0 1 Alphanumeric, (coded according to 3GPP TS 23.038 [9] GSM 7-bit default alphabet) 1 1 0 Abbreviated
 * number 1 1 1 Reserved for extension
 *
 * @author sergey vetyutnev
 *
 */
public enum TypeOfNumber {

    Unknown(0), InternationalNumber(1), NationalNumber(2), NetworkSpecificNumber(3), SubscriberNumber(4), Alphanumeric(5), AbbreviatedNumber(
            6), Reserved(7);

    private int code;

    private TypeOfNumber(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TypeOfNumber getInstance(int code) {
        switch (code) {
            case 0:
                return Unknown;
            case 1:
                return InternationalNumber;
            case 2:
                return NationalNumber;
            case 3:
                return NetworkSpecificNumber;
            case 4:
                return SubscriberNumber;
            case 5:
                return Alphanumeric;
            case 6:
                return AbbreviatedNumber;
            default:
                return Reserved;
        }
    }
}