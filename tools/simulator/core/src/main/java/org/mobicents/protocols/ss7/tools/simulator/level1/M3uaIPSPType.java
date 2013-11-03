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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class M3uaIPSPType extends EnumeratedBase {

    private static final long serialVersionUID = 4496910985349923292L;
    public static final int VAL_CLIENT = 1;
    public static final int VAL_SERVER = 2;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(VAL_CLIENT, "CLIENT");
        intMap.put(VAL_SERVER, "SERVER");

        stringMap.put("CLIENT", VAL_CLIENT);
        stringMap.put("SERVER", VAL_SERVER);
    }

    public M3uaIPSPType() {
    }

    public M3uaIPSPType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaIPSPType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public M3uaIPSPType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static M3uaIPSPType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return null;
        else
            return new M3uaIPSPType(i1);
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
