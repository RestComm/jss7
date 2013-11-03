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

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;
import java.nio.charset.Charset;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.smstpdu.SmsTpduImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsSignalInfoImpl implements SmsSignalInfo, MAPAsnPrimitive {

    public static final String _PrimitiveName = "SmsSignalInfo";

    private byte[] data;
    private Charset gsm8Charset;

    public SmsSignalInfoImpl() {
    }

    public SmsSignalInfoImpl(byte[] data, Charset gsm8Charset) {
        this.data = data;
        this.gsm8Charset = gsm8Charset;
    }

    public SmsSignalInfoImpl(SmsTpdu tpdu, Charset gsm8Charset) throws MAPException {
        if (tpdu == null)
            throw new MAPException("SmsTpdu must not be null");

        this.data = tpdu.encodeData();
        this.gsm8Charset = gsm8Charset;
    }

    public byte[] getData() {
        return this.data;
    }

    public SmsTpdu decodeTpdu(boolean mobileOriginatedMessage) throws MAPException {
        return SmsTpduImpl.createInstance(this.data, mobileOriginatedMessage, this.gsm8Charset);
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

    private void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.data == null || this.data.length == 0)
            throw new MAPException("Error when encoding " + _PrimitiveName + ": data is empty");

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SmsSignalInfo [");

        boolean moExists = false;
        try {
            SmsTpdu tpdu = SmsTpduImpl.createInstance(this.data, true, gsm8Charset);
            sb.append("MO case: ");
            sb.append(tpdu.toString());
            moExists = true;
        } catch (MAPException e) {
        }
        try {
            if (moExists)
                sb.append("\n");
            SmsTpdu tpdu = SmsTpduImpl.createInstance(this.data, false, gsm8Charset);
            sb.append("MT case: ");
            sb.append(tpdu.toString());
        } catch (MAPException e) {
        }

        sb.append("]");

        return sb.toString();
    }
}
