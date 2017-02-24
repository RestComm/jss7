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
import org.mobicents.protocols.ss7.cap.api.gap.GapCriteria;
import org.mobicents.protocols.ss7.cap.api.gap.GapIndicators;
import org.mobicents.protocols.ss7.cap.api.gap.GapTreatment;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;
import org.mobicents.protocols.ss7.cap.gap.GapCriteriaImpl;
import org.mobicents.protocols.ss7.cap.gap.GapIndicatorsImpl;
import org.mobicents.protocols.ss7.cap.gap.GapTreatmentImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 */
public class CallGapRequestImpl extends CircuitSwitchedCallMessageImpl implements CallGapRequest {

    public static final int _ID_gapCriteria = 0;
    public static final int _ID_gapIndicators = 1;
    public static final int _ID_controlType = 2;
    public static final int _ID_gapTreatment = 3;
    public static final int _ID_capExtension = 4;

    private static final String IS_CAP_VERSION_3_OR_LATER = "isCAPVersion3orLater";

    public static final String _PrimitiveName = "CallGapRequestIndication";
    private static final String GAP_CRITERIA = "gapCriteria";
    private static final String GAP_INDICATOR = "gapIndicators";
    private static final String CONTROL_TYPE = "controlType";
    private static final String GAP_TREATMENT = "gapTreatment";
    private static final String CAP_EXTENSION = "capExtension";

    private GapCriteria gapCriteria;
    private GapIndicators gapIndicators;
    private ControlType controlType;
    private GapTreatment gapTreatment;
    private CAPExtensions capExtensions;

    public CallGapRequestImpl() {
    }

    private boolean isCAPVersion3orLater;

    public CallGapRequestImpl(boolean isCAPVersion3orLater) {
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    public CallGapRequestImpl(GapCriteria gapCriteria, GapIndicators gapIndicators, ControlType controlType, GapTreatment gapTreatment,
            CAPExtensions capExtension) {
        this.gapCriteria = gapCriteria;
        this.gapIndicators = gapIndicators;
        this.controlType = controlType;
        this.gapTreatment = gapTreatment;
        this.capExtensions = capExtension;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.callGap_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.callGap;
    }

    @Override
    public int getTag() throws CAPException {
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
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.gapCriteria = null;
        this.gapIndicators = null;
        this.controlType = null;
        this.gapTreatment = null;
        this.capExtensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            int i1;

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_gapCriteria: {
                        this.gapCriteria = new GapCriteriaImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((GapCriteriaImpl) this.gapCriteria).decodeAll(ais2);
                        break;
                    }
                    case _ID_gapIndicators: {
                        this.gapIndicators = new GapIndicatorsImpl();
                        ((GapIndicatorsImpl) this.gapIndicators).decodeAll(ais);
                        break;
                    }
                    case _ID_controlType: {
                        i1 = (int) ais.readInteger();
                        this.controlType = ControlType.getInstance(i1);
                        break;
                    }
                    case _ID_gapTreatment: {
                        this.gapTreatment = new GapTreatmentImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((GapTreatmentImpl) this.gapTreatment).decodeAll(ais2);
                        break;
                    }
                    case _ID_capExtension: {
                        this.capExtensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.capExtensions).decodeAll(ais);
                        break;
                    }
                    default: {
                        ais.advanceElement();
                        break;
                    }
                }
            } else {
                ais.advanceElement();
            }
        }

        if (gapCriteria == null) {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter gapCriteria is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } else if (gapIndicators == null) {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter gapIndicators is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
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

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.gapCriteria == null) {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": gapCriteria must not be null");
        }
        if (this.gapIndicators == null) {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": gapIndicators must not be null");
        }

        try {

            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_gapCriteria);
            int pos = asnOs.StartContentDefiniteLength();
            ((GapCriteriaImpl) this.gapCriteria).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);

            ((GapIndicatorsImpl) this.gapIndicators).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gapIndicators);

            if (this.controlType != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_controlType, this.controlType.getCode());
            }
            if (this.gapTreatment != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_gapTreatment);
                int pos2 = asnOs.StartContentDefiniteLength();
                ((GapTreatmentImpl) this.gapTreatment).encodeAll(asnOs);
                asnOs.FinalizeContent(pos2);
            }
            if (this.capExtensions != null) {
                ((CAPExtensionsImpl) this.capExtensions).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_capExtension);
            }
        } catch (IOException ex) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + ex.getMessage(), ex);
        } catch (AsnException ex) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + ex.getMessage(), ex);
        }

    }

    public GapCriteria getGapCriteria() {
        return gapCriteria;
    }

    public GapIndicators getGapIndicators() {
        return gapIndicators;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public GapTreatment getGapTreatment() {
        return gapTreatment;
    }

    public CAPExtensions getExtensions() {
        return capExtensions;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallGapRequestImpl> CALLGAP_REQUEST_XML = new XMLFormat<CallGapRequestImpl>(
            CallGapRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallGapRequestImpl callGapRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, callGapRequest);

            callGapRequest.isCAPVersion3orLater = xml.getAttribute(IS_CAP_VERSION_3_OR_LATER, false);

            callGapRequest.gapCriteria = xml.get(GAP_CRITERIA, GapCriteriaImpl.class);
            callGapRequest.gapIndicators = xml.get(GAP_INDICATOR, GapIndicatorsImpl.class);

            String str = xml.get(CONTROL_TYPE, String.class);
            if (str != null) {
                callGapRequest.controlType = Enum.valueOf(ControlType.class, str);
            }

            callGapRequest.gapTreatment = xml.get(GAP_TREATMENT, GapTreatmentImpl.class);
            callGapRequest.capExtensions = xml.get(CAP_EXTENSION, CAPExtensionsImpl.class);
        }

        @Override
        public void write(CallGapRequestImpl callGapRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(callGapRequest, xml);

            xml.setAttribute(IS_CAP_VERSION_3_OR_LATER, callGapRequest.isCAPVersion3orLater);

            xml.add((GapCriteriaImpl) callGapRequest.getGapCriteria(), GAP_CRITERIA, GapCriteriaImpl.class);
            xml.add((GapIndicatorsImpl) callGapRequest.getGapIndicators(), GAP_INDICATOR, GapIndicatorsImpl.class);

            if (callGapRequest.getControlType() != null) {
                xml.add((String) callGapRequest.getControlType().toString(), CONTROL_TYPE, String.class);
            }
            if (callGapRequest.getGapTreatment() != null) {
                xml.add((GapTreatmentImpl) callGapRequest.getGapTreatment(), GAP_TREATMENT, GapTreatmentImpl.class);
            }
            if (callGapRequest.getExtensions() != null) {
                xml.add((CAPExtensionsImpl) callGapRequest.getExtensions(), CAP_EXTENSION, CAPExtensionsImpl.class);
            }
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        this.addInvokeIdInfo(sb);

        sb.append(", gapCriteria=");
        sb.append(gapCriteria);

        sb.append(", gapIndicators=");
        sb.append(gapIndicators);

        if (this.controlType != null) {
            sb.append(", controlType");
            sb.append(controlType.toString());
        }
        if (this.gapTreatment != null) {
            sb.append(", gapTreatment");
            sb.append(gapTreatment.toString());
        }
        if (this.capExtensions != null) {
            sb.append(", capExtensions");
            sb.append(capExtensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
