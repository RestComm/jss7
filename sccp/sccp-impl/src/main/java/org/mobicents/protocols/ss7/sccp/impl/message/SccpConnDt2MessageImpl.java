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

package org.mobicents.protocols.ss7.sccp.impl.message;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpConnDt2Message;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SequenceNumber;
import org.mobicents.protocols.ss7.sccp.parameter.SequencingSegmenting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SccpConnDt2MessageImpl extends SccpConnSegmentableMessageImpl implements SccpConnDt2Message {
    protected SequencingSegmenting sequencingSegmenting;

    public SccpConnDt2MessageImpl(int maxDataLen, int sls, int localSsn) {
        super(maxDataLen, MESSAGE_TYPE_DT2, sls, localSsn);
    }

    protected SccpConnDt2MessageImpl(int maxDataLen, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, MESSAGE_TYPE_DT2, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public SequencingSegmenting getSequencingSegmenting() {
        return sequencingSegmenting;
    }

    @Override
    public void setSequencingSegmenting(SequencingSegmenting sequencingSegmenting) {
        this.sequencingSegmenting = sequencingSegmenting;
    }

    @Override
    public void decode(InputStream in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            byte[] buffer = new byte[3];
            in.read(buffer);
            LocalReferenceImpl ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            destinationLocalReferenceNumber = ref;

            buffer = new byte[2];
            in.read(buffer);
            SequencingSegmentingImpl sequencing = new SequencingSegmentingImpl();
            sequencing.decode(buffer, factory, sccpProtocolVersion);
            sequencingSegmenting = sequencing;

            int dataPointer = in.read() & 0xFF;
            in.mark(in.available());

            in.skip(dataPointer - 1);
            int len = in.read() & 0xff;

            buffer = new byte[len];
            in.read(buffer);

            userData = buffer;
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger, boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            if (type == 0) {
                return new EncodingResultData(EncodingResult.MessageTypeMissing, null, null, null);
            }
            if (destinationLocalReferenceNumber == null) {
                return new EncodingResultData(EncodingResult.DestinationLocalReferenceNumberMissing, null, null, null);
            }
            if (sequencingSegmenting == null) {
                return new EncodingResultData(EncodingResult.SequencingSegmentingMissing, null, null, null);
            }
            if (userData == null) {
                return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
            }

            byte[] bf = new byte[0];
            if (userData != null) {
                bf = userData;
            }

            // 8 = 6 (fixed fields length) + 1 (variable fields pointers) + 1
            // (variable fields lengths)
            int fieldsLen = 8;
            int availLen = maxMtp3UserDataLength - fieldsLen;

            if (availLen > 256)
                availLen = 256;

            if (bf.length > availLen) { // message is too long
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Failure when sending a DT2 message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] seq = ((SequencingSegmentingImpl) sequencingSegmenting).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(seq);

            // we have 1 pointers (data), cdp starts after 1 octets then
            int len = 1;

            out.write(len);
            out.write((byte) bf.length);
            out.write(bf);

            return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public boolean isMoreData() {
        return sequencingSegmenting.isMoreData();
    }

    @Override
    public void setMoreData(boolean moreData) {
        if (sequencingSegmenting == null) {
            // sendSequenceNumber and receiveSequenceNumber are later re-initialized in MessageSender
            sequencingSegmenting = new SequencingSegmentingImpl();
        }
        sequencingSegmenting.setMoreData(moreData);
    }

    public void setSequencing(SequenceNumber sendSequenceNumber, SequenceNumber receiveSequenceNumber) {
        if (sequencingSegmenting == null) {
            // sendSequenceNumber and receiveSequenceNumber are later re-initialized in MessageSender
            sequencingSegmenting = new SequencingSegmentingImpl();
        }
        sequencingSegmenting.setSendSequenceNumber(sendSequenceNumber);
        sequencingSegmenting.setReceiveSequenceNumber(receiveSequenceNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Sccp Msg [Type=DT2");
        sb.append(" networkId=");
        sb.append(this.networkId);
        sb.append(" sls=");
        sb.append(this.sls);
        sb.append(" incomingOpc=");
        sb.append(this.incomingOpc);
        sb.append(" incomingDpc=");
        sb.append(this.incomingDpc);
        sb.append(" outgoingDpc=");
        sb.append(this.outgoingDpc);
        sb.append(" DataLen=");
        if (this.userData != null)
            sb.append(this.userData.length);

        sb.append(" destLR=");
        if (this.destinationLocalReferenceNumber != null)
            sb.append(this.destinationLocalReferenceNumber.getValue());
        sb.append(" sequencingSegmenting=");
        if (this.sequencingSegmenting != null)
            sb.append(this.sequencingSegmenting);
        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
