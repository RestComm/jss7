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


package org.restcomm.protocols.ss7.tools.simulator.tests.checkimei;

import java.util.Hashtable;

import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 * @author mnowa
 *
 */
public class CheckImeiClientAction extends EnumeratedBase {

    private static final long serialVersionUID = -6385525152361290635L;
    public static final int VAL_MANUAL_OPERATION = 1;
    public static final int VAL_AUTO_SendCheckImeiRequest = 2;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_MANUAL_OPERATION, "Manual operation");
        intMap.put(VAL_AUTO_SendCheckImeiRequest, "Auto sending CheckImeiRequest");

        stringMap.put("Manual operation", VAL_MANUAL_OPERATION);
        stringMap.put("Auto sending CheckImeiRequest", VAL_AUTO_SendCheckImeiRequest);
    }

    public CheckImeiClientAction() {
    }

    public CheckImeiClientAction(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public CheckImeiClientAction(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public CheckImeiClientAction(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static CheckImeiClientAction createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new CheckImeiClientAction(VAL_MANUAL_OPERATION);
        else
            return new CheckImeiClientAction(i1);
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

