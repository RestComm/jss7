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
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MiscCallInfoImpl implements MiscCallInfo, INAPAsnPrimitive {

    private static final String MESSAGE_TYPE = "messageType";
    private static final String DP_ASSIGNMENT = "dpAssignment";

    public static final int _ID_messageType = 0;
    public static final int _ID_dpAssignment = 1;

    public static final String _PrimitiveName = "MiscCallInfo";

    private MiscCallInfoMessageType messageType;
    private MiscCallInfoDpAssignment dpAssignment;

    public MiscCallInfoImpl() {
    }

    public MiscCallInfoImpl(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment) {
        this.messageType = messageType;
        this.dpAssignment = dpAssignment;
    }

    @Override
    public MiscCallInfoMessageType getMessageType() {
        return messageType;
    }

    @Override
    public MiscCallInfoDpAssignment getDpAssignment() {
        return dpAssignment;
    }

    @Override
    public int getTag() throws INAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
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

        this.messageType = null;
        this.dpAssignment = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int i1;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_messageType:
                        i1 = (int) ais.readInteger();
                        this.messageType = MiscCallInfoMessageType.getInstance(i1);
                        break;
                    case _ID_dpAssignment:
                        i1 = (int) ais.readInteger();
                        this.dpAssignment = MiscCallInfoDpAssignment.getInstance(i1);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.messageType == null)
            throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": messageType is mandatory but not found ", INAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws INAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws INAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new INAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws INAPException {

        if (this.messageType == null)
            throw new INAPException("Error while encoding the " + _PrimitiveName + ": messageType must not be empty");

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_messageType, this.messageType.getCode());

            if (this.dpAssignment != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_dpAssignment, this.dpAssignment.getCode());
        } catch (IOException e) {
            throw new INAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new INAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.messageType != null) {
            sb.append("messageType=");
            sb.append(messageType);
        }
        if (this.dpAssignment != null) {
            sb.append(", dpAssignment=");
            sb.append(dpAssignment);
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MiscCallInfoImpl> MISC_CALL_INFO_XML = new XMLFormat<MiscCallInfoImpl>(
            MiscCallInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MiscCallInfoImpl miscCallInfo) throws XMLStreamException {
            Integer integ = xml.get(MESSAGE_TYPE, Integer.class);
            if (integ != null) {
                miscCallInfo.messageType = MiscCallInfoMessageType.getInstance(integ);
            }

            integ = xml.get(DP_ASSIGNMENT, Integer.class);
            if (integ != null) {
                miscCallInfo.dpAssignment = MiscCallInfoDpAssignment.getInstance(integ);
            }
        }

        @Override
        public void write(MiscCallInfoImpl miscCallInfo, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            if (miscCallInfo.messageType != null) {
                xml.add(miscCallInfo.messageType.getCode(), MESSAGE_TYPE, Integer.class);
            }

            if (miscCallInfo.dpAssignment != null) {
                xml.add(miscCallInfo.dpAssignment.getCode(), DP_ASSIGNMENT, Integer.class);
            }
        }
    };
}
