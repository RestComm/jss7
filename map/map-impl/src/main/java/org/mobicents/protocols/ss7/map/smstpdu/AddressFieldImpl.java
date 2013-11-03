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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharset;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.primitives.TbcdString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AddressFieldImpl implements AddressField {

    private TypeOfNumber typeOfNumber;
    private NumberingPlanIdentification numberingPlanIdentification;
    private String addressValue;

    private AddressFieldImpl() {
    }

    public AddressFieldImpl(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification,
            String addressValue) {
        this.typeOfNumber = typeOfNumber;
        this.numberingPlanIdentification = numberingPlanIdentification;
        this.addressValue = addressValue;
    }

    public static AddressFieldImpl createMessage(InputStream stm) throws MAPException {

        if (stm == null)
            throw new MAPException("Error creating AddressField: stream must not be null");

        AddressFieldImpl res = new AddressFieldImpl();

        try {
            // Address-Length
            int addressLength = stm.read();
            if (addressLength == -1)
                throw new MAPException("Error creating AddressField: Address-Length field not found");
            if (addressLength < 0 || addressLength > 20)
                throw new MAPException(
                        "Error creating AddressField: Address-Length field must be equal from 0 to 20, found: addressLength");

            // Type-of-Address
            int typeOfAddress = stm.read();
            if (typeOfAddress == -1)
                throw new MAPException("Error creating AddressField: Type-of-Address field not found");
            res.typeOfNumber = TypeOfNumber.getInstance((typeOfAddress & 0x70) >> 4);
            res.numberingPlanIdentification = NumberingPlanIdentification.getInstance(typeOfAddress & 0x0F);

            int addressArrayLength = (addressLength + 1) / 2;

            // Address-Value
            if (res.typeOfNumber == TypeOfNumber.Alphanumeric) {
                byte[] rawAddress = new byte[addressArrayLength];
                int dataRead = stm.read(rawAddress);

                ByteBuffer bb = ByteBuffer.wrap(rawAddress, 0, dataRead);
                GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
                GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
                int totalSeptetCount = (addressLength < 14 ? addressArrayLength : addressArrayLength + 1);
                GSMCharsetDecodingData encodingData = new GSMCharsetDecodingData(totalSeptetCount, 0);
                decoder.setGSMCharsetDecodingData(encodingData);

                CharBuffer bf = decoder.decode(bb);
                res.addressValue = bf.toString();
            } else {
                // Address-Value
                res.addressValue = TbcdString.decodeString(stm, addressArrayLength);
            }

        } catch (IOException e) {
            throw new MAPException("IOException when creating AddressField: " + e.getMessage(), e);
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when creating AddressField: " + e.getMessage(), e);
        }

        return res;
    }

    public TypeOfNumber getTypeOfNumber() {
        return this.typeOfNumber;
    }

    public NumberingPlanIdentification getNumberingPlanIdentification() {
        return this.numberingPlanIdentification;
    }

    public String getAddressValue() {
        return this.addressValue;
    }

    public void encodeData(OutputStream stm) throws MAPException {

        if (typeOfNumber == null || numberingPlanIdentification == null || addressValue == null)
            throw new MAPException(
                    "Error encoding AddressFieldImpl: typeOfNumber, addressValue and numberingPlanIdentification fields must not be null");

        try {
            int addrLen = addressValue.length();
            int tpOfAddr = 0x80 + (this.typeOfNumber.getCode() << 4) + this.numberingPlanIdentification.getCode();

            if (this.typeOfNumber == TypeOfNumber.Alphanumeric) {
                GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
                GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
                ByteBuffer bb = encoder.encode(CharBuffer.wrap(this.addressValue));
                int dataLength = bb.limit();
                byte[] data = new byte[dataLength];
                bb.get(data);

                // As per 3GPP TS 23.040 (23040-3a[1].pdf)
                // The Address-Length field is an integer representation of the
                // number of useful semi-octets within the Address-Value field,
                // i.e. excludes any semi octet containing only fill bits.

                // TODO Here we have added flag 0xF0 as filler check. This needs
                // to verify for correctness
                // if ((data[dataLength - 1] & 0xF0) == 0x00) {
                // dataLength = (dataLength * 2) - 1;
                // } else {
                // dataLength = dataLength * 2;
                // }
                int semiOct = addrLen * 2 - (int) (addrLen / 4);

                stm.write(semiOct);
                stm.write(tpOfAddr);
                stm.write(data);
            } else {
                stm.write(addrLen);
                stm.write(tpOfAddr);
                TbcdString.encodeString(stm, addressValue);
            }
        } catch (IOException e) {
            // This can not occur
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AddressField [");

        if (typeOfNumber != null) {
            sb.append("typeOfNumber=");
            sb.append(typeOfNumber);
        }
        if (numberingPlanIdentification != null) {
            sb.append(", numberingPlanIdentification=");
            sb.append(numberingPlanIdentification);
        }
        if (addressValue != null) {
            sb.append(", addressValue=");
            sb.append(addressValue);
        }
        sb.append("]");

        return sb.toString();
    }
}
