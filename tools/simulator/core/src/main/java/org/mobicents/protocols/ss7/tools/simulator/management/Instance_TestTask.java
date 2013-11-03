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

package org.mobicents.protocols.ss7.tools.simulator.management;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Instance_TestTask extends EnumeratedBase {

    private static final long serialVersionUID = 5744729092059179670L;
    public static final int VAL_NO = 0;
    public static final int VAL_USSD_TEST_CLIENT = 1;
    public static final int VAL_USSD_TEST_SERVER = 2;
    public static final int VAL_SMS_TEST_CLIENT = 3;
    public static final int VAL_SMS_TEST_SERVER = 4;
    public static final int VAL_CAP_TEST_SCF = 5;
    public static final int VAL_CAP_TEST_SSF = 6;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_NO, "NO");
        intMap.put(VAL_USSD_TEST_CLIENT, "USSD_TEST_CLIENT");
        intMap.put(VAL_USSD_TEST_SERVER, "USSD_TEST_SERVER");
        intMap.put(VAL_SMS_TEST_CLIENT, "SMS_TEST_CLIENT");
        intMap.put(VAL_SMS_TEST_SERVER, "SMS_TEST_SERVER");
        intMap.put(VAL_CAP_TEST_SCF, "CAP_TEST_SCF");
        intMap.put(VAL_CAP_TEST_SSF, "CAP_TEST_SSF");

        stringMap.put("NO", VAL_NO);
        stringMap.put("USSD_TEST_CLIENT", VAL_USSD_TEST_CLIENT);
        stringMap.put("USSD_TEST_SERVER", VAL_USSD_TEST_SERVER);
        stringMap.put("SMS_TEST_CLIENT", VAL_SMS_TEST_CLIENT);
        stringMap.put("SMS_TEST_SERVER", VAL_SMS_TEST_SERVER);
        stringMap.put("CAP_TEST_SCF", VAL_CAP_TEST_SCF);
        stringMap.put("CAP_TEST_SSF", VAL_CAP_TEST_SSF);
    }

    public Instance_TestTask() {
    }

    public Instance_TestTask(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public Instance_TestTask(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public Instance_TestTask(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static Instance_TestTask createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new Instance_TestTask(VAL_NO);
        else
            return new Instance_TestTask(i1);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }
}
