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

package org.mobicents.protocols.ss7.cap.service.sms;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.FCIBCCCAMELsequence1SMSImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 *
 */
public class FurnishChargingInformationSMSRequestImpl extends SmsMessageImpl implements FurnishChargingInformationSMSRequest {

    private static final String FCI_BCC_CAMEL_SEQUENCE1 = "fCIBCCCAMELsequence1";

    public static final String _PrimitiveName = "FurnishChargingInformationSMSRequest";

    private FCIBCCCAMELsequence1SMS FCIBCCCAMELsequence1;

    public FurnishChargingInformationSMSRequestImpl() {
    }

    public FurnishChargingInformationSMSRequestImpl(FCIBCCCAMELsequence1SMS fciBCCCAMELsequence1) {
        this.FCIBCCCAMELsequence1 = fciBCCCAMELsequence1;
    }

    @Override
    public FCIBCCCAMELsequence1SMS getFCIBCCCAMELsequence1() {
        return this.FCIBCCCAMELsequence1;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.furnishChargingInformationSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.furnishChargingInformationSMS;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException,
            AsnException, MAPParsingComponentException {

        this.FCIBCCCAMELsequence1 = null;

        byte[] buf = ansIS.readOctetStringData(length);
        if (buf.length < 5 || buf.length > 255)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data length must be from 5 to 255, found: " + buf.length,
                    CAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = new AsnInputStream(buf);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case FCIBCCCAMELsequence1SMSImpl._ID_FCIBCCCAMELsequence1:
                        this.FCIBCCCAMELsequence1 = new FCIBCCCAMELsequence1SMSImpl();
                        ((FCIBCCCAMELsequence1SMSImpl) this.FCIBCCCAMELsequence1).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.FCIBCCCAMELsequence1 == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": the single choice FCIBCCCAMELsequence1 is not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.FCIBCCCAMELsequence1 == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": FCIBCCCAMELsequence1 must not be null");

        AsnOutputStream aos = new AsnOutputStream();
        ((FCIBCCCAMELsequence1SMSImpl) this.FCIBCCCAMELsequence1).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                FCIBCCCAMELsequence1SMSImpl._ID_FCIBCCCAMELsequence1);

        byte[] buf = aos.toByteArray();
        if (buf.length < 5 || buf.length > 255)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data length must be from 5 to 255, encoded: "
                    + buf.length);

        asnOs.writeOctetStringData(buf);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        this.addInvokeIdInfo(sb);

        if (this.FCIBCCCAMELsequence1 != null) {
            sb.append(", FCIBCCCAMELsequence1=");
            sb.append(FCIBCCCAMELsequence1.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<FurnishChargingInformationSMSRequestImpl> FURNISH_CHARGING_INFORMATION_SMS_REQUEST_XML = new XMLFormat<FurnishChargingInformationSMSRequestImpl>(
            FurnishChargingInformationSMSRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, FurnishChargingInformationSMSRequestImpl fciSmsRequest)
                throws XMLStreamException {
            CAP_MESSAGE_XML.read(xml, fciSmsRequest);

            fciSmsRequest.FCIBCCCAMELsequence1 = xml.get(FCI_BCC_CAMEL_SEQUENCE1, FCIBCCCAMELsequence1SMSImpl.class);
        }

        @Override
        public void write(FurnishChargingInformationSMSRequestImpl fciSmsRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            CAP_MESSAGE_XML.write(fciSmsRequest, xml);

            if (fciSmsRequest.FCIBCCCAMELsequence1 != null)
                xml.add((FCIBCCCAMELsequence1SMSImpl) fciSmsRequest.FCIBCCCAMELsequence1, FCI_BCC_CAMEL_SEQUENCE1,
                        FCIBCCCAMELsequence1SMSImpl.class);
        }
    };
}
