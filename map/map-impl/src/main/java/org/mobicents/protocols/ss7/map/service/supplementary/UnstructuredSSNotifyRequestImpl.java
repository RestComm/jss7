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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * @author amit bhayani
 *
 */
public class UnstructuredSSNotifyRequestImpl extends SupplementaryMessageImpl implements UnstructuredSSNotifyRequest {

    private static final String MSISDN = "msisdn";
    private static final String ALERTING_PATTERN = "alertingPattern";

    private static final int _TAG_MSISDN = 0;

    private ISDNAddressString msisdnAddressString = null;
    private AlertingPattern alertingPattern = null;

    /**
     * @param ussdDataCodingSch
     * @param ussdString
     */
    public UnstructuredSSNotifyRequestImpl() {
        super();
    }

    public UnstructuredSSNotifyRequestImpl(CBSDataCodingScheme ussdDataCodingSch, USSDString ussdString,
            AlertingPattern alertingPattern, ISDNAddressString msisdnAddressString) {
        super(ussdDataCodingSch, ussdString);
        this.alertingPattern = alertingPattern;
        this.msisdnAddressString = msisdnAddressString;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.unstructuredSSNotify_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.unstructuredSS_Notify;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
     * ProcessUnstructuredSSRequestIndication#getMSISDNAddressString()
     */
    public ISDNAddressString getMSISDNAddressString() {
        return this.msisdnAddressString;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
     * ProcessUnstructuredSSRequestIndication#getAlertingPattern()
     */
    public AlertingPattern getAlertingPattern() {
        return this.alertingPattern;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding UnstructuredSSNotifyIndication: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding UnstructuredSSNotifyIndication: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding UnstructuredSSNotifyIndication: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding UnstructuredSSNotifyIndication: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int tag = ais.readTag();

        // ussd-DataCodingScheme USSD-DataCodingScheme
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding UnstructuredSSNotifyIndication: Parameter ussd-DataCodingScheme bad tag class or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        int length1 = ais.readLength();
        this.ussdDataCodingSch = new CBSDataCodingSchemeImpl(ais.readOctetStringData(length1)[0]);

        tag = ais.readTag();

        // ussd-String USSD-String
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding UnstructuredSSNotifyIndication: Parameter ussd-String bad tag class or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        this.ussdString = new USSDStringImpl(this.ussdDataCodingSch);
        ((USSDStringImpl) this.ussdString).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            switch (tag) {
                case _TAG_MSISDN:
                    // msisdn [0] ISDN-AddressString OPTIONAL
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException(
                                "Error while decoding UnstructuredSSNotifyIndication: Parameter msisdn bad tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    this.msisdnAddressString = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.msisdnAddressString).decodeAll(ais);
                    break;
                default:
                    // alertingPattern AlertingPattern OPTIONAL
                    if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()) {
                        this.alertingPattern = new AlertingPatternImpl();
                        ((AlertingPatternImpl) this.alertingPattern).decodeAll(ais);
                    } else {
                        ais.advanceElement();
                    }
                    break;
            }
        }

    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding UnstructuredSSNotifyIndication", e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ussdString == null)
            throw new MAPException("ussdString must not be null");

        try {
            asnOs.writeOctetString(new byte[] { (byte) this.ussdDataCodingSch.getCode() });

            ((USSDStringImpl) this.ussdString).encodeAll(asnOs);

            if (this.alertingPattern != null) {
                ((AlertingPatternImpl) this.alertingPattern).encodeAll(asnOs);
            }

            if (this.msisdnAddressString != null) {
                ((ISDNAddressStringImpl) this.msisdnAddressString).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MSISDN);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding UnstructuredSSNotifyIndication", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding UnstructuredSSNotifyIndication", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnstructuredSSNotifyRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        sb.append(super.toString());

        if (alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (msisdnAddressString != null) {
            sb.append(", msisdn=");
            sb.append(msisdnAddressString.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<UnstructuredSSNotifyRequestImpl> UNSTRUCTURED_SS_NOTIFY_REQUEST_XML = new XMLFormat<UnstructuredSSNotifyRequestImpl>(
            UnstructuredSSNotifyRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UnstructuredSSNotifyRequestImpl ussdMessage)
                throws XMLStreamException {
            USSD_MESSAGE_XML.read(xml, ussdMessage);
            ussdMessage.msisdnAddressString = xml.get(MSISDN, ISDNAddressStringImpl.class);
            ussdMessage.alertingPattern = xml.get(ALERTING_PATTERN, AlertingPatternImpl.class);

        }

        @Override
        public void write(UnstructuredSSNotifyRequestImpl ussdMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            USSD_MESSAGE_XML.write(ussdMessage, xml);
            xml.add(((ISDNAddressStringImpl) ussdMessage.msisdnAddressString), MSISDN, ISDNAddressStringImpl.class);
            xml.add(((AlertingPatternImpl) ussdMessage.alertingPattern), ALERTING_PATTERN, AlertingPatternImpl.class);
        }
    };

}
