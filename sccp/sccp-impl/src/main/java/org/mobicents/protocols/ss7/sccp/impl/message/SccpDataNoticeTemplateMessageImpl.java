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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;
import static org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil.calculateLudtFieldsLengthWithoutData;
import static org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData;
import static org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData2;
import static org.mobicents.protocols.ss7.sccp.impl.message.MessageUtil.calculateUdtFieldsLengthWithoutData;

/**
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 */
public abstract class SccpDataNoticeTemplateMessageImpl extends SccpSegmentableMessageImpl {

    protected ImportanceImpl importance;
    protected SccpDataNoticeTemplateMessageImpl(int maxDataLen,int type, int outgoingSls, int localSsn,
            SccpAddress calledParty, SccpAddress callingParty, byte[] data, HopCounter hopCounter, Importance importance) {
        super(maxDataLen,type, outgoingSls, localSsn, calledParty, callingParty, data, hopCounter);
        this.importance = (ImportanceImpl) importance;
    }

    protected SccpDataNoticeTemplateMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc,
            int incomingSls, int networkId) {
        super(maxDataLen,type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance p) {
        importance = (ImportanceImpl) p;
    }

    protected abstract boolean getIsProtocolClass1();

    protected abstract boolean getSecondParamaterPresent();

    protected abstract byte[] getSecondParamaterData(boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    protected abstract void setSecondParamaterData(int data, SccpProtocolVersion sccpProtocolVersion) throws ParseException;

    @Override
    public void decode(InputStream in, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            switch (this.type) {
                case SccpMessage.MESSAGE_TYPE_UDT:
                case SccpMessage.MESSAGE_TYPE_UDTS: {
                    this.setSecondParamaterData(in.read(), sccpProtocolVersion);

                    int cpaPointer = in.read() & 0xff;
                    in.mark(in.available());

                    in.skip(cpaPointer - 1);
                    int len = in.read() & 0xff;

                    byte[] buffer = new byte[len];
                    in.read(buffer);

                    super.calledParty = createAddress(buffer,factory,sccpProtocolVersion);


                    in.reset();
                    cpaPointer = in.read() & 0xff;
                    in.mark(in.available());

                    in.skip(cpaPointer - 1);
                    len = in.read() & 0xff;

                    buffer = new byte[len];
                    in.read(buffer);

                    super.callingParty = createAddress(buffer,factory,sccpProtocolVersion);

                    in.reset();
                    cpaPointer = in.read() & 0xff;

                    in.skip(cpaPointer - 1);
                    len = in.read() & 0xff;

                    data = new byte[len];
                    in.read(data);
                }
                    break;

                case SccpMessage.MESSAGE_TYPE_XUDT:
                case SccpMessage.MESSAGE_TYPE_XUDTS: {
                    this.setSecondParamaterData(in.read(), sccpProtocolVersion);

                    this.hopCounter = new HopCounterImpl((byte) in.read());
                    if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH
                            || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
                        throw new IOException("Hop Counter must be between 1 and 15, it is: " + this.hopCounter);
                    }

                    int pointer = in.read() & 0xff;
                    in.mark(in.available());
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    int len = in.read() & 0xff;

                    byte[] buffer = new byte[len];
                    in.read(buffer);

                    calledParty = createAddress(buffer,factory,sccpProtocolVersion);

                    in.reset();

                    pointer = in.read() & 0xff;

                    in.mark(in.available());

                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    len = in.read() & 0xff;

                    buffer = new byte[len];
                    in.read(buffer);

                    callingParty = createAddress(buffer,factory,sccpProtocolVersion);

                    in.reset();
                    pointer = in.read() & 0xff;
                    in.mark(in.available());
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    len = in.read() & 0xff;

                    data = new byte[len];
                    in.read(data);

                    in.reset();
                    pointer = in.read() & 0xff;
                    in.mark(in.available());

                    if (pointer == 0) {
                        // we are done
                        return;
                    }
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }

                    int paramCode = 0;
                    // EOP
                    while ((paramCode = in.read() & 0xFF) != 0) {
                        len = in.read() & 0xff;
                        buffer = new byte[len];
                        in.read(buffer);
                        this.decodeOptional(paramCode, buffer, sccpProtocolVersion);
                    }
                }
                    break;

                case SccpMessage.MESSAGE_TYPE_LUDT:
                case SccpMessage.MESSAGE_TYPE_LUDTS: {
                    this.setSecondParamaterData(in.read(), sccpProtocolVersion);

                    this.hopCounter = new HopCounterImpl((byte) in.read());
                    if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH
                            || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
                        throw new IOException("Hop Counter must be between 1 and 15, it is: " + this.hopCounter);
                    }

                    int pointer = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                    in.mark(in.available());
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    int len = in.read() & 0xff;
                    byte[] buffer = new byte[len];
                    in.read(buffer);
                    calledParty = createAddress(buffer,factory,sccpProtocolVersion);

                    in.reset();
                    pointer = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                    in.mark(in.available());
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    len = in.read() & 0xff;
                    buffer = new byte[len];
                    in.read(buffer);
                    callingParty = createAddress(buffer,factory,sccpProtocolVersion);

                    in.reset();
                    pointer = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                    in.mark(in.available());
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }
                    len = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                    data = new byte[len];
                    in.read(data);

                    in.reset();
                    pointer = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                    in.mark(in.available());

                    if (pointer == 0) {
                        // we are done
                        return;
                    }
                    if (pointer - 1 != in.skip(pointer - 1)) {
                        throw new IOException("Not enough data in buffer");
                    }

                    int paramCode = 0;
                    // EOP
                    while ((paramCode = in.read() & 0xFF) != 0) {
                        len = in.read() & 0xff;
                        buffer = new byte[len];
                        in.read(buffer);
                        this.decodeOptional(paramCode, buffer, sccpProtocolVersion);
                    }
                }
                    break;
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    private void decodeOptional(int code, byte[] buffer, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {

        switch (code) {
            case Segmentation.PARAMETER_CODE:
                this.segmentation = new SegmentationImpl();
                this.segmentation.decode(buffer, null, sccpProtocolVersion);
                break;
            case Importance.PARAMETER_CODE:
                this.importance = new ImportanceImpl();
                this.importance.decode(buffer, null, sccpProtocolVersion);
                break;

            default:
                throw new ParseException("Uknown optional parameter code: " + code);
        }
    }

    @Override
    public EncodingResultData encode(SccpStackImpl sccpStackImpl, LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger,
            boolean removeSPC, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        try {
            byte[] bf = this.getData();
            if (bf == null || bf.length == 0)
                return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
            if (bf.length > super.maxDataLen)
                return new EncodingResultData(EncodingResult.DataMaxLengthExceeded, null, null, null);

            if (calledParty == null)
                return new EncodingResultData(EncodingResult.CalledPartyAddressMissing, null, null, null);
            if (callingParty == null)
                return new EncodingResultData(EncodingResult.CallingPartyAddressMissing, null, null, null);
            if (!this.getSecondParamaterPresent())
                return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);

            byte[] cdp = ((SccpAddressImpl) super.calledParty).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());
            byte[] cnp = ((SccpAddressImpl) super.callingParty).encode(sccpStackImpl.isRemoveSpc(), sccpStackImpl.getSccpProtocolVersion());

            if (longMessageRuleType == null)
                longMessageRuleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
            if (this.isMtpOriginated && this.type == SccpMessage.MESSAGE_TYPE_UDT || this.type == SccpMessage.MESSAGE_TYPE_UDTS)
                // if we have received an UDT message from MTP3, leave UDT style
                // if this is UDTS message, leave this type
                longMessageRuleType = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;

            boolean isServiceMessage = true;
            if (this instanceof SccpDataMessageImpl)
                isServiceMessage = false;

            int fieldsLen = calculateUdtFieldsLengthWithoutData(cdp.length, cnp.length);
            int availLen = maxMtp3UserDataLength - fieldsLen;
            if (availLen > 254)
                availLen = 254;
            if (sccpProtocolVersion == SccpProtocolVersion.ANSI && availLen > 252)
                availLen = 252;

            Boolean useShortMessage=false;
            if (longMessageRuleType == LongMessageRuleType.LONG_MESSAGE_FORBBIDEN)
                useShortMessage=true;
            else if(longMessageRuleType == LongMessageRuleType.XUDT_ENABLED && bf.length <= availLen)
                useShortMessage=true;

            if (useShortMessage) {
                // use UDT / UDTS
                if (bf.length > availLen) { // message is too long to encode UDT
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Failure when sending a UDT message: message is too long. SccpMessageSegment=%s", this));
                    }
                    return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

                if (isServiceMessage)
                    this.type = SccpMessage.MESSAGE_TYPE_UDTS;
                else
                    this.type = SccpMessage.MESSAGE_TYPE_UDT;
                out.write(this.type);
                out.write(this.getSecondParamaterData(removeSPC, sccpProtocolVersion));

                int len = 3;
                out.write(len);

                len = (cdp.length + 3);
                out.write(len);

                len += (cnp.length);
                out.write(len);

                out.write((byte) cdp.length);
                out.write(cdp);

                out.write((byte) cnp.length);
                out.write(cnp);

                out.write((byte) bf.length);
                out.write(bf);

                return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
            } else if (longMessageRuleType == LongMessageRuleType.XUDT_ENABLED) {

                // use XUDT / XUDTS
                if (isServiceMessage)
                    this.type = SccpMessage.MESSAGE_TYPE_XUDTS;
                else
                    this.type = SccpMessage.MESSAGE_TYPE_XUDT;
                if (this.hopCounter == null)
                    this.hopCounter = new HopCounterImpl(15);

                int fieldsLenX = calculateXudtFieldsLengthWithoutData(cdp.length, cnp.length, false,
                        this.importance != null);
                int fieldsLen2 = calculateXudtFieldsLengthWithoutData2(cdp.length, cnp.length);
                int availLenX = maxMtp3UserDataLength - fieldsLenX;
                if (availLenX > fieldsLen2)
                    availLenX = fieldsLen2;
                int fieldsLenXSegm = calculateXudtFieldsLengthWithoutData(cdp.length, cnp.length, true,
                        this.importance != null);
                int availLenXSegm = maxMtp3UserDataLength - fieldsLenXSegm;
                if (availLenXSegm > fieldsLen2)
                    availLenXSegm = fieldsLen2;

                if (bf.length <= availLenX && bf.length <= sccpStackImpl.getZMarginXudtMessage()) {
                    // one segment
                    ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLenX + bf.length);

                    out.write(this.type);

                    out.write(this.getSecondParamaterData(removeSPC, sccpProtocolVersion));
                    out.write(this.hopCounter.getValue());

                    // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4 octests than
                    int len = 4;
                    out.write(len);

                    len += cdp.length;
                    out.write(len);

                    len += cnp.length;
                    out.write(len);
                    boolean optionalPresent = false;
                    if (importance != null) {
                        len += (bf.length);
                        out.write(len);
                        optionalPresent = true;
                    } else {
                        // in case there is no optional
                        out.write(0);
                    }

                    out.write((byte) cdp.length);
                    out.write(cdp);

                    out.write((byte) cnp.length);
                    out.write(cnp);

                    out.write((byte) bf.length);
                    out.write(bf);

                    if (importance != null) {
                        out.write(Importance.PARAMETER_CODE);
                        byte[] b = importance.encode(removeSPC, sccpProtocolVersion);
                        out.write(b.length);
                        out.write(b);
                    }

                    if (optionalPresent)
                        out.write(0x00);

                    return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
                } else {
                    // several segments
                    if (bf.length > availLenXSegm * 16) {
                        if (logger.isEnabledFor(Level.WARN)) {
                            logger.warn(String.format(
                                    "Failure when segmenting a message XUDT: message is too long. SccpMessageSegment=%s", this));
                        }
                        return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
                    }
                    int segmLen;
                    if (bf.length <= sccpStackImpl.getZMarginXudtMessage() * 16)
                        segmLen = sccpStackImpl.getZMarginXudtMessage();
                    else
                        segmLen = availLenXSegm;
                    if (segmLen > availLenXSegm)
                        segmLen = availLenXSegm;
                    int segmCount = (bf.length - 1) / segmLen + 1;

                    if (this.isMtpOriginated) {
                        if (this.segmentation == null) {
                            // MTP3 originated message - we may make segmentation
                            // only if incoming message has a "Segmentation" field
                            if (logger.isEnabledFor(Level.WARN)) {
                                logger.warn(String
                                        .format("Failure when segmenting a message: message is not locally originated but \"segmentation\" field is absent. SccpMessageSegment=%s",
                                                this));
                            }
                            return new EncodingResultData(EncodingResult.ReturnFailure, null, null,
                                    ReturnCauseValue.SEG_FAILURE);
                        }

                        this.segmentation = new SegmentationImpl(true, this.segmentation.isClass1Selected(), (byte) segmCount,
                                this.segmentation.getSegmentationLocalRef());
                    } else {
                        this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) segmCount,
                                sccpStackImpl.newSegmentationLocalRef());
                    }

                    byte[] importanceBuf = null;
                    if (importance != null) {
                        importanceBuf = importance.encode(removeSPC, sccpProtocolVersion);
                    }

                    ArrayList<byte[]> res = new ArrayList<byte[]>();
                    for (int num = 0; num < segmCount; num++) {
                        int fst = num * segmLen;
                        int last = fst + segmLen;
                        if (last > bf.length)
                            last = bf.length;
                        int mLen = last - fst;

                        ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLenXSegm + mLen);

                        out.write(this.type);

                        out.write(this.getSecondParamaterData(removeSPC, sccpProtocolVersion));
                        out.write(this.hopCounter.getValue());

                        // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4 octests than
                        int len = 4;
                        out.write(len);

                        len += cdp.length;
                        out.write(len);

                        len += cnp.length;
                        out.write(len);

                        len += (mLen);
                        out.write(len);

                        out.write((byte) cdp.length);
                        out.write(cdp);

                        out.write((byte) cnp.length);
                        out.write(cnp);

                        out.write((byte) mLen);
                        out.write(bf, fst, mLen);

                        out.write(Segmentation.PARAMETER_CODE);
                        segmentation.setRemainingSegments((byte) (segmentation.getRemainingSegments() - 1));
                        byte[] b = segmentation.encode(removeSPC, sccpProtocolVersion);
                        out.write(b.length);
                        out.write(b);
                        segmentation.setFirstSegIndication(false);

                        if (importanceBuf != null) {
                            out.write(Importance.PARAMETER_CODE);
                            out.write(importanceBuf.length);
                            out.write(importanceBuf);
                        }

                        out.write(0x00);

                        res.add(out.toByteArray());
                    }

                    return new EncodingResultData(EncodingResult.Success, null, res, null);
                }
            } else {

                // use LUDT / LUDTS
                if (isServiceMessage)
                    this.type = SccpMessage.MESSAGE_TYPE_LUDTS;
                else
                    this.type = SccpMessage.MESSAGE_TYPE_LUDT;
                if (this.hopCounter == null)
                    this.hopCounter = new HopCounterImpl(15);

                if (longMessageRuleType == LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION) {
                    this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) 0,
                            sccpStackImpl.newSegmentationLocalRef());
                }
                int fieldsLenL = calculateLudtFieldsLengthWithoutData(cdp.length, cnp.length,
                        this.segmentation != null, this.importance != null);
                availLen = maxMtp3UserDataLength - fieldsLenL;
                if (bf.length > availLen) { // message is too long to encode LUDT
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Failure when sending a LUDT message: message is too long. SccpMessageSegment=%s", this));
                    }
                    return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLenL + bf.length);

                out.write(this.type);

                out.write(this.getSecondParamaterData(removeSPC, sccpProtocolVersion));
                out.write(this.hopCounter.getValue());

                // we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 8 octests than
                int len = 7;
                out.write(len & 0xFF);
                out.write((len >> 8) & 0xFF);

                len += cdp.length - 1;
                out.write(len & 0xFF);
                out.write((len >> 8) & 0xFF);

                len += cnp.length - 1;
                out.write(len & 0xFF);
                out.write((len >> 8) & 0xFF);
                boolean optionalPresent = false;
                if (importance != null || segmentation != null) {
                    len += (bf.length);
                    out.write(len & 0xFF);
                    out.write((len >> 8) & 0xFF);
                    optionalPresent = true;
                } else {
                    // in case there is no optional
                    out.write(0);
                    out.write(0);
                }

                out.write((byte) cdp.length);
                out.write(cdp);

                out.write((byte) cnp.length);
                out.write(cnp);

                out.write(bf.length & 0xFF);
                out.write((bf.length >> 8) & 0xFF);
                out.write(bf);

                if (segmentation != null) {
                    out.write(Segmentation.PARAMETER_CODE);
                    byte[] b = segmentation.encode(removeSPC, sccpProtocolVersion);
                    out.write(b.length);
                    out.write(b);
                }
                if (importance != null) {
                    out.write(Importance.PARAMETER_CODE);
                    byte[] b = importance.encode(removeSPC, sccpProtocolVersion);
                    out.write(b.length);
                    out.write(b);
                }

                if (optionalPresent)
                    out.write(0x00);

                return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
            }
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    protected SccpAddress createAddress(byte[] buffer, ParameterFactory factory, SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        SccpAddressImpl addressImpl = new SccpAddressImpl();
        addressImpl.decode(buffer, factory, sccpProtocolVersion);
        return addressImpl;
    }
}
