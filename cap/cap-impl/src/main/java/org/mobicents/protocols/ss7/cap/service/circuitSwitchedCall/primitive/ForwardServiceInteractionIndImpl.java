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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallingPartyRestrictionIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class ForwardServiceInteractionIndImpl extends SequenceBase implements ForwardServiceInteractionInd {
    public static final int _ID_conferenceTreatmentIndicator = 1;
    public static final int _ID_callDiversionTreatmentIndicator = 2;
    public static final int _ID_callingPartyRestrictionIndicator = 4;

    private ConferenceTreatmentIndicator conferenceTreatmentIndicator;
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    private CallingPartyRestrictionIndicator callingPartyRestrictionIndicator;

    private static final String CONFERENCE_TREATMENT_INDICATOR = "conferenceTreatmentIndicator";
    private static final String CALL_DIVERSION_TREATMENT_INDICATOR = "callDiversionTreatmentIndicator";
    private static final String CALLING_PARTY_RESTRICTION_INDICATOR = "callingPartyRestrictionIndicator";

    public ForwardServiceInteractionIndImpl() {
        super("ForwardServiceInteractionInd");
    }

    public ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
        super("ForwardServiceInteractionInd");

        this.conferenceTreatmentIndicator = conferenceTreatmentIndicator;
        this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
        this.callingPartyRestrictionIndicator = callingPartyRestrictionIndicator;
    }

    @Override
    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
        return conferenceTreatmentIndicator;
    }

    @Override
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
        return callDiversionTreatmentIndicator;
    }

    @Override
    public CallingPartyRestrictionIndicator getCallingPartyRestrictionIndicator() {
        return callingPartyRestrictionIndicator;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.conferenceTreatmentIndicator = null;
        this.callDiversionTreatmentIndicator = null;
        this.callingPartyRestrictionIndicator = null;

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
                case _ID_callDiversionTreatmentIndicator:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callDiversionTreatmentIndicator: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                    data = ais.readOctetString();
                    if (data == null || data.length == 0)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callDiversionTreatmentIndicator: Parameter length is null", CAPParsingComponentExceptionReason.MistypedParameter);
                    i1 = data[0] & 0xFF;
                    this.callDiversionTreatmentIndicator = CallDiversionTreatmentIndicator.getInstance(i1);
                    break;
                case _ID_callingPartyRestrictionIndicator:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callingPartyRestrictionIndicator: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                    data = ais.readOctetString();
                    if (data == null || data.length == 0)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callingPartyRestrictionIndicator: Parameter length is null", CAPParsingComponentExceptionReason.MistypedParameter);
                    i1 = data[0] & 0xFF;
                    this.callingPartyRestrictionIndicator = CallingPartyRestrictionIndicator.getInstance(i1);
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
            if (this.callDiversionTreatmentIndicator != null) {
                byte[] data = new byte[] { (byte) this.callDiversionTreatmentIndicator.getCode() };
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callDiversionTreatmentIndicator, data);
            }
            if (this.callingPartyRestrictionIndicator != null) {
                byte[] data = new byte[] { (byte) this.callingPartyRestrictionIndicator.getCode() };
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callingPartyRestrictionIndicator, data);
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
    protected static final XMLFormat<ForwardServiceInteractionIndImpl> FORWARD_SERVICE_INTERACTION_IND_XML = new XMLFormat<ForwardServiceInteractionIndImpl>(
            ForwardServiceInteractionIndImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, ForwardServiceInteractionIndImpl forwardServiceInteractionInd) throws XMLStreamException {

            String str = xml.get(CONFERENCE_TREATMENT_INDICATOR, String.class);
            if (str != null)
                forwardServiceInteractionInd.conferenceTreatmentIndicator = Enum.valueOf(ConferenceTreatmentIndicator.class, str);
            str = xml.get(CALL_DIVERSION_TREATMENT_INDICATOR, String.class);
            if (str != null)
                forwardServiceInteractionInd.callDiversionTreatmentIndicator = Enum.valueOf(CallDiversionTreatmentIndicator.class, str);
            str = xml.get(CALLING_PARTY_RESTRICTION_INDICATOR, String.class);
            if (str != null)
                forwardServiceInteractionInd.callingPartyRestrictionIndicator = Enum.valueOf(CallingPartyRestrictionIndicator.class, str);
        }

        public void write(ForwardServiceInteractionIndImpl forwardServiceInteractionInd, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (forwardServiceInteractionInd.conferenceTreatmentIndicator != null)
                xml.add(forwardServiceInteractionInd.conferenceTreatmentIndicator.toString(), CONFERENCE_TREATMENT_INDICATOR, String.class);
            if (forwardServiceInteractionInd.callDiversionTreatmentIndicator != null)
                xml.add(forwardServiceInteractionInd.callDiversionTreatmentIndicator.toString(), CALL_DIVERSION_TREATMENT_INDICATOR, String.class);
            if (forwardServiceInteractionInd.callingPartyRestrictionIndicator != null)
                xml.add(forwardServiceInteractionInd.callingPartyRestrictionIndicator.toString(), CALLING_PARTY_RESTRICTION_INDICATOR, String.class);
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
        if (this.callDiversionTreatmentIndicator != null) {
            sb.append("callDiversionTreatmentIndicator=");
            sb.append(callDiversionTreatmentIndicator.toString());
            sb.append(", ");
        }
        if (this.callingPartyRestrictionIndicator != null) {
            sb.append("callingPartyRestrictionIndicator=");
            sb.append(callingPartyRestrictionIndicator.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
