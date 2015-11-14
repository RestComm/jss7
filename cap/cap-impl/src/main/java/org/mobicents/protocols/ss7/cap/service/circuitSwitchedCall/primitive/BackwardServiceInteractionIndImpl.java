/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallCompletionTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class BackwardServiceInteractionIndImpl extends SequenceBase implements BackwardServiceInteractionInd {
    public static final int _ID_conferenceTreatmentIndicator = 1;
    public static final int _ID_callCompletionTreatmentIndicator = 2;

    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private CallCompletionTreatmentIndicator callCompletionTreatmentIndicator;

    private static final String CONFERENCE_TREATMENT_INDICATOR = "conferenceTreatmentIndicator";
    private static final String CALL_COMPLETION_TREATMENT_INDICATOR = "callCompletionTreatmentIndicator";

    public BackwardServiceInteractionIndImpl() {
        super("BackwardServiceInteractionInd");
    }

    public BackwardServiceInteractionIndImpl(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
        super("BackwardServiceInteractionInd");

        this.conferenceTreatmentIndicator = conferenceTreatmentIndicator;
        this.callCompletionTreatmentIndicator = callCompletionTreatmentIndicator;
    }

    @Override
    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
        return conferenceTreatmentIndicator;
    }

    @Override
    public CallCompletionTreatmentIndicator getCallCompletionTreatmentIndicator() {
        return callCompletionTreatmentIndicator;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.conferenceTreatmentIndicator = null;
        this.callCompletionTreatmentIndicator = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_conferenceTreatmentIndicator:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".conferenceTreatmentIndicator: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                    byte[] data = ais.readOctetString();
                    if (data == null || data.length == 0)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".conferenceTreatmentIndicator: Parameter length is null", CAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = data[0] & 0xFF;
                    this.conferenceTreatmentIndicator = ConferenceTreatmentIndicator.getInstance(i1);
                    break;
                case _ID_callCompletionTreatmentIndicator:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callCompletionTreatmentIndicator: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                    data = ais.readOctetString();
                    if (data == null || data.length == 0)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callCompletionTreatmentIndicator: Parameter length is null", CAPParsingComponentExceptionReason.MistypedParameter);
                    i1 = data[0] & 0xFF;
                    this.callCompletionTreatmentIndicator = CallCompletionTreatmentIndicator.getInstance(i1);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            if (this.conferenceTreatmentIndicator != null) {
                byte[] data = new byte[] { (byte) this.conferenceTreatmentIndicator.getCode() };
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_conferenceTreatmentIndicator, data);
            }
            if (this.callCompletionTreatmentIndicator != null) {
                byte[] data = new byte[] { (byte) this.callCompletionTreatmentIndicator.getCode() };
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callCompletionTreatmentIndicator, data);
            }

        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<BackwardServiceInteractionIndImpl> BACKWARD_SERVICE_INTERACTION_IND_XML = new XMLFormat<BackwardServiceInteractionIndImpl>(
            BackwardServiceInteractionIndImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, BackwardServiceInteractionIndImpl backwardServiceInteractionInd) throws XMLStreamException {

            String str = xml.get(CONFERENCE_TREATMENT_INDICATOR, String.class);
            if (str != null)
                backwardServiceInteractionInd.conferenceTreatmentIndicator = Enum.valueOf(ConferenceTreatmentIndicator.class, str);
            str = xml.get(CALL_COMPLETION_TREATMENT_INDICATOR, String.class);
            if (str != null)
                backwardServiceInteractionInd.callCompletionTreatmentIndicator = Enum.valueOf(CallCompletionTreatmentIndicator.class, str);
        }

        public void write(BackwardServiceInteractionIndImpl backwardServiceInteractionInd, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (backwardServiceInteractionInd.conferenceTreatmentIndicator != null)
                xml.add(backwardServiceInteractionInd.conferenceTreatmentIndicator.toString(), CONFERENCE_TREATMENT_INDICATOR, String.class);
            if (backwardServiceInteractionInd.callCompletionTreatmentIndicator != null)
                xml.add(backwardServiceInteractionInd.callCompletionTreatmentIndicator.toString(), CALL_COMPLETION_TREATMENT_INDICATOR, String.class);
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.conferenceTreatmentIndicator != null) {
            sb.append("conferenceTreatmentIndicator=");
            sb.append(conferenceTreatmentIndicator.toString());
            sb.append(", ");
        }
        if (this.callCompletionTreatmentIndicator != null) {
            sb.append("callCompletionTreatmentIndicator=");
            sb.append(callCompletionTreatmentIndicator.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
