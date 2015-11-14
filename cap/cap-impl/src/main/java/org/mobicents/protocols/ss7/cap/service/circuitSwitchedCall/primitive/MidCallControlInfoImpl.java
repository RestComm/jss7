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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfo;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class MidCallControlInfoImpl extends SequenceBase implements MidCallControlInfo {

    private static final String MINIMUM_NUMBER_OF_DIGITS = "minimumNumberOfDigits";
    private static final String MAXIMUM_NUMBER_OF_DIGITS = "maximumNumberOfDigits";
    private static final String END_OF_REPLY_DIGIT = "endOfReplyDigit";
    private static final String CANCEL_DIGIT = "cancelDigit";
    private static final String START_DIGIT = "startDigit";
    private static final String INTER_DIGIT_TIMEOUT = "interDigitTimeout";

    public static final int _ID_minimumNumberOfDigits = 0;
    public static final int _ID_maximumNumberOfDigits = 1;
    public static final int _ID_endOfReplyDigit = 2;
    public static final int _ID_cancelDigit = 3;
    public static final int _ID_startDigit = 4;
    public static final int _ID_interDigitTimeout = 6;

    private Integer minimumNumberOfDigits;
    private Integer maximumNumberOfDigits;
    private String endOfReplyDigit;
    private String cancelDigit;
    private String startDigit;
    private Integer interDigitTimeout;

    public MidCallControlInfoImpl() {
        super("MidCallControlInfo");
    }

    public MidCallControlInfoImpl(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit, String startDigit,
            Integer interDigitTimeout) {
        super("MidCallControlInfo");

        this.minimumNumberOfDigits = minimumNumberOfDigits;
        this.maximumNumberOfDigits = maximumNumberOfDigits;
        this.endOfReplyDigit = endOfReplyDigit;
        this.cancelDigit = cancelDigit;
        this.startDigit = startDigit;
        this.interDigitTimeout = interDigitTimeout;
    }

    @Override
    public Integer getMinimumNumberOfDigits() {
        return minimumNumberOfDigits;
    }

    @Override
    public Integer getMaximumNumberOfDigits() {
        return maximumNumberOfDigits;
    }

    @Override
    public String getEndOfReplyDigit() {
        return endOfReplyDigit;
    }

    @Override
    public String getCancelDigit() {
        return cancelDigit;
    }

    @Override
    public String getStartDigit() {
        return startDigit;
    }

    @Override
    public Integer getInterDigitTimeout() {
        return interDigitTimeout;
    }

    protected int encodeNumber(char c, String parameterName) throws CAPException {
        switch (c) {
        case '0':
            return 0;
        case '1':
            return 1;
        case '2':
            return 2;
        case '3':
            return 3;
        case '4':
            return 4;
        case '5':
            return 5;
        case '6':
            return 6;
        case '7':
            return 7;
        case '8':
            return 8;
        case '9':
            return 9;
        case '*':
            return 10;
        case '#':
            return 11;
        default:
            throw new CAPException(this._PrimitiveName + ": Error when encoding parameter " + parameterName
                    + ": as a value must be digits 1-9, * and #, found char: " + c);
        }
    }

    protected char decodeNumber(int i, String parameterName) throws CAPParsingComponentException {
        switch (i) {
        case 0:
            return '0';
        case 1:
            return '1';
        case 2:
            return '2';
        case 3:
            return '3';
        case 4:
            return '4';
        case 5:
            return '5';
        case 6:
            return '6';
        case 7:
            return '7';
        case 8:
            return '8';
        case 9:
            return '9';
        case 10:
            return '*';
        case 11:
            return '#';
        default:
            throw new CAPParsingComponentException(this._PrimitiveName + ": Error when decoding parameter " + parameterName
                    + ": as a value must be digits 1-9, * and #, found code: " + i, CAPParsingComponentExceptionReason.MistypedParameter);

        }
    }

    private String readStringData(AsnInputStream ais, String parameterName) throws CAPParsingComponentException {
        try {
            byte[] val = ais.readOctetString();
            if (val.length < 1 || val.length > 2)
                throw new CAPParsingComponentException(this._PrimitiveName + ": Error when decoding parameter " + parameterName
                        + ": octet string must have length 1-2 bytes, found: " + val.length, CAPParsingComponentExceptionReason.MistypedParameter);

            StringBuilder sb = new StringBuilder();
            for (byte b : val) {
                char ch = decodeNumber(b, parameterName);
                sb.append(ch);
            }
            return sb.toString();
        } catch (AsnException e) {
            throw new CAPParsingComponentException(this._PrimitiveName + ": AsnException when decoding parameter " + parameterName,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (IOException e) {
            throw new CAPParsingComponentException(this._PrimitiveName + ": IOException when decoding parameter " + parameterName,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void writeStringData(AsnOutputStream aos, int tag, String value, String parameterName) throws CAPException {
        if (value.length() < 1 || value.length() > 2)
            throw new CAPException(this._PrimitiveName + ": Error when encoding parameter " + parameterName
                    + ": string must have length 1-2 chars, found: " + value.length());

        byte[] val = new byte[value.length()];
        for (int i1 = 0; i1 < value.length(); i1++) {
            val[i1] = (byte) encodeNumber(value.charAt(i1), parameterName);
        }

        try {
            aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, tag, val);
        } catch (IOException e) {
            throw new CAPException(this._PrimitiveName + ": IOException when encoding parameter " + parameterName);
        } catch (AsnException e) {
            throw new CAPException(this._PrimitiveName + ": AsnException when encoding parameter " + parameterName);
        }
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.minimumNumberOfDigits = null;
        this.maximumNumberOfDigits = null;
        this.endOfReplyDigit = null;
        this.cancelDigit = null;
        this.startDigit = null;
        this.interDigitTimeout = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_minimumNumberOfDigits:
                    this.minimumNumberOfDigits = (int) ais.readInteger();
                    break;
                case _ID_maximumNumberOfDigits:
                    this.maximumNumberOfDigits = (int) ais.readInteger();
                    break;
                case _ID_endOfReplyDigit:
                    this.endOfReplyDigit = this.readStringData(ais, "endOfReplyDigit");
                    break;
                case _ID_cancelDigit:
                    this.cancelDigit = this.readStringData(ais, "cancelDigit");
                    break;
                case _ID_startDigit:
                    this.startDigit = this.readStringData(ais, "startDigit");
                    break;
                case _ID_interDigitTimeout:
                    this.interDigitTimeout = (int) ais.readInteger();
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {
        try {
            if (this.minimumNumberOfDigits != null) {
                if (this.minimumNumberOfDigits < 1 || this.minimumNumberOfDigits > 30)
                    throw new CAPException("IOException when encoding " + _PrimitiveName + ": minimumNumberOfDigits must be from 1 to 30, it is="
                            + minimumNumberOfDigits);
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_minimumNumberOfDigits, this.minimumNumberOfDigits);
            }
            if (this.maximumNumberOfDigits != null) {
                if (this.maximumNumberOfDigits < 1 || this.maximumNumberOfDigits > 30)
                    throw new CAPException("IOException when encoding " + _PrimitiveName + ": maximumNumberOfDigits must be from 1 to 30, it is="
                            + maximumNumberOfDigits);
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_maximumNumberOfDigits, this.maximumNumberOfDigits);
            }

            if (this.endOfReplyDigit != null) {
                writeStringData(aos, _ID_endOfReplyDigit, this.endOfReplyDigit, "endOfReplyDigit");
            }
            if (this.cancelDigit != null) {
                writeStringData(aos, _ID_cancelDigit, this.cancelDigit, "cancelDigit");
            }
            if (this.startDigit != null) {
                writeStringData(aos, _ID_startDigit, this.startDigit, "startDigit");
            }

            if (this.interDigitTimeout != null) {
                if (this.interDigitTimeout < 1 || this.interDigitTimeout > 127)
                    throw new CAPException("IOException when encoding " + _PrimitiveName + ": interDigitTimeout must be from 1 to 127, it is="
                            + interDigitTimeout);
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_interDigitTimeout, this.interDigitTimeout);
            }
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

        if (this.minimumNumberOfDigits != null) {
            sb.append("minimumNumberOfDigits=");
            sb.append(minimumNumberOfDigits);
            sb.append(", ");
        }
        if (this.maximumNumberOfDigits != null) {
            sb.append("maximumNumberOfDigits=");
            sb.append(maximumNumberOfDigits);
            sb.append(", ");
        }
        if (this.endOfReplyDigit != null) {
            sb.append("endOfReplyDigit=\"");
            sb.append(endOfReplyDigit);
            sb.append("\", ");
        }
        if (this.cancelDigit != null) {
            sb.append("cancelDigit=\"");
            sb.append(cancelDigit);
            sb.append("\", ");
        }
        if (this.startDigit != null) {
            sb.append("startDigit=\"");
            sb.append(startDigit);
            sb.append("\", ");
        }
        if (this.interDigitTimeout != null) {
            sb.append("interDigitTimeout=");
            sb.append(interDigitTimeout);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MidCallControlInfoImpl> MID_CALL_CONTROL_INFO_XML = new XMLFormat<MidCallControlInfoImpl>(
            MidCallControlInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MidCallControlInfoImpl midCallControlInfo) throws XMLStreamException {
            midCallControlInfo.minimumNumberOfDigits = xml.get(MINIMUM_NUMBER_OF_DIGITS, Integer.class);
            midCallControlInfo.maximumNumberOfDigits = xml.get(MAXIMUM_NUMBER_OF_DIGITS, Integer.class);
            midCallControlInfo.endOfReplyDigit = xml.get(END_OF_REPLY_DIGIT, String.class);
            midCallControlInfo.cancelDigit = xml.get(CANCEL_DIGIT, String.class);
            midCallControlInfo.startDigit = xml.get(START_DIGIT, String.class);
            midCallControlInfo.interDigitTimeout = xml.get(INTER_DIGIT_TIMEOUT, Integer.class);
        }

        @Override
        public void write(MidCallControlInfoImpl midCallControlInfo, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (midCallControlInfo.minimumNumberOfDigits != null)
                xml.add((Integer) midCallControlInfo.minimumNumberOfDigits, MINIMUM_NUMBER_OF_DIGITS, Integer.class);
            if (midCallControlInfo.maximumNumberOfDigits != null)
                xml.add((Integer) midCallControlInfo.maximumNumberOfDigits, MAXIMUM_NUMBER_OF_DIGITS, Integer.class);
            if (midCallControlInfo.endOfReplyDigit != null)
                xml.add((String) midCallControlInfo.endOfReplyDigit, END_OF_REPLY_DIGIT, String.class);
            if (midCallControlInfo.cancelDigit != null)
                xml.add((String) midCallControlInfo.cancelDigit, CANCEL_DIGIT, String.class);
            if (midCallControlInfo.startDigit != null)
                xml.add((String) midCallControlInfo.startDigit, START_DIGIT, String.class);
            if (midCallControlInfo.interDigitTimeout != null)
                xml.add((Integer) midCallControlInfo.interDigitTimeout, INTER_DIGIT_TIMEOUT, Integer.class);
        }
    };

}
