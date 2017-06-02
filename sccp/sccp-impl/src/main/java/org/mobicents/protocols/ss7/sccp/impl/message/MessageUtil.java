/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

/**
 * @author baranowb
 *
 */
public class MessageUtil {

    private MessageUtil() {
        // TODO Auto-generated constructor stub
    }



    public static int calculateUdtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen) {
        // 8 = 2 (fixed fields length) + 3 (variable fields pointers) + 3
        // (variable fields lengths)
        return 8 + calledPartyLen + callingPartyLen;
    }

    public static int calculateXudtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen, boolean segmented,
                                                           boolean importancePresense) {
        // 10 = 3 (fixed fields length) + 4 (variable fields pointers) + 3
        // (variable fields lengths)
        int res = 10 + calledPartyLen + callingPartyLen;
        if (segmented || importancePresense)
            res++; // optional part present - adding End of optional parameters
        if (segmented)
            res += 6;
        if (importancePresense)
            res += 3;

        return res;
    }

    public static int calculateXudtFieldsLengthWithoutData2(int calledPartyLen, int callingPartyLen) {
        int res = 254 - (3 + calledPartyLen + callingPartyLen);
        return res;
    }

    public static int calculateLudtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen, boolean segmented,
                                                           boolean importancePresense) {
        // 15 = 3 (fixed fields length) + 8 (variable fields pointers) + 4 (variable fields lengths)
        int res = 15 + calledPartyLen + callingPartyLen;
        if (segmented || importancePresense)
            res++; // optional part present - adding End of optional parameters
        if (segmented)
            res += 6;
        if (importancePresense)
            res += 3;

        return res;
    }

    public static int calculateCrFieldsLengthWithoutData(int calledPartyLen, boolean creditPresence, int callingPartyLen,
                                                         boolean hopCounterPresence, boolean importancePresence) {
        // 11 = 8 (fixed fields length) + 2 (pointers) + 1 (variable field length)
        int res = 11;
        if (calledPartyLen > 0 || creditPresence || callingPartyLen > 0 || hopCounterPresence || importancePresence)
            res++; // optional part present - adding End of optional parameters
        if (calledPartyLen > 0) {
            // + field type octet, length octet
            res += calledPartyLen + 2;
        }
        if (creditPresence)
            res += 3;
        if (callingPartyLen > 0) {
            // + field type octet, length octet
            res += callingPartyLen + 2;
        }
        if (hopCounterPresence) {
            res += 3;
        }
        if (importancePresence) {
            res += 3;
        }

        return res;
    }

    public static int calculateCcFieldsLengthWithoutData(boolean creditPresence, int calledPartyLen, boolean dataPresence,
                                                         boolean importancePresence) {
        // 12 = 11 (fixed fields length) + 1 (pointers)
        int res = 12;
        if (creditPresence || calledPartyLen != 0 || dataPresence || importancePresence)
            res++; // optional part present - adding End of optional parameters
        if (creditPresence)
            res += 3;
        // when it's 0 it will be ignored
        res += calledPartyLen + 2; // + type field, length
        if (dataPresence)
            res += 2; // field type, length
        if (importancePresence)
            res += 3;

        return res;
    }

    public static int calculateCrefFieldsLengthWithoutData(int calledPartyAddressLen, boolean importancePresence) {
        // 6 = 5 (fixed fields length) + 1 (pointers)
        int res = 6;

        if (calledPartyAddressLen != 0 || importancePresence)
            res++; // optional part present - adding End of optional parameters
        // when it's 0 it will be ignored
        res += calledPartyAddressLen + 2; // + type field, length
        if (importancePresence)
            res += 3;

        return res;
    }

    public static int calculateRlsdFieldsLengthWithoutData(boolean dataPresence, boolean importancePresence) {
        // 9 = 8 (fixed fields length) + 1 (pointers)

        int res = 9;
        if (dataPresence || importancePresence)
            res++; // optional part present - adding End of optional parameters
        if (dataPresence)
            res += 2; // type field, field length
        if (importancePresence)
            res += 3;

        return res;
    }
}
