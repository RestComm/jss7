/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>*
 */
public class SLRReaction extends EnumeratedBase {

    private static final long serialVersionUID = 86L;

    public static final int VAL_RETURN_SUCCESS = 1;
    public static final int VAL_ERROR_SYSTEM_FAILURE = 2;
    public static final int VAL_DATA_MISSING = 3;
    public static final int VAL_RESOURCE_LIMITATION = 4;
    public static final int VAL_UNEXPECTED_DATA_VALUE = 5;
    public static final int VAL_ERROR_UNKNOWN_SUBSCRIBER = 6;
    public static final int VAL_UNAUTHORIZED_REQUESTING_NETWORK = 7;
    public static final int VAL_UNKNOWN_OR_UNREACHABLE_LCS_CLIENT = 8;


    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_RETURN_SUCCESS, "Return success");
        intMap.put(VAL_ERROR_SYSTEM_FAILURE, "Return error system failure");
        intMap.put(VAL_DATA_MISSING, "Return error data missing");
        intMap.put(VAL_RESOURCE_LIMITATION, "Return error resource limitation");
        intMap.put(VAL_UNEXPECTED_DATA_VALUE, "Return error unexpected data value");
        intMap.put(VAL_ERROR_UNKNOWN_SUBSCRIBER, "Return error unknown subscriber");
        intMap.put(VAL_UNAUTHORIZED_REQUESTING_NETWORK, "Return error unauthorized requesting network");
        intMap.put(VAL_UNKNOWN_OR_UNREACHABLE_LCS_CLIENT, "Return error unknown or unreachable LCS client");

        stringMap.put("Return success", VAL_RETURN_SUCCESS);
        stringMap.put("Return error system failure", VAL_ERROR_SYSTEM_FAILURE);
        stringMap.put("Return error data missing", VAL_DATA_MISSING);
        stringMap.put("Return error resource limitation", VAL_RESOURCE_LIMITATION);
        stringMap.put("Return error unexpected data value", VAL_UNEXPECTED_DATA_VALUE);
        stringMap.put("Return error unknown subscriber", VAL_ERROR_UNKNOWN_SUBSCRIBER);
        stringMap.put("Return error unauthorized requesting network", VAL_UNAUTHORIZED_REQUESTING_NETWORK);
        stringMap.put("Return error unknown or unreachable LCS client", VAL_UNKNOWN_OR_UNREACHABLE_LCS_CLIENT);
    }

    public SLRReaction() {
    }

    public SLRReaction(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SLRReaction(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public SLRReaction(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static SLRReaction createInstance(String s) {
        Integer instance = doCreateInstance(s, stringMap, intMap);
        if (instance == null)
            return new SLRReaction(VAL_RETURN_SUCCESS);
        else
            return new SLRReaction(instance);
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
