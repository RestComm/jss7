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
public class Instance_L3 extends EnumeratedBase {

    private static final long serialVersionUID = -4922961511501888701L;
    public static final int VAL_NO = 0;
    public static final int VAL_MAP = 1;
    public static final int VAL_CAP = 2;
    public static final int VAL_INAP = 3;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_NO, "NO");
        intMap.put(VAL_MAP, "TCAP+MAP");
        intMap.put(VAL_CAP, "TCAP+CAP");
        intMap.put(VAL_INAP, "TCAP+INAP");

        stringMap.put("NO", VAL_NO);
        stringMap.put("TCAP+MAP", VAL_MAP);
        stringMap.put("TCAP+CAP", VAL_CAP);
        stringMap.put("TCAP+INAP", VAL_INAP);
    }

    public Instance_L3() {
    }

    public Instance_L3(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public Instance_L3(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public Instance_L3(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static Instance_L3 createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new Instance_L3(VAL_NO);
        else
            return new Instance_L3(i1);
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
