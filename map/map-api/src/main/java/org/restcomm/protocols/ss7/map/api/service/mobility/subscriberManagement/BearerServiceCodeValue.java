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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum BearerServiceCodeValue {

    allBearerServices(0x00),

    allDataCDAServices(0x10), dataCDA_300bps(0x11), dataCDA_1200bps(0x12), dataCDA_1200_75bps(0x13), dataCDA_2400bps(0x14), dataCDA_4800bps(0x15), dataCDA_9600bps(
            0x16), general_dataCDA(0x17),

    allDataCDS_Services(0x18), dataCDS_1200bps(0x1A), dataCDS_2400bps(0x1C), dataCDS_4800bps(0x1D), dataCDS_9600bps(0x1E), general_dataCDS(0x1F),

    allPadAccessCA_Services(0x20), padAccessCA_300bps(0x21), padAccessCA_1200bps(0x22), padAccessCA_1200_75bps(0x23), padAccessCA_2400bps(0x24), padAccessCA_4800bps(
            0x25), padAccessCA_9600bps(0x26), general_padAccessCA(0x27),

    allDataPDS_Services(0x28), dataPDS_2400bps(0x2C), dataPDS_4800bps(0x2D), dataPDS_9600bps(0x2E), general_dataPDS(0x2F),

    allAlternateSpeech_DataCDA(0x30), allAlternateSpeech_DataCDS(0x38), allSpeechFollowedByDataCDA(0x40), allSpeechFollowedByDataCDS(0x48),

    allDataCircuitAsynchronous(0x50), allAsynchronousServices(0x60), allDataCircuitSynchronous(0x58), allSynchronousServices(0x68),

    allPLMN_specificBS(0xD0), plmn_specificBS_1(0xD1), plmn_specificBS_2(0xD2), plmn_specificBS_3(0xD3), plmn_specificBS_4(0xD4), plmn_specificBS_5(0xD5), plmn_specificBS_6(
            0xD6), plmn_specificBS_7(0xD7), plmn_specificBS_8(0xD8), plmn_specificBS_9(0xD9), plmn_specificBS_A(0xDA), plmn_specificBS_B(0xDB), plmn_specificBS_C(
            0xDC), plmn_specificBS_D(0xDD), plmn_specificBS_E(0xDE), plmn_specificBS_F(0xDF);

//    PlmnSpecificBearerServices(-1),
//
//    AllServices(0),
//
//    AsynchronousGeneralBearerService(0x20), Asynchronous300bps(0x21), Asynchronous1_2kbps(0x22), Asynchronous1200_75bps(0x23), Asynchronous2_4kbps(
//            0x24), Asynchronous4_8kbps(0x25), Asynchronous9_6kbps(0x26),
//
//    SynchronousGeneralBearerService(0x30), Synchronous1_2kbps(0x31), Synchronous2_4kbps(0x32), Synchronous4_8kbps(0x33), Synchronous9_6kbps(
//            0x34),
//
//    GeneralPADAccessBearerService(0x40), PADAccess300bps(0x41), PADAccess1_2kbps(0x42), PADAccess1200_75bps(0x43), PADAccess2_4kbps(
//            0x44), PADAccess4_8kbps(0x45), PADAccess9_6kbps(0x46),
//
//    GeneralPacketAccessBearerService(0x50), PacketAccess2_4kbps(0x51), PacketAccess4_8kbps(0x52), PacketAccess9_5kbps(0x53),
//
//    AlternateSpeechData(0x61),
//
//    GPRS(0x70),
//
//    SpeechFollowedByData(0x81);

    private int code;

    private BearerServiceCodeValue(int code) {

        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

//    public int getBearerServiceCode() {
//        if (this.code == -1)
//            return -1;
//        else
//            return ((this.code & 0xF0) >> 1) | (this.code & 0x07);
//    }

    public static BearerServiceCodeValue getInstance(int code) {

        switch (code) {
        case 0:
            return BearerServiceCodeValue.allBearerServices;

        case 0x10:
            return BearerServiceCodeValue.allDataCDAServices;
        case 0x11:
            return BearerServiceCodeValue.dataCDA_300bps;
        case 0x12:
            return BearerServiceCodeValue.dataCDA_1200bps;
        case 0x13:
            return BearerServiceCodeValue.dataCDA_1200_75bps;
        case 0x14:
            return BearerServiceCodeValue.dataCDA_2400bps;
        case 0x15:
            return BearerServiceCodeValue.dataCDA_4800bps;
        case 0x16:
            return BearerServiceCodeValue.dataCDA_9600bps;
        case 0x17:
            return BearerServiceCodeValue.general_dataCDA;

        case 0x18:
            return BearerServiceCodeValue.allDataCDS_Services;
        case 0x1A:
            return BearerServiceCodeValue.dataCDS_1200bps;
        case 0x1C:
            return BearerServiceCodeValue.dataCDS_2400bps;
        case 0x1D:
            return BearerServiceCodeValue.dataCDS_4800bps;
        case 0x1E:
            return BearerServiceCodeValue.dataCDS_9600bps;
        case 0x1F:
            return BearerServiceCodeValue.general_dataCDS;

        case 0x20:
            return BearerServiceCodeValue.allPadAccessCA_Services;
        case 0x21:
            return BearerServiceCodeValue.padAccessCA_300bps;
        case 0x22:
            return BearerServiceCodeValue.padAccessCA_1200bps;
        case 0x23:
            return BearerServiceCodeValue.padAccessCA_1200_75bps;
        case 0x24:
            return BearerServiceCodeValue.padAccessCA_2400bps;
        case 0x25:
            return BearerServiceCodeValue.padAccessCA_4800bps;
        case 0x26:
            return BearerServiceCodeValue.padAccessCA_9600bps;
        case 0x27:
            return BearerServiceCodeValue.general_padAccessCA;

        case 0x28:
            return BearerServiceCodeValue.allDataPDS_Services;
        case 0x2C:
            return BearerServiceCodeValue.dataPDS_2400bps;
        case 0x2D:
            return BearerServiceCodeValue.dataPDS_4800bps;
        case 0x2E:
            return BearerServiceCodeValue.dataPDS_9600bps;
        case 0x2F:
            return BearerServiceCodeValue.general_dataPDS;

        case 0x30:
            return BearerServiceCodeValue.allAlternateSpeech_DataCDA;
        case 0x38:
            return BearerServiceCodeValue.allAlternateSpeech_DataCDS;
        case 0x40:
            return BearerServiceCodeValue.allSpeechFollowedByDataCDA;
        case 0x48:
            return BearerServiceCodeValue.allSpeechFollowedByDataCDS;

        case 0x50:
            return BearerServiceCodeValue.allDataCircuitAsynchronous;
        case 0x60:
            return BearerServiceCodeValue.allAsynchronousServices;
        case 0x58:
            return BearerServiceCodeValue.allDataCircuitSynchronous;
        case 0x68:
            return BearerServiceCodeValue.allSynchronousServices;

        case 0xD0:
            return BearerServiceCodeValue.allPLMN_specificBS;
        case 0xD1:
            return BearerServiceCodeValue.plmn_specificBS_1;
        case 0xD2:
            return BearerServiceCodeValue.plmn_specificBS_2;
        case 0xD3:
            return BearerServiceCodeValue.plmn_specificBS_3;
        case 0xD4:
            return BearerServiceCodeValue.plmn_specificBS_4;
        case 0xD5:
            return BearerServiceCodeValue.plmn_specificBS_5;
        case 0xD6:
            return BearerServiceCodeValue.plmn_specificBS_6;
        case 0xD7:
            return BearerServiceCodeValue.plmn_specificBS_7;
        case 0xD8:
            return BearerServiceCodeValue.plmn_specificBS_8;
        case 0xD9:
            return BearerServiceCodeValue.plmn_specificBS_9;
        case 0xDA:
            return BearerServiceCodeValue.plmn_specificBS_A;
        case 0xDB:
            return BearerServiceCodeValue.plmn_specificBS_B;
        case 0xDC:
            return BearerServiceCodeValue.plmn_specificBS_C;
        case 0xDD:
            return BearerServiceCodeValue.plmn_specificBS_D;
        case 0xDE:
            return BearerServiceCodeValue.plmn_specificBS_E;
        case 0xDF:
            return BearerServiceCodeValue.plmn_specificBS_F;

        default:
            return null;
        }


//        if (code >= 128)
//            return BearerServiceCodeValue.PlmnSpecificBearerServices;
//
//        code = ((code & 0x78) << 1) | (code & 0x07);
//
//        switch (code) {
//            case 0:
//                return BearerServiceCodeValue.AllServices;
//
//            case 0x20:
//                return BearerServiceCodeValue.AsynchronousGeneralBearerService;
//            case 0x21:
//                return BearerServiceCodeValue.Asynchronous300bps;
//            case 0x22:
//                return BearerServiceCodeValue.Asynchronous1_2kbps;
//            case 0x23:
//                return BearerServiceCodeValue.Asynchronous1200_75bps;
//            case 0x24:
//                return BearerServiceCodeValue.Asynchronous2_4kbps;
//            case 0x25:
//                return BearerServiceCodeValue.Asynchronous4_8kbps;
//            case 0x26:
//                return BearerServiceCodeValue.Asynchronous9_6kbps;
//
//            case 0x30:
//                return BearerServiceCodeValue.SynchronousGeneralBearerService;
//            case 0x31:
//                return BearerServiceCodeValue.Synchronous1_2kbps;
//            case 0x32:
//                return BearerServiceCodeValue.Synchronous2_4kbps;
//            case 0x33:
//                return BearerServiceCodeValue.Synchronous4_8kbps;
//            case 0x34:
//                return BearerServiceCodeValue.Synchronous9_6kbps;
//
//            case 0x40:
//                return BearerServiceCodeValue.GeneralPADAccessBearerService;
//            case 0x41:
//                return BearerServiceCodeValue.PADAccess300bps;
//            case 0x42:
//                return BearerServiceCodeValue.PADAccess1_2kbps;
//            case 0x43:
//                return BearerServiceCodeValue.PADAccess1200_75bps;
//            case 0x44:
//                return BearerServiceCodeValue.PADAccess2_4kbps;
//            case 0x45:
//                return BearerServiceCodeValue.PADAccess4_8kbps;
//            case 0x46:
//                return BearerServiceCodeValue.PADAccess9_6kbps;
//
//            case 0x50:
//                return BearerServiceCodeValue.GeneralPacketAccessBearerService;
//            case 0x51:
//                return BearerServiceCodeValue.PacketAccess2_4kbps;
//            case 0x52:
//                return BearerServiceCodeValue.PacketAccess4_8kbps;
//            case 0x53:
//                return BearerServiceCodeValue.PacketAccess9_5kbps;
//
//            case 0x61:
//                return BearerServiceCodeValue.AlternateSpeechData;
//            case 0x70:
//                return BearerServiceCodeValue.GPRS;
//            case 0x81:
//                return BearerServiceCodeValue.SpeechFollowedByData;
//
//            default:
//                return null;
//        }
    }
}
