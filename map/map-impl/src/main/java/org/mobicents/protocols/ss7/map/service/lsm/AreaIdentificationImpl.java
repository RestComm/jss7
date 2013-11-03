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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;
import org.mobicents.protocols.ss7.map.primitives.TbcdString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AreaIdentificationImpl extends OctetStringBase implements AreaIdentification {

    public AreaIdentificationImpl() {
        super(2, 7, "AreaIdentification");
    }

    public AreaIdentificationImpl(byte[] data) {
        super(2, 7, "AreaIdentification", data);
    }

    public AreaIdentificationImpl(AreaType type, int mcc, int mnc, int lac, int Rac_CellId_UtranCellId) throws MAPException {
        super(2, 7, "AreaIdentification");

        if (type == null)
            throw new MAPException("type is undefined");
        if (mcc < 1 || mcc > 999)
            throw new MAPException("Bad mcc value");
        if (mnc < 0 || mnc > 999)
            throw new MAPException("Bad mnc value");

        switch (type) {
            case countryCode:
                this.data = new byte[2];
                break;
            case plmnId:
                this.data = new byte[3];
                break;
            case locationAreaId:
                this.data = new byte[5];
                break;
            case routingAreaId:
                this.data = new byte[6];
                break;
            case cellGlobalId:
            case utranCellId:
                this.data = new byte[7];
                break;
            default:
                throw new MAPException("Bad type value");
        }

        StringBuilder sb = new StringBuilder();
        if (mcc < 100)
            sb.append("0");
        if (mcc < 10)
            sb.append("0");
        sb.append(mcc);

        StringBuilder sb2 = new StringBuilder();
        if (type != AreaType.countryCode) {
            if (mnc < 100) {
                if (mnc < 10)
                    sb2.append("0");
                sb2.append(mnc);
            } else {
                sb.append(mnc % 10);
                sb2.append(mnc / 10);
            }
        }

        AsnOutputStream asnOs = new AsnOutputStream();
        TbcdString.encodeString(asnOs, sb.toString());
        System.arraycopy(asnOs.toByteArray(), 0, this.data, 0, 2);

        if (type != AreaType.countryCode) {
            asnOs = new AsnOutputStream();
            TbcdString.encodeString(asnOs, sb2.toString());
            System.arraycopy(asnOs.toByteArray(), 0, this.data, 2, 1);
        }

        if (type == AreaType.locationAreaId || type == AreaType.routingAreaId || type == AreaType.cellGlobalId) {
            data[3] = (byte) (lac / 256);
            data[4] = (byte) (lac % 256);
        }

        if (type == AreaType.routingAreaId) {
            data[5] = (byte) (Rac_CellId_UtranCellId);
        }

        if (type == AreaType.cellGlobalId) {
            data[5] = (byte) (Rac_CellId_UtranCellId / 256);
            data[6] = (byte) (Rac_CellId_UtranCellId % 256);
        }

        if (type == AreaType.utranCellId) {
            data[3] = (byte) ((Rac_CellId_UtranCellId >> 24) & 0xFF);
            data[4] = (byte) ((Rac_CellId_UtranCellId >> 16) & 0xFF);
            data[5] = (byte) ((Rac_CellId_UtranCellId >> 8) & 0xFF);
            data[6] = (byte) ((Rac_CellId_UtranCellId) & 0xFF);
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getMCC() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 2)
            throw new MAPException("Data length must be at least 2");

        AsnInputStream ansIS = new AsnInputStream(data);
        String res = null;
        try {
            res = TbcdString.decodeString(ansIS, 2);
        } catch (IOException e) {
            throw new MAPException("IOException when decoding TbcdString: " + e.getMessage(), e);
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
        }

        if (res.length() < 3 || res.length() > 4)
            throw new MAPException("Decoded TbcdString must be equal 3 or 4");

        String sMcc = res.substring(0, 3);

        return Integer.parseInt(sMcc);
    }

    public int getMNC() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 3)
            throw new MAPException("Data length must be at least 3");

        AsnInputStream ansIS = new AsnInputStream(data);
        String res = null;
        try {
            res = TbcdString.decodeString(ansIS, 3);
        } catch (IOException e) {
            throw new MAPException("IOException when decoding TbcdString: " + e.getMessage(), e);
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when decoding TbcdString: " + e.getMessage(), e);
        }

        if (res.length() < 5 || res.length() > 6)
            throw new MAPException("Decoded TbcdString must be equal 5 or 6");

        String sMnc;
        if (res.length() == 5) {
            sMnc = res.substring(3);
        } else {
            sMnc = res.substring(4) + res.substring(3, 4);
        }

        return Integer.parseInt(sMnc);
    }

    public int getLac() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 5)
            throw new MAPException("Data length must be at least 5");

        int res = (data[3] & 0xFF) * 256 + (data[4] & 0xFF);
        return res;
    }

    public int getRac() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 6)
            throw new MAPException("Data length must be at least 6");

        int res = (data[5] & 0xFF);
        return res;
    }

    public int getCellId() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 7)
            throw new MAPException("Data length must be at least 7");

        int res = (data[5] & 0xFF) * 256 + (data[6] & 0xFF);
        return res;
    }

    public int getUtranCellId() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length < 7)
            throw new MAPException("Data length must be at least 7");

        int res = ((data[3] & 0xFF) << 24) + ((data[4] & 0xFF) << 16) + ((data[5] & 0xFF) << 8) + (data[6] & 0xFF);
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        try {
            int mcc = this.getMCC();
            sb.append("mcc=");
            sb.append(mcc);
        } catch (MAPException e) {
        }
        try {
            int mnc = this.getMNC();
            sb.append(", mnc=");
            sb.append(mnc);
        } catch (MAPException e) {
        }
        try {
            int lac = this.getLac();
            sb.append(", lac=");
            sb.append(lac);
        } catch (MAPException e) {
        }
        try {
            int rac = this.getRac();
            sb.append(", rac=");
            sb.append(rac);
        } catch (MAPException e) {
        }
        try {
            int cellId = this.getCellId();
            sb.append(", cellId=");
            sb.append(cellId);
        } catch (MAPException e) {
        }
        try {
            int utranCellId = this.getUtranCellId();
            sb.append(", utranCellId=");
            sb.append(utranCellId);
        } catch (MAPException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
