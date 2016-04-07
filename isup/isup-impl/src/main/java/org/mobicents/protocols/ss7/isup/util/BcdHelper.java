/**
 *  Copyright 2016 Grzegorz Figiel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.mobicents.protocols.ss7.isup.util;

/**
 * Modification of the BCD Conversion in java (BCD.java Source Copyright 2010 Firat Salgur) as to:
 * <ul>
 * <li>change nibbles order in BCD encoded bytes table.</li>
 * <li>add String digits support</li>
 * <li>add support for ODD and EVEN digits number</li>
 * </ul>
 * @see <a href="https://gist.github.com/neuro-sys/953548">https://gist.github.com/neuro-sys/953548</a>
 *
 * @author <a href="mailto:grzegorz.figiel@pro-ids.com"> Grzegorz Figiel (ProIDS sp. z o.o.)</a>
 */
public class BcdHelper {

    public static byte[] stringToBCD(String bcdString) {
        long num = Long.parseLong(bcdString);
        return decimalToBCD(num);
    }

    public static String getBinString(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result += Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
            result += " ";
        }
        return result;
    }

    public static byte[] decimalToBCD(long num) {
        int digits = 0;

        long temp = num;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }

        boolean isOdd = digits % 2 == 0 ? false : true;
        if (isOdd) {
            digits++;
        }
        int byteLen = digits / 2;

        byte bcd[] = new byte[byteLen];

        for (int i = 0; i < digits; i++) {
            byte tmp = (byte) (num % 10);
            if(isOdd && i==0) {
                bcd[i / 2] = tmp;
                num /= 10;
                i++;
                continue;
            }
            if (i % 2 == 0) {
                bcd[i / 2] = (byte) (tmp << 4);
            } else {
                bcd[i / 2] |= tmp;
            }

            num /= 10;
        }

        for (int i = 0; i < byteLen / 2; i++) {
            byte tmp = bcd[i];
            bcd[i] = bcd[byteLen - i - 1];
            bcd[byteLen - i - 1] = tmp;
        }

        return bcd;
    }

    public static long bcdToDecimal(boolean isOdd, byte[] bcd) {
        return Long.valueOf(bcdToString(isOdd, bcd));
    }

    public static String bcdToString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(low);
        sb.append(high);

        return sb.toString();
    }

    public static String bcdToString(boolean isOdd, byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(bcdToString(bcd[i]));
        }

        if(isOdd) {
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}
