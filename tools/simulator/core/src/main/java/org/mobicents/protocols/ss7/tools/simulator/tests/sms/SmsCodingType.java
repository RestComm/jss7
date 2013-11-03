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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsCodingType extends EnumeratedBase {

    private static final long serialVersionUID = 5488127927287051917L;

    public static final int VAL_GSM7 = 1;
    public static final int VAL_UCS2 = 2;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_GSM7, "GSM7");
        intMap.put(VAL_UCS2, "UCS2");

        stringMap.put("GSM7", VAL_GSM7);
        stringMap.put("UCS2", VAL_UCS2);
    }

    public SmsCodingType() {
    }

    public SmsCodingType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SmsCodingType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SmsCodingType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static SmsCodingType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new SmsCodingType(VAL_GSM7);
        else
            return new SmsCodingType(i1);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }

    public int getSupportesMaxMessageLength(int udhLen) {
        int maxMsgLen = 160 - udhLen;
        if (intValue() == SmsCodingType.VAL_UCS2)
            maxMsgLen = 70 - (udhLen + 1) / 2;
        return maxMsgLen;
    }
}
