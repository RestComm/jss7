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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class LACImpl implements LAC, MAPAsnPrimitive {

    public static final String _PrimitiveName = "LAC";

    private byte[] data;

    public LACImpl() {
    }

    public LACImpl(byte[] data) {
        this.data = data;
    }

    public LACImpl(int lac) throws MAPException {

        this.data = new byte[2];

        data[0] = (byte) (lac / 256);
        data[1] = (byte) (lac % 256);
    }

    public byte[] getData() {
        return data;
    }

    public int getLac() throws MAPException {

        if (data == null)
            throw new MAPException("Data must not be empty");
        if (data.length != 2)
            throw new MAPException("Data length must be equal 5");

        int res = (data[0] & 0xFF) * 256 + (data[1] & 0xFF);
        return res;
    }

    public int getTag() throws MAPException {
        return Tag.STRING_OCTET;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        try {
            this.data = ansIS.readOctetStringData(length);
            if (this.data.length != 2)
                throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                        + ": the field must contain from 2 to 2 octets. Contains: " + length,
                        MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding LAIFixedLength: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.data == null)
            throw new MAPException("Error while encoding the " + _PrimitiveName + ": data is not defined");
        if (this.data.length != 2)
            throw new MAPException("Error while encoding the " + _PrimitiveName + ": field length must be equal 2");

        asnOs.writeOctetStringData(this.data);
    }

    @Override
    public String toString() {

        int lac = 0;
        boolean goodData = false;

        try {
            lac = this.getLac();
            goodData = true;
        } catch (MAPException e) {
        }

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (goodData) {
            sb.append("Lac=");
            sb.append(lac);
        } else {
            sb.append("Data=");
            sb.append(this.printDataArr());
        }
        sb.append("]");

        return sb.toString();
    }

    private String printDataArr() {
        StringBuilder sb = new StringBuilder();
        if (this.data != null) {
            for (int b : this.data) {
                sb.append(b);
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LACImpl other = (LACImpl) obj;
        if (!Arrays.equals(data, other.data))
            return false;
        return true;
    }
}
