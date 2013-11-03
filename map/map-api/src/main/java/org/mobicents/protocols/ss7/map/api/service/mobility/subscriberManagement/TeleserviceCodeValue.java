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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum TeleserviceCodeValue {
    allServices(0),

    allSpeechTransmissionServices(0x10), telephony(0x11), emergencyCalls(0x12),

    allShortMessageServices(0x20), shortMessageMT_PP(0x21), shortMessageMO_PP(0x22), cellBroadcast(0x23),

    allFacsimileTransmissionServices(0x60), facsimileGroup3AndAlterSpeech(0x61), automaticFacsimileGroup3(0x62), facsimileGroup4(
            0x63),

    allVoiceGroupCallServices(0x90), voiceGroupCall(0x91), voiceBroadcastCall(0x92);

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
                return TeleserviceCodeValue.allServices;
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
            case 0x90:
                return TeleserviceCodeValue.allVoiceGroupCallServices;
            case 0x91:
                return TeleserviceCodeValue.voiceGroupCall;
            case 0x92:
                return TeleserviceCodeValue.voiceBroadcastCall;
            default:
                return null;
        }
    }
}
