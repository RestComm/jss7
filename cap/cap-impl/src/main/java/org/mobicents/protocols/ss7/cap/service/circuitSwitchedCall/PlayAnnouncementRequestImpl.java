/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InformationToSendImpl;

/**
 *
 * @author sergey vetyutnev
 * @author kiss.balazs@alerant.hu
 *
 */
public class PlayAnnouncementRequestImpl extends CircuitSwitchedCallMessageImpl implements PlayAnnouncementRequest {

    private static final String INFORMATION_TO_SEND = "informationToSend";
    private static final String DISCONNECT_FROM_IP_FORBIDDEN = "disconnectFromIPForbidden";
    private static final String REQUEST_ANNOUNCEMENT_COMPLETE_NOTIFICATION = "requestAnnouncementCompleteNotification";
    private static final String EXTENSIONS = "extensions";
    private static final String CALL_SEGMENT_ID = "callSegmentID";
    private static final String REQUEST_ANNOUNCEMENT_STARTED_NOTIFICATION = "requestAnnouncementStartedNotification";

    public static final int _ID_informationToSend = 0;
    public static final int _ID_disconnectFromIPForbidden = 1;
    public static final int _ID_requestAnnouncementCompleteNotification = 2;
    public static final int _ID_extensions = 3;
    public static final int _ID_callSegmentID = 5;
    public static final int _ID_requestAnnouncementStartedNotification = 51;

    public static final String _PrimitiveName = "PlayAnnouncementRequestIndication";

    private InformationToSend informationToSend;
    private Boolean disconnectFromIPForbidden;
    private Boolean requestAnnouncementCompleteNotification;
    private CAPExtensions extensions;
    private Integer callSegmentID;
    private Boolean requestAnnouncementStartedNotification;

    public PlayAnnouncementRequestImpl() {
    }

    public PlayAnnouncementRequestImpl(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) {
        this.informationToSend = informationToSend;
        this.disconnectFromIPForbidden = disconnectFromIPForbidden;
        this.requestAnnouncementCompleteNotification = requestAnnouncementCompleteNotification;
        this.extensions = extensions;
        this.callSegmentID = callSegmentID;
        this.requestAnnouncementStartedNotification = requestAnnouncementStartedNotification;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.playAnnouncement_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.playAnnouncement;
    }

    @Override
    public InformationToSend getInformationToSend() {
        return informationToSend;
    }

    @Override
    public Boolean getDisconnectFromIPForbidden() {
        return disconnectFromIPForbidden;
    }

    @Override
    public Boolean getRequestAnnouncementCompleteNotification() {
        return requestAnnouncementCompleteNotification;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public Integer getCallSegmentID() {
        return callSegmentID;
    }

    @Override
    public Boolean getRequestAnnouncementStartedNotification() {
        return requestAnnouncementStartedNotification;
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
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.informationToSend = null;
        this.disconnectFromIPForbidden = null;
        this.requestAnnouncementCompleteNotification = null;
        this.extensions = null;
        this.callSegmentID = null;
        this.requestAnnouncementStartedNotification = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_informationToSend:
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.informationToSend = new InformationToSendImpl();
                        ((InformationToSendImpl) this.informationToSend).decodeAll(ais2);
                        break;
                    case _ID_disconnectFromIPForbidden:
                        this.disconnectFromIPForbidden = ais.readBoolean();
                        break;
                    case _ID_requestAnnouncementCompleteNotification:
                        this.requestAnnouncementCompleteNotification = ais.readBoolean();
                        break;
                    case _ID_extensions:
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                        break;
                    case _ID_callSegmentID:
                        this.callSegmentID = (int) ais.readInteger();
                        break;
                    case _ID_requestAnnouncementStartedNotification:
                        this.requestAnnouncementStartedNotification = ais.readBoolean();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.informationToSend == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter informationToSend is mandatory but not found",
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

        if (this.informationToSend == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": informationToSend must not be null");

        try {

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_informationToSend);
            int pos = aos.StartContentDefiniteLength();
            ((InformationToSendImpl) this.informationToSend).encodeAll(aos);
            aos.FinalizeContent(pos);

            if (this.disconnectFromIPForbidden != null)
                aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_disconnectFromIPForbidden, this.disconnectFromIPForbidden);
            if (this.requestAnnouncementCompleteNotification != null)
                aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requestAnnouncementCompleteNotification,
                        this.requestAnnouncementCompleteNotification);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

            if (this.callSegmentID != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callSegmentID, this.callSegmentID);
            if (this.requestAnnouncementStartedNotification != null)
                aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requestAnnouncementStartedNotification,
                        this.requestAnnouncementStartedNotification);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        this.addInvokeIdInfo(sb);

        if (this.informationToSend != null) {
            sb.append(", informationToSend=");
            sb.append(informationToSend.toString());
        }
        if (this.disconnectFromIPForbidden != null) {
            sb.append(", disconnectFromIPForbidden=");
            sb.append(disconnectFromIPForbidden);
        }
        if (this.requestAnnouncementCompleteNotification != null) {
            sb.append(", requestAnnouncementCompleteNotification=");
            sb.append(requestAnnouncementCompleteNotification);
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID);
        }
        if (this.requestAnnouncementStartedNotification != null) {
            sb.append(", requestAnnouncementStartedNotification=");
            sb.append(requestAnnouncementStartedNotification);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<PlayAnnouncementRequestImpl> PLAY_ANNOUNCEMENT_REQUEST_XML = new XMLFormat<PlayAnnouncementRequestImpl>(
            PlayAnnouncementRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, PlayAnnouncementRequestImpl playAnnouncementRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, playAnnouncementRequest);

            playAnnouncementRequest.informationToSend = xml.get(INFORMATION_TO_SEND, InformationToSendImpl.class);
            playAnnouncementRequest.disconnectFromIPForbidden = xml.get(DISCONNECT_FROM_IP_FORBIDDEN, Boolean.class);
            playAnnouncementRequest.requestAnnouncementCompleteNotification = xml.get(REQUEST_ANNOUNCEMENT_COMPLETE_NOTIFICATION,
                    Boolean.class);

            playAnnouncementRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            playAnnouncementRequest.callSegmentID = xml.get(CALL_SEGMENT_ID, Integer.class);

            playAnnouncementRequest.requestAnnouncementStartedNotification = xml.get(REQUEST_ANNOUNCEMENT_STARTED_NOTIFICATION, Boolean.class);
        }

        @Override
        public void write(PlayAnnouncementRequestImpl playAnnoucnementRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(playAnnoucnementRequest, xml);
            xml.add((InformationToSendImpl) playAnnoucnementRequest.getInformationToSend(), INFORMATION_TO_SEND,
                    InformationToSendImpl.class);

            if (playAnnoucnementRequest.getDisconnectFromIPForbidden() != null) {
                xml.add(playAnnoucnementRequest.getDisconnectFromIPForbidden(), DISCONNECT_FROM_IP_FORBIDDEN, Boolean.class);
            }

            if (playAnnoucnementRequest.getRequestAnnouncementCompleteNotification() != null) {
                xml.add(playAnnoucnementRequest.getRequestAnnouncementCompleteNotification(),
                        REQUEST_ANNOUNCEMENT_COMPLETE_NOTIFICATION, Boolean.class);
            }

            if (playAnnoucnementRequest.getExtensions() != null) {
                xml.add((CAPExtensionsImpl) playAnnoucnementRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
            }
            if (playAnnoucnementRequest.getCallSegmentID() != null) {
                xml.add(playAnnoucnementRequest.getCallSegmentID(), CALL_SEGMENT_ID, Integer.class);
            }

            if (playAnnoucnementRequest.getRequestAnnouncementStartedNotification() != null) {
                xml.add(playAnnoucnementRequest.getRequestAnnouncementStartedNotification(),
                        REQUEST_ANNOUNCEMENT_STARTED_NOTIFICATION, Boolean.class);
            }
        }

    };

}
