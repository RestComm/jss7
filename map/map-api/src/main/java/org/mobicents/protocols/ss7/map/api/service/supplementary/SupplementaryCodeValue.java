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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum SupplementaryCodeValue {

    // TODO: I am not sure that all supplementary codes a correct

    allServices(0),

    allSpeechTransmissionServices(0x10), telephony(0x11), emergencyCalls(0x12),

    allShortMessageServices(0x20), shortMessageMT_PP(0x21), shortMessageMO_PP(0x22), cellBroadcast(0x23),

    allFacsimileTransmissionServices(0x60), facsimileGroup3AndAlterSpeech(0x61), automaticFacsimileGroup3(0x62),

    allVoiceGroupCallServices(0xC0), voiceGroupCall(0xC1), voiceBroadcastCall(0xC2),

    AsynchronousGeneralBearerService(0x70),

    SynchronousGeneralBearerService(0x80),

    GPRS(0xD0);

    private int code;

    private SupplementaryCodeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static SupplementaryCodeValue getInstance(int code) {
        switch (code) {
            case 0x0:
                return SupplementaryCodeValue.allServices;
            case 0x10:
                return SupplementaryCodeValue.allSpeechTransmissionServices;
            case 0x11:
                return SupplementaryCodeValue.telephony;
            case 0x12:
                return SupplementaryCodeValue.emergencyCalls;
            case 0x20:
                return SupplementaryCodeValue.allShortMessageServices;
            case 0x21:
                return SupplementaryCodeValue.shortMessageMT_PP;
            case 0x22:
                return SupplementaryCodeValue.shortMessageMO_PP;
            case 0x23:
                return SupplementaryCodeValue.cellBroadcast;
            case 0x60:
                return SupplementaryCodeValue.allFacsimileTransmissionServices;
            case 0x61:
                return SupplementaryCodeValue.facsimileGroup3AndAlterSpeech;
            case 0x62:
                return SupplementaryCodeValue.automaticFacsimileGroup3;
            case 0xC0:
                return SupplementaryCodeValue.allVoiceGroupCallServices;
            case 0xC1:
                return SupplementaryCodeValue.voiceGroupCall;
            case 0xC2:
                return SupplementaryCodeValue.voiceBroadcastCall;

            case 0x70:
                return SupplementaryCodeValue.AsynchronousGeneralBearerService;
            case 0x80:
                return SupplementaryCodeValue.SynchronousGeneralBearerService;
            case 0xD0:
                return SupplementaryCodeValue.GPRS;

            default:
                return null;
        }
    }

}