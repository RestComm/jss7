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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;

/**
 *
 * @author tamas gyorgyey
 *
 */
public class SplitLegRequestImpl extends CircuitSwitchedCallMessageImpl implements SplitLegRequest {
    private static final long serialVersionUID = 1L;

    public static final int _ID_legToBeSplit = 0;
    public static final int _ID_newCallSegment = 1;
    public static final int _ID_extensions = 2;

    private static final String LEG_TO_BE_SPLIT = "legToBeSplit";
    private static final String NEW_CALL_SEGMENT = "newCallSegment";
    private static final String EXTENSIONS = "extensions";

    public static final String _PrimitiveName = "SplitLegRequest";

    private LegID legToBeSplit;
    private Integer newCallSegment;
    private CAPExtensions extensions;

    public SplitLegRequestImpl() {
    }

    // TODO !

    public SplitLegRequestImpl(LegID legIDToMove, Integer newCallSegment, CAPExtensions extensions) {
        this.legToBeSplit = legIDToMove;
        this.newCallSegment = newCallSegment;
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.splitLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.splitLeg;
    }

    public LegID getLegToBeSplit() {
        return legToBeSplit;
    }

    public Integer getNewCallSegment() {
        return newCallSegment;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (Exception e) {
            throw new CAPParsingComponentException(e.getClass().getSimpleName() + " when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (Exception e) {
            throw new CAPParsingComponentException(e.getClass().getSimpleName() + " when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException,
            INAPParsingComponentException, IOException, AsnException {

        this.legToBeSplit = null;
        this.newCallSegment = null;
        this.extensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_legToBeSplit:
                        this.legToBeSplit = new LegIDImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((LegIDImpl) this.legToBeSplit).decodeAll(ais2);
                        break;
                    case _ID_newCallSegment:
                        this.newCallSegment = (int) ais.readInteger();
                        if (this.newCallSegment < 1 || this.newCallSegment > 127)
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": newCallSegment value must be 1..127, found: " + newCallSegment,
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        break;
                    case _ID_extensions:
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.legToBeSplit == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": legToBeSplit is mandatory but not found ",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

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

    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.legToBeSplit == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": legToBeSplit must not be null");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, this.getIsPrimitive(), _ID_legToBeSplit);
            int pos = aos.StartContentDefiniteLength();
            ((LegIDImpl) this.legToBeSplit).encodeAll(aos);
            aos.FinalizeContent(pos);

            if (this.newCallSegment != null) {
                if (this.newCallSegment < 1 || this.newCallSegment > 127)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": newCallSegment value must be 1..127, found: " + newCallSegment);
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_newCallSegment, this.newCallSegment);
            }

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

        } catch (Exception e) {
            throw new CAPException(e.getClass().getSimpleName() + " when encoding " + _PrimitiveName + ": "
                    + e.getMessage(), e);
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.legToBeSplit != null) {
            sb.append("legToBeSplit=");
            sb.append(legToBeSplit.toString());
        }
        if (this.newCallSegment != null) {
            sb.append(", newCallSegment=");
            sb.append(newCallSegment.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<SplitLegRequestImpl> SPLIT_LEG_XML = new XMLFormat<SplitLegRequestImpl>(
            SplitLegRequestImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, SplitLegRequestImpl splitLegRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, splitLegRequest);

            splitLegRequest.legToBeSplit = xml.get(LEG_TO_BE_SPLIT, LegIDImpl.class);
            if (splitLegRequest.legToBeSplit == null)
                throw new XMLStreamException("Error while decoding " + _PrimitiveName
                        + ": legToBeSplit is mandatory but not found ");

            splitLegRequest.newCallSegment = xml.get(NEW_CALL_SEGMENT, Integer.class);
            splitLegRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
        }

        public void write(SplitLegRequestImpl splitLegRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(splitLegRequest, xml);

            xml.add((LegIDImpl) splitLegRequest.getLegToBeSplit(), LEG_TO_BE_SPLIT, LegIDImpl.class);
            xml.add(splitLegRequest.newCallSegment, NEW_CALL_SEGMENT, Integer.class);
            xml.add((CAPExtensionsImpl) splitLegRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
        }
    };

}
