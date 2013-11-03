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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 *
 */
public class UnstructuredSSNotifyResponseImpl extends SupplementaryMessageImpl implements UnstructuredSSNotifyResponse,
        MAPAsnPrimitive {

    public MAPDialogSupplementary getMAPDialog() {
        return (MAPDialogSupplementary) super.getMAPDialog();
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.unstructuredSSNotify_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.unstructuredSS_Notify;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        throw new MAPParsingComponentException("UnstructuredSSNotifyResponseIndication has no MAP message primitive",
                MAPParsingComponentExceptionReason.MistypedParameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        throw new MAPParsingComponentException("UnstructuredSSNotifyResponseIndication has no MAP message primitive",
                MAPParsingComponentExceptionReason.MistypedParameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        throw new MAPException("UnstructuredSSNotifyResponseIndication has no MAP message primitive");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        throw new MAPException("UnstructuredSSNotifyResponseIndication has no MAP message primitive");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        throw new MAPException("UnstructuredSSNotifyResponseIndication has no MAP message primitive");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnstructuredSSNotifyResponse [");
        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }
        sb.append(super.toString());
        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<UnstructuredSSNotifyResponseImpl> UNSTRUCTURED_SS_NOTIFY_RESPONSE_XML = new XMLFormat<UnstructuredSSNotifyResponseImpl>(
            UnstructuredSSNotifyResponseImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UnstructuredSSNotifyResponseImpl ussdMessage)
                throws XMLStreamException {
            USSD_MESSAGE_XML.read(xml, ussdMessage);
        }

        @Override
        public void write(UnstructuredSSNotifyResponseImpl ussdMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            USSD_MESSAGE_XML.write(ussdMessage, xml);
        }
    };
}
