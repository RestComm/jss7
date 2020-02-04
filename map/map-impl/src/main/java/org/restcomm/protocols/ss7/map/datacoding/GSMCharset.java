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

package org.restcomm.protocols.ss7.map.datacoding;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;

/**
 * <p>
 * The encoding/decoding of 7 bits characters in USSD strings is used doing GSMCharset.
 * </p>
 * <br/>
 * <p>
 * For further details look at GSM 03.38 Specs
 * </p>
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class GSMCharset extends Charset {

    public static final String GSM_CANONICAL_NAME = "GSM";

    protected static final float averageCharsPerByte = 8 / 7f;
    protected static final float maxCharsPerByte = 2f;

    protected static final float averageBytesPerChar = 2f;
    protected static final float maxBytesPerChar = 2f;

    protected static final int BUFFER_SIZE = 256;

    public static final byte ESCAPE = 0x1B;

    protected int[] mainTable;
    protected int[] extensionTable;

    public GSMCharset(String canonicalName, String[] aliases) {
        this(canonicalName, aliases, basicMap,basicExtentionMap);
    }

    public GSMCharset(String canonicalName, String[] aliases, int[] mainTable, int[] extentionTable) {
        super(canonicalName, aliases);

        this.mainTable = mainTable;
        this.extensionTable = extentionTable;
    }

    public GSMCharset(String canonicalName, String[] aliases,
            NationalLanguageIdentifier nationalLanguageLockingShiftIdentifier,
            NationalLanguageIdentifier nationalLanguageSingleShiftIdentifier) {
        super(canonicalName, aliases);

        if (nationalLanguageLockingShiftIdentifier == null) {
            this.mainTable = basicMap;
        } else {
            switch (nationalLanguageLockingShiftIdentifier) {
                case Spanish:
                    this.mainTable = basicMap;
                    break;
                case Portuguese:
                    this.mainTable = portugeseMap;
                    break;
                case Turkish:
                    this.mainTable = turkishMap;
                    break;
                case Urdu:
                    this.mainTable = urduMap;
                    break;
                case Hindi:
                    this.mainTable = hindiMap;
                    break;
                case Bengali:
                    this.mainTable = bengaliMap;
                    break;
                case Punjabi:
                    this.mainTable = punjabiMap;
                    break;
                case Gujarati:
                    this.mainTable = gujaratiMap;
                    break;
                case Oriya:
                    this.mainTable = oriyaMap;
                    break;
                case Tamil:
                    this.mainTable = tamilMap;
                    break;
                case Telugu:
                    this.mainTable = teluguMap;
                    break;
                case Kannada:
                    this.mainTable = kannadaMap;
                    break;
                case Malayalam:
                    this.mainTable = malayalamMap;
                    break;
                default:
                    this.mainTable = basicMap;
                    break;
            }
        }
        if (nationalLanguageSingleShiftIdentifier == null) {
            this.extensionTable = basicExtentionMap;
        } else
            switch (nationalLanguageSingleShiftIdentifier) {
                case Spanish:
                    this.mainTable = spanishExtentionMap;
                    break;
                case Portuguese:
                    this.mainTable = portugeseExtentionMap;
                    break;
                case Turkish:
                    this.mainTable = turkishExtentionMap;
                    break;
                case Urdu:
                    this.mainTable = urduExtentionMap;
                    break;
                case Hindi:
                    this.mainTable = hindiExtentionMap;
                    break;
                case Bengali:
                    this.mainTable = bengaliExtentionMap;
                    break;
                case Punjabi:
                    this.mainTable = punjabiExtentionMap;
                    break;
                case Gujarati:
                    this.mainTable = gujaratiExtentionMap;
                    break;
                case Oriya:
                    this.mainTable = oriyaExtentionMap;
                    break;
                case Tamil:
                    this.mainTable = tamilExtentionMap;
                    break;
                case Telugu:
                    this.mainTable = teluguExtentionMap;
                    break;
                case Kannada:
                    this.mainTable = kannadaExtentionMap;
                    break;
                case Malayalam:
                    this.mainTable = malayalamExtentionMap;
                    break;
                default:
                    this.extensionTable = basicExtentionMap;
                    break;
            }
    }

    @Override
    public boolean contains(Charset cs) {
        return this.getClass().isInstance(cs);
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new GSMCharsetDecoder(this, averageCharsPerByte, maxCharsPerByte);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new GSMCharsetEncoder(this, averageBytesPerChar, maxBytesPerChar);
    }

    /**
     * Returns true if all characters in data String is included in main and extension encoding tables of the GSM7 charset
     *
     * @param data
     * @return
     */
    public boolean checkAllCharsCanBeEncoded(String data) {
        return checkAllCharsCanBeEncoded(data, this.mainTable, this.extensionTable);
    }

    /**
     * Returns true if all characters in data String is included in main and extension encoding tables of the GSM7 charset
     *
     * @param data
     * @return
     */
    public static boolean checkAllCharsCanBeEncoded(String data, int[] mainTable, int[] extentionTable) {
        if (data == null)
            return true;

        if (mainTable == null)
            return false;

        for (int i1 = 0; i1 < data.length(); i1++) {
            char c = data.charAt(i1);

            boolean found = false;
            for (int i = 0; i < mainTable.length; i++) {
                if (mainTable[i] == c) {
                    found = true;
                    break;
                }
            }
            if (!found && extentionTable != null) {
                for (int i = 0; i < extentionTable.length; i++) {
                    if (c != 0 && extentionTable[i] == c) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
                return false;
        }

        return true;
    }

    /**
     * Returns a count in characters / septets of the data String after which the String will be GSM7 style encoded. For all
     * characters from the extension character table two bytes will be reserved. For all characters from the main character
     * table or which are not present in main or extension character tables one byte will be reserved.
     *
     * @param data
     * @return
     */
    public int checkEncodedDataLengthInChars(String data) {
        return checkEncodedDataLengthInChars(data, this.mainTable, this.extensionTable);
    }

    /**
     * Returns a count in characters / septets of the data String after which the String will be GSM7 style encoded. For all
     * characters from the extension character table two bytes will be reserved. For all characters from the main character
     * table or which are not present in main or extension character tables one byte will be reserved.
     *
     * @param data
     * @return
     */
    public static int checkEncodedDataLengthInChars(String data, int[] mainTable, int[] extentionTable) {
        if (data == null)
            return 0;

        if (mainTable == null)
            return 0;

        int cnt = 0;
        for (int i1 = 0; i1 < data.length(); i1++) {
            char c = data.charAt(i1);

            boolean found = false;
            for (int i = 0; i < mainTable.length; i++) {
                if (mainTable[i] == c) {
                    found = true;
                    cnt++;
                    break;
                }
            }
            if (!found && extentionTable != null) {
                for (int i = 0; i < extentionTable.length; i++) {
                    if (c != 0 && extentionTable[i] == c) {
                        found = true;
                        cnt += 2;
                        break;
                    }
                }
            }
            if (!found)
                cnt++;
        }

        return cnt;
    }

    /**
     * Calculates how many octets encapsulate the provides septets count.
     *
     * @param data
     * @return
     */
    public static int septetsToOctets(int septCnt) {
        int byteCnt = (septCnt + 1) * 7 / 8;
        return byteCnt;
    }

    /**
     * Calculates how many septets are encapsulated in the provides octets count.
     *
     * @param data
     * @return
     */
    public static int octetsToSeptets(int byteCnt) {
        int septCnt = (byteCnt * 8 - 1) / 7 + 1;
        return septCnt;
    }

    /**
     * Slicing of a data String into substrings that fits to characters / septets count in charCount parameter.
     *
     * @param data
     * @return
     */
    public String[] sliceString(String data, int charCount) {
        return sliceString(data, charCount, this.mainTable, this.extensionTable);
    }

    /**
     * Slicing of a data String into substrings that fits to characters / septets count in charCount parameter.
     *
     * @param data
     * @return
     */
    public static String[] sliceString(String data, int charCount, int[] mainTable, int[] extentionTable) {
        if (data == null)
            return null;

        if (mainTable == null)
            return null;

        ArrayList<String> res = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        int chCnt = 0;
        for (int i1 = 0; i1 < data.length(); i1++) {
            char c = data.charAt(i1);

            boolean found = false;
            for (int i = 0; i < mainTable.length; i++) {
                if (mainTable[i] == c) {
                    found = true;
                    chCnt++;
                    if (chCnt > charCount) {
                        chCnt = 1;
                        res.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    sb.append(c);
                    break;
                }
            }
            if (!found && extentionTable != null) {
                for (int i = 0; i < extentionTable.length; i++) {
                    if (extentionTable[i] == c) {
                        found = true;
                        chCnt += 2;
                        if (chCnt > charCount) {
                            chCnt = 2;
                            res.add(sb.toString());
                            sb = new StringBuilder();
                        }
                        sb.append(c);
                        break;
                    }
                }
            }
            if (!found) {
                chCnt++;
                if (chCnt > charCount) {
                    chCnt = 1;
                    res.add(sb.toString());
                    sb = new StringBuilder();
                }
                sb.append(c);
            }
        }

        res.add(sb.toString());
        String[] arr = new String[res.size()];
        res.toArray(arr);
        return arr;
    }

    public static final  int[] basicMap = new int[] {(int)'@',(int)'£',(int)'$',(int)'¥',(int)'è',(int)'é',(int)'ù',(int)'ì',(int)'ò',(int)'Ç',(int)'\n',(int)'Ø',(int)'ø',(int)'\r',(int)'Å',(int)'å',(int)'Δ',(int)'_',(int)'Φ',(int)'Γ',(int)'Λ',(int)'Ω',(int)'Π',(int)'Ψ',(int)'Σ',(int)'Θ',(int)'Ξ',0xffff,(int)'Æ',(int)'æ',(int)'ß',(int)'É',(int)' ',(int)'!',(int)'"',(int)'#',(int)'¤',(int)'%',(int)'&',(int)'\'',(int)'(',(int)')',(int)'*',(int)'+',(int)',',(int)'-',(int)'.',(int)'/',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';',(int)'<',(int)'=',(int)'>',(int)'?',(int)'¡',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z',(int)'Ä',(int)'Ö',(int)'Ñ',(int)'Ü',(int)'§',(int)'¿',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',(int)'ä',(int)'ö',(int)'ñ',(int)'ü',(int)'à'};
    public static final int[] basicExtentionMap = { 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x000C, 0x0000, 0x0000, 0x000D, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'^', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'{', (int)'}', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetDefault = new GSMCharset(GSM_CANONICAL_NAME, new String[0], basicMap,basicExtentionMap);

    public static final int[] spanishExtentionMap = { 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ç', 0x000C, 0x0000, 0x0000, 0x000D, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'^', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'{', (int)'}', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|', (int)'Á', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,(int)'Í', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ó', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ú', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'á', 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, (int)'í', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ó', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ú', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetSpanish = new GSMCharset(GSM_CANONICAL_NAME, new String[0], basicMap,spanishExtentionMap);

    public static final  int[] portugeseMap = new int[] {(int)'@',(int)'£',(int)'$',(int)'¥',(int)'ê',(int)'é',(int)'ú',(int)'í',(int)'ó',(int)'ç',(int)'\n',(int)'Ô',(int)'ô',(int)'\r',(int)'Á',(int)'á',(int)'Δ',(int)'_',(int)'ª',(int)'Ç',(int)'À',(int)'∞',(int)'^',(int)'\\',(int)'€',(int)'Ó',(int)'|',0xffff,(int)'Â',(int)'â',(int)'Ê',(int)'É',(int)' ',(int)'!',(int)'"',(int)'#',(int)'º',(int)'%',(int)'&',(int)'\'',(int)'(',(int)')',(int)'*',(int)'+',(int)',',(int)'-',(int)'.',(int)'/',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';',(int)'<',(int)'=',(int)'>',(int)'?',(int)'Í',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z',(int)'Ã',(int)'Õ',(int)'Ú',(int)'Ü',(int)'§',(int)'~',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',(int)'ã',(int)'õ',(int)'`',(int)'ü',(int)'à'};
    public static final int[] portugeseExtentionMap = { 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ê', 0x0000, 0x0000, 0x0000, (int)'ç', 0x000C, (int)'Ô', (int)'ô', 0x000D, (int)'Á', (int)'á', 0x0000, 0x0000, (int)'Φ', (int)'Γ', (int)'^', (int)'Ω', (int)'Π', (int)'Ψ', (int)'Σ', (int)'Θ', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ê', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'{', (int)'}', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|', (int)'À', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Í', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ó', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ú', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ã', (int)'Õ', 0x0000, 0x0000, 0x0000, 0x0000, (int)'Â', 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, (int)'í', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ó', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ú', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ã', (int)'õ', 0x0000, 0x0000, (int)'â'};
    public static Charset gsm7CharsetPortugese = new GSMCharset(GSM_CANONICAL_NAME, new String[0], portugeseMap,portugeseExtentionMap);

    public static final  int[] turkishMap = new int[] {(int)'@',(int)'£',(int)'$',(int)'¥',(int)'€',(int)'é',(int)'ù',(int)'ı',(int)'ò',(int)'Ç',(int)'\n',(int)'Ğ',(int)'ğ',(int)'\r',(int)'Å',(int)'å',(int)'Δ',(int)'_',(int)'Φ',(int)'Γ',(int)'Λ',(int)'Ω',(int)'Π',(int)'Ψ',(int)'Σ',(int)'Θ',(int)'Ξ',0xffff,(int)'Ş',(int)'ş',(int)'ß',(int)'É',(int)' ',(int)'!',(int)'"',(int)'#',(int)'¤',(int)'%',(int)'&',(int)'\'',(int)'(',(int)')',(int)'*',(int)'+',(int)',',(int)'-',(int)'.',(int)'/',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';',(int)'<',(int)'=',(int)'>',(int)'?',(int)'İ',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z',(int)'Ä',(int)'Ö',(int)'Ñ',(int)'Ü',(int)'§',(int)'ç',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',(int)'ä',(int)'ö',(int)'ñ',(int)'ü',(int)'à'};
    public static final int[] turkishExtentionMap = { 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x000C, 0x0000, 0x0000, 0x000D, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'^', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'{', (int)'}', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,(int)'Ğ', 0x0000,(int)'İ', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'Ş', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ç', 0x0000, (int)'€', 0x0000, (int)'ğ', 0x0000, (int)'ı', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'ş', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetTurkish = new GSMCharset(GSM_CANONICAL_NAME, new String[0], turkishMap,turkishExtentionMap);

    public static final int[] urduMap = new int[] { (int)'ا',(int)'آ',(int)'ب',(int)'ٻ',(int)'ڀ',(int)'پ',(int)'ڦ',(int)'ت',(int)'ۂ',(int)'ٿ',(int)'\n',(int)'ٹ',(int)'ٽ',(int)'\r',(int)'ٺ',(int)'ټ',(int)'ث',(int)'ج',(int)'ځ',(int)'ڄ',(int)'ڃ',(int)'څ',(int)'چ',(int)'ڇ',(int)'ح',(int)'خ',(int)'د',0xffff,(int)'ڌ',(int)'ڈ',(int)'ډ',(int)'ڊ',(int)' ',(int)'!',(int)'ڏ',(int)'ڍ',(int)'ذ',(int)'ر',(int)'ڑ',(int)'ړ',(int)')',(int)'(',(int)'ڙ',(int)'ز',(int)',',(int)'ږ',(int)'.',(int)'ژ',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';',(int)'ښ',(int)'س',(int)'ش',(int)'?',(int)'ص',(int)'ض',(int)'ط',(int)'ظ',(int)'ع',(int)'ف',(int)'ق',(int)'ک',(int)'ڪ',(int)'ګ',(int)'گ',(int)'ڳ',(int)'ڱ',(int)'ل',(int)'م',(int)'ن',(int)'ں',(int)'ڻ',(int)'ڼ',(int)'و',(int)'ۄ',(int)'ە',(int)'ہ',(int)'ھ',(int)'ء',(int)'ی',(int)'ې',(int)'ے',0x064D, 0x0650, 0x064F, 0x0657, 0x0657,(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',0x0655, 0x0651, 0x0653, 0x0656, 0x0670};
    public static final int[] urduExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', 0x0600, 0x0601, 0x0000, (int)'۰', (int)'۱', (int)'۲', (int)'۳', (int)'۴', (int)'۵',(int)'۶', (int)'۷', (int)'۸', (int)'۹', (int)'،', (int)'؍', (int)'{', (int)'}', (int)'؎', (int)'؏', 0x0610, 0x0611, 0x0612, (int)'\\',0x0613, 0x0614, (int)'؛', (int)'؟', (int)'ـ', 0x0652, 0x0658, (int)'٫', (int)'٬', (int)'ٲ', (int)'ٳ', (int)'ۍ', (int)'[', (int)'~',(int)']', (int)'۔', (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetUrdu = new GSMCharset(GSM_CANONICAL_NAME, new String[0], urduMap, urduExtentionMap);

    public static final int[] hindiMap = new int[] { 0x0901, 0x0902, 0x0903, (int)'अ',(int)'आ',(int)'इ',(int)'ई',(int)'उ',(int)'ऊ',(int)'ऋ',(int)'\n',(int)'ऌ',(int)'ऍ',(int)'\r',(int)'ऎ',(int)'ए',(int)'ऐ',(int)'ऑ',(int)'ऒ',(int)'ओ',(int)'औ',(int)'क',(int)'ख',(int)'ग',(int)'घ',(int)'ङ',(int)'च',0xffff,(int)'छ',(int)'ज',(int)'झ',(int)'ञ',(int)' ',(int)'!',(int)'ट',(int)'ठ',(int)'ड',(int)'ढ',(int)'ण',(int)'त',(int)')',(int)'(',(int)'थ',(int)'द',(int)',',(int)'ध',(int)'.',(int)'न',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';',(int)'ऩ',(int)'प',(int)'फ',(int)'?',(int)'ब',(int)'भ',(int)'म',(int)'य',(int)'र',(int)'ऱ',(int)'ल',(int)'ळ',(int)'ऴ',(int)'व',(int)'श',(int)'ष',(int)'स',(int)'ह', 0x093C,(int)'ऽ', 0x093E, 0x093F, 0x0940, 0x0941, 0x0942, 0x0943, 0x0944, 0x0945, 0x0946, 0x0947, 0x0948, 0x0949, 0x094A, 0x094B, 0x094C, 0x094D, (int)'ॐ',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',(int)'ॲ', (int)'ॻ', (int)'ॼ', (int)'ॾ', (int)'ॿ'};
    public static final int[] hindiExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'०', (int)'१', (int)'२', (int)'३', (int)'४', (int)'५',(int)'६', (int)'७', (int)'८', (int)'९', 0x0951, 0x0952, (int)'{', (int)'}', 0x0953, 0x0954, 0x0958, 0x0959, 0x095A, (int)'\\',0x095B, 0x095C, 0x095D, 0x095E, 0x095F, (int)'ॠ', (int)'ॡ', 0x0962, 0x0963, (int)'॰', (int)'ॱ', 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetHindi = new GSMCharset(GSM_CANONICAL_NAME, new String[0], hindiMap, hindiExtentionMap);

    public static final int[] bengaliMap = new int[] { 0x0981, 0x0982, 0x0983, (int)'অ',(int)'আ',(int)'ই',(int)'ঈ',(int)'উ',(int)'ঊ',(int)'ঋ',(int)'\n',(int)'ঌ',0x0000,(int)'\r',0x00,(int)'এ',(int)'ঐ', 0x0000, 0x0000,(int)'ও',(int)'ঔ',(int)'ক',(int)'খ',(int)'গ',(int)'ঘ',(int)'ঙ',(int)'চ',0xffff,(int)'ছ',(int)'জ',(int)'ঝ',(int)'ঞ',(int)' ',(int)'!',(int)'ট',(int)'ঠ',(int)'ড',(int)'ঢ',(int)'ণ',(int)'ত',(int)')',(int)'(',(int)'থ',(int)'দ',(int)',',(int)'ধ',(int)'.',(int)'ন',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000,(int)'প',(int)'ফ',(int)'?', 0x09AC,(int)'ভ',(int)'ম',(int)'য',(int)'র', 0x0000,(int)'ল', 0x0000, 0x0000, 0x0000,(int)'শ',(int)'ষ',(int)'স',(int)'হ', 0x09BC,(int)'ঽ', 0x09BE, 0x09BF, 0x09C0, 0x09C1, 0x09C2, 0x09C3, 0x09C4, 0x0000, 0x0000, 0x09C7, 0x09C8, 0x0000, 0x0000, 0x09CB, 0x09CC, 0x09CD, (int)'ৎ',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',0x09D7, 0x09DC, 0x09DD, (int)'ৰ', (int)'ৱ'};
    public static final int[] bengaliExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'০', (int)'১', 0x0000, (int)'২', (int)'৩', (int)'৪', (int)'৫', (int)'৬', (int)'৭',(int)'৮', (int)'৯', 0x09DF, 0x09E0, 0x09E1, 0x09E2, (int)'{', (int)'}', 0x09E3, (int)'৲', (int)'৳', (int)'৴', (int)'৵', (int)'\\',(int)'৶', (int)'৷', (int)'৸', (int)'৹', (int)'৺', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetBengali = new GSMCharset(GSM_CANONICAL_NAME, new String[0], bengaliMap, bengaliExtentionMap);

    public static final int[] punjabiMap = new int[] { 0x0A01, 0x0A02, 0x0A03, (int)'ਅ',(int)'ਆ',(int)'ਇ',(int)'ਈ',(int)'ਉ',(int)'ਊ', 0x0000,(int)'\n', 0x000,0x0000,(int)'\r',0x0000,(int)'ਏ',(int)'ਐ', 0x0000, 0x0000,(int)'ਓ',(int)'ਔ',(int)'ਕ',(int)'ਖ',(int)'ਗ',(int)'ਘ',(int)'ਙ',(int)'ਚ',0xffff,(int)'ਛ',(int)'ਜ',(int)'ਝ',(int)'ਞ',(int)' ',(int)'!',(int)'ਟ',(int)'ਠ',(int)'ਡ',(int)'ਢ',(int)'ਣ',(int)'ਤ',(int)')',(int)'(',(int)'ਥ',(int)'ਦ',(int)',',(int)'ਧ',(int)'.',(int)'ਨ',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000,(int)'ਪ',(int)'ਫ',(int)'?',(int)'ਬ',(int)'ਭ',(int)'ਮ',(int)'ਯ',(int)'ਰ', 0x0000,(int)'ਲ', 0x0A33, 0x0000, (int)'ਵ', 0x0A36, 0x0000,(int)'ਸ',(int)'ਹ', 0x0A3C, 0x0000, 0x0A3E, 0x0A3F, 0x0A40, 0x0A41, 0x0A42, 0x0000, 0x0000, 0x0000, 0x0000, 0x0A47, 0x0A48, 0x0000, 0x0000, 0x0A4B, 0x0A4C, 0x0A4D, 0x0A51,(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',0x0A70, 0x0A71, (int)'ੲ', (int)'ੳ', (int)'ੴ'};
    public static final int[] punjabiExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'੦', (int)'੧', (int)'੨', (int)'੩', (int)'੪', (int)'੫',(int)'੬', (int)'੭', (int)'੮', (int)'੯', 0x0A59, 0x0A5A, (int)'{', (int)'}', 0x0A5B, (int)'ੜ', 0x0A5E, 0x0A75, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetPunjabi = new GSMCharset(GSM_CANONICAL_NAME, new String[0], punjabiMap, punjabiExtentionMap);

    public static final int[] gujaratiMap = new int[] { 0x0A81, 0x0A82, 0x0A83, (int)'અ',(int)'આ',(int)'ઇ',(int)'ઈ',(int)'ઉ',(int)'ઊ', (int)'ઋ',(int)'\n', (int)'ઌ',(int)'ઍ',(int)'\r',0x0000,(int)'એ',(int)'ઐ', (int)'ઑ', 0x0000,(int)'ઓ',(int)'ઔ',(int)'ક',(int)'ખ',(int)'ગ',(int)'ઘ',(int)'ઙ',(int)'ચ',0xffff,(int)'છ',(int)'જ',(int)'ઝ',(int)'ઞ',(int)' ',(int)'!',(int)'ટ',(int)'ઠ',(int)'ડ',(int)'ઢ',(int)'ણ',(int)'ત',(int)')',(int)'(',(int)'થ',(int)'દ',(int)',',(int)'ધ',(int)'.',(int)'ન',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000,(int)'પ',(int)'ફ',(int)'?',(int)'બ',(int)'ભ',(int)'મ',(int)'ય',(int)'ર', 0x0000,(int)'લ', (int)'ળ', 0x0000, (int)'વ', (int)'શ', (int)'ષ',(int)'સ',(int)'હ', 0x0ABC, (int)'ઽ', 0x0ABE, 0x0ABF, 0x0AC0, 0x0AC1, 0x0AC2, 0x0AC3, 0x0AC4, 0x0AC5, 0x0000, 0x0AC7, 0x0AC8, 0x0AC9, 0x0000, 0x0ACB, 0x0ACC, 0x0ACD, (int)'ૐ',(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z',(int)'ૠ', (int)'ૡ', 0x0AE2, 0x0AE3, (int)'૱'};
    public static final int[] gujaratiExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'૦', (int)'૧', (int)'૨', (int)'૩', (int)'૪', (int)'૫',(int)'૬', (int)'૭', (int)'૮', (int)'૯', 0x0000, 0x0000, (int)'{', (int)'}', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetGujarati = new GSMCharset(GSM_CANONICAL_NAME, new String[0], gujaratiMap, gujaratiExtentionMap);

    public static final int[] oriyaMap = new int[] { 0x0B01, 0x0B02, 0x0B03, (int)'ଅ',(int)'ଆ',(int)'ଇ',(int)'ଈ',(int)'ଉ',(int)'ଊ', (int)'ଋ',(int)'\n', (int)'ଌ', 0x0000,(int)'\r',0x0000,(int)'ଏ',(int)'ଐ', 0x0000, 0x0000,(int)'ଓ',(int)'ଔ',(int)'କ',(int)'ଖ',(int)'ଗ',(int)'ଘ',(int)'ଙ',(int)'ଚ',0xffff,(int)'ଛ',(int)'ଜ',(int)'ଝ',(int)'ଞ',(int)' ',(int)'!',(int)'ଟ',(int)'ଠ',(int)'ଡ',(int)'ଢ',(int)'ଣ',(int)'ତ',(int)')',(int)'(',(int)'ଥ',(int)'ଦ',(int)',',(int)'ଧ',(int)'.',(int)'ନ',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000,(int)'ପ',(int)'ଫ',(int)'?',(int)'ବ',(int)'ଭ',(int)'ମ',(int)'ଯ',(int)'ର', 0x0000,(int)'ଲ', (int)'ଳ', 0x0000, (int)'ଵ', (int)'ଶ', (int)'ଷ',(int)'ସ',(int)'ହ', 0x0B3C, (int)'ଽ', 0x0B3E, 0x0B3F, 0x0B40, 0x0B41, 0x0B42, 0x0B43, 0x0B44, 0x0000, 0x0000, 0x0B47, 0x0B48, 0x0000, 0x0000, 0x0B4B, 0x0B4C, 0x0B4D, 0x0B56,(int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z', 0x0B57, (int)'ୠ', (int)'ୡ', 0x0B62, 0x0B63};
    public static final int[] oriyaExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'୦', (int)'୧', (int)'୨', (int)'୩', (int)'୪', (int)'୫',(int)'୬', (int)'୭', (int)'୮', (int)'୯', 0x0B5C, 0x0B5D, (int)'{', (int)'}', (int)'ୟ', (int)'୰', (int)'ୱ', 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetOriya = new GSMCharset(GSM_CANONICAL_NAME, new String[0], oriyaMap, oriyaExtentionMap);

    public static final int[] tamilMap = new int[] { 0x00, 0x0B82, 0x0B83, (int)'அ',(int)'ஆ',(int)'இ',(int)'ஈ',(int)'உ',(int)'ஊ', 0x0000,(int)'\n', 0x0000, 0x0000,(int)'\r', (int)'எ',(int)'ஏ',(int)'ஐ', 0x0000, (int)'ஒ', (int)'ஓ',(int)'ஔ',(int)'க', 0x0000, 0x0000, 0x0000,(int)'ங',(int)'ச',0xffff, 0x0000,(int)'ஜ', 0x0000, (int)'ஞ', (int)' ',(int)'!',(int)'ட', 0x0000, 0x0000, 0x0000, (int)'ண',(int)'த',(int)')',(int)'(', 0x0000, 0x0000,(int)',', 0x0000, (int)'.', (int)'ந',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', (int)'ன', (int)'ப', 0x0000,(int)'?', 0x0000, 0x0000,(int)'ம',(int)'ய',(int)'ர', (int)'ற', (int)'ல', (int)'ள', (int)'ழ', (int)'வ', (int)'ஶ', (int)'ஷ',(int)'ஸ',(int)'ஹ', 0x0000, 0x0000, 0x0BBE, 0x0BBF, 0x0BC0, 0x0BC1, 0x0BC2, 0x0000, 0x0000, 0x0000, 0x0BC6, 0x0BC7, 0x0BC8, 0x0000, 0x0BCA, 0x0BCB, 0x0BCC, 0x0BCD, (int)'ௐ', (int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z', 0x0BD7, (int)'௰', (int)'௱', (int)'௲', (int)'௹'};
    public static final int[] tamilExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'௦', (int)'௧', (int)'௨', (int)'௩', (int)'௪', (int)'௫',(int)'௬', (int)'௭', (int)'௮', (int)'௯', (int)'௳', (int)'௴', (int)'{', (int)'}', (int)'௵', (int)'௶', (int)'௷', (int)'௸', (int)'௺', (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetTamil = new GSMCharset(GSM_CANONICAL_NAME, new String[0], tamilMap, tamilExtentionMap);

    public static final int[] teluguMap = new int[] { 0x0C01, 0x0C02, 0x0C03, (int)'అ',(int)'ఆ',(int)'ఇ',(int)'ఈ',(int)'ఉ',(int)'ఊ', (int)'ఋ',(int)'\n', (int)'ఌ', 0x0000,(int)'\r', (int)'ఎ',(int)'ఏ',(int)'ఐ', 0x0000, (int)'ఒ', (int)'ఓ',(int)'ఔ',(int)'క', (int)'ఖ', (int)'గ', (int)'ఘ',(int)'ఙ',(int)'చ',0xffff, (int)'ఛ',(int)'జ', (int)'ఝ', (int)'ఞ', (int)' ',(int)'!',(int)'ట', (int)'ఠ', (int)'డ', (int)'ఢ', (int)'ణ',(int)'త',(int)')',(int)'(', (int)'థ', (int)'ద',(int)',', (int)'ధ', (int)'.', (int)'న',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000, (int)'ప', (int)'ఫ',(int)'?', (int)'బ', (int)'భ',(int)'మ',(int)'య',(int)'ర', (int)'ఱ', (int)'ల', (int)'ళ', 0x0000, (int)'వ', (int)'శ', (int)'ష',(int)'స',(int)'హ', 0x0000, (int)'ఽ', 0x0C3E, 0x0C3F, 0x0C40, 0x0C41, 0x0C42, 0x0C43, 0x0C44, 0x0000, 0x0C46, 0x0C47, 0x0C48, 0x0000, 0x0C4A, 0x0C4B, 0x0C4C, 0x0C4D, 0x0C55, (int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z', 0x0C56, (int)'ౠ', (int)'ౡ', 0x0C62, 0x0C63};
    public static final int[] teluguExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', 0x0000, 0x0000, 0x0000, (int)'౦', (int)'౧', (int)'౨', (int)'౩', (int)'౪', (int)'౫',(int)'౬', (int)'౭', (int)'౮', (int)'౯', (int)'ౘ', (int)'ౙ', (int)'{', (int)'}', (int)'౸', (int)'౹', (int)'౺', (int)'౻', (int)'౼', (int)'\\', (int)'౽', (int)'౾', (int)'౿', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetTelugu = new GSMCharset(GSM_CANONICAL_NAME, new String[0], teluguMap, teluguExtentionMap);

    public static final int[] kannadaMap = new int[] { 0x00, (int)'ಂ', (int)'ಃ', (int)'ಅ',(int)'ಆ',(int)'ಇ',(int)'ಈ',(int)'ಉ',(int)'ಊ', (int)'ಋ', (int)'\n', (int)'ಌ', 0x0000,(int)'\r', (int)'ಎ',(int)'ಏ',(int)'ಐ', 0x0000, (int)'ಒ', (int)'ಓ',(int)'ಔ',(int)'ಕ', (int)'ಖ', (int)'ಗ', (int)'ಘ', (int)'ಙ',(int)'ಚ', 0xffff, (int)'ಛ',(int)'ಜ', (int)'ಝ', (int)'ಞ', (int)' ',(int)'!',(int)'ಟ', (int)'ಠ', (int)'ಪ', (int)'ಢ', (int)'ಣ',(int)'ತ',(int)')',(int)'(', (int)'ಥ', (int)'ದ',(int)',', (int)'ಧ', (int)'.', (int)'ನ',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000, (int)'ಪ', (int)'ಫ',(int)'?', (int)'ಬ', (int)'ಭ',(int)'ಮ',(int)'ಯ',(int)'ರ', (int)'ಱ', (int)'ಲ', (int)'ಳ', 0x0000, (int)'ವ', (int)'ಶ', (int)'ಷ',(int)'ಸ',(int)'ಹ', (int)'಼', (int)'ಽ', (int)'ಾ', (int)'ಿ', (int)'ೀ', (int)'ು', (int)'ೂ', (int)'ೃ', (int)'ೄ', 0x0000, (int)'ೆ', (int)'ೇ', (int)'ೈ', 0x0000, (int)'ೊ', (int)'ೋ', (int)'ೌ', (int)'್', (int)'ೕ', (int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z', (int)'ೖ', (int)'ೠ', (int)'ೡ', (int)'ೢ', (int)'ೣ'};
    public static final int[] kannadaExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'೦', (int)'೧', (int)'೨', (int)'೩', (int)'೪', (int)'೫',(int)'೬', (int)'೭', (int)'೮', (int)'೯', (int)'ೞ', (int)'ೱ', (int)'{', (int)'}', (int)'ೲ', 0x0000, 0x0000, 0x0000, 0x0000, (int)'\\', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'|',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetKannada = new GSMCharset(GSM_CANONICAL_NAME, new String[0], kannadaMap, kannadaExtentionMap);

    public static final int[] malayalamMap = new int[] { 0x00, (int)'ം', (int)'ഃ', (int)'അ',(int)'ആ',(int)'ഇ',(int)'ഈ',(int)'ഉ',(int)'ഊ', (int)'ഋ', (int)'\n', (int)'ഌ', 0x0000,(int)'\r', (int)'എ',(int)'ഏ',(int)'ഐ', 0x0000, (int)'ഒ', (int)'ഓ',(int)'ഔ',(int)'ക', (int)'ഖ', (int)'ഗ', (int)'ഘ', (int)'ങ',(int)'ച', 0xffff, (int)'ഛ',(int)'ജ', (int)'ഝ', (int)'ഞ', (int)' ',(int)'!',(int)'ട', (int)'ഠ', (int)'ഡ', (int)'ഢ', (int)'ണ',(int)'ത',(int)')',(int)'(', (int)'ഥ', (int)'ദ',(int)',', (int)'ധ', (int)'.', (int)'ന',(int)'0',(int)'1',(int)'2',(int)'3',(int)'4',(int)'5',(int)'6',(int)'7',(int)'8',(int)'9',(int)':',(int)';', 0x0000, (int)'പ', (int)'ഫ',(int)'?', (int)'ബ', (int)'ഭ',(int)'മ',(int)'യ',(int)'ര', (int)'റ', (int)'ല', (int)'ള', (int)'ഴ', (int)'വ', (int)'ശ', (int)'ഷ',(int)'സ',(int)'ഹ', 0x0000, (int)'ഽ', (int)'ാ', (int)'ി', (int)'ീ', (int)'ു', (int)'ൂ', (int)'ൃ', (int)'ൄ', 0x0000, (int)'െ', (int)'േ', (int)'ൈ', 0x0000, (int)'ൊ', (int)'ോ', (int)'ൌ', (int)'്', (int)'ൗ', (int)'a',(int)'b',(int)'c',(int)'d',(int)'e',(int)'f',(int)'g',(int)'h',(int)'i',(int)'j',(int)'k',(int)'l',(int)'m',(int)'n',(int)'o',(int)'p',(int)'q',(int)'r',(int)'s',(int)'t',(int)'u',(int)'v',(int)'w',(int)'x',(int)'y',(int)'z', (int)'ൠ', (int)'ൡ', (int)'ൢ', (int)'ൣ', (int)'൹'};
    public static final int[] malayalamExtentionMap = { (int)'@', (int)'£', (int)'$', (int)'¥', (int)'¿', (int)'"',(int)'¤', (int)'%', (int)'&', (int)'\'', 0x000C, (int)'*', (int)'+', 0x000D, (int)'-', (int)'/', (int)'<', (int)'=', (int)'>', (int)'¡',(int)'^', (int)'¡', (int)'_', (int)'#', (int)'*', (int)'।', (int)'॥', 0x0000, (int)'൦', (int)'൧', (int)'൨', (int)'൩', (int)'൪', (int)'൫',(int)'൬', (int)'൭', (int)'൮', (int)'൯', (int)'൰', (int)'൱', (int)'{', (int)'}', (int)'൲', (int)'൳', (int)'൴', (int)'൵', (int)'ൺ', (int)'\\', (int)'ൻ', (int)'ർ', (int)'ൽ', (int)'ൾ', (int)'ൿ', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'[', (int)'~',(int)']', 0x0000, (int)'-',(int)'A',(int)'B',(int)'C',(int)'D',(int)'E',(int)'F',(int)'G',(int)'H',(int)'I',(int)'J',(int)'K',(int)'L',(int)'M',(int)'N',(int)'O',(int)'P',(int)'Q',(int)'R',(int)'S',(int)'T',(int)'U',(int)'V',(int)'W',(int)'X',(int)'Y',(int)'Z', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, (int)'€', 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
    public static Charset gsm7CharsetMalayalam = new GSMCharset(GSM_CANONICAL_NAME, new String[0], malayalamMap, malayalamExtentionMap);
}