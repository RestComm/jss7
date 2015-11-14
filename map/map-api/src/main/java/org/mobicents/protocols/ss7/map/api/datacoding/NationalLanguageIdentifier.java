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

package org.mobicents.protocols.ss7.map.api.datacoding;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum NationalLanguageIdentifier {
    Reserved(0), Turkish(1), Spanish(2), Portuguese(3), Bengali(4), Gujarati(5), Hindi(6), Kannada(7), Malayalam(8), Oriya(9), Punjabi(
            10), Tamil(11), Telugu(12), Urdu(13);

    private int code;

    private NationalLanguageIdentifier(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NationalLanguageIdentifier getInstance(int code) {
        switch (code) {
            case 1:
                return Turkish;
            case 2:
                return Spanish;
            case 3:
                return Portuguese;
            case 4:
                return Bengali;
            case 5:
                return Gujarati;
            case 6:
                return Hindi;
            case 7:
                return Kannada;
            case 8:
                return Malayalam;
            case 9:
                return Oriya;
            case 10:
                return Punjabi;
            case 11:
                return Tamil;
            case 12:
                return Telugu;
            case 13:
                return Urdu;
            default:
                return Reserved;
        }
    }
}
