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

package org.mobicents.protocols.ss7.mtp;

import java.nio.ByteBuffer;

public class Utils {
    // ///////////////////////
    // Some common statics //
    // ///////////////////////
    /**
     * Indicate value not set;
     */
    public static final byte _VALUE_NOT_SET = -1;

    public static final String dump(ByteBuffer buff, int size, boolean asBits) {
        return dump(buff.array(), size, asBits);
    }

    public static final String dump(byte[] buff, int size, boolean asBits) {
        String s = "";
        for (int i = 0; i < size; i++) {
            String ss = null;
            if (!asBits) {
                ss = Integer.toHexString(buff[i] & 0xff);
            } else {
                ss = Integer.toBinaryString(buff[i] & 0xff);
            }
            ss = fillInZeroPrefix(ss, asBits);
            s += " " + ss;
        }
        return s;
    }

    public static final String fillInZeroPrefix(String ss, boolean asBits) {
        if (asBits) {
            if (ss.length() < 8) {
                for (int j = ss.length(); j < 8; j++) {
                    ss = "0" + ss;
                }
            }
        } else {
            // hex
            if (ss.length() < 2) {

                ss = "0" + ss;
            }
        }

        return ss;
    }

    public static final String dump(int[] buff, int size) {
        String s = "";
        for (int i = 0; i < size; i++) {
            String ss = Integer.toHexString(buff[i] & 0xff);
            if (ss.length() == 1) {
                ss = "0" + ss;
            }
            s += " " + ss;
        }
        return s;
    }

    public static void createTrace(Throwable t, StringBuilder sb, boolean top) {

        if (!top) {
            sb.append("\nCaused by: " + t.toString());

        }
        StackTraceElement[] trace = t.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            sb.append("\tat " + trace[i]);
        }
        Throwable ourCause = t.getCause();
        if (ourCause != null) {
            createTrace(ourCause, sb, false);
        }
    }

    public static String createTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();
        createTrace(t, sb, true);
        return sb.toString();
    }

    /**
     * Construct a String containing a hex-dump of a byte array
     *
     * @param label the label of the hexdump or null
     * @param bytes the data to dump
     * @return a string containing the hexdump
     */
    public static String hexDump(String label, byte[] bytes) {
        final int modulo = 16;
        final int brk = modulo / 2;
        int indent = (label == null) ? 0 : label.length();

        StringBuffer sb = new StringBuffer(indent + 1);

        while (indent > 0) {
            sb.append(" ");
            indent--;
        }

        String ind = sb.toString();

        if (bytes == null) {
            return null;
        }

        sb = new StringBuffer(bytes.length * 4);

        StringBuffer cb = new StringBuffer(16);
        boolean nl = true;
        int i = 0;

        for (i = 1; i <= bytes.length; i++) {
            // start of line?
            if (nl) {
                nl = false;

                if (i > 1) {
                    sb.append(ind);
                } else if (label != null) {
                    sb.append(label);
                }

                String ha = Integer.toHexString(i - 1);

                for (int j = ha.length(); j <= 8; j++) {
                    sb.append("0");
                }

                sb.append(ha).append(" ");
            }

            sb.append(" ");

            int c = (bytes[i - 1] & 0xFF);
            String hx = Integer.toHexString(c).toUpperCase();

            if (hx.length() == 1) {
                sb.append("0");
            }

            sb.append(hx);
            cb.append((c < 0x21 || c > 0x7e) ? '.' : (char) (c));

            if ((i % brk) == 0) {
                sb.append(" ");
            }

            if ((i % modulo) == 0) {
                sb.append("|").append(cb).append("|\n");
                nl = true;
                cb = new StringBuffer(16);
            }
        }

        int mod = i % modulo;

        if (mod != 1) {
            // Fill the rest of the line
            while (mod <= modulo) {
                sb.append("   ");

                if ((mod % brk) == 0) {
                    sb.append(" ");
                }

                mod++;
            }

            sb.append("|").append(cb).append("|\n");
        }

        return sb.toString();
    }

}
