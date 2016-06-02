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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 *
 * @author sergey vetyutnev
 * @author kiss.balazs@alerant.hu
 *
 */
public class SpecializedResourceReportRequestImpl extends CircuitSwitchedCallMessageImpl implements
        SpecializedResourceReportRequest {

    private static final String ALL_ANNOUNCEMENTS_COMPLETE = "allAnnouncementsComplete";
    private static final String FIRST_ANNOUNCEMENT_STARTED = "firstAnnouncementStarted";
    private static final String LINKED_ID = "linkedId";

    public static final int _ID_allAnnouncementsComplete = 50;
    public static final int _ID_firstAnnouncementStarted = 51;

    public static final String _PrimitiveName = "SpecializedResourceReportRequestIndication";

    private Long linkedId;
    private Invoke linkedInvoke;

    private boolean isAllAnnouncementsComplete;
    private boolean isFirstAnnouncementStarted;

    private boolean isCAPVersion4orLater;

    public SpecializedResourceReportRequestImpl() {
    }

    public SpecializedResourceReportRequestImpl(boolean isCAPVersion4orLater) {
        this.isCAPVersion4orLater = isCAPVersion4orLater;
    }

    public SpecializedResourceReportRequestImpl(boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted,
            boolean isCAPVersion4orLater) {
        this.isAllAnnouncementsComplete = isAllAnnouncementsComplete;
        this.isFirstAnnouncementStarted = isFirstAnnouncementStarted;
        this.isCAPVersion4orLater = isCAPVersion4orLater;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.specializedResourceReport_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.specializedResourceReport;
    }

    @Override
    public Long getLinkedId() {
        return linkedId;
    }

    @Override
    public void setLinkedId(Long val) {
        linkedId = val;
    }

    @Override
    public Invoke getLinkedInvoke() {
        return linkedInvoke;
    }

    @Override
    public void setLinkedInvoke(Invoke val) {
        linkedInvoke = val;
    }

    @Override
    public boolean getAllAnnouncementsComplete() {
        return isAllAnnouncementsComplete;
    }

    @Override
    public boolean getFirstAnnouncementStarted() {
        return isFirstAnnouncementStarted;
    }

    @Override
    public int getTag() throws CAPException {
        if (this.isCAPVersion4orLater) {
            if (this.isAllAnnouncementsComplete)
                return _ID_allAnnouncementsComplete;
            else
                return _ID_firstAnnouncementStarted;
        } else {
            return Tag.NULL;
        }
    }

    @Override
    public int getTagClass() {
        if (this.isCAPVersion4orLater) {
            return Tag.CLASS_CONTEXT_SPECIFIC;
        } else {
            return Tag.CLASS_UNIVERSAL;
        }
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
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

        this.isAllAnnouncementsComplete = false;
        this.isFirstAnnouncementStarted = false;

        if (this.isCAPVersion4orLater) {
            switch (ansIS.getTag()) {
                case _ID_allAnnouncementsComplete:
                    this.isAllAnnouncementsComplete = true;
                    break;
                case _ID_firstAnnouncementStarted:
                    this.isFirstAnnouncementStarted = true;
                    break;
                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": bad tag for CAP V4 or later.", CAPParsingComponentExceptionReason.MistypedParameter);
            }

        } else {
            if (ansIS.getTagClass() != Tag.CLASS_UNIVERSAL)
                throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": bad tag class for CAP V2 or V3. It must must be UNIVERSAL",
                        CAPParsingComponentExceptionReason.MistypedParameter);
        }

        ansIS.readNullData(length);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.isCAPVersion4orLater) {
            if (this.isAllAnnouncementsComplete && this.isFirstAnnouncementStarted || !this.isAllAnnouncementsComplete
                    && !this.isFirstAnnouncementStarted)
                throw new CAPException("Error while encoding " + _PrimitiveName
                        + ": only one of choice must be selected when CAP V4 or later");
        }

        asnOs.writeNullData();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        this.addInvokeIdInfo(sb);

        if (this.linkedId != null) {
            sb.append(", linkedId=");
            sb.append(this.linkedId);
        }
        if (this.isAllAnnouncementsComplete) {
            sb.append(", isAllAnnouncementsComplete");
        }
        if (this.isFirstAnnouncementStarted) {
            sb.append(", isFirstAnnouncementStarted");
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<SpecializedResourceReportRequestImpl> SPECIALIZED_RESOURCE_REPORT_XML = new XMLFormat<SpecializedResourceReportRequestImpl>(
            SpecializedResourceReportRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                SpecializedResourceReportRequestImpl specializedResourceReportRequest) throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, specializedResourceReportRequest);
            Boolean annComp = xml.get(ALL_ANNOUNCEMENTS_COMPLETE, Boolean.class);
            Boolean fnnComp = xml.get(FIRST_ANNOUNCEMENT_STARTED, Boolean.class);

            if (annComp != null && annComp) {
                specializedResourceReportRequest.isAllAnnouncementsComplete = true;
                specializedResourceReportRequest.isCAPVersion4orLater = true;
            }
            if (fnnComp != null && fnnComp) {
                specializedResourceReportRequest.isFirstAnnouncementStarted = true;
                specializedResourceReportRequest.isCAPVersion4orLater = true;
            }
            specializedResourceReportRequest.linkedId = xml.get(LINKED_ID, Long.class);
        }

        @Override
        public void write(SpecializedResourceReportRequestImpl specializedResourceReportRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(specializedResourceReportRequest, xml);
            if (specializedResourceReportRequest.getAllAnnouncementsComplete()) {
                xml.add(true, ALL_ANNOUNCEMENTS_COMPLETE, Boolean.class);
            } else {
                xml.add(true, FIRST_ANNOUNCEMENT_STARTED, Boolean.class);
            }
            if (specializedResourceReportRequest.getLinkedId() != null) {
                xml.add(specializedResourceReportRequest.getLinkedId(), LINKED_ID, Long.class);
            }
        }

    };

}
