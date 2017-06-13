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
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpConnRlsdMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.Parameter;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil.calculateRlsdFieldsLengthWithoutData;

public class SccpConnRlsdMessageImpl extends SccpMessageImpl implements SccpConnRlsdMessage {
    protected LocalReference destinationLocalReferenceNumber;
    protected LocalReference sourceLocalReferenceNumber;
    protected ReleaseCause releaseCause;
    protected byte[] userData;
    protected Importance importance;

    protected SccpConnRlsdMessageImpl(int sls, int localSsn) {
        super(130, MESSAGE_TYPE_RLSD, sls, localSsn);
    }

    protected SccpConnRlsdMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(130, MESSAGE_TYPE_RLSD, incomingOpc, incomingDpc, incomingSls, networkId);
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
    public LocalReference getSourceLocalReferenceNumber() {
        return sourceLocalReferenceNumber;
    }

    @Override
    public void setSourceLocalReferenceNumber(LocalReference sourceLocalReferenceNumber) {
        this.sourceLocalReferenceNumber = sourceLocalReferenceNumber;
    }

    @Override
    public ReleaseCause getReleaseCause() {
        return releaseCause;
    }

    @Override
    public void setReleaseCause(ReleaseCause releaseCause) {
        this.releaseCause = releaseCause;
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
    public Importance getImportance() {
        return importance;
    }

    @Override
    public void setImportance(Importance importance) {
        this.importance = importance;
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
            ReleaseCauseImpl cause = new ReleaseCauseImpl();
            cause.decode(buffer, factory, sccpProtocolVersion);
            releaseCause = cause;

            int pointer = in.read() & 0xff;
            in.mark(in.available());

            if (pointer == 0) {
                // we are done
                return;
            }
            if (pointer - 1 != in.skip(pointer - 1)) {
                throw new IOException("Not enough data in buffer");
            }

            int paramCode = 0;
            int len;
            // EOP
            while ((paramCode = in.read() & 0xFF) != 0) {
                if (paramCode != Parameter.DATA_PARAMETER_CODE
                        && paramCode != Importance.PARAMETER_CODE) {
                    throw new ParseException(String.format("Code %d is not supported for RLSD message", paramCode));
                }
                len = in.read() & 0xff;
                buffer = new byte[len];
                in.read(buffer);
                decodeOptional(paramCode, buffer, sccpProtocolVersion);
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
            if (releaseCause == null) {
                return new EncodingResultData(EncodingResult.ReleaseCauseMissing, null, null, null);
            }

            byte[] bf = new byte[0];
            if (userData != null) {
                bf = userData;
            }

            int fieldsLen = calculateRlsdFieldsLengthWithoutData(userData != null, importance != null);
            int availLen = maxMtp3UserDataLength - fieldsLen;

            if (availLen > 130)
                availLen = 130;

            if (bf.length > availLen) { // message is too long
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Failure when sending a RLSD message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] slr = ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] rel = ((ReleaseCauseImpl)releaseCause).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(slr);
            out.write(rel);

            // we have 1 pointers (optionals), cdp starts after 1 octets then
            int len = 1;

            boolean optionalPresent = false;
            if (userData != null || importance != null) {
                out.write(len); // optionals pointer

                optionalPresent = true;
            } else {
                // in case there is no optional
                out.write(0);
            }

            if (userData != null) {
                out.write(Parameter.DATA_PARAMETER_CODE);
                out.write(userData.length);
                out.write(userData);
            }
            if (importance != null) {
                out.write(Importance.PARAMETER_CODE);
                byte[] b = ((ImportanceImpl)importance).encode(removeSPC, sccpProtocolVersion);
                out.write(b.length);
                out.write(b);
            }

            if (optionalPresent) {
                out.write(0x00);
            }

            return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    protected void decodeOptional(int code, byte[] buffer, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        switch (code) {
            case Parameter.DATA_PARAMETER_CODE:
                userData = buffer;
                break;

            case Importance.PARAMETER_CODE:
                ImportanceImpl imp = new ImportanceImpl();
                imp.decode(buffer, null, sccpProtocolVersion);
                importance = imp;
                break;

            default:
                throw new ParseException("Unknown optional parameter code: " + code);
        }
    }
}
