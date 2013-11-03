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
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.primitives.MiscCallInfoImpl;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class EventReportBCSMRequestImpl extends CircuitSwitchedCallMessageImpl implements EventReportBCSMRequest {

    private static final String EVENT_TYPE_BCSM = "eventTypeBCSM";
    private static final String EVENT_SPECIFIC_INFO_BCSM = "eventSpecificInformationBCSM";
    private static final String LEG_ID = "legID";
    private static final String MISC_CALL_INFO = "miscCallInfo";
    private static final String EXTENSIONS = "extensions";

    public static final int _ID_eventTypeBCSM = 0;
    public static final int _ID_eventSpecificInformationBCSM = 2;
    public static final int _ID_legID = 3;
    public static final int _ID_miscCallInfo = 4;
    public static final int _ID_extensions = 5;

    public static final String _PrimitiveName = "EventReportBCSMRequest";

    private EventTypeBCSM eventTypeBCSM;
    private EventSpecificInformationBCSM eventSpecificInformationBCSM;
    private ReceivingSideID legID;
    private MiscCallInfo miscCallInfo;
    private CAPExtensions extensions;

    public EventReportBCSMRequestImpl() {
    }

    public EventReportBCSMRequestImpl(EventTypeBCSM eventTypeBCSM, EventSpecificInformationBCSM eventSpecificInformationBCSM,
            ReceivingSideID legID, MiscCallInfo miscCallInfo, CAPExtensions extensions) {
        this.eventTypeBCSM = eventTypeBCSM;
        this.eventSpecificInformationBCSM = eventSpecificInformationBCSM;
        this.legID = legID;
        this.miscCallInfo = miscCallInfo;
        this.extensions = extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.eventReportBCSM_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.eventReportBCSM;
    }

    @Override
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    @Override
    public EventSpecificInformationBCSM getEventSpecificInformationBCSM() {
        return eventSpecificInformationBCSM;
    }

    @Override
    public ReceivingSideID getLegID() {
        return legID;
    }

    @Override
    public MiscCallInfo getMiscCallInfo() {
        return miscCallInfo;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            INAPParsingComponentException {

        this.eventTypeBCSM = null;
        this.eventSpecificInformationBCSM = null;
        this.legID = null;
        this.miscCallInfo = null;
        // this.miscCallInfo = new
        // MiscCallInfoImpl(MiscCallInfoMessageType.request, null);
        this.extensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int i1;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_eventTypeBCSM:
                        i1 = (int) ais.readInteger();
                        this.eventTypeBCSM = EventTypeBCSM.getInstance(i1);
                        break;
                    case _ID_eventSpecificInformationBCSM:
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl();
                        ((EventSpecificInformationBCSMImpl) this.eventSpecificInformationBCSM).decodeAll(ais2);
                        break;
                    case _ID_legID:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.legID = new ReceivingSideIDImpl();
                        ((ReceivingSideIDImpl) this.legID).decodeAll(ais2);
                        break;
                    case _ID_miscCallInfo:
                        this.miscCallInfo = new MiscCallInfoImpl();
                        ((MiscCallInfoImpl) this.miscCallInfo).decodeAll(ais);
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

        if (this.eventTypeBCSM == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": eventTypeBCSM is mandatory but not found ", CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.eventTypeBCSM == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": eventTypeBCSM must not be null");

        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_eventTypeBCSM, this.eventTypeBCSM.getCode());

            if (this.eventSpecificInformationBCSM != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_eventSpecificInformationBCSM);
                int pos = aos.StartContentDefiniteLength();
                ((EventSpecificInformationBCSMImpl) this.eventSpecificInformationBCSM).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.legID != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_legID);
                int pos = aos.StartContentDefiniteLength();
                ((ReceivingSideIDImpl) this.legID).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.miscCallInfo != null)
                ((MiscCallInfoImpl) this.miscCallInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_miscCallInfo);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.eventTypeBCSM != null) {
            sb.append("eventTypeBCSM=");
            sb.append(eventTypeBCSM.toString());
        }
        if (this.eventSpecificInformationBCSM != null) {
            sb.append(", eventSpecificInformationBCSM=");
            sb.append(eventSpecificInformationBCSM.toString());
        }
        if (this.legID != null) {
            sb.append(", legID=");
            sb.append(legID.toString());
        }
        if (this.miscCallInfo != null) {
            sb.append(", miscCallInfo=");
            sb.append(miscCallInfo.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<EventReportBCSMRequestImpl> EVENT_REPORT_BCSM_REQUEST_XML = new XMLFormat<EventReportBCSMRequestImpl>(
            EventReportBCSMRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, EventReportBCSMRequestImpl eventReportBCSMRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, eventReportBCSMRequest);

            String str = xml.get(EVENT_TYPE_BCSM, String.class);
            if (str != null)
                eventReportBCSMRequest.eventTypeBCSM = Enum.valueOf(EventTypeBCSM.class, str);

            eventReportBCSMRequest.eventSpecificInformationBCSM = xml.get(EVENT_SPECIFIC_INFO_BCSM,
                    EventSpecificInformationBCSMImpl.class);

            eventReportBCSMRequest.legID = xml.get(LEG_ID, ReceivingSideIDImpl.class);

            eventReportBCSMRequest.miscCallInfo = xml.get(MISC_CALL_INFO, MiscCallInfoImpl.class);
            eventReportBCSMRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
        }

        @Override
        public void write(EventReportBCSMRequestImpl eventReportBCSMRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(eventReportBCSMRequest, xml);

            if (eventReportBCSMRequest.eventTypeBCSM != null) {
                xml.add(eventReportBCSMRequest.eventTypeBCSM.toString(), EVENT_TYPE_BCSM, String.class);
            }

            if (eventReportBCSMRequest.eventSpecificInformationBCSM != null) {
                xml.add((EventSpecificInformationBCSMImpl) eventReportBCSMRequest.eventSpecificInformationBCSM,
                        EVENT_SPECIFIC_INFO_BCSM, EventSpecificInformationBCSMImpl.class);
            }

            if (eventReportBCSMRequest.legID != null) {
                xml.add((ReceivingSideIDImpl) eventReportBCSMRequest.legID, LEG_ID, ReceivingSideIDImpl.class);
            }

            if (eventReportBCSMRequest.miscCallInfo != null) {
                xml.add((MiscCallInfoImpl) eventReportBCSMRequest.miscCallInfo, MISC_CALL_INFO, MiscCallInfoImpl.class);
            }

            if (eventReportBCSMRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) eventReportBCSMRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
        }
    };
}
