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

package org.restcomm.protocols.ss7.tools.simulator.level1;

import java.util.Hashtable;

import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
*
* @author sergey vetyutnev
*
*/
public class M3uaRoutingLabelFormat extends EnumeratedBase {

    private static final long serialVersionUID = 4496910985349923292L;
    public static final int VAL_ITU = 1;
    public static final int VAL_ANSI_Sls8Bit = 2;
    public static final int VAL_ANSI_Sls5Bit = 3;
    public static final int VAL_Japan_TTC_DDI = 4;
    public static final int VAL_China = 5;
    public static final int VAL_Japan_NTT = 6;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_ITU, "ITU");
        intMap.put(VAL_ANSI_Sls8Bit, "ANSI_Sls8Bit");
        intMap.put(VAL_ANSI_Sls5Bit, "ANSI_Sls5Bit");
        intMap.put(VAL_Japan_TTC_DDI, "Japan_TTC_DDI");
        intMap.put(VAL_China, "China");
        intMap.put(VAL_Japan_NTT, "Japan_NTT");

        stringMap.put("ITU", VAL_ITU);
        stringMap.put("ANSI_Sls8Bit", VAL_ANSI_Sls8Bit);
        stringMap.put("ANSI_Sls5Bit", VAL_ANSI_Sls5Bit);
        stringMap.put("Japan_TTC_DDI", VAL_Japan_TTC_DDI);
        stringMap.put("China", VAL_China);
        stringMap.put("Japan_NTT", VAL_Japan_NTT);
    }

    public M3uaRoutingLabelFormat() {
    }

    public M3uaRoutingLabelFormat(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaRoutingLabelFormat(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaRoutingLabelFormat(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static M3uaRoutingLabelFormat createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return null;
        else
            return new M3uaRoutingLabelFormat(i1);
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
