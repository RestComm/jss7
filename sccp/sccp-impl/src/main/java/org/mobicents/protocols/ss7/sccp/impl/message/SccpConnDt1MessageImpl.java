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
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentingReassemblingImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpConnDt1Message;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SegmentingReassembling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SccpConnDt1MessageImpl extends SccpMessageImpl implements SccpConnDt1Message {
    protected LocalReference destinationLocalReferenceNumber;
    protected SegmentingReassembling segmentingReassembling;
    protected byte[] userData;

    protected SccpConnDt1MessageImpl(int maxDataLen, int sls, int localSsn) {
        super(maxDataLen, MESSAGE_TYPE_DT1, sls, localSsn);
    }

    protected SccpConnDt1MessageImpl(int maxDataLen, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, MESSAGE_TYPE_DT1, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public LocalReference getDestinationLocalReferenceNumber() {
        return destinationLocalReferenceNumber;
    }

    @Override
    public void setDestinationLocalReferenceNumber(LocalReference destinationLocalReferenceNumber) {
        this.destinationLocalReferenceNumber = destinationLocalReferenceNumber;
    }

    @Override
    public SegmentingReassembling getSegmentingReassembling() {
        return segmentingReassembling;
    }

    @Override
    public void setSegmentingReassembling(SegmentingReassembling segmentingReassembling) {
        this.segmentingReassembling = segmentingReassembling;
    }

    @Override
    public byte[] getUserData() {
        return userData;
    }

    @Override
    public void setUserData(byte[] userData) {
        this.userData = userData;
    }

    @Override
    public void decode(InputStream in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            byte[] buffer = new byte[3];
            in.read(buffer);
            LocalReferenceImpl ref = new LocalReferenceImpl();
            ref.decode(buffer, factory, sccpProtocolVersion);
            destinationLocalReferenceNumber = ref;

            buffer = new byte[1];
            in.read(buffer);
            SegmentingReassemblingImpl segmenting = new SegmentingReassemblingImpl();
            segmenting.decode(buffer, factory, sccpProtocolVersion);
            segmentingReassembling = segmenting;

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
            if (segmentingReassembling == null) {
                return new EncodingResultData(EncodingResult.SegmentingReassemblingMissing, null, null, null);
            }
            if (userData == null) {
                return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
            }

            byte[] bf = new byte[0];
            if (userData != null) {
                bf = userData;
            }

            // 7 = 5 (fixed fields length) + 1 (variable fields pointers) + 1
            // (variable fields lengths)
            int fieldsLen = 7;
            int availLen = maxMtp3UserDataLength - fieldsLen;

            if (availLen > 256)
                availLen = 256;

            if (bf.length > availLen) { // message is too long
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Failure when sending a DT1 message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] seg = ((SegmentingReassemblingImpl) segmentingReassembling).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(seg);

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
}
