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
public class SRIReaction extends EnumeratedBase {

    private static final long serialVersionUID = 335591919825753448L;

    public static final int VAL_RETURN_SUCCESS = 1;
    public static final int VAL_RETURN_SUCCESS_WITH_LMSI = 2;
    public static final int VAL_ERROR_SYSTEM_FAILURE = 3;
    public static final int VAL_ERROR_CALL_BARRED = 4;
    public static final int VAL_ERROR_ABSENT_SUBSCRIBER = 5;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_RETURN_SUCCESS, "Return success");
        intMap.put(VAL_RETURN_SUCCESS_WITH_LMSI, "Return success with LMSI");
        intMap.put(VAL_ERROR_SYSTEM_FAILURE, "Return error system failure");
        intMap.put(VAL_ERROR_CALL_BARRED, "Return error call barred");
        intMap.put(VAL_ERROR_ABSENT_SUBSCRIBER, "Return error absent subscriber");

        stringMap.put("Return success", VAL_RETURN_SUCCESS);
        stringMap.put("Return success with LMSI", VAL_RETURN_SUCCESS_WITH_LMSI);
        stringMap.put("Return error system failure", VAL_ERROR_SYSTEM_FAILURE);
        stringMap.put("Return error call barred", VAL_ERROR_CALL_BARRED);
        stringMap.put("Return error absent subscriber", VAL_ERROR_ABSENT_SUBSCRIBER);
    }

    public SRIReaction() {
    }

    public SRIReaction(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SRIReaction(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SRIReaction(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static SRIReaction createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new SRIReaction(VAL_RETURN_SUCCESS);
        else
            return new SRIReaction(i1);
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
