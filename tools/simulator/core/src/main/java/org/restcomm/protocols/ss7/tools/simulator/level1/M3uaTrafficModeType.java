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
public class M3uaTrafficModeType extends EnumeratedBase {

    private static final long serialVersionUID = 2921729783676278118L;
    public static final int VAL_OVERRIDE = 1;
    public static final int VAL_LOADSHARE = 2;
    public static final int VAL_BROADCAST = 3;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_OVERRIDE, "OVERRIDE");
        intMap.put(VAL_LOADSHARE, "LOADSHARE");
        intMap.put(VAL_BROADCAST, "BROADCAST");

        stringMap.put("OVERRIDE", VAL_OVERRIDE);
        stringMap.put("LOADSHARE", VAL_LOADSHARE);
        stringMap.put("BROADCAST", VAL_BROADCAST);
    }

    public M3uaTrafficModeType() {
    }

    public M3uaTrafficModeType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaTrafficModeType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaTrafficModeType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static M3uaTrafficModeType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return null;
        else
            return new M3uaTrafficModeType(i1);
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
