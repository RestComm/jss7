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
import java.util.ArrayList;

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
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.primitives.BCSMEventImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class RequestReportBCSMEventRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestReportBCSMEventRequest {

    public static final int _ID_bcsmEvents = 0;
    public static final int _ID_extensions = 2;

    public static final String _PrimitiveName = "RequestReportBCSMEventRequest";

    private static final String EXTENSIONS = "extensions";
    private static final String BCSM_EVENT = "bcsmEvent";
    private static final String BCSM_EVENT_LIST = "bcsmEventList";

    private ArrayList<BCSMEvent> bcsmEventList;
    private CAPExtensions extensions;

    public RequestReportBCSMEventRequestImpl() {
    }

    public RequestReportBCSMEventRequestImpl(ArrayList<BCSMEvent> bcsmEventList, CAPExtensions extensions) {
        this.bcsmEventList = bcsmEventList;
        this.extensions = extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.requestReportBCSMEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.requestReportBCSMEvent;
    }

    @Override
    public ArrayList<BCSMEvent> getBCSMEventList() {
        return bcsmEventList;
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

        this.bcsmEventList = null;
        this.extensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_bcsmEvents:
                        this.bcsmEventList = new ArrayList<BCSMEvent>();
                        AsnInputStream ais2 = ais.readSequenceStream();

                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new CAPParsingComponentException(
                                        "Error when decoding RequestReportBCSMEventRequest: bad tag or tagClass or is primitive of BCSMEvent element",
                                        CAPParsingComponentExceptionReason.MistypedParameter);

                            BCSMEventImpl elem = new BCSMEventImpl();
                            elem.decodeAll(ais2);
                            this.bcsmEventList.add(elem);
                        }

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

        if (this.bcsmEventList == null)
            throw new CAPParsingComponentException(
                    "Error while decoding RequestReportBCSMEventRequest: bcsmEventList is mandatory but not found ",
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
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.bcsmEventList == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": bcsmEventList must not be null");
        if (this.bcsmEventList.size() < 1 || this.bcsmEventList.size() > 30)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": bcsmEventList length must be from 1 to 30");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_bcsmEvents);
            int pos = aos.StartContentDefiniteLength();
            for (BCSMEvent be : this.bcsmEventList) {
                BCSMEventImpl bee = (BCSMEventImpl) be;
                bee.encodeAll(aos);
            }
            aos.FinalizeContent(pos);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.bcsmEventList != null) {
            sb.append("bcsmEventList=[");
            boolean firstItem = true;
            for (BCSMEvent be : this.bcsmEventList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
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
    protected static final XMLFormat<RequestReportBCSMEventRequestImpl> REQUEST_REPORT_BCSM_EVENT_REQUEST_XML = new XMLFormat<RequestReportBCSMEventRequestImpl>(
            RequestReportBCSMEventRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                RequestReportBCSMEventRequestImpl requestReportBCSMEventRequest) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, requestReportBCSMEventRequest);

            RequestReportBCSMEventRequest_BCSMEvent al = xml
                    .get(BCSM_EVENT_LIST, RequestReportBCSMEventRequest_BCSMEvent.class);
            if (al != null) {
                requestReportBCSMEventRequest.bcsmEventList = al.getData();
            }

            requestReportBCSMEventRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
        }

        @Override
        public void write(RequestReportBCSMEventRequestImpl requestReportBCSMEventRequest,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(requestReportBCSMEventRequest, xml);

            if (requestReportBCSMEventRequest.getBCSMEventList() != null) {
                RequestReportBCSMEventRequest_BCSMEvent al = new RequestReportBCSMEventRequest_BCSMEvent(
                        requestReportBCSMEventRequest.getBCSMEventList());
                xml.add(al, BCSM_EVENT_LIST, RequestReportBCSMEventRequest_BCSMEvent.class);
            }

            if (requestReportBCSMEventRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) requestReportBCSMEventRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
        }
    };

    public static class RequestReportBCSMEventRequest_BCSMEvent extends ArrayListSerializingBase<BCSMEvent> {

        public RequestReportBCSMEventRequest_BCSMEvent() {
            super(BCSM_EVENT, BCSMEventImpl.class);
        }

        public RequestReportBCSMEventRequest_BCSMEvent(ArrayList<BCSMEvent> data) {
            super(BCSM_EVENT, BCSMEventImpl.class, data);
        }

    }
}
