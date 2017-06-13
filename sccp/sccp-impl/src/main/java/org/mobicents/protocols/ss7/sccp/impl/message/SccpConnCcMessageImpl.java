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
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCcMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.Parameter;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SccpConnCcMessageImpl extends SccpMessageImpl implements SccpConnCcMessage {
    protected LocalReference destinationLocalReferenceNumber;
    protected LocalReference sourceLocalReferenceNumber;
    protected SccpAddress calledPartyAddress;
    protected ProtocolClass protocolClass;
    protected Credit credit;
    protected byte[] userData;
    protected Importance importance;

    protected SccpConnCcMessageImpl(int sls, int localSsn) {
        super(130, MESSAGE_TYPE_CC, sls, localSsn);
    }

    protected SccpConnCcMessageImpl(int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(130, MESSAGE_TYPE_CC, incomingOpc, incomingDpc, incomingSls, networkId);
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
    public SccpAddress getCalledPartyAddress() {
        return calledPartyAddress;
    }

    @Override
    public void setCalledPartyAddress(SccpAddress calledPartyAddress) {
        this.calledPartyAddress = calledPartyAddress;
    }

    @Override
    public ProtocolClass getProtocolClass() {
        return protocolClass;
    }

    @Override
    public void setProtocolClass(ProtocolClass protocolClass) {
        this.protocolClass = protocolClass;
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
            ProtocolClassImpl protocol = new ProtocolClassImpl();
            protocol.decode(buffer, factory, sccpProtocolVersion);
            protocolClass = protocol;

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
                if (paramCode != Credit.PARAMETER_CODE
                        && paramCode != SccpAddress.CDA_PARAMETER_CODE
                        && paramCode != Parameter.DATA_PARAMETER_CODE
                        && paramCode != Importance.PARAMETER_CODE) {
                    throw new ParseException(String.format("Code %d is not supported for CC message", paramCode));
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
            if (protocolClass == null) {
                return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);
            }

            byte[] protocol = ((ProtocolClassImpl) protocolClass).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            byte[] cdp = new byte[0];
            if (calledPartyAddress != null) {
                cdp = ((SccpAddressImpl) calledPartyAddress).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            }

            byte[] bf = new byte[0];
            if (userData != null) {
                bf = userData;
            }

            int fieldsLen = MessageUtil.calculateCcFieldsLengthWithoutData(credit != null, cdp.length,
                    userData != null, importance != null);
            int availLen = maxMtp3UserDataLength - fieldsLen;

            if (availLen > 130) {
                availLen = 130;
            }
            if (bf.length > availLen) { // message is too long
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Failure when sending a CC message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

            byte[] dlr = ((LocalReferenceImpl) destinationLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] slr = ((LocalReferenceImpl) sourceLocalReferenceNumber).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            out.write(type);
            out.write(dlr);
            out.write(slr);
            out.write(protocol);

            // we have 1 pointers (optionals), cdp starts after 1 octets then
            int len = 1;

            boolean optionalPresent = false;
            if (credit != null || calledPartyAddress != null || userData != null || importance != null) {
                out.write(len); // optionals pointer

                optionalPresent = true;
            } else {
                // in case there is no optional
                out.write(0);
            }

            if (calledPartyAddress != null) {
                out.write(SccpAddress.CDA_PARAMETER_CODE);
                out.write(cdp.length);
                out.write(cdp);
            }

            if (credit != null) {
                out.write(Credit.PARAMETER_CODE);
                byte[] b = ((CreditImpl)credit).encode(removeSPC, sccpProtocolVersion);
                out.write(b.length);
                out.write(b);
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
            case Credit.PARAMETER_CODE:
                CreditImpl cred = new CreditImpl();
                cred.decode(buffer, null, sccpProtocolVersion);
                credit = cred;
                break;

            case SccpAddress.CDA_PARAMETER_CODE:
                SccpAddressImpl address2 = new SccpAddressImpl();
                address2.decode(buffer, null, sccpProtocolVersion);
                calledPartyAddress = address2;
                break;

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
