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

package org.restcomm.protocols.ss7.tools.simulator.level2;

import java.util.Hashtable;

import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GlobalTitleType extends EnumeratedBase {

    private static final long serialVersionUID = -4004139747608172281L;
    public static final int VAL_NOA_ONLY = 1;
    public static final int VAL_TT_ONLY = 2;
    public static final int VAL_TT_NP_ES = 3;
    public static final int VAL_TT_NP_ES_NOA = 4;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_NOA_ONLY, "Nature of address indicator only");
        intMap.put(VAL_TT_ONLY, "Translation type only");
        intMap.put(VAL_TT_NP_ES, "Translation type, numbering plan and encoding scheme");
        intMap.put(VAL_TT_NP_ES_NOA, "Translation type, numbering plan, encoding scheme and NOA ind");

        stringMap.put("Nature of address indicator only", VAL_NOA_ONLY);
        stringMap.put("Translation type only", VAL_TT_ONLY);
        stringMap.put("Translation type, numbering plan and encoding scheme", VAL_TT_NP_ES);
        stringMap.put("Translation type, numbering plan, encoding scheme and NOA ind", VAL_TT_NP_ES_NOA);
    }

    public GlobalTitleType() {
    }

    public GlobalTitleType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public GlobalTitleType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public GlobalTitleType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static GlobalTitleType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new GlobalTitleType(VAL_TT_NP_ES_NOA);
        else
            return new GlobalTitleType(i1);
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
