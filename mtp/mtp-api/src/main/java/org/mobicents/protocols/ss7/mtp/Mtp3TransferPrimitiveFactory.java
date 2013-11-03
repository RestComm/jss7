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
package org.mobicents.protocols.ss7.mtp;

/**
 *
 * @author amit bhayani
 *
 */
public class Mtp3TransferPrimitiveFactory {

    private final RoutingLabelFormat pointCodeFormat;

    public Mtp3TransferPrimitiveFactory(RoutingLabelFormat pointCodeFormat) {
        this.pointCodeFormat = pointCodeFormat;
    }

    public Mtp3TransferPrimitive createMtp3TransferPrimitive(int si, int ni, int mp, int opc, int dpc, int sls, byte[] data) {
        Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data,
                this.pointCodeFormat);
        return mtp3TransferPrimitive;
    }

    public Mtp3TransferPrimitive createMtp3TransferPrimitive(byte[] msg) {
        Mtp3TransferPrimitive mtp3TransferPrimitive = null;

        // sio
        int sio = msg[0];
        int si = sio & 0x0F;
        int ssi = (sio & 0xF0) >> 4;
        int ni = ssi >> 2;
        int mp = ssi & 0x03;

        int dpc = 0;
        int opc = 0;
        int sls = 0;
        byte[] data = null;

        switch (this.pointCodeFormat) {
            case ITU:
                // routing label
                byte b1 = msg[1];
                byte b2 = msg[2];
                byte b3 = msg[3];
                byte b4 = msg[4];
                dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
                opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
                sls = ((b4 & 0xf0) >> 4);

                // msu data
                data = new byte[msg.length - 5];
                System.arraycopy(msg, 5, data, 0, data.length);
                break;
            case ANSI_Sls8Bit:
                dpc = ((msg[3] & 0xff) << 16) | ((msg[2] & 0xff) << 8) | (msg[1] & 0xff);
                opc = ((msg[6] & 0xff) << 16) | ((msg[5] & 0xff) << 8) | (msg[4] & 0xff);
                sls = (msg[7] & 0xff);

                // msu data
                data = new byte[msg.length - 8];
                System.arraycopy(msg, 8, data, 0, data.length);
                break;

            default:
                // TODO : We don't support rest justyet
                break;
        }
        mtp3TransferPrimitive = new Mtp3TransferPrimitive(si, ni, mp, opc, dpc, sls, data, this.pointCodeFormat);
        return mtp3TransferPrimitive;
    }
}
