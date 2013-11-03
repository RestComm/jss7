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
public enum BearerServiceCodeValue {

    PlmnSpecificBearerServices(-1),

    AllServices(0),

    AsynchronousGeneralBearerService(0x20), Asynchronous300bps(0x21), Asynchronous1_2kbps(0x22), Asynchronous1200_75bps(0x23), Asynchronous2_4kbps(
            0x24), Asynchronous4_8kbps(0x25), Asynchronous9_6kbps(0x26),

    SynchronousGeneralBearerService(0x30), Synchronous1_2kbps(0x31), Synchronous2_4kbps(0x32), Synchronous4_8kbps(0x33), Synchronous9_6kbps(
            0x34),

    GeneralPADAccessBearerService(0x40), PADAccess300bps(0x41), PADAccess1_2kbps(0x42), PADAccess1200_75bps(0x43), PADAccess2_4kbps(
            0x44), PADAccess4_8kbps(0x45), PADAccess9_6kbps(0x46),

    GeneralPacketAccessBearerService(0x50), PacketAccess2_4kbps(0x51), PacketAccess4_8kbps(0x52), PacketAccess9_5kbps(0x53),

    AlternateSpeechData(0x61),

    GPRS(0x70),

    SpeechFollowedByData(0x81);

    private int code;

    private BearerServiceCodeValue(int code) {

        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public int getBearerServiceCode() {
        if (this.code == -1)
            return -1;
        else
            return ((this.code & 0xF0) >> 1) | (this.code & 0x07);
    }

    public static BearerServiceCodeValue getInstance(int code) {

        if (code >= 128)
            return BearerServiceCodeValue.PlmnSpecificBearerServices;

        code = ((code & 0x78) << 1) | (code & 0x07);

        switch (code) {
            case 0:
                return BearerServiceCodeValue.AllServices;

            case 0x20:
                return BearerServiceCodeValue.AsynchronousGeneralBearerService;
            case 0x21:
                return BearerServiceCodeValue.Asynchronous300bps;
            case 0x22:
                return BearerServiceCodeValue.Asynchronous1_2kbps;
            case 0x23:
                return BearerServiceCodeValue.Asynchronous1200_75bps;
            case 0x24:
                return BearerServiceCodeValue.Asynchronous2_4kbps;
            case 0x25:
                return BearerServiceCodeValue.Asynchronous4_8kbps;
            case 0x26:
                return BearerServiceCodeValue.Asynchronous9_6kbps;

            case 0x30:
                return BearerServiceCodeValue.SynchronousGeneralBearerService;
            case 0x31:
                return BearerServiceCodeValue.Synchronous1_2kbps;
            case 0x32:
                return BearerServiceCodeValue.Synchronous2_4kbps;
            case 0x33:
                return BearerServiceCodeValue.Synchronous4_8kbps;
            case 0x34:
                return BearerServiceCodeValue.Synchronous9_6kbps;

            case 0x40:
                return BearerServiceCodeValue.GeneralPADAccessBearerService;
            case 0x41:
                return BearerServiceCodeValue.PADAccess300bps;
            case 0x42:
                return BearerServiceCodeValue.PADAccess1_2kbps;
            case 0x43:
                return BearerServiceCodeValue.PADAccess1200_75bps;
            case 0x44:
                return BearerServiceCodeValue.PADAccess2_4kbps;
            case 0x45:
                return BearerServiceCodeValue.PADAccess4_8kbps;
            case 0x46:
                return BearerServiceCodeValue.PADAccess9_6kbps;

            case 0x50:
                return BearerServiceCodeValue.GeneralPacketAccessBearerService;
            case 0x51:
                return BearerServiceCodeValue.PacketAccess2_4kbps;
            case 0x52:
                return BearerServiceCodeValue.PacketAccess4_8kbps;
            case 0x53:
                return BearerServiceCodeValue.PacketAccess9_5kbps;

            case 0x61:
                return BearerServiceCodeValue.AlternateSpeechData;
            case 0x70:
                return BearerServiceCodeValue.GPRS;
            case 0x81:
                return BearerServiceCodeValue.SpeechFollowedByData;

            default:
                return null;
        }
    }
}
