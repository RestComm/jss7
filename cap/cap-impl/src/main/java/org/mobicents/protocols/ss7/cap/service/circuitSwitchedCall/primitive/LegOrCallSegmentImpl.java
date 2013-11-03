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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LegOrCallSegment;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Povilas Jurna
 *
 */
public class LegOrCallSegmentImpl implements LegOrCallSegment, CAPAsnPrimitive {

    private static final String CALL_SEGMENT_ID = "callSegmentID";
    private static final String LEG_ID = "legID";

    public static final int _ID_callSegmentID = 0;
    public static final int _ID_legID = 1;

    public static final String _PrimitiveName = "LegOrCallSegment";

    private Integer callSegmentID;
    private LegID legID;

    public LegOrCallSegmentImpl() {
    }

    public LegOrCallSegmentImpl(Integer callSegmentID) {
        this.callSegmentID = callSegmentID;
    }

    public LegOrCallSegmentImpl(LegID legID) {
        this.legID = legID;
    }

    @Override
    public int getTag() throws CAPException {
        if (callSegmentID != null)
            return _ID_callSegmentID;
        else
            return _ID_legID;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (callSegmentID != null)
            return true;
        else
            return false;
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException,
            MAPParsingComponentException, IOException, AsnException, INAPParsingComponentException {

        this.callSegmentID = null;
        this.legID = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_callSegmentID:
                this.callSegmentID = (int) ais.readIntegerData(length);
                break;
            case _ID_legID:
                this.legID = new LegIDImpl();
                ais.readTag();
                ((LegIDImpl) this.legID).decodeAll(ais);
                break;

            default:
                throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                        CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
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
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.callSegmentID == null && this.legID == null)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": both callSegmentID and legID must not be null");

        try {
            if (this.callSegmentID != null) {
                if (this.callSegmentID < 1 || this.callSegmentID > 127)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": callSegmentID must be from 1 to 127");
                aos.writeIntegerData(this.callSegmentID);
            } else {
                ((LegIDImpl) this.legID).encodeAll(aos);
            }
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.callSegmentID != null) {
            sb.append("callSegmentID=");
            sb.append(callSegmentID);
            sb.append(", ");
        }
        if (this.legID != null) {
            sb.append("legID=");
            sb.append(legID.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LegOrCallSegmentImpl> TIME_INFORMATION_XML = new XMLFormat<LegOrCallSegmentImpl>(
            LegOrCallSegmentImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LegOrCallSegmentImpl legOrCallSegment)
                throws XMLStreamException {
            legOrCallSegment.callSegmentID = xml.get(CALL_SEGMENT_ID, Integer.class);
            legOrCallSegment.legID = xml.get(LEG_ID, LegIDImpl.class);
        }

        @Override
        public void write(LegOrCallSegmentImpl legOrCallSegment, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (legOrCallSegment.callSegmentID != null) {
                xml.add(legOrCallSegment.callSegmentID, CALL_SEGMENT_ID, Integer.class);
            }

            if (legOrCallSegment.legID != null) {
                xml.add((LegIDImpl) legOrCallSegment.legID, LEG_ID, LegIDImpl.class);
            }
        }
    };

    @Override
    public Integer getCallSegmentID() {
        return callSegmentID;
    }

    @Override
    public LegID getLegID() {
        return legID;
    }
}
