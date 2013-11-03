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

package org.mobicents.protocols.ss7.map.primitives;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharset;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncodingData;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class USSDStringImpl extends OctetStringBase implements USSDString {

    private CBSDataCodingScheme dataCodingScheme;

    private static GSMCharset gsm7Charset = new GSMCharset("GSM", new String[] {});
    private static GSMCharset gsm7Charset_Urdu = new GSMCharset("GSM", new String[] {}, GSMCharset.BYTE_TO_CHAR_UrduAlphabet,
            GSMCharset.BYTE_TO_CHAR_UrduAlphabetExtentionTable);
    private static Charset ucs2Charset = Charset.forName("UTF-16BE");

    public USSDStringImpl(CBSDataCodingScheme dataCodingScheme) {
        super(1, 160, "USSDString");

        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;
    }

    public USSDStringImpl(byte[] data, CBSDataCodingScheme dataCodingScheme) {
        super(1, 160, "USSDString", data);

        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;
    }

    public USSDStringImpl(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) throws MAPException {
        super(1, 160, "USSDString");

        if (ussdString == null) {
            ussdString = "";
        }
        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;

        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed message
            throw new MAPException("Error encoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    Charset cSet = gsm7Charset;
                    if (dataCodingScheme.getNationalLanguageShiftTable() == CBSNationalLanguage.Arabic) {
                        cSet = gsm7Charset_Urdu;
                    }
                    GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
                    encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData());
                    ByteBuffer bb = null;
                    try {
                        bb = encoder.encode(CharBuffer.wrap(ussdString));
                    } catch (Exception e) {
                        // This can not occur
                    }
                    if (bb != null) {
                        this.data = new byte[bb.limit()];
                        bb.get(this.data);
                    } else
                        this.data = new byte[0];
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                        bb = gsm8Charset.encode(ussdString);
                        this.data = new byte[bb.limit()];
                        bb.get(this.data);
                    } else {
                        throw new MAPException(
                                "Error encoding a text in USSDStringImpl: gsm8Charset is not defined for GSM8 dataCodingScheme");
                    }
                    break;

                case UCS2:
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        if (ussdString.length() < 1)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 2)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 3)
                            ussdString = ussdString + "\n";
                        cSet = gsm7Charset;
                        encoder = (GSMCharsetEncoder) cSet.newEncoder();
                        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData());
                        bb = null;
                        try {
                            String sb = ussdString.substring(0, 3);
                            bb = encoder.encode(CharBuffer.wrap(sb));
                        } catch (Exception e) {
                            // This can not occur
                        }
                        byte[] buf1;
                        if (bb != null) {
                            buf1 = new byte[bb.limit()];
                            bb.get(buf1);
                        } else
                            buf1 = new byte[0];

                        String sb2 = ussdString.substring(3);
                        bb = ucs2Charset.encode(sb2);
                        this.data = new byte[buf1.length + bb.limit()];
                        System.arraycopy(buf1, 0, this.data, 0, buf1.length);
                        bb.get(this.data, buf1.length, this.data.length - buf1.length);
                    } else {
                        bb = ucs2Charset.encode(ussdString);
                        this.data = new byte[bb.limit()];
                        bb.get(this.data);
                    }
                    break;
            }
        }
    }

    public byte[] getEncodedString() {
        return this.data;
    }

    public String getString(Charset gsm8Charset) throws MAPException {

        String res = "";

        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        if (this.data == null) {
            throw new MAPException("Error decoding a text in USSDStringImpl: encoded data can not be null");
        }

        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed sms message
            throw new MAPException("Error decoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    GSMCharset cSet = gsm7Charset;
                    if (dataCodingScheme.getNationalLanguageShiftTable() == CBSNationalLanguage.Arabic) {
                        cSet = gsm7Charset_Urdu;
                    }
                    GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
                    decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData());
                    ByteBuffer bb = ByteBuffer.wrap(this.data);
                    CharBuffer bf = null;
                    try {
                        bf = decoder.decode(bb);
                    } catch (CharacterCodingException e) {
                        // This can not occur
                    }
                    if (bf != null)
                        res = bf.toString();
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                        byte[] buf = this.data;
                        bb = ByteBuffer.wrap(buf);
                        bf = gsm8Charset.decode(bb);
                        res = bf.toString();
                    }
                    break;

                case UCS2:
                    String pref = "";
                    byte[] buf = this.data;
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        cSet = gsm7Charset;
                        decoder = (GSMCharsetDecoder) cSet.newDecoder();
                        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData());
                        byte[] buf2 = new byte[3];
                        if (this.data.length < 3)
                            buf2 = new byte[this.data.length];
                        System.arraycopy(this.data, 0, buf2, 0, buf2.length);
                        bb = ByteBuffer.wrap(buf2);
                        bf = null;
                        try {
                            bf = decoder.decode(bb);
                        } catch (CharacterCodingException e) {
                            // This can not occur
                        }
                        if (bf != null)
                            pref = bf.toString();

                        if (this.data.length <= 3) {
                            buf = new byte[0];
                        } else {
                            buf = new byte[this.data.length - 3];
                            System.arraycopy(this.data, 3, buf, 0, buf.length);
                        }

                        bb = ByteBuffer.wrap(buf);
                        bf = ucs2Charset.decode(bb);
                        res = pref + bf.toString();
                    } else {
                        bb = ByteBuffer.wrap(buf);
                        bf = ucs2Charset.decode(bb);
                        res = bf.toString();
                    }
                    break;
            }
        }

        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        try {
            String s1 = this.getString(null);
            sb.append(s1);
        } catch (MAPException e) {
        }

        if (this.dataCodingScheme != null) {
            sb.append(", dcs=");
            sb.append(dataCodingScheme);
        }

        sb.append("]");

        return sb.toString();
    }
}
