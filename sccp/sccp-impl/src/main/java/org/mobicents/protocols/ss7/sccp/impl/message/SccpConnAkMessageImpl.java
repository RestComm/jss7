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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpConnAkMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ReceiveSequenceNumber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SccpConnAkMessageImpl extends SccpMessageImpl implements SccpConnAkMessage {
    protected LocalReference destinationLocalReferenceNumber;
    protected ReceiveSequenceNumber receiveSequenceNumber;
    protected Credit credit;

    protected SccpConnAkMessageImpl(int sls, int localSsn) {
        super(0, MESSAGE_TYPE_AK, sls, localSsn);
    }

    protected SccpConnAkMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(0, MESSAGE_TYPE_AK, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    @Override
    public LocalReference getDestinationLocalReferenceNumber() {
        return destinationLocalReferenceNumber;
    }

    @Override
    public void setDestinationLocalReferenceNumber(LocalReference number) {
        this.destinationLocalReferenceNumber = number;
    }

    @Override
    public ReceiveSequenceNumber getReceiveSequenceNumber() {
        return receiveSequenceNumber;
    }

    @Override
    public void setReceiveSequenceNumber(ReceiveSequenceNumber receiveSequenceNumber) {
        this.receiveSequenceNumber = receiveSequenceNumber;
    }

    @Override
    public Credit getCredit() {
        return credit;
    }

    @Override
    public void setCredit(Credit credit) {
        this.credit = credit;
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
            ReceiveSequenceNumberImpl sequenceNumber = new ReceiveSequenceNumberImpl();
            sequenceNumber.decode(buffer, factory, sccpProtocolVersion);
            receiveSequenceNumber = sequenceNumber;

            in.read(buffer);
            CreditImpl cred = new CreditImpl();
            cred.decode(buffer, factory, sccpProtocolVersion);
            credit = cred;
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
            if (receiveSequenceNumber == null) {
                return new EncodingResultData(EncodingResult.ReceiveSequenceNumberMissing, null, null, null);
            }
            if (credit == null) {
                return new EncodingResultData(EncodingResult.CreditMissing, null, null, null);
            }

            // 6 is sum of 4 fixed-length field lengths
            ByteArrayOutputStream out = new ByteArrayOutputStream(6);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] sqn = ((ReceiveSequenceNumberImpl) receiveSequenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] crd = ((CreditImpl) credit).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(sqn);
            out.write(crd);
            return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }
}
