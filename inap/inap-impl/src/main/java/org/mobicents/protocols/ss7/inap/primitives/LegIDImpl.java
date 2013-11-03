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

package org.mobicents.protocols.ss7.inap.primitives;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LegIDImpl implements LegID, INAPAsnPrimitive {

    public static final int _ID_sendingSideID = 0;
    public static final int _ID_receivingSideID = 1;

    private static final String SENDING_SIDE_ID = "sendingSideID";
    private static final String RECEIVING_SIDE_ID = "receivingSideID";

    private static final String DEFAULT_STRING_VALUE = null;

    public static final String _PrimitiveName = "LegID";

    private LegType sendingSideID;
    private LegType receivingSideID;

    public LegIDImpl() {
    }

    public LegIDImpl(boolean isSendingSideID, LegType legID) {
        if (isSendingSideID)
            this.sendingSideID = legID;
        else
            this.receivingSideID = legID;
    }

    @Override
    public LegType getSendingSideID() {
        return sendingSideID;
    }

    @Override
    public LegType getReceivingSideID() {
        return receivingSideID;
    }

    @Override
    public int getTag() throws INAPException {
        if (this.sendingSideID != null) {
            return _ID_sendingSideID;
        } else {
            return _ID_receivingSideID;
        }
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
    public void decodeAll(AsnInputStream ansIS) throws INAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws INAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    INAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream asnIS, int length) throws INAPParsingComponentException, IOException, AsnException {

        if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
            throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
                    INAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf;
        switch (asnIS.getTag()) {
            case _ID_sendingSideID:
                buf = asnIS.readOctetStringData(length);
                if (buf.length != 1)
                    throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": sendingSideID length must be 1 but it equals " + buf.length,
                            INAPParsingComponentExceptionReason.MistypedParameter);
                this.sendingSideID = LegType.getInstance(buf[0]);
                if (this.sendingSideID == null)
                    throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": sendingSideID value must be 1 or 2 it equals " + buf[0],
                            INAPParsingComponentExceptionReason.MistypedParameter);
                break;
            case _ID_receivingSideID:
                buf = asnIS.readOctetStringData(length);
                if (buf.length != 1)
                    throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": sendingSideID length must be 1 but it equals " + buf.length,
                            INAPParsingComponentExceptionReason.MistypedParameter);
                this.receivingSideID = LegType.getInstance(buf[0]);
                if (this.receivingSideID == null)
                    throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": sendingSideID value must be 1 or 2 it equals " + buf[0],
                            INAPParsingComponentExceptionReason.MistypedParameter);
                break;
            default:
                throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag : tag="
                        + asnIS.getTag(), INAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws INAPException {

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws INAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new INAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws INAPException {

        if (this.sendingSideID == null && this.receivingSideID == null || this.sendingSideID != null
                && this.receivingSideID != null)
            throw new INAPException("Error while encoding the " + _PrimitiveName
                    + ": one of sendingSideID or receivingSideID (not both) must not be empty");

        byte[] buf = new byte[1];
        if (this.sendingSideID != null)
            buf[0] = (byte) sendingSideID.getCode();
        else
            buf[0] = (byte) receivingSideID.getCode();
        asnOs.writeOctetStringData(buf);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("LegID [");
        if (this.sendingSideID != null) {
            sb.append("sendingSideID=");
            sb.append(sendingSideID);
        }
        if (this.receivingSideID != null) {
            sb.append("receivingSideID=");
            sb.append(receivingSideID);
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LegIDImpl> LEG_ID_XML = new XMLFormat<LegIDImpl>(LegIDImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LegIDImpl legId) throws XMLStreamException {
            String sendingSideID = xml.getAttribute(SENDING_SIDE_ID, DEFAULT_STRING_VALUE);
            if (sendingSideID != null) {
                legId.sendingSideID = Enum.valueOf(LegType.class, sendingSideID);
            }

            String receivingSideID = xml.getAttribute(RECEIVING_SIDE_ID, DEFAULT_STRING_VALUE);
            if (receivingSideID != null) {
                legId.receivingSideID = Enum.valueOf(LegType.class, receivingSideID);
            }
        }

        @Override
        public void write(LegIDImpl legId, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (legId.sendingSideID != null)
                xml.setAttribute(SENDING_SIDE_ID, legId.sendingSideID.toString());
            if (legId.receivingSideID != null)
                xml.setAttribute(RECEIVING_SIDE_ID, legId.receivingSideID.toString());
        }
    };
}
