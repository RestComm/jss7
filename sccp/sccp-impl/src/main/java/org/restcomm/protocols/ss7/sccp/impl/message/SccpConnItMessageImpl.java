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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.message.SccpConnItMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SequenceNumber;
import org.restcomm.protocols.ss7.sccp.parameter.SequencingSegmenting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SccpConnItMessageImpl extends SccpConnReferencedMessageImpl implements SccpConnItMessage {
    protected ProtocolClass protocolClass;
    protected SequencingSegmenting sequencingSegmenting;
    protected Credit credit;

    public SccpConnItMessageImpl(int sls, int localSsn) {
        super(0, MESSAGE_TYPE_IT, sls, localSsn);
    }

    public SccpConnItMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(0, MESSAGE_TYPE_IT, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public ProtocolClass getProtocolClass() {
        return protocolClass;
    }

    @Override
    public void setProtocolClass(ProtocolClass value) {
        this.protocolClass = value;
    }

    @Override
    public SequencingSegmenting getSequencingSegmenting() {
        return sequencingSegmenting;
    }

    @Override
    public void setSequencingSegmenting(SequencingSegmenting value) {
        this.sequencingSegmenting = value;
    }

    @Override
    public Credit getCredit() {
        return credit;
    }

    @Override
    public void setCredit(Credit value) {
        this.credit = value;
    }

    @Override
    public void decode(InputStream in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            byte[] buffer = new byte[3];
            in.read(buffer);
            LocalReferenceImpl ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            destinationLocalReferenceNumber = ref;

            in.read(buffer);
            ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            sourceLocalReferenceNumber = ref;

            buffer = new byte[1];
            in.read(buffer);
            ProtocolClassImpl protocol = new ProtocolClassImpl();
            protocol.decode(buffer, factory, sccpProtocolVersion);
            protocolClass = protocol;

            if (protocol.getProtocolClass() != 2) {
                buffer = new byte[2];
                in.read(buffer);
                SequencingSegmentingImpl sequencing = new SequencingSegmentingImpl();
                sequencing.decode(buffer, factory, sccpProtocolVersion);
                sequencingSegmenting = sequencing;

                buffer = new byte[1];
                in.read(buffer);
                CreditImpl cred = new CreditImpl();
                cred.decode(buffer, factory, sccpProtocolVersion);
                credit = cred;
            }
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
            if (sourceLocalReferenceNumber == null) {
                return new EncodingResultData(EncodingResult.SourceLocalReferenceNumberMissing, null, null, null);
            }
            if (protocolClass == null) {
                return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);
            }
            if (sequencingSegmenting == null) {
                return new EncodingResultData(EncodingResult.SequencingSegmentingMissing, null, null, null);
            }
            if (credit == null) {
                return new EncodingResultData(EncodingResult.CreditMissing, null, null, null);
            }

            // 11 is sum of 6 fixed-length field lengths
            ByteArrayOutputStream out = new ByteArrayOutputStream(11);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] slr = ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] proto = ((ProtocolClassImpl) protocolClass).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] seq = ((SequencingSegmentingImpl) sequencingSegmenting).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] cred = ((CreditImpl) credit).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(slr);
            out.write(proto);
            out.write(seq);
            out.write(cred);

            return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    public boolean isMoreData() {
        return sequencingSegmenting.isMoreData();
    }

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

        sb.append("Sccp Msg [Type=IT");
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

        sb.append(" sourceLR=");
        if (this.sourceLocalReferenceNumber != null)
            sb.append(this.sourceLocalReferenceNumber.getValue());
        sb.append(" destLR=");
        if (this.destinationLocalReferenceNumber != null)
            sb.append(this.destinationLocalReferenceNumber.getValue());

        sb.append(" protocolClass=");
        if (this.protocolClass != null)
            sb.append(this.protocolClass.getProtocolClass());
        sb.append(" sequencingSegmenting=");
        if (this.sequencingSegmenting != null)
            sb.append(this.sequencingSegmenting);
        sb.append(" credit=");
        if (this.credit != null)
            sb.append(this.credit.getValue());
        sb.append(" isMtpOriginated=");
        sb.append(this.isMtpOriginated);

        sb.append("]");

        return sb.toString();
    }
}
