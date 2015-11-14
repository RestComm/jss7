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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
*
<code>
The following codes are used in the calling party's category parameter field.
0 0 0 0 0 0 0 0 calling party's category unknown at this time (national use)
0 0 0 0 0 0 0 1 operator, language French
0 0 0 0 0 0 1 0 operator, language English
0 0 0 0 0 0 1 1 operator, language German
0 0 0 0 0 1 0 0 operator, language Russian
0 0 0 0 0 1 0 1 operator, language Spanish
0 0 0 0 0 1 1 0
0 0 0 0 0 1 1 1
0 0 0 0 1 0 0 0
- (available to Administrations for selection a particular language by mutual agreement)
0 0 0 0 1 0 0 1 reserved (see ITU-T Recommendation Q.104) (Note) (national use)
0 0 0 0 1 0 1 0 ordinary calling subscriber
0 0 0 0 1 0 1 1 calling subscriber with priority
0 0 0 0 1 1 0 0 data call (voice band data)
0 0 0 0 1 1 0 1 test call
0 0 0 0 1 1 1 0 spare
0 0 0 0 1 1 1 1 payphone

0 0 0 1 0 0 0 0
to
1 1 0 1 1 1 1 1
- spare

1 1 1 0 0 0 0 0
to
1 1 1 1 1 1 1 0
- reserved for national use

1 1 1 1 1 1 1 1 - spare

NOTE  In national networks, code 00001001 may be used to indicate that the calling party is a national
operator.
</code>
*
*
*
* @author sergey vetyutnev
*
*/
public enum CategoryValue {

    categoryUnknownAtThisTime_NationalUse(0), operator_languageFrench(1), operator_languageEnglish(2), operator_languageGerman(3), operator_languageRussian(4), operator_languageSpanish(
            5), operator_languageAdmSelection_6(6), operator_languageAdmSelection_7(7), operator_languageAdmSelection_8(8), reserved_9(9), ordinaryCallingSubscriber(
            10), callingSubscriberWithPriority(11), dataCall_VoiceBandData(12), testCall(13), spare_14(14), payphone(15);

    private int code;

    private CategoryValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CategoryValue getInstance(int code) {
        switch (code) {
        case 0:
            return CategoryValue.categoryUnknownAtThisTime_NationalUse;
        case 1:
            return CategoryValue.operator_languageFrench;
        case 2:
            return CategoryValue.operator_languageEnglish;
        case 3:
            return CategoryValue.operator_languageGerman;
        case 4:
            return CategoryValue.operator_languageRussian;
        case 5:
            return CategoryValue.operator_languageSpanish;
        case 6:
            return CategoryValue.operator_languageAdmSelection_6;
        case 7:
            return CategoryValue.operator_languageAdmSelection_7;
        case 8:
            return CategoryValue.operator_languageAdmSelection_8;
        case 9:
            return CategoryValue.reserved_9;
        case 10:
            return CategoryValue.ordinaryCallingSubscriber;
        case 11:
            return CategoryValue.callingSubscriberWithPriority;
        case 12:
            return CategoryValue.dataCall_VoiceBandData;
        case 13:
            return CategoryValue.testCall;
        case 14:
            return CategoryValue.spare_14;
        case 15:
            return CategoryValue.payphone;

        default:
            return null;
        }
    }
}
