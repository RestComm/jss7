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

package org.mobicents.protocols.ss7.cap.service.sms.primitive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharset;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SMSAddressStringImpl extends AddressStringImpl implements SMSAddressString {

    public SMSAddressStringImpl() {
    }

    public SMSAddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(addressNature, numberingPlan, address);
    }

    @Override
    protected void _testLengthDecode(int length) throws MAPParsingComponentException {
        if (length > 11)
            throw new MAPParsingComponentException(
                    "Error when decoding SMSAddressString: mesage length must not exceed 11",
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    protected void _testLengthEncode() throws MAPException {

        if (this.address == null && this.address.length() > 28)
            throw new MAPException("Error when encoding SMSAddressString: address length must not exceed 28 digits");
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {
        int pos = ansIS.position();
        int nature = ansIS.read();
        ansIS.position(pos);
        AddressNature an = AddressNature.getInstance((nature & NATURE_OF_ADD_IND_MASK) >> 4);

        if (an == AddressNature.reserved) {
            nature = ansIS.read();
            int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);
            this.addressNature = AddressNature.getInstance(natureOfAddInd);
            int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);
            this.numberingPlan = NumberingPlan.getInstance(numbPlanInd);

            byte[] rawAddress;
            try {
                rawAddress = ansIS.readOctetStringData(length - 1);
            } catch (AsnException e) {
                throw new MAPParsingComponentException("AsnException when reading data from ansIS: " + e.getMessage(), e,
                        MAPParsingComponentExceptionReason.MistypedParameter);
            }
            ByteBuffer bb = ByteBuffer.wrap(rawAddress, 0, rawAddress.length);
            GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
            GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
            int totalSeptetCount = (rawAddress.length < 7 ? rawAddress.length : rawAddress.length + 1);
            GSMCharsetDecodingData encodingData = new GSMCharsetDecodingData(totalSeptetCount, 0);
            decoder.setGSMCharsetDecodingData(encodingData);

            CharBuffer bf = decoder.decode(bb);
            this.address = bf.toString();
        } else {
            super._decode(ansIS, length);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.addressNature == AddressNature.reserved) {
            int tpOfAddr = 0x80 + (this.addressNature.getIndicator() << 4) + this.numberingPlan.getIndicator();
            asnOs.write(tpOfAddr);

            GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
            GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
            ByteBuffer bb = null;
            try {
                bb = encoder.encode(CharBuffer.wrap(this.address));
            } catch (CharacterCodingException e) {
                throw new MAPException("CharacterCodingException when encoding SMSAddressString: " + e.getMessage(), e);
            }
            int dataLength = bb.limit();
            byte[] data = new byte[dataLength];
            bb.get(data);

            asnOs.write(data);
        } else {
            super.encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        return "SMSAddressString [AddressNature=" + this.addressNature.toString() + ", NumberingPlan="
                + this.numberingPlan.toString() + ", Address=" + this.address + "]";
    }

}
