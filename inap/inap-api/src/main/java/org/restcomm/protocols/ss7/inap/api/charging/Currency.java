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

package org.restcomm.protocols.ss7.inap.api.charging;

/**
 *
 <code>
Currency ::= ENUMERATED {
noIndication (0),
australianDollar (1),
austrianSchilling (2),
belgianFranc (3),
britishPound (4),
czechKoruna (5),
danishKrone (6),
dutchGuilder (7),
euro (8),
finnishMarkka (9),
frenchFranc (10),
germanMark (11),
greekDrachma (12),
hungarianForint (13),
irishPunt (14),
italianLira (15),
japaneseYen (16),
luxembourgian-Franc (17),
norwegianKrone (18),
polishZloty (19),
portugeseEscudo (20),
russianRouble (21),
slovakKoruna (22),
spanishPeseta (23),
swedishKrone (24),
swissFranc (25),
turkishLira (26),
uSDollar (27),
...
}
</code>
 *
 * @author sergey vetyutnev
 *
 */
public enum Currency {
    noIndication(0), australianDollar(1), austrianSchilling(2), belgianFranc(3), britishPound(4), czechKoruna(5), danishKrone(6), dutchGuilder(
            7), euro(8), finnishMarkka(9), frenchFranc(10), germanMark(11), greekDrachma(12), hungarianForint(13), irishPunt(14), italianLira(
            15), japaneseYen(16), luxembourgianFranc(17), norwegianKrone(18), polishZloty(19), portugeseEscudo(20), russianRouble(
            21), slovakKoruna(22), spanishPeseta(23), swedishKrone(24), swissFranc(25), turkishLira(26), uSDollar(27);

    private int code;

    private Currency(int code) {
        this.code = code;
    }

    public static Currency getInstance(int code) {
        switch (code) {
            case 0:
                return Currency.noIndication;
            case 1:
                return Currency.australianDollar;
            case 2:
                return Currency.austrianSchilling;
            case 3:
                return Currency.belgianFranc;
            case 4:
                return Currency.britishPound;
            case 5:
                return Currency.czechKoruna;
            case 6:
                return Currency.danishKrone;
            case 7:
                return Currency.dutchGuilder;
            case 8:
                return Currency.euro;
            case 9:
                return Currency.finnishMarkka;
            case 10:
                return Currency.frenchFranc;
            case 11:
                return Currency.germanMark;
            case 12:
                return Currency.greekDrachma;
            case 13:
                return Currency.hungarianForint;
            case 14:
                return Currency.irishPunt;
            case 15:
                return Currency.italianLira;
            case 16:
                return Currency.japaneseYen;
            case 17:
                return Currency.luxembourgianFranc;
            case 18:
                return Currency.norwegianKrone;
            case 19:
                return Currency.polishZloty;
            case 20:
                return Currency.portugeseEscudo;
            case 21:
                return Currency.russianRouble;
            case 22:
                return Currency.slovakKoruna;
            case 23:
                return Currency.spanishPeseta;
            case 24:
                return Currency.swedishKrone;
            case 25:
                return Currency.swissFranc;
            case 26:
                return Currency.turkishLira;
            case 27:
                return Currency.uSDollar;
        }

        return null;
    }

    public int getCode() {
        return code;
    }

}
