/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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
package org.restcomm.protocols.ss7.isup.util;

import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * This class implements support for BCD encoding and decoding string represention of digits.
 * Only BCD_EVEN and BCD_ODD encoding is supported currently
 *
 * @author <a href="mailto:grzegorz.figiel@pro-ids.com"> Grzegorz Figiel (ProIDS sp. z o.o.)</a>
 */
public class BcdHelper {

    public static String getBinString(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result += Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
            result += " ";
        }
        return result;
    }


    /**
     * Converts telco char according to statement
     * If the BCD even or BCD odd encoding scheme is used, then the following encoding shall be applied for the
     * non-decimal characters: 1011 (*), 1100 (#).
     * 11=b=*
     * 12=c=#
     *
     * @param ch character to be checked and converted
     * @return hex cher representing the telco char
     */
    public static char convertTelcoCharToHexDigit(char ch) {
        switch (ch) {
            case '*':
                return 'b';
            case '#':
                return 'c';
            default:
                return ch;
        }
    }

    public static String convertTelcoCharsToHexDigits(String telcoDigits) {
        StringBuilder sb = new StringBuilder();
        char[] chars = telcoDigits.toCharArray();
        for (char c : chars) {
            sb.append(convertTelcoCharToHexDigit(c));
        }
        return sb.toString();
    }

    /**
     * Converts byte into telco specific char according to statement
     * If the BCD even or BCD odd encoding scheme is used, then the following encoding shall be applied for the
     * non-decimal characters: 1011 (*), 1100 (#).
     * 11=b=*
     * 12=c=#
     *
     * @param byteValue byte value to convert into char
     * @return
     */
    public static String convertHexByteToTelcoChar(byte byteValue) {
        switch (byteValue) {
            case 11:
                return "*";
            case 12:
                return "#";
            default:
                return String.format("%1x", Byte.valueOf(byteValue));
        }
    }


    /**
     * Encodes hex digits string into BCD digits. Supports specific telco chars '*' and '#'.
     *
     * @param hexString hex digit string to be encoded
     * @return BCD even or odd encoded digits
     */
    public static byte[] encodeHexStringToBCD(String hexString) {
        hexString=hexString.toLowerCase();
        int noOfDigits = hexString.length();

        boolean isOdd = noOfDigits % 2 == 0 ? false : true;
        int noOfBytes = noOfDigits / 2;
        if (isOdd) {
            noOfBytes++;
        }
        byte[] bcdDigits = new byte[noOfBytes];

        char[] chars = hexString.toCharArray();
        int digit = -1;
        for (int i = 0; i< noOfDigits; i++) {
            digit = Character.digit(convertTelcoCharToHexDigit(chars[i]), 16);
            byte tmpByte = (byte) digit;
            if (i % 2 == 0) {
                bcdDigits[i / 2] = tmpByte;
            } else {
                bcdDigits[i / 2] |= (byte) (tmpByte << 4);
            }
        }

        return bcdDigits;
    }

    /**
     * Converts single BCD encoded byte into String Hex representation.
     *
     * @param bcdByte
     * @return String representation of BCD digits (two hex digits returned)
     */
    public static String bcdToHexString(int encodingScheme, byte bcdByte) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte leftNibble = (byte) (bcdByte & 0xf0);
        leftNibble = (byte) (leftNibble >>> 4);
        leftNibble = (byte) (leftNibble & 0x0f);
        byte rightNibble = (byte) (bcdByte & 0x0f);

        switch (encodingScheme) {
            case GenericDigits._ENCODING_SCHEME_BCD_EVEN:
            case GenericDigits._ENCODING_SCHEME_BCD_ODD:
                sb.append(convertHexByteToTelcoChar(rightNibble));
                sb.append(convertHexByteToTelcoChar(leftNibble));
                break;
            default:
                throw new UnsupportedEncodingException("Specified GenericDigits encoding: " + encodingScheme + " is unsupported");
        }
        return sb.toString();
    }

    /**
     * Decoded BCD encoded string into Hex digits string.
     *
     * @param encodingScheme  BCD endcoding scheme to be used
     * @param bcdBytes bytes table to decode.
     * @return hex string representation of BCD encoded digits
     */
    public static String bcdDecodeToHexString(int encodingScheme, byte[] bcdBytes) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        for (byte b : bcdBytes) {
            sb.append(bcdToHexString(encodingScheme, b));
        }
        if (GenericDigits._ENCODING_SCHEME_BCD_ODD == encodingScheme) {
            sb.deleteCharAt(sb.length()-1);
        }

        return sb.toString();
    }

}
