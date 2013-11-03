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

package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public abstract class SccpDataNoticeTemplateMessageImpl extends SccpSegmentableMessageImpl {

    protected ImportanceImpl importance;

    protected SccpDataNoticeTemplateMessageImpl(SccpStackImpl sccpStackImpl, int type, int outgoingSls, int localSsn,
            SccpAddress calledParty, SccpAddress callingParty, byte[] data, HopCounter hopCounter, Importance importance) {
        super(sccpStackImpl, type, outgoingSls, localSsn, calledParty, callingParty, data, hopCounter);

        this.importance = (ImportanceImpl) importance;
    }

    protected SccpDataNoticeTemplateMessageImpl(SccpStackImpl sccpStackImpl, int type, int incomingOpc, int incomingDpc,
            int incomingSls) {
        super(sccpStackImpl, type, incomingOpc, incomingDpc, incomingSls);
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance p) {
        importance = (ImportanceImpl) p;
    }

    protected abstract boolean getIsProtocolClass1();

    protected abstract boolean getSecondParamaterPresent();

    protected abstract byte[] getSecondParamaterData() throws IOException;

    protected abstract void setSecondParamaterData(int data) throws IOException;

    @Override
    public void decode(InputStream in) throws IOException {
        switch (this.type) {
            case SccpMessage.MESSAGE_TYPE_UDT:
            case SccpMessage.MESSAGE_TYPE_UDTS: {
                this.setSecondParamaterData(in.read());

                int cpaPointer = in.read() & 0xff;
                in.mark(in.available());

                in.skip(cpaPointer - 1);
                int len = in.read() & 0xff;

                byte[] buffer = new byte[len];
                in.read(buffer);

                calledParty = SccpAddressCodec.decode(buffer);

                in.reset();
                cpaPointer = in.read() & 0xff;
                in.mark(in.available());

                in.skip(cpaPointer - 1);
                len = in.read() & 0xff;

                buffer = new byte[len];
                in.read(buffer);

                callingParty = SccpAddressCodec.decode(buffer);

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
                this.setSecondParamaterData(in.read());

                this.hopCounter = new HopCounterImpl((byte) in.read());
                if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
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

                calledParty = SccpAddressCodec.decode(buffer);

                in.reset();

                pointer = in.read() & 0xff;

                in.mark(in.available());

                if (pointer - 1 != in.skip(pointer - 1)) {
                    throw new IOException("Not enough data in buffer");
                }
                len = in.read() & 0xff;

                buffer = new byte[len];
                in.read(buffer);

                callingParty = SccpAddressCodec.decode(buffer);

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
                    this.decodeOptional(paramCode, buffer);
                }
            }
                break;

            case SccpMessage.MESSAGE_TYPE_LUDT:
            case SccpMessage.MESSAGE_TYPE_LUDTS: {
                this.setSecondParamaterData(in.read());

                this.hopCounter = new HopCounterImpl((byte) in.read());
                if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
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
                calledParty = SccpAddressCodec.decode(buffer);

                in.reset();
                pointer = (in.read() & 0xff) + ((in.read() & 0xff) << 8);
                in.mark(in.available());
                if (pointer - 1 != in.skip(pointer - 1)) {
                    throw new IOException("Not enough data in buffer");
                }
                len = in.read() & 0xff;
                buffer = new byte[len];
                in.read(buffer);
                callingParty = SccpAddressCodec.decode(buffer);

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
                    this.decodeOptional(paramCode, buffer);
                }
            }
                break;
        }
    }

    private void decodeOptional(int code, byte[] buffer) throws IOException {

        switch (code) {
            case Segmentation.PARAMETER_CODE:
                this.segmentation = new SegmentationImpl();
                this.segmentation.decode(buffer);
                break;
            case Importance.PARAMETER_CODE:
                this.importance = new ImportanceImpl();
                this.importance.decode(buffer);
                break;

            default:
                throw new IOException("Uknown optional parameter code: " + code);
        }
    }

    @Override
    public EncodingResultData encode(LongMessageRuleType longMessageRuleType, int maxMtp3UserDataLength, Logger logger)
            throws IOException {

        byte[] bf = this.getData();
        if (bf == null || bf.length == 0)
            return new EncodingResultData(EncodingResult.DataMissed, null, null, null);
        if (bf.length > this.sccpStackImpl.getMaxDataMessage())
            return new EncodingResultData(EncodingResult.DataMaxLengthExceeded, null, null, null);

        if (calledParty == null)
            return new EncodingResultData(EncodingResult.CalledPartyAddressMissing, null, null, null);
        if (callingParty == null)
            return new EncodingResultData(EncodingResult.CallingPartyAddressMissing, null, null, null);
        if (!this.getSecondParamaterPresent())
            return new EncodingResultData(EncodingResult.ProtocolClassMissing, null, null, null);

        byte[] cdp = SccpAddressCodec.encode(calledParty, this.sccpStackImpl.isRemoveSpc());
        byte[] cnp = SccpAddressCodec.encode(callingParty, this.sccpStackImpl.isRemoveSpc());

        if (longMessageRuleType == null)
            longMessageRuleType = LongMessageRuleType.LongMessagesForbidden;
        if (this.isMtpOriginated && this.type == SccpMessage.MESSAGE_TYPE_UDT || this.type == SccpMessage.MESSAGE_TYPE_UDTS)
            // if we have received an UDT message from MTP3, leave UDT style
            // if this is UDTS message, leave this type
            longMessageRuleType = LongMessageRuleType.LongMessagesForbidden;

        boolean isServiceMessage = true;
        if (this instanceof SccpDataMessageImpl)
            isServiceMessage = false;

        if (longMessageRuleType == LongMessageRuleType.LongMessagesForbidden) {
            // use UDT / UDTS
            int fieldsLen = this.sccpStackImpl.calculateUdtFieldsLengthWithoutData(cdp.length, cnp.length);
            int availLen = maxMtp3UserDataLength - fieldsLen;
            if (availLen > 254)
                availLen = 254;
            if (bf.length > availLen) { // message is too long to encode UDT
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format("Failure when sending a UDT message: message is too long. SccpMessageSegment=%s",
                            this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_NOT_SUPPORTED);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLen + bf.length);

            if (isServiceMessage)
                this.type = SccpMessage.MESSAGE_TYPE_UDTS;
            else
                this.type = SccpMessage.MESSAGE_TYPE_UDT;
            out.write(this.type);
            out.write(this.getSecondParamaterData());

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
        } else if (longMessageRuleType == LongMessageRuleType.XudtEnabled) {

            // use XUDT / XUDTS
            if (isServiceMessage)
                this.type = SccpMessage.MESSAGE_TYPE_XUDTS;
            else
                this.type = SccpMessage.MESSAGE_TYPE_XUDT;
            if (this.hopCounter == null)
                this.hopCounter = new HopCounterImpl(15);

            int fieldsLenX = this.sccpStackImpl.calculateXudtFieldsLengthWithoutData(cdp.length, cnp.length, false,
                    this.importance != null);
            int fieldsLen2 = this.sccpStackImpl.calculateXudtFieldsLengthWithoutData2(cdp.length, cnp.length);
            int availLenX = maxMtp3UserDataLength - fieldsLenX;
            if (availLenX > fieldsLen2)
                availLenX = fieldsLen2;
            int fieldsLenXSegm = this.sccpStackImpl.calculateXudtFieldsLengthWithoutData(cdp.length, cnp.length, true,
                    this.importance != null);
            int availLenXSegm = maxMtp3UserDataLength - fieldsLenXSegm;
            if (availLenXSegm > fieldsLen2)
                availLenXSegm = fieldsLen2;

            if (bf.length <= availLenX && bf.length <= this.sccpStackImpl.getZMarginXudtMessage()) {
                // one segment
                ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLenX + bf.length);

                out.write(this.type);

                out.write(this.getSecondParamaterData());
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
                    byte[] b = importance.encode();
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
                if (bf.length <= this.sccpStackImpl.getZMarginXudtMessage() * 16)
                    segmLen = this.sccpStackImpl.getZMarginXudtMessage();
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
                        return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
                    }

                    this.segmentation = new SegmentationImpl(true, this.segmentation.isClass1Selected(), (byte) segmCount,
                            this.segmentation.getSegmentationLocalRef());
                } else {
                    this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) segmCount,
                            this.sccpStackImpl.newSegmentationLocalRef());
                }

                byte[] importanceBuf = null;
                if (importance != null) {
                    importanceBuf = importance.encode();
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

                    out.write(this.getSecondParamaterData());
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
                    byte[] b = segmentation.encode();
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

            if (longMessageRuleType == LongMessageRuleType.LudtEnabled_WithSegmentationField) {
                this.segmentation = new SegmentationImpl(true, this.getIsProtocolClass1(), (byte) 0,
                        this.sccpStackImpl.newSegmentationLocalRef());
            }
            int fieldsLenL = this.sccpStackImpl.calculateLudtFieldsLengthWithoutData(cdp.length, cnp.length,
                    this.segmentation != null, this.importance != null);
            int availLen = maxMtp3UserDataLength - fieldsLenL;
            if (bf.length > availLen) { // message is too long to encode LUDT
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Failure when sending a LUDT message: message is too long. SccpMessageSegment=%s", this));
                }
                return new EncodingResultData(EncodingResult.ReturnFailure, null, null, ReturnCauseValue.SEG_FAILURE);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(fieldsLenL + bf.length);

            out.write(this.type);

            out.write(this.getSecondParamaterData());
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
                byte[] b = segmentation.encode();
                out.write(b.length);
                out.write(b);
            }
            if (importance != null) {
                out.write(Importance.PARAMETER_CODE);
                byte[] b = importance.encode();
                out.write(b.length);
                out.write(b);
            }

            if (optionalPresent)
                out.write(0x00);

            return new EncodingResultData(EncodingResult.Success, out.toByteArray(), null, null);
        }
    }
}
