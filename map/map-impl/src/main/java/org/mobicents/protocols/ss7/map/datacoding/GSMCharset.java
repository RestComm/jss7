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

package org.mobicents.protocols.ss7.map.datacoding;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;

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
        this(canonicalName, aliases, BYTE_TO_CHAR_DefaultAlphabet, BYTE_TO_CHAR_DefaultAlphabetExtentionTable);
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

        // TODO: after implementing National Language Shift Tables make here selection depending on nationalLanguageIdentifier
        // nationalLanguageLockingShift and nationalLanguageIdentifierSingleShift can be ==null

        if (nationalLanguageLockingShiftIdentifier == null) {
            this.mainTable = BYTE_TO_CHAR_DefaultAlphabet;
        } else {
            switch (nationalLanguageLockingShiftIdentifier) {
                case Urdu:
                    this.mainTable = BYTE_TO_CHAR_UrduAlphabet;
                    break;
                default:
                    this.mainTable = BYTE_TO_CHAR_DefaultAlphabet;
                    break;
            }
        }
        if (nationalLanguageSingleShiftIdentifier == null) {
            this.extensionTable = BYTE_TO_CHAR_DefaultAlphabetExtentionTable;
        } else
            switch (nationalLanguageSingleShiftIdentifier) {
                case Urdu:
                    this.extensionTable = BYTE_TO_CHAR_UrduAlphabetExtentionTable;
                    break;
                default:
                    this.extensionTable = BYTE_TO_CHAR_DefaultAlphabetExtentionTable;
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

    // Look at http://www.unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT
    public static final int[] BYTE_TO_CHAR_DefaultAlphabet = { 0x0040, 0x00A3, 0x0024, 0x00A5, 0x00E8, 0x00E9, 0x00F9, 0x00EC,
            0x00F2, 0x00E7, 0x000A, 0x00D8, 0x00F8, 0x000D, 0x00C5, 0x00E5, 0x0394, 0x005F, 0x03A6, 0x0393, 0x039B, 0x03A9,
            0x03A0, 0x03A8, 0x03A3, 0x0398, 0x039E, 0x00A0, 0x00C6, 0x00E6, 0x00DF, 0x00C9, 0x0020, 0x0021, 0x0022, 0x0023,
            0x00A4, 0x0025, 0x0026, 0x0027, 0x0028, 0x0029, 0x002A, 0x002B, 0x002C, 0x002D, 0x002E, 0x002F, 0x0030, 0x0031,
            0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037, 0x0038, 0x0039, 0x003A, 0x003B, 0x003C, 0x003D, 0x003E, 0x003F,
            0x00A1, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D,
            0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x00C4,
            0x00D6, 0x00D1, 0x00DC, 0x00A7, 0x00BF, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, 0x0068, 0x0069,
            0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F, 0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077,
            0x0078, 0x0079, 0x007A, 0x00E4, 0x00F6, 0x00F1, 0x00FC, 0x00E0 };
    public static final int[] BYTE_TO_CHAR_DefaultAlphabetExtentionTable = { 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x000C, 0x0000, 0x0000, 0x000D, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x005E, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x007B, 0x007D, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x005C,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x005B, 0x007E,
            0x005D, 0x0000, 0x007C, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x20AC, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, };

    public static final int[] BYTE_TO_CHAR_UrduAlphabet = { 0x0627, 0x0622, 0x0628, 0x067B, 0x0680, 0x067E, 0x06A6, 0x062A,
            0x06C2, 0x067F, 0x000A, 0x0679, 0x067D, 0x000D, 0x067A, 0x067C, 0x062B, 0x062C, 0x0681, 0x0684, 0x0683, 0x0685,
            0x0686, 0x0687, 0x062D, 0x062E, 0x062F, 0x00A0, 0x068C, 0x0688, 0x0689, 0x068A, 0x0020, 0x0021, 0x068F, 0x068D,
            0x0630, 0x0631, 0x0691, 0x0693, 0x0028, 0x0029, 0x0699, 0x0632, 0x002C, 0x0696, 0x002E, 0x0698, 0x0030, 0x0031,
            0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037, 0x0038, 0x0039, 0x003A, 0x003B, 0x069A, 0x0633, 0x0634, 0x003F,
            0x0635, 0x0636, 0x0637, 0x0638, 0x0639, 0x0641, 0x0642, 0x06A9, 0x06AA, 0x06AB, 0x06AF, 0x06B3, 0x06B1, 0x0644,
            0x0645, 0x0646, 0x06BA, 0x06BB, 0x06BC, 0x0648, 0x06C4, 0x06D5, 0x06C1, 0x06BE, 0x0621, 0x06CC, 0x06D0, 0x06D2,
            0x064D, 0x0650, 0x064F, 0x0657, 0x0657, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, 0x0068, 0x0069,
            0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F, 0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077,
            0x0078, 0x0079, 0x007A, 0x0655, 0x0651, 0x0653, 0x0656, 0x0670 };
    public static final int[] BYTE_TO_CHAR_UrduAlphabetExtentionTable = { 0x0040, 0x00A3, 0x0024, 0x00A5, 0x00BF, 0x0022,
            0x00A4, 0x0025, 0x0026, 0x0027, 0x000C, 0x002A, 0x002B, 0x000D, 0x002D, 0x002F, 0x003C, 0x003D, 0x003E, 0x00A1,
            0x005E, 0x00A1, 0x005F, 0x0023, 0x002A, 0x0600, 0x0601, 0x0000, 0x06F0, 0x06F1, 0x06F2, 0x06F3, 0x06F4, 0x06F5,
            0x06F6, 0x06F7, 0x06F8, 0x06F9, 0x060C, 0x060D, 0x007B, 0x007D, 0x060E, 0x060F, 0x0610, 0x0611, 0x0612, 0x005C,
            0x0613, 0x0614, 0x061B, 0x061F, 0x0640, 0x0652, 0x0658, 0x066B, 0x066C, 0x0672, 0x0673, 0x06CD, 0x005B, 0x007E,
            0x005D, 0x06D4, 0x007C, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x004B,
            0x004C, 0x004E, 0x004E, 0x004F, 0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059,
            0x005A, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x20AC, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000,
            0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, };
}
