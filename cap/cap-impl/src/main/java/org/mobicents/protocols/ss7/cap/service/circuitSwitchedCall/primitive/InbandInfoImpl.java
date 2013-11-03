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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InbandInfoImpl implements InbandInfo, CAPAsnPrimitive {

    public static final int _ID_messageID = 0;
    public static final int _ID_numberOfRepetitions = 1;
    public static final int _ID_duration = 2;
    public static final int _ID_interval = 3;

    public static final String _PrimitiveName = "InbandInfo";

    private MessageID messageID;
    private Integer numberOfRepetitions;
    private Integer duration;
    private Integer interval;

    public InbandInfoImpl() {
    }

    public InbandInfoImpl(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval) {
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
}
