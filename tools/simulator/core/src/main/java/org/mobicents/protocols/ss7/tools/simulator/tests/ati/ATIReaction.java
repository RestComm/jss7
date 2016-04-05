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

package org.mobicents.protocols.ss7.tools.simulator.tests.ati;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
*
* @author sergey vetyutnev
*
*/
public class ATIReaction extends EnumeratedBase {

    private static final long serialVersionUID = -4059517194931173303L;

    public static final int VAL_RETURN_SUCCESS = 1;
    public static final int VAL_RETURN_SUCCESS_SUBSCRIBER_STATE = 2;
    public static final int VAL_ERROR_SYSTEM_FAILURE = 3;
    public static final int VAL_DATA_MISSING = 4;
    public static final int VAL_ERROR_UNKNOWN_SUBSCRIBER = 5;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_RETURN_SUCCESS, "Return success");
        intMap.put(VAL_RETURN_SUCCESS_SUBSCRIBER_STATE, "Return success - subscriber state");
        intMap.put(VAL_ERROR_SYSTEM_FAILURE, "Return error system failure");
        intMap.put(VAL_DATA_MISSING, "Return error data missing");
        intMap.put(VAL_ERROR_UNKNOWN_SUBSCRIBER, "Return error unknown subscriber");

        stringMap.put("Return success", VAL_RETURN_SUCCESS);
        stringMap.put("Return success - subscriber state", VAL_RETURN_SUCCESS_SUBSCRIBER_STATE);
        stringMap.put("Return error system failure", VAL_ERROR_SYSTEM_FAILURE);
        stringMap.put("Return error data missing", VAL_DATA_MISSING);
        stringMap.put("Return error unknown subscriber", VAL_ERROR_UNKNOWN_SUBSCRIBER);
    }

    public ATIReaction() {
    }

    public ATIReaction(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public ATIReaction(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public ATIReaction(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static ATIReaction createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new ATIReaction(VAL_RETURN_SUCCESS);
        else
            return new ATIReaction(i1);
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
