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

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class SendingSideIDImpl implements SendingSideID, CAPAsnPrimitive {

    private static final String SENDING_SIDE_ID = "sendingSideID";

    public static final int _ID_sendingSideID = 0;

    public static final String _PrimitiveName = "SendingSideID";

    public LegType sendingSideID;

    public SendingSideIDImpl() {
    }

    public SendingSideIDImpl(LegType sendingSideID) {
        this.sendingSideID = sendingSideID;
    }

    @Override
    public LegType getSendingSideID() {
        return sendingSideID;
    }

    @Override
    public int getTag() throws CAPException {
        return _ID_sendingSideID;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
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
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.sendingSideID = null;

        if (ansIS.getTag() != _ID_sendingSideID || ansIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                    + ": sendingSideID choice has bad tag oe tagClass, tag=" + ansIS.getTag() + ", tagClass="
                    + ansIS.getTagClass(), CAPParsingComponentExceptionReason.MistypedParameter);

        int i1 = (int) ansIS.readIntegerData(length);
        this.sendingSideID = LegType.getInstance(i1);
        if (this.sendingSideID == null)
            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                    + ": LegType must be leg1 or leg2, but the code = " + i1,
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

        if (this.sendingSideID == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": sendingSideID field must not be null");

        try {
            asnOs.writeIntegerData(this.sendingSideID.getCode());
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.sendingSideID != null) {
            sb.append("sendingSideID=");
            sb.append(sendingSideID.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<SendingSideIDImpl> SENDING_SIDE_ID_XML = new XMLFormat<SendingSideIDImpl>(
            SendingSideIDImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, SendingSideIDImpl sendingSideID) throws XMLStreamException {
            sendingSideID.sendingSideID = LegType.getInstance(xml.get(SENDING_SIDE_ID, Integer.class));
        }

        @Override
        public void write(SendingSideIDImpl sendingSideID, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.add(sendingSideID.sendingSideID.getCode(), SENDING_SIDE_ID, Integer.class);
        }
    };
}
