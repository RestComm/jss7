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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharset;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;
import org.mobicents.protocols.ss7.map.primitives.TbcdString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CalledPartyBCDNumberImpl extends OctetStringBase implements CalledPartyBCDNumber {

    private static final String NAI = "nai";
    private static final String NPI = "npi";
    private static final String NUMBER = "number";
    // private static final String IS_EXTENSION = "isExtension";

    private static final String DEFAULT_STRING_VALUE = null;

    protected static final int NO_EXTENSION_MASK = 0x80;
    protected static final int NATURE_OF_ADD_IND_MASK = 0x70;
    protected static final int NUMBERING_PLAN_IND_MASK = 0x0F;

    public CalledPartyBCDNumberImpl() {
        super(1, 41, "CalledPartyBCDNumber");
    }

    public CalledPartyBCDNumberImpl(byte[] data) {
        super(1, 41, "CalledPartyBCDNumber", data);
    }

    public CalledPartyBCDNumberImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address)
            throws CAPException {
        super(1, 41, "CalledPartyBCDNumber");

        this.setParameters(addressNature, numberingPlan, address);
    }

    protected void setParameters(AddressNature addressNature, NumberingPlan numberingPlan, String address) throws CAPException {

        if (addressNature == null || numberingPlan == null || address == null)
            throw new CAPException("Error when encoding " + _PrimitiveName
                    + ": addressNature, numberingPlan or address is empty");

        this._testLengthEncode(address);

        ByteArrayOutputStream stm = new ByteArrayOutputStream();

        int nature = 0x80;
        // if (!isExtension)
        // nature = 0x80;
        // else
        // nature = 0;
        nature = nature | (addressNature.getIndicator() << 4);
        nature = nature | (numberingPlan.getIndicator());
        stm.write(nature);

        if (numberingPlan == NumberingPlan.spare_5) {
            // -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or
            // -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric
            // -- character string. In this case, type-of-number '101'B is used, in accordance
            // -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the
            // -- GSM 7-bit default alphabet definition and the SMS packing rules
            // -- as specified in 3GPP TS 23.038 [15] in this case.

            GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
            GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
            ByteBuffer bb;
            try {
                bb = encoder.encode(CharBuffer.wrap(address));
                int dataLength = bb.limit();
                byte[] data = new byte[dataLength];
                bb.get(data);
                stm.write(data);
            } catch (CharacterCodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                TbcdString.encodeString(stm, address);
            } catch (MAPException e) {
                throw new CAPException(e);
            }
        }

        this.data = stm.toByteArray();
    }

    public byte[] getData() {
        return data;
    }

    public AddressNature getAddressNature() {

        if (this.data == null || this.data.length == 0)
            return null;

        int nature = this.data[0];
        int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);
        return AddressNature.getInstance(natureOfAddInd);
    }

    public NumberingPlan getNumberingPlan() {

        if (this.data == null || this.data.length == 0)
            return null;

        int nature = this.data[0];
        int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);
        return NumberingPlan.getInstance(numbPlanInd);
    }

    public boolean isExtension() {

        if (this.data == null || this.data.length == 0)
            return false;

        int nature = this.data[0];
        if ((nature & NO_EXTENSION_MASK) == 0x80)
            return false;
        else
            return true;
    }

    public String getAddress() {

        if (this.data == null || this.data.length == 0)
            return null;

        try {
            ByteArrayInputStream stm = new ByteArrayInputStream(this.data);
            stm.read();
            if (this.getNumberingPlan() == NumberingPlan.spare_5) {
                // -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or
                // -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric
                // -- character string. In this case, type-of-number '101'B is used, in accordance
                // -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the
                // -- GSM 7-bit default alphabet definition and the SMS packing rules
                // -- as specified in 3GPP TS 23.038 [15] in this case.

                if (data.length == 1)
                    return "";

                int addressLength = this.data.length - 1;
                ByteBuffer bb = ByteBuffer.wrap(this.data, 1, addressLength);
                GSMCharset cs = new GSMCharset(GSMCharset.GSM_CANONICAL_NAME, new String[] {});
                GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
                int totalSeptetCount = addressLength + (addressLength / 8);
                GSMCharsetDecodingData encodingData = new GSMCharsetDecodingData(totalSeptetCount, 0);
                decoder.setGSMCharsetDecodingData(encodingData);

                CharBuffer bf = decoder.decode(bb);
                return bf.toString();
            } else {

                String address = TbcdString.decodeString(stm, this.data.length - 1);
                return address;
            }
        } catch (MAPParsingComponentException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    protected void _testLengthEncode(String address) throws CAPException {

        if (address.length() > 38)
            throw new CAPException("Error when encoding AddressString: address length must not exceed 38 digits");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.getAddressNature() != null) {
            sb.append("addressNature=");
            sb.append(this.getAddressNature());
        }
        if (this.getNumberingPlan() != null) {
            sb.append(", numberingPlan=");
            sb.append(this.getNumberingPlan());
        }
        if (this.getAddress() != null) {
            sb.append(", address=");
            sb.append(this.getAddress());
        }
        if (this.isExtension()) {
            sb.append(", extension");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CalledPartyBCDNumberImpl> CALLED_PARTY_BCD_NUMBER_XML = new XMLFormat<CalledPartyBCDNumberImpl>(
            CalledPartyBCDNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CalledPartyBCDNumberImpl calledPartyBCDNumber)
                throws XMLStreamException {
            try {
                AddressNature addressNature = null;
                NumberingPlan numberingPlan = null;
                String nai = xml.getAttribute(NAI, DEFAULT_STRING_VALUE);
                String npi = xml.getAttribute(NPI, DEFAULT_STRING_VALUE);
                if (nai != null) {
                    addressNature = Enum.valueOf(AddressNature.class, nai);
                }
                if (npi != null) {
                    numberingPlan = Enum.valueOf(NumberingPlan.class, npi);
                }

                calledPartyBCDNumber.setParameters(addressNature, numberingPlan, xml.getAttribute(NUMBER, ""));
            } catch (CAPException e) {
                throw new XMLStreamException("CAPException when CalledPartyBCDNumber data setting", e);
            }
        }

        @Override
        public void write(CalledPartyBCDNumberImpl calledPartyBCDNumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            xml.setAttribute(NUMBER, calledPartyBCDNumber.getAddress());
            xml.setAttribute(NAI, calledPartyBCDNumber.getAddressNature().toString());
            xml.setAttribute(NPI, calledPartyBCDNumber.getNumberingPlan().toString());
            // if (calledPartyBCDNumber.isExtension()) {
            // xml.setAttribute(IS_EXTENSION, true);
            // }
        }
    };
}
