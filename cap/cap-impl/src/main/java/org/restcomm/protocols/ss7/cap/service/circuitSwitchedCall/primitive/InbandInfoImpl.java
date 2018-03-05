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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InbandInfoImpl extends SequenceBase implements InbandInfo {

    public static final int _ID_messageID = 0;
    public static final int _ID_numberOfRepetitions = 1;
    public static final int _ID_duration = 2;
    public static final int _ID_interval = 3;

    private static final String MESSAGE_ID = "messageID";
    private static final String NUMBER_OF_REPETITIONS = "numberOfRepetitions";
    private static final String DURATION = "duration";
    private static final String INTERVAL = "interval";

    private MessageID messageID;
    private Integer numberOfRepetitions;
    private Integer duration;
    private Integer interval;

    public InbandInfoImpl() {
        super("InbandInfo");
    }

    public InbandInfoImpl(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval) {
        super("InbandInfo");

        this.messageID = messageID;
        this.numberOfRepetitions = numberOfRepetitions;
        this.duration = duration;
        this.interval = interval;
    }

    @Override
    public MessageID getMessageID() {
        return messageID;
    }

    @Override
    public Integer getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    @Override
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Integer getInterval() {
        return interval;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.messageID = null;
        this.numberOfRepetitions = null;
        this.duration = null;
        this.interval = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_messageID:
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.messageID = new MessageIDImpl();
                        ((MessageIDImpl) this.messageID).decodeAll(ais2);
                        break;
                    case _ID_numberOfRepetitions:
                        this.numberOfRepetitions = (int) ais.readInteger();
                        break;
                    case _ID_duration:
                        this.duration = (int) ais.readInteger();
                        break;
                    case _ID_interval:
                        this.interval = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.messageID == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": messageID is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.messageID == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": messageID must not be null");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_messageID);
            int pos = aos.StartContentDefiniteLength();
            ((MessageIDImpl) this.messageID).encodeAll(aos);
            aos.FinalizeContent(pos);

            if (this.numberOfRepetitions != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_numberOfRepetitions, this.numberOfRepetitions);
            if (this.duration != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_duration, this.duration);
            if (this.interval != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_interval, this.interval);

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

        if (this.messageID != null) {
            sb.append("messageID=");
            sb.append(messageID.toString());
        }
        if (this.numberOfRepetitions != null) {
            sb.append(", numberOfRepetitions=");
            sb.append(numberOfRepetitions);
        }
        if (this.duration != null) {
            sb.append(", duration=");
            sb.append(duration);
        }
        if (this.interval != null) {
            sb.append(", interval=");
            sb.append(interval);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<InbandInfoImpl> INBAND_INFO_XML = new XMLFormat<InbandInfoImpl>(InbandInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, InbandInfoImpl inbandInfo) throws XMLStreamException {
            int vali = xml.getAttribute(NUMBER_OF_REPETITIONS, -1);
            if (vali != -1)
                inbandInfo.numberOfRepetitions = vali;
            vali = xml.getAttribute(DURATION, -1);
            if (vali != -1)
                inbandInfo.duration = vali;
            vali = xml.getAttribute(INTERVAL, -1);
            if (vali != -1)
                inbandInfo.interval = vali;

            inbandInfo.messageID = xml.get(MESSAGE_ID, MessageIDImpl.class);
        }

        @Override
        public void write(InbandInfoImpl inbandInfo, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (inbandInfo.numberOfRepetitions != null)
                xml.setAttribute(NUMBER_OF_REPETITIONS, (int) inbandInfo.numberOfRepetitions);
            if (inbandInfo.duration != null)
                xml.setAttribute(DURATION, (int) inbandInfo.duration);
            if (inbandInfo.interval != null)
                xml.setAttribute(INTERVAL, (int) inbandInfo.interval);

            xml.add((MessageIDImpl) inbandInfo.messageID, MESSAGE_ID, MessageIDImpl.class);
        }
    };
}
