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
 * @author sergey vetyutnev
 *
 */
public enum TeleserviceCodeValue {
    allTeleservices(0),

    allSpeechTransmissionServices(0x10), telephony(0x11), emergencyCalls(0x12),

    allShortMessageServices(0x20), shortMessageMT_PP(0x21), shortMessageMO_PP(0x22), cellBroadcast(0x23),

    allFacsimileTransmissionServices(0x60), facsimileGroup3AndAlterSpeech(0x61), automaticFacsimileGroup3(0x62), facsimileGroup4(0x63),

    allDataTeleservices(0x70), allTeleservices_ExeptSMS(0x80),

    allVoiceGroupCallServices(0x90), voiceGroupCall(0x91), voiceBroadcastCall(0x92),

    allPLMN_specificTS(0xd0), plmn_specificTS_1(0xd1), plmn_specificTS_2(0xd2), plmn_specificTS_3(0xd3), plmn_specificTS_4(0xd4), plmn_specificTS_5(0xd5), plmn_specificTS_6(
            0xd6), plmn_specificTS_7(0xd7), plmn_specificTS_8(0xd8), plmn_specificTS_9(0xd9), plmn_specificTS_A(0xdA), plmn_specificTS_B(0xdB), plmn_specificTS_C(
            0xdC), plmn_specificTS_D(0xdD), plmn_specificTS_E(0xdE), plmn_specificTS_F(0xdF);

    private int code;

    private TeleserviceCodeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TeleserviceCodeValue getInstance(int code) {
        switch (code) {
            case 0:
                return TeleserviceCodeValue.allTeleservices;

            case 0x10:
                return TeleserviceCodeValue.allSpeechTransmissionServices;
            case 0x11:
                return TeleserviceCodeValue.telephony;
            case 0x12:
                return TeleserviceCodeValue.emergencyCalls;

            case 0x20:
                return TeleserviceCodeValue.allShortMessageServices;
            case 0x21:
                return TeleserviceCodeValue.shortMessageMT_PP;
            case 0x22:
                return TeleserviceCodeValue.shortMessageMO_PP;
            case 0x23:
                return TeleserviceCodeValue.cellBroadcast;

            case 0x60:
                return TeleserviceCodeValue.allFacsimileTransmissionServices;
            case 0x61:
                return TeleserviceCodeValue.facsimileGroup3AndAlterSpeech;
            case 0x62:
                return TeleserviceCodeValue.automaticFacsimileGroup3;
            case 0x63:
                return TeleserviceCodeValue.facsimileGroup4;

            case 0x70:
                return TeleserviceCodeValue.allDataTeleservices;
            case 0x80:
                return TeleserviceCodeValue.allTeleservices_ExeptSMS;

            case 0x90:
                return TeleserviceCodeValue.allVoiceGroupCallServices;
            case 0x91:
                return TeleserviceCodeValue.voiceGroupCall;
            case 0x92:
                return TeleserviceCodeValue.voiceBroadcastCall;

            case 0xd0:
                return TeleserviceCodeValue.allPLMN_specificTS;
            case 0xd1:
                return TeleserviceCodeValue.plmn_specificTS_1;
            case 0xd2:
                return TeleserviceCodeValue.plmn_specificTS_2;
            case 0xd3:
                return TeleserviceCodeValue.plmn_specificTS_3;
            case 0xd4:
                return TeleserviceCodeValue.plmn_specificTS_4;
            case 0xd5:
                return TeleserviceCodeValue.plmn_specificTS_5;
            case 0xd6:
                return TeleserviceCodeValue.plmn_specificTS_6;
            case 0xd7:
                return TeleserviceCodeValue.plmn_specificTS_7;
            case 0xd8:
                return TeleserviceCodeValue.plmn_specificTS_8;
            case 0xd9:
                return TeleserviceCodeValue.plmn_specificTS_9;
            case 0xda:
                return TeleserviceCodeValue.plmn_specificTS_A;
            case 0xdb:
                return TeleserviceCodeValue.plmn_specificTS_B;
            case 0xdc:
                return TeleserviceCodeValue.plmn_specificTS_C;
            case 0xdd:
                return TeleserviceCodeValue.plmn_specificTS_D;
            case 0xde:
                return TeleserviceCodeValue.plmn_specificTS_E;
            case 0xdf:
                return TeleserviceCodeValue.plmn_specificTS_F;

            default:
                return null;
        }
    }
}
