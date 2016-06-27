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
    public static final int VAL_ATI_TEST_CLIENT = 7;
    public static final int VAL_ATI_TEST_SERVER = 8;
    public static final int VAL_CHECK_IMEI_TEST_CLIENT = 9;
    public static final int VAL_CHECK_IMEI_TEST_SERVER = 10;
    public static final int VAL_MAP_LCS_TEST_CLIENT = 11;
    public static final int VAL_MAP_LCS_TEST_SERVER = 12;

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
        intMap.put(VAL_ATI_TEST_CLIENT, "ATI_TEST_CLIENT");
        intMap.put(VAL_ATI_TEST_SERVER, "ATI_TEST_SERVER");
        intMap.put(VAL_CHECK_IMEI_TEST_CLIENT, "CHECK_IMEI_TEST_CLIENT");
        intMap.put(VAL_CHECK_IMEI_TEST_SERVER, "CHECK_IMEI_TEST_SERVER");
        intMap.put(VAL_MAP_LCS_TEST_CLIENT, "MAP_LCS_TEST_CLIENT");
        intMap.put(VAL_MAP_LCS_TEST_SERVER, "MAP_LCS_TEST_SERVER");

        stringMap.put("NO", VAL_NO);
        stringMap.put("USSD_TEST_CLIENT", VAL_USSD_TEST_CLIENT);
        stringMap.put("USSD_TEST_SERVER", VAL_USSD_TEST_SERVER);
        stringMap.put("SMS_TEST_CLIENT", VAL_SMS_TEST_CLIENT);
        stringMap.put("SMS_TEST_SERVER", VAL_SMS_TEST_SERVER);
        stringMap.put("CAP_TEST_SCF", VAL_CAP_TEST_SCF);
        stringMap.put("CAP_TEST_SSF", VAL_CAP_TEST_SSF);
        stringMap.put("ATI_TEST_CLIENT", VAL_ATI_TEST_CLIENT);
        stringMap.put("ATI_TEST_SERVER", VAL_ATI_TEST_SERVER);
        stringMap.put("CHECK_IMEI_TEST_CLIENT", VAL_CHECK_IMEI_TEST_CLIENT);
        stringMap.put("CHECK_IMEI_TEST_SERVER", VAL_CHECK_IMEI_TEST_SERVER);
        stringMap.put("MAP_LCS_TEST_CLIENT",VAL_MAP_LCS_TEST_CLIENT);
        stringMap.put("MAP_LCS_TEST_SERVER",VAL_MAP_LCS_TEST_SERVER);
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
